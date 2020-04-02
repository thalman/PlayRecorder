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

    static final int c4 = 0;
    static final int d4 = 2;
    static final int e4 = 4;
    static final int f4 = 5;
    static final int g4 = 7;
    static final int a4 = 9;
    static final int b4 = 11;

    static final int c3 = c4 - 12;
    static final int d3 = d4 - 12;
    static final int e3 = e4 - 12;
    static final int f3 = f4 - 12;
    static final int g3 = g4 - 12;
    static final int a3 = a4 - 12;
    static final int b3 = b4 - 12;

    static final int c5 = c4 + 12;
    static final int d5 = d4 + 12;
    static final int e5 = e4 + 12;
    static final int f5 = f4 + 12;
    static final int g5 = g4 + 12;
    static final int a5 = a4 + 12;
    static final int b5 = b4 + 12;

    static final int c6 = c4 + 24;
    static final int d6 = d4 + 24;
    static final int e6 = e4 + 24;
    static final int f6 = f4 + 24;
    static final int g6 = g4 + 24;
    static final int a6 = a4 + 24;
    static final int b6 = b4 + 24;

    static final int c7 = c4 + 36;
    static final int d7 = d4 + 36;
    static final int e7 = e4 + 36;
    static final int f7 = f4 + 36;
    static final int g7 = g4 + 36;
    static final int a7 = a4 + 36;
    static final int b7 = b4 + 36;

    static final int c8 = c4 + 48;


    private int value = 0;
    private Accidentals accidentals = Accidentals.NONE;
    private boolean is_trill = false;

    public Note(int value, Accidentals accidentals, boolean tr)
    {
        value (value);
        accidentals (accidentals);
        trill(tr);
    }

    public Note(Note note)
    {
        this.value = note.value();
        this.accidentals = note.accidentals();
        this.is_trill = note.trill();
    }

    void set(Note note)
    {
        if (note != null) {
            value(note.value());
            accidentals(note.accidentals());
            trill(note.trill());
        }
    }

    void set(int value, Accidentals accidentals, boolean trill)
    {
        this.value = value;
        this.accidentals = accidentals;
        this.is_trill = trill;
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

    boolean trill()
    {
        return is_trill;
    }

    void trill(boolean t)
    {
        is_trill = t;
    }
}
