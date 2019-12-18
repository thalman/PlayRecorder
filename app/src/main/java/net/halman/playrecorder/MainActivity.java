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

import android.content.DialogInterface;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecoderApp app = new RecoderApp();
    ScoreView score = null;
    GripView grip = null;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        score = findViewById(R.id.Score);
        score.setOnTouchListener(scoreOnTouchListener);
        score.setOnClickListener(scoreOnClickListener);
        grip = findViewById(R.id.Grip);
        score.setGripView(grip);
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
            case R.id.actionTunning:
                onTunning();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onFingering()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.fingering_title);
        builder.setItems(R.array.fingering_items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        app.recorderFingering(Recorder.Fingering.BAROQUE);
                        grip.invalidate();
                        score.invalidate();
                        break;
                    case 1:
                        app.recorderFingering(Recorder.Fingering.GERMAN);
                        grip.invalidate();
                        score.invalidate();
                        break;
                }
            }
        });
        builder.show();
    }

    private void onTunning()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tunning_title);
        builder.setItems(R.array.tunning_items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        app.recorderTunning(Recorder.Tunning.C);
                        grip.invalidate();
                        score.invalidate();
                        break;
                    case 1:
                        app.recorderTunning(Recorder.Tunning.F);
                        grip.invalidate();
                        score.invalidate();
                        break;
                }
            }
        });
        builder.show();
    }
}
