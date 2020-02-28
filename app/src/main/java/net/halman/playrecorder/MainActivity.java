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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity {
    RecorderApp app = null;
    ScoreView score = null;
    GripView grip = null;
    Thread frequencyAnalyzer = null;
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

    Handler freqHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
            if (inputMessage.what == Frequency.MSG_FREQUENCY) {
                int freq100 = inputMessage.arg1;
                Log.d("FREQUENCY", "Frequency update: " + freq100);
                onFrequency(freq100);
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
        grip = findViewById(R.id.Grip);
        grip.setOnTouchListener(gripOnTouchListener);
        grip.setOnClickListener(gripOnClickListener);
        score.setGripView(grip);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_playrecorder, menu);

        // show hide fingering for recorder
        MenuItem item = menu.findItem(R.id.actionFingering);
        item.setVisible(Constants.isRecorder(app.instrumentType()));

        item = menu.findItem(R.id.actionListen);
        item.setVisible(frequencyAnalyzer == null);

        item = menu.findItem(R.id.actionStopListening);
        item.setVisible(frequencyAnalyzer != null);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.actionFingering:
                onFingering();
                return true;
            case R.id.actionInstrument:
                onInstrument();
                return true;
            case R.id.actionClef:
                onClef();
                return true;
            case R.id.actionListen:
                if (frequencyAnalyzer == null) {
                    onListen();
                }
                return true;
            case R.id.actionStopListening:
                if (frequencyAnalyzer != null) {
                    onListen();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart () {
        super.onStart();
        loadState();
        updateTitle();
        updateOrientation();
    }

    @Override
    public void onStop () {
        super.onStop();
        if (frequencyAnalyzer != null) {
            frequencyAnalyzer.interrupt();
            frequencyAnalyzer = null;
            grip.listen(false);
        }
        saveState();
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
        editor.putInt("grip-orientation", grip.orientation());
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
            sharedPref.getInt("note-accidentals", 0)
        );
        grip.orientation(sharedPref.getInt("grip-orientation", Orientation.UP));
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
                }
                grip.invalidate();
                score.invalidate();
                invalidateOptionsMenu();
                updateTitle();
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

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case 42:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onListen();
                }
        }
    }

    public void onListen()
    {
        if (frequencyAnalyzer == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 42);
            } else {
                frequencyAnalyzer = new Thread(new Frequency(freqHandler));
                frequencyAnalyzer.start();
                grip.listen(true);
                invalidateOptionsMenu();
            }
        } else {
            frequencyAnalyzer.interrupt();
            frequencyAnalyzer = null;
            grip.listen(false);
            invalidateOptionsMenu();
        }
    }

    public void onFrequency(int freq100)
    {
        if (freq100 < 2000) {
            Log.d("FREQUENCY", "too deep frequency");
            grip.onFrequency(false, 0, 0);
            return;
        }

        Note n = app.scale.frequencyNearestNote(freq100);
        if (! app.canPlay(n)) {
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
}
