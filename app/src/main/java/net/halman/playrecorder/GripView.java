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
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

import static net.halman.playrecorder.Grip.Hole.OPEN;

public class GripView extends View {
    MainActivity activity = null;

    private Drawable hole_close = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_hole, null);
    private Drawable hole_open = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_hole_empty, null);
    private Drawable hole_half = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_hole_half, null);
    private Drawable hole_bell = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_hole_bell, null);

    private double scalefactor = 1;
    private int grip_width = 600;
    private int grip_height = grip_width;
    private int grip_center_x = 0;
    private int grip_center_y = 0;

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

    int scale(int dim)
    {
        return (int)Math.round(dim * scalefactor);
    }

    int unscale(int dim)
    {
        return (int)Math.round(dim / scalefactor);
    }

    int getItemCenterX(Drawable drawable) {
        return drawable.getIntrinsicWidth() / 2;
    }

    int getItemCenterY(Drawable drawable) {
        return drawable.getIntrinsicHeight() / 2;
    }

    void putDrawable(int x, int y, Drawable drawable, Canvas canvas, double zoom)
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

    void drawText(int x, int y, String txt, Canvas c)
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        int height = (int)scale(50);
        paint.setTextSize(height);
        int width = (int)paint.measureText(txt);
        c.drawText(txt, scale(x) - width / 2 + grip_center_x,
                scale(y) - height / 2 + grip_center_y, paint);
    }

    void drawGrip(ArrayList<Grip> grips, Canvas canvas)
    {
        int [] xpos = new int[]      {0,   0,   0,   0,   0,   0,   -15, +15, -15, +15,   0};
        int [] ypos = new int[]      {150, 220, 270, 320, 390, 440, 490, 490, 540, 540, 590};
        double [] zoom = new double[]{1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.0, 1.0, 1.0, 1.0, 1.5};

        if ((grips == null) || (grips.size() == 0)) {
            return;
        }

        int step = grip_width / (grips.size() + 1);

        for (int g = 0; g < grips.size(); g++) {
            int x = step + g * step;
            Grip grip = grips.get(g);
            for (int i = 0; i < Grip.NUMBER_OF_HOLES - 1; i++) {
                int y = ypos[i];
                switch (grip.get(i)) {
                    case OPEN:
                        putDrawable(x + xpos[i], y, hole_open, canvas, zoom[i]);
                        break;
                    case CLOSE:
                        putDrawable(x + xpos[i], y, hole_close, canvas, zoom[i]);
                        break;
                    case HALFOPEN:
                        putDrawable(x + xpos[i], y, hole_half, canvas, zoom[i]);
                        break;
                }

            }
            if (grip.get(Grip.NUMBER_OF_HOLES - 1) != OPEN) {
                putDrawable(x + xpos[Grip.NUMBER_OF_HOLES-1], ypos[Grip.NUMBER_OF_HOLES-1], hole_bell, canvas, 2.0);
            }
        }
    }

    protected void onDraw(Canvas canvas) {
        if (activity != null && activity.app != null) {
            calculateScale();
            ArrayList<Grip> grips = activity.app.grips();
            drawGrip(grips, canvas);
            drawText(grip_width / 2, 80, activity.app.noteName(), canvas);
        }
    }

}
