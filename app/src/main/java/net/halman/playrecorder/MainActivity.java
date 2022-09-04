/*
 * This file is part of PlayRecorder.
 *
 * PlayRecorder is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlayRecorder is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.halman.playrecorder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import net.halman.playrecorder.R.id;

import java.io.InputStream;

import cn.sherlock.com.sun.media.sound.SF2Soundbank;
import cn.sherlock.com.sun.media.sound.SoftSynthesizer;
import jp.kshoji.javax.sound.midi.MidiChannel;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity implements ScoreView.ScoreViewListener, GripView.GripViewListener {
    public static final int MSG_MIDIOFF = 2;
    public static final int MSG_MIDION = 3;
    public static final int MSG_MIDINEXT = 4;
    public static final int MSG_FEEDBACK = 5;
    private static final int LONG_NOTE_DURATION = 2000;
    private static final int SHORT_NOTE_DURATION = 1000;

    RecorderApp app = null;
    ScoreView score = null;
    GripView grip = null;
    Thread frequencyAnalyzer = null;
    SoftSynthesizer synthesizer = null;
    boolean keepScreenOn = false;
    boolean playSound = true;
    Promotion promotion = null;
    Long midiOffTimestamp = 0l;
    int playCounter = 0;
    PointF lastTouch = new PointF();

    View.OnTouchListener scoreOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                lastTouch.set(event.getX(), event.getY());
            }

            return false;
        }
    };

    View.OnClickListener scoreOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (score !=  null) {
                score.onClick(lastTouch);
            }
        }
    };

    PointF lastGripTouch = new PointF();

    View.OnTouchListener gripOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                lastGripTouch.set(event.getX(), event.getY());
            }

            return false;
        }
    };

    View.OnClickListener gripOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (grip !=  null) {
                grip.onClick(lastGripTouch);
            }
        }
    };

    Handler msgHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
            switch (inputMessage.what) {
                case Frequency.MSG_FREQUENCY: {
                    int freq100 = inputMessage.arg1;
                    int freq100_low_precision = inputMessage.arg2;
                    Log.d("FREQUENCY", "Frequency update: " + freq100 + " " + freq100_low_precision);
                    if (midiOffTimestamp + 1500 < System.currentTimeMillis()) {
                        onFrequency(freq100, freq100_low_precision);
                    } else {
                        onFrequency(0, 0);
                    }
                    break;
                }
                case MSG_MIDIOFF: {
                    noteOff();
                    break;
                }
                case MSG_MIDION: {
                    noteOn(inputMessage.arg1);
                    break;
                }
                case MSG_MIDINEXT: {
                    noteOff();
                    longPlayMidiNote();
                    break;
                }
                case MSG_FEEDBACK: {
                    promotion.promote();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        score = findViewById(R.id.Score);
        score.setOnTouchListener(scoreOnTouchListener);
        score.setOnClickListener(scoreOnClickListener);
        score.setScoreViewListener(this);
        grip = findViewById(R.id.Grip);
        grip.setOnTouchListener(gripOnTouchListener);
        grip.setOnClickListener(gripOnClickListener);
        grip.setGripViewListener(this);

        // initialize midi synthesizer
        try {
            /* credit to https://stackoverflow.com/questions/56541361/android-play-soundfont-with-midi-file */
            InputStream sff = getResources().openRawResource(R.raw.soundfont);
            SF2Soundbank sf = new SF2Soundbank(sff);
            synthesizer = new SoftSynthesizer();
            synthesizer.open();
            synthesizer.loadAllInstruments(sf);
        } catch (Exception e) {
            synthesizer = null;
        }

        promotion = new Promotion(this);
    }

    @Override
    protected void onDestroy() {
        if (synthesizer != null) {
            synthesizer.close();
            synthesizer = null;
        }

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_playrecorder, menu);

        // show hide fingering for recorder
        MenuItem item = menu.findItem(R.id.actionFingering);
        item.setVisible(Constants.isRecorder(app.instrumentType()));

        item = menu.findItem(R.id.actionListen);
        item.setChecked(frequencyAnalyzer != null);

        item = menu.findItem(R.id.actionKeepScreenOn);
        item.setChecked(keepScreenOn);

        item = menu.findItem(R.id.actionPlaySound);
        item.setChecked(playSound);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();
        if (id == R.id.actionFingering) {
            onFingering();
            return true;
        }
        if (id ==R.id.actionInstrument) {
            onInstrument();
            return true;
        }
        if (id == R.id.actionClef) {
            onClef();
            return true;
        }
        if (id == R.id.actionScale) {
            onScale();
            return true;
        }
        if (id == R.id.actionPlaySound) {
            item.setChecked(!item.isChecked());
            onPlaySound(item.isChecked());
            return true;
        }
        if (id == R.id.actionListen) {
            item.setChecked(!item.isChecked());
            onListen(item.isChecked());
            return true;
        }
        if (id == R.id.actionKeepScreenOn) {
            item.setChecked(!item.isChecked());
            onKeepScreenOn(item.isChecked());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart () {
        super.onStart();
        loadState();
        updateTitle();
        updateOrientation();
        updateMidiInstrument();
    }

    @Override
    public void onStop () {
        super.onStop();
        if (frequencyAnalyzer != null) {
            frequencyAnalyzer.interrupt();
            frequencyAnalyzer = null;
            grip.listen(false);
        }

        noteOff();
        saveState();
    }

    private void noteOn(int note)
    {
        if (synthesizer != null) {
            try {
                MidiChannel channel = synthesizer.getChannels()[0];
                channel.allNotesOff();
                channel.noteOn(note, 127);
            } catch (Exception e) {}
        }
        midiOffTimestamp = System.currentTimeMillis() + LONG_NOTE_DURATION;
    }

    private void noteOff()
    {
        if (synthesizer != null) {
            try {
                synthesizer.getChannels()[0].allNotesOff();
            } catch (Exception e) {}
        }
        midiOffTimestamp = System.currentTimeMillis();
    }

    private void updateOrientation() {
        LinearLayout l = (LinearLayout) findViewById(R.id.Layout);
        if (l == null) {
            return;
        }

        switch (getResources().getConfiguration().orientation) {
            case ORIENTATION_PORTRAIT:
                l.setOrientation(LinearLayout.VERTICAL);
                break;
            default:
                l.setOrientation(LinearLayout.HORIZONTAL);
                break;
        }
    }

    private void updateMidiInstrument() {
        if (synthesizer == null) {
            return;
        }

        try {
            if (Constants.isTinWhistle(app.instrumentType())) {
                synthesizer.getChannels()[0].programChange(1);
            } else {
                // default - recorder
                synthesizer.getChannels()[0].programChange(0);
            }
        } catch (Exception e) {};
    }

    private void stopMidiNote() {
        if (synthesizer == null) {
            return;
        }

        msgHandler.removeMessages(MSG_MIDION);
        msgHandler.removeMessages(MSG_MIDIOFF);
        msgHandler.removeMessages(MSG_MIDINEXT);
        noteOff();
        playCounter = 0;
    }

    private void playMidiNote() {
        if (synthesizer == null) {
            return;
        }

        stopMidiNote();
        if (app.noteTrill()) {
            Pair<Integer, Integer> notes = app.getTrillMidiNotes();
            int i;
            for (i = 0; i < 8; i+=2) {
                msgHandler.sendMessageDelayed(msgHandler.obtainMessage(MSG_MIDION, notes.second, 0), i * 90);
                msgHandler.sendMessageDelayed(msgHandler.obtainMessage(MSG_MIDION, notes.first, 0), (i + 1) * 90);
            }
        } else {
            msgHandler.sendMessage(msgHandler.obtainMessage(MSG_MIDION, app.getMidiNote(), 0));
        }
        msgHandler.sendMessageDelayed(msgHandler.obtainMessage(MSG_MIDIOFF), SHORT_NOTE_DURATION);
    }

    private void longPlayMidiNote() {
        if (synthesizer == null) {
            return;
        }

        int left = playCounter - 1;
        stopMidiNote();
        if (left > 0) {
            playCounter = left;

            if (app.noteTrill()) {
                Pair<Integer, Integer> notes = app.getTrillMidiNotes();
                int i;
                for (i = 0; i < 16; i+=2) {
                    msgHandler.sendMessageDelayed(msgHandler.obtainMessage(MSG_MIDION, notes.second, 0), i * 90);
                    msgHandler.sendMessageDelayed(msgHandler.obtainMessage(MSG_MIDION, notes.first, 0), (i + 1) * 90);
                }
            } else {
                msgHandler.sendMessage(msgHandler.obtainMessage(MSG_MIDION, app.getMidiNote(), 0));
            }

            msgHandler.sendMessageDelayed(msgHandler.obtainMessage(MSG_MIDIOFF), LONG_NOTE_DURATION - 100);
            msgHandler.sendMessageDelayed(msgHandler.obtainMessage(MSG_MIDINEXT), LONG_NOTE_DURATION);
        }
    }

    void saveState () {
        if (app == null) {
            return;
        }

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("score-signature", app.signature());
        editor.putInt("score-clef", app.clefAsInt());
        editor.putInt("instrument-type", app.instrumentType());
        editor.putInt("recorder-fingering", app.lastRecorderFingering());
        editor.putInt("note-value", app.noteValue());
        editor.putInt("note-accidentals", app.noteAccidentalsAsInt());
        editor.putBoolean("note-trill", app.noteTrill());
        editor.putInt("grip-orientation", grip.orientation());
        editor.putBoolean("keep-screen-on", keepScreenOn);
        editor.putBoolean("play-sound", playSound);
        editor.apply();
    }

    void loadState ()
    {
        app = new RecorderApp();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        app.signature(sharedPref.getInt("score-signature", 0));
        app.clef(sharedPref.getInt("score-clef", 0));
        app.instrument(sharedPref.getInt("instrument-type", Constants.RECORDER_SOPRANO_BAROQUE));
        app.lastRecorderFingering(sharedPref.getInt("recorder-fingering", Recorder.BAROQUE));
        app.apparentNote(
            sharedPref.getInt("note-value", 0),
            sharedPref.getInt("note-accidentals", 0),
            sharedPref.getBoolean("note-trill", false)
        );
        grip.orientation(sharedPref.getInt("grip-orientation", Orientation.UP));
        onKeepScreenOn(sharedPref.getBoolean("keep-screen-on", false));
        onPlaySound(sharedPref.getBoolean("play-sound", true));
        app.checkLimits();
    }

    public void updateTitle() {
        String type = "";
        String name = "";
        String [] fingering = getResources().getStringArray(R.array.fingering_items);
        String [] instruments = getResources().getStringArray(R.array.instrument_items);

        if (app == null) {
            return;
        }

        if (Constants.isRecorder(app.instrumentType())) {
            switch (app.instrumentType()) {
                case Constants.RECORDER_SOPRANINO_BAROQUE:
                    type = fingering[0];
                    name = instruments[0];
                    break;
                case Constants.RECORDER_SOPRANO_BAROQUE:
                    type = fingering[0];
                    name = instruments[1];
                    break;
                case Constants.RECORDER_ALT_BAROQUE:
                    type = fingering[0];
                    name = instruments[2];
                    break;
                case Constants.RECORDER_TENOR_BAROQUE:
                    type = fingering[0];
                    name = instruments[3];
                    break;
                case Constants.RECORDER_BASS_BAROQUE:
                    type = fingering[0];
                    name = instruments[4];
                    break;
                case Constants.RECORDER_SOPRANINO_GERMAN:
                    type = fingering[1];
                    name = instruments[0];
                    break;
                case Constants.RECORDER_SOPRANO_GERMAN:
                    type = fingering[1];
                    name = instruments[1];
                    break;
                case Constants.RECORDER_ALT_GERMAN:
                    type = fingering[1];
                    name = instruments[2];
                    break;
                case Constants.RECORDER_TENOR_GERMAN:
                    type = fingering[1];
                    name = instruments[3];
                    break;
                case Constants.RECORDER_BASS_GERMAN:
                    type = fingering[1];
                    name = instruments[4];
                    break;
            }
            setTitle(getString(R.string.recorder_app_title, name, type));
            return;
        }

        if (Constants.isTinWhistle(app.instrumentType())) {
            switch (app.instrumentType()) {
                case Constants.TIN_WHISTLE_D:
                    setTitle(instruments[5]);
                    break;
                case Constants.TIN_WHISTLE_G:
                    setTitle(instruments[6]);
                    break;
            }
            return;
        }

        if (Constants.isFife(app.instrumentType())) {
            setTitle(instruments[7]);
            return;
        }
        setTitle("Play Recorder");
    }

    private void onFingering()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.fingering_title);
        builder.setItems(R.array.fingering_items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int currentType = app.instrumentType();
                switch (which) {
                    case 0:
                        if (Constants.isGermanRecorder(currentType)) {
                            // switch german type to baroque
                            app.instrument(currentType - 8);
                            grip.invalidate();
                            score.invalidate();
                            updateTitle();
                        }
                        break;
                    case 1:
                        if (Constants.isBaroqueRecorder(currentType)) {
                            // switch baroque german type to baroque
                            app.instrument(currentType + 8);
                            grip.invalidate();
                            score.invalidate();
                            updateTitle();
                        }
                        break;
                }
            }
        });
        builder.show();
    }

    private void onInstrument()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.instrument_title);
        builder.setItems(R.array.instrument_items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        app.instrument(app.lastRecorderFingering() == Recorder.BAROQUE ?
                                Constants.RECORDER_SOPRANINO_BAROQUE :
                                Constants.RECORDER_SOPRANINO_GERMAN);
                        break;
                    case 1:
                        app.instrument(app.lastRecorderFingering() == Recorder.BAROQUE ?
                                Constants.RECORDER_SOPRANO_BAROQUE :
                                Constants.RECORDER_SOPRANO_GERMAN);
                        break;
                    case 2:
                        app.instrument(app.lastRecorderFingering() == Recorder.BAROQUE ?
                                Constants.RECORDER_ALT_BAROQUE :
                                Constants.RECORDER_ALT_GERMAN);
                        break;
                    case 3:
                        app.instrument(app.lastRecorderFingering() == Recorder.BAROQUE ?
                                Constants.RECORDER_TENOR_BAROQUE :
                                Constants.RECORDER_TENOR_GERMAN);
                        break;
                    case 4:
                        app.instrument(app.lastRecorderFingering() == Recorder.BAROQUE ?
                                Constants.RECORDER_BASS_BAROQUE :
                                Constants.RECORDER_BASS_GERMAN);
                        break;
                    case 5:
                        app.instrument(Constants.TIN_WHISTLE_D);
                        break;
                    case 6:
                        app.instrument(Constants.TIN_WHISTLE_G);
                        break;
                    case 7:
                        app.instrument(Constants.FIFE);
                        break;
                }
                app.checkLimits();
                grip.invalidate();
                score.invalidate();
                invalidateOptionsMenu();
                updateTitle();
                updateMidiInstrument();
            }
        });
        builder.show();
    }

    public void onClef()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.clef_title);
        builder.setItems(R.array.clef_items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        app.clef(Scale.Clefs.G);
                        grip.invalidate();
                        score.invalidate();
                        break;
                    case 1:
                        app.clef(Scale.Clefs.F);
                        grip.invalidate();
                        score.invalidate();
                        break;
                }
            }
        });
        builder.show();
    }

    public void onScale()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.scale_title);
        builder.setItems(R.array.scale_names, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                app.signature(which - 7);
                grip.invalidate();
                score.invalidate();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case 42:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onListen(true);
                }
        }
    }

    public void onListen(boolean listen)
    {
        if (!listen) {
            if (frequencyAnalyzer != null) {
                frequencyAnalyzer.interrupt();
                frequencyAnalyzer = null;
            }
            grip.listen(false);
            invalidateOptionsMenu();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 42);
        } else {
            frequencyAnalyzer = new Thread(new Frequency(msgHandler, app.instrumentHighestFreq100()));
            frequencyAnalyzer.start();
            grip.listen(true);
            invalidateOptionsMenu();
        }
    }

    public void onFrequency(int freq100, int freq100_low_precision)
    {

        Note n = app.scale.frequencyNearestNote(freq100);
        Note nlp = app.scale.frequencyNearestNote(freq100_low_precision);

        if (nlp == null || n == null) {
            grip.onFrequency(false, 0, 0);
            return;
        }

        if (app.canPlay(nlp) && ! nlp.equal(n)) {
            freq100 = freq100_low_precision;
            n = nlp;
        }

        if (!app.canPlay(n)) {
            Log.d("FREQUENCY", "this sound can't be played on the instrument");
            grip.onFrequency(false, 0, 0);
            return;
        }

        app.realNote(n);
        Log.d("FREQUENCY", "Note: " + n.value() + " " + n.accidentals());
        grip.onFrequency(true, freq100, freq100 - app.scale.noteToFrequency(n));
        grip.invalidate();
        score.invalidate();
    }

    private void onKeepScreenOn(boolean keep)
    {
        keepScreenOn = keep;
        if (keep) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        invalidateOptionsMenu();
    }

    private void onPlaySound(boolean playSound)
    {
        this.playSound = playSound;

        invalidateOptionsMenu();
    }

    private void sendFeedbackMessage()
    {
        msgHandler.removeMessages(MSG_FEEDBACK);
        msgHandler.sendMessageDelayed(msgHandler.obtainMessage(MSG_FEEDBACK), 7000);
    }

    // ScoreView Interface
    public RecorderApp getRecorderApp()
    {
        return app;
    }

    public void onScoreViewNoteUp()
    {
        app.noteUp();
        grip.invalidate();
        sendFeedbackMessage();
        if (playSound) {
            playMidiNote();
        }
    }

    public void onScoreViewNoteDown()
    {
        app.noteDown();
        grip.invalidate();
        sendFeedbackMessage();
        if (playSound) {
            playMidiNote();
        }
    }

    public void onScoreViewNoteUpHalf()
    {
        app.noteUpHalf();
        grip.invalidate();
        sendFeedbackMessage();
        if (playSound) {
            playMidiNote();
        }
    }

    public void onScoreViewNoteDownHalf()
    {
        app.noteDownHalf();
        grip.invalidate();
        sendFeedbackMessage();
        if (playSound) {
            playMidiNote();
        }
    }

    public void onScoreViewNotePosition(int position)
    {
        app.noteByPosition(position);
        grip.invalidate();
        sendFeedbackMessage();
        if (playSound) {
            playMidiNote();
        }
    }

    public void onScoreViewSignatureUp()
    {
        app.signatureUp();
        grip.invalidate();
    }

    public void onScoreViewSignatureDown()
    {
        app.signatureDown();
        grip.invalidate();
    }

    public void onScoreViewTrill()
    {
        app.noteTrill(!app.noteTrill());
        grip.invalidate();
        if (playSound) {
            playMidiNote();
        }
    }

    public void onScoreViewClef()
    {
        onClef();
    }

    public void onScoreViewScale()
    {
        onScale();
    }

    public void onGripViewListen(boolean listen) { onListen(listen); }

    public void onScoreViewPlay() {
        if (playCounter > 0) {
            stopMidiNote();
        } else {
            playCounter = 15 + 1;
            longPlayMidiNote();
        }
    }
}
