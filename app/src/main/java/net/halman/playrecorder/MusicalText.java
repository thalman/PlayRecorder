package net.halman.playrecorder;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class MusicalText {
    public static void draw(int x, int y, int size, boolean center, String txt, int color, Canvas c, Drawable sharp, Drawable flat)
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setTextSize(size);

        String txt_space = txt.replace("♯", " ").replace("♭"," ");
        int width = (int)paint.measureText(txt_space);
        int space_width = (int)paint.measureText(" ");
        int xpos = x;
        int ypos = y  + size / 2;
        if (center) {
            xpos -= width / 2;
        }
        c.drawText(txt_space, xpos, ypos, paint);

        int idx = txt.indexOf("♯");
        while (idx > 0) {
            String tmp = txt_space.substring(0, idx);
            int inpos = (int)paint.measureText(tmp);
            sharp.setBounds(
                    xpos + inpos,
                    ypos - size,
                    xpos + inpos + space_width,
                    ypos);
            sharp.draw(c);
            idx = txt.indexOf("♯", idx + 1);
        }
        idx = txt.indexOf("♭");
        while (idx > 0) {
            String tmp = txt_space.substring(0, idx);
            int inpos = (int)paint.measureText(tmp);
            flat.setBounds(
                    xpos + inpos,
                    ypos - size,
                    xpos + inpos + space_width,
                    ypos);
            flat.draw(c);
            idx = txt.indexOf("♭", idx + 1);
        }
    }

    public static void draw(int x, int y, int size, boolean center, String txt, Canvas c, Drawable sharp, Drawable flat)
    {
        draw(x, y, size, center, txt, Color.BLACK, c, sharp, flat);
    }

}
