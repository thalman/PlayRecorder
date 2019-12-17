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

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.PointF;
import android.os.Bundle;
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
}
