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

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static net.halman.playrecorder.Recorder.Fingering.BAROQUE;
import static net.halman.playrecorder.Recorder.Fingering.GERMAN;

public class MainActivity extends AppCompatActivity {
    RecorderApp app = null; //new RecorderApp();
    ScoreView score = null;
    GripView grip = null;
    AdView adView = null;

    final private String stateFile = "playrecorder.bin";

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
        adView = findViewById(R.id.adView);
        Bundle extras = new Bundle();
        extras.putString("max_ad_content_rating", "G");
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .tagForChildDirectedTreatment(true)
                .build();
        adView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_playrecorder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.actionFingering:
                onFingering();
                return true;
            case R.id.actionTuning:
                onTuning();
                return true;
            case R.id.actionClef:
                onClef();
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
        editor.putInt("recorder-fingering", app.fingeringAsInt());
        editor.putInt("recorder-tuning", app.tuningAsInt());
        editor.putInt("note-value", app.noteValue());
        editor.putInt("note-accidentals", app.noteAccidentalsAsInt());
        editor.putInt("grip-orientation", grip.orientation());
        editor.apply();
    }

    void loadState ()
    {
        try {

            FileInputStream file = openFileInput(stateFile);
            ObjectInputStream ois = new ObjectInputStream(file);
            app = (RecorderApp) ois.readObject();
            app.recorder.fingering(app.recorder.fingering());
            ois.close();
            file.close();
        } catch (Exception e) {
            app = new RecorderApp();
        }

        app = new RecorderApp();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        app.signature(sharedPref.getInt("score-signature", 0));
        app.clef(sharedPref.getInt("score-clef", 0));
        app.fingering(sharedPref.getInt("recorder-fingering", 0));
        app.tuning(sharedPref.getInt("recorder-tuning", 0));
        app.note(
            sharedPref.getInt("note-value", 0),
            sharedPref.getInt("note-accidentals", 0)
        );
        grip.orientation(sharedPref.getInt("grip-orientation", grip.UP));
    }

    public void updateTitle() {
        String type = "";
        String tuning = "";
        String [] fingering = getResources().getStringArray(R.array.fingering_items);
        String [] noteNames = getResources().getStringArray(R.array.note_names);

        if (app == null) {
            return;
        }

        switch (app.fingering()) {
            case BAROQUE:
                type = fingering[0];
                break;
            case GERMAN:
                type = fingering[1];
                break;
        }

        switch (app.recordertuning()) {
            case C:
                tuning = noteNames[0];
                break;
            case F:
                tuning = noteNames[5];
                break;
        }

        setTitle("Play Recorder / " + getString(R.string.app_title, type, tuning));
    }

    private void onFingering()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.fingering_title);
        builder.setItems(R.array.fingering_items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        app.recorderFingering(BAROQUE);
                        grip.invalidate();
                        score.invalidate();
                        updateTitle();
                        break;
                    case 1:
                        app.recorderFingering(GERMAN);
                        grip.invalidate();
                        score.invalidate();
                        updateTitle();
                        break;
                }
            }
        });
        builder.show();
    }

    private void onTuning()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tuning_title);
        builder.setItems(R.array.tuning_items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        app.recordertuning(Recorder.Tuning.C);
                        grip.invalidate();
                        score.invalidate();
                        updateTitle();
                        break;
                    case 1:
                        app.recordertuning(Recorder.Tuning.F);
                        grip.invalidate();
                        score.invalidate();
                        updateTitle();
                        break;
                }
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
}
