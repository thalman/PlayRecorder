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


public class Note {
    enum Accidentals {
        NONE,
        RELEASE,
        SHARP,
        FLAT
    }

    static final int C = 0;
    static final int D = 2;
    static final int E = 4;
    static final int F = 5;
    static final int G = 7;
    static final int A = 9;
    static final int B = 11;

    static int c1 = 0;
    static int d1 = 2;
    static int e1 = 4;
    static int f1 = 5;
    static int g1 = 7;
    static int a1 = 9;
    static int b1 = 11;

    static int c2 = c1 + 12;
    static int d2 = d1 + 12;
    static int e2 = e1 + 12;
    static int f2 = f1 + 12;
    static int g2 = g1 + 12;
    static int a2 = a1 + 12;
    static int b2 = b1 + 12;

    static int c3 = c1 + 24;
    static int d3 = d1 + 24;
    static int e3 = e1 + 24;
    static int f3 = f1 + 24;
    static int g3 = g1 + 24;
    static int a3 = a1 + 24;
    static int b3 = b1 + 24;

    static int c4 = c1 + 36;

    private int value = 0;
    private Accidentals accidentals = Accidentals.NONE;


    public Note(int value, Accidentals accidentals)
    {
        value (value);
        accidentals (accidentals);
    }

    public Note(Note note)
    {
        this.value = note.value();
        this.accidentals = note.accidentals();
    }

    void set(Note note)
    {
        if (note != null) {
            value(note.value());
            accidentals(note.accidentals());
        }
    }

    void set(int value, Accidentals accidentals)
    {
        this.value = value;
        this.accidentals = accidentals;
    }

    int value()
    {
        return this.value;
    }

    void value(int value)
    {
        this.value = value;
    }

    Accidentals accidentals()
    {
        return this.accidentals;
    }

    void accidentals(Accidentals accidentals)
    {
        this.accidentals = accidentals;
    }
}
