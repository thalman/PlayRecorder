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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GripView extends View {

    private enum Buttons {
        SWITCH,
    }

    private MainActivity activity = null;

    private Drawable hole_close = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_hole, null);
    private Drawable hole_open = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_hole_empty, null);
    private Drawable hole_half = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_hole_half, null);
    private Drawable hole_bell = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_hole_bell, null);
    private Drawable sharp = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_sharp, null);
    private Drawable flat = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_flat, null);
    private Drawable switch_direction = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_switch_direction, null);

    private double scalefactor = 1;
    private int grip_width = 610;
    private int grip_height = grip_width;
    private int grip_center_x = 0;
    private int grip_center_y = 0;
    private String noteNames[] = null;
    private int current_orientation = Orientation.UP;

    private Map<GripView.Buttons, Rect> buttonPositions = new HashMap<GripView.Buttons, Rect>() {{
        put(GripView.Buttons.SWITCH, new Rect(0, grip_height - 80, 70, grip_height - 10));
    }};

    public GripView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GripView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        activity = (MainActivity) context;
        noteNames = getResources().getStringArray(R.array.note_names);
    }

    private void calculateScale() {
        double sh, sv;
        double height, width;

        width = getWidth();
        height = getHeight();
        sh = width / grip_width;
        sv = height / grip_height;
        scalefactor = (sv < sh) ? sv : sh;
        if (sh < sv) {
            scalefactor = sh;
            grip_center_x = 0;
            grip_center_y = (int) (height - width) / 2;
        } else {
            scalefactor = sv;
            grip_center_y = 0;
            grip_center_x = (int) (width - height) / 2;
        }
    }

    int orientation()
    {
        return current_orientation;
    }

    void orientation(int o)
    {
        if (o >= Orientation.UP && o <= Orientation.DOWN) {
            current_orientation = o;
        } else {
            current_orientation = Orientation.UP;
        }
        invalidate();
    }

    void changeOrientation() {
        if (current_orientation == Orientation.UP) {
            current_orientation = Orientation.DOWN;
        } else {
            current_orientation = Orientation.UP;
        }
        invalidate();
    }

    void onClick(PointF point)
    {
        int x = unscale(Math.round(point.x) - grip_center_x);
        int y = unscale(Math.round(point.y) - grip_center_y);

        for (Map.Entry<Buttons, Rect> item: buttonPositions.entrySet()) {
            Rect r = item.getValue();
            if (r.contains (x, y)) {
                switch (item.getKey()) {
                    case SWITCH:
                        changeOrientation();
                        return;
                }
            }
        }
    }

    private int scale(int dim)
    {
        return (int)Math.round(dim * scalefactor);
    }

    private int unscale(int dim)
    {
        return (int)Math.round(dim / scalefactor);
    }

    private int getItemCenterX(Drawable drawable) {
        return drawable.getIntrinsicWidth() / 2;
    }

    private int getItemCenterY(Drawable drawable) {
        return drawable.getIntrinsicHeight() / 2;
    }

    private void putDrawable(int x, int y, Drawable drawable, Canvas canvas, double zoom)
    {
        int offsetx = getItemCenterX(drawable);
        int offsety = getItemCenterY(drawable);
        drawable.setBounds(
                scale(x - (int)(offsetx * zoom)) + grip_center_x,
                scale(y - (int)(offsety * zoom)) + grip_center_y,
                scale(x + (int)((-offsetx + drawable.getIntrinsicWidth()) * zoom)) + grip_center_x,
                scale(y + (int)((-offsety + drawable.getIntrinsicHeight()) * zoom)) + grip_center_y);
        drawable.draw(canvas);
    }

    private void drawButtons(Canvas canvas)
    {
        putDrawable(buttonPositions.get(Buttons.SWITCH).centerX(), buttonPositions.get(Buttons.SWITCH).centerY(), switch_direction, canvas, 1.6);
    }

    private void drawText(int x, int y, String txt, Canvas c)
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        int height = (int)scale(50);
        paint.setTextSize(height);

        String txt_space = txt.replace("♯", " ").replace("♭"," ");
        int width = (int)paint.measureText(txt_space);
        int xpos = scale(x) - width / 2 + grip_center_x;
        int ypos = scale(y) - height / 2 + grip_center_y;
        c.drawText(txt_space, xpos, ypos, paint);

        double zoom = scale(100) * 0.01;
        int idx = txt.indexOf("♯");
        if (idx > 0) {
            String tmp = txt_space.substring(0, idx);
            int inpos = (int)paint.measureText(tmp);
            sharp.setBounds(
                    xpos + inpos,
                    ypos - height,
                    xpos + inpos + (int)(sharp.getIntrinsicWidth() * zoom),
                    ypos + (int)(sharp.getIntrinsicHeight() * zoom) - height);
            sharp.draw(c);
        }
        idx = txt.indexOf("♭");
        if (idx > 0) {
            String tmp = txt_space.substring(0, idx);
            int inpos = (int)paint.measureText(tmp);
            flat.setBounds(
                    xpos + inpos,
                    ypos - height,
                    xpos + inpos + (int)(flat.getIntrinsicWidth() * zoom),
                    ypos + (int)(flat.getIntrinsicHeight() * zoom) - height);
            flat.draw(c);

        }
    }

    private void drawGrip(ArrayList<Grip> grips, Canvas canvas)
    {
        if ((grips == null) || (grips.size() == 0)) {
            return;
        }

        int holesCount = activity.app.numberOfHoles();

        int step = grip_width / (grips.size() + 1);

        for (int g = 0; g < grips.size(); g++) {
            int x = step + g * step;
            Grip grip = grips.get(g);
            for (int i = 0; i < holesCount; i++) {
                Hole h = activity.app.getHole(current_orientation, i);
                switch (grip.get(i)) {
                    case Hole.OPEN:
                        putDrawable(x + h.x, h.y, hole_open, canvas, h.zoom);
                        break;
                    case Hole.CLOSE:
                        putDrawable(x + h.x, h.y, hole_close, canvas, h.zoom);
                        break;
                    case Hole.HALFOPEN:
                        putDrawable(x + h.x, h.y, hole_half, canvas, h.zoom);
                        break;
                    case Hole.BELLCLOSE:
                        putDrawable(x + h.x, h.y, hole_bell, canvas, h.zoom);
                        break;
                }

            }
        }
    }

    protected void onDraw(Canvas canvas) {
        if (activity != null && activity.app != null) {
            calculateScale();
            ArrayList<Grip> grips = activity.app.grips();

            drawButtons(canvas);
            drawGrip(grips, canvas);
            drawText(grip_width / 2, 90, noteNames[activity.app.noteNameIndex()], canvas);
        }
    }

}
