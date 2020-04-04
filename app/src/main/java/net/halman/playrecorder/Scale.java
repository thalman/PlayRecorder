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

import static net.halman.playrecorder.Note.Accidentals.FLAT;
import static net.halman.playrecorder.Note.Accidentals.NONE;
import static net.halman.playrecorder.Note.Accidentals.RELEASE;
import static net.halman.playrecorder.Note.Accidentals.SHARP;

public class Scale {
    enum Clefs {
        G,
        F
    }

                                    /* c  d  e  f  g  a  b   c */
                                    /* 0  1  2  3  4  5  6   7 */
    private int[] scaleIntervals    = {0, 2, 4, 5, 7, 9, 11};
    private String[] scaleNames     = {"Cb","Gb","Db","Ab","Eb","Bb","F","C","G","D","A","E","B","F#","C#"};
    private int[] scalesAccidentals = { 3,   0,   4,   1,   5,   2,   6, -1,  3,  0,  4,  1,  5,  2,   6};
    private int scaleSignature;
    private Clefs scaleClef;
    private Note[] scale;
    private int[] frequencies = {26163, 27718, 29366, 31113, 32963, 34923, 36999, 39200, 41530, 44000, 46616, 49388};

    Scale(int asignature) {
        signature(asignature);
        clef(Clefs.G);
    }

    Scale(int asignature, Clefs aclef) {
        signature(asignature);
        clef(aclef);
        // noteNames = getResources().getStringArray(R.array.note_names);
    }

    int signature()
    {
        return scaleSignature;
    }

    void signature(int signature)
    {
        if ((signature >= -7) && (signature <= 7)) {
            scaleSignature = signature;
        } else {
            scaleSignature = 0;
        }
        initScale();
    }

    Clefs clef()
    {
        return scaleClef;
    }

    void clef(Clefs aclef)
    {
        scaleClef = aclef;
    }

    private void initScale()
    {
        scale = new Note[7];
        for (int i = 0; i < 7; i++) {
            scale[i] = new Note(scaleIntervals[i], Note.Accidentals.NONE, false);
        }
        if (scaleSignature > 0) {
            /* # */
            for (int i = 8; i <= scaleSignature + 7; i++) {
                scale[scalesAccidentals[i]].accidentals(SHARP);
            }
        }
        if (scaleSignature < 0) {
            /* b */
            for (int i = 6; i >= scaleSignature + 7; i--) {
                scale[scalesAccidentals[i]].accidentals(FLAT);
            }
        }
    }

    String name (int signature) {
        if (signature >= -7 && signature <= 7) {
            return scaleNames[signature + 7];
        }
        return "";
    }

    private int noteIdx (Note note)
    {
        int v = note.value();
        while (v < 0) { v += 12; };
        int m = v % 12;
        for(int i = 0; i < 8; i++) {
            if (scale[i].value () >= m) {
                return i;
            }
        }
        return 0;
    }

    public int notePosition (Note note)
    {
        int idx = noteIdx (note);
        int octave = note.value() / 12;
        if (note.value() < 0) {
            octave--;
        }
        if (scaleClef == Clefs.F) {
            idx -= 2;
        }
        return idx + octave * 7;
    }

    public Note noteByPosition (int position)
    {
        Note note = new Note(Note.c4, NONE, false);
        int pos = notePosition(note);

        while (pos < position) {
            noteUp(note);
            pos = notePosition(note);
        }

        while (pos > position) {
            noteDown(note);
            pos = notePosition(note);
        }

        return note;
    }

    void noteUp (Note note)
    {
        int octave = note.value() / 12;
        if (note.value() < 0 && note.value() % 12 != 0) {
            octave--;
        }

        int idx = noteIdx(note) + 1;
        if (idx > 6) {
            idx = 0;
            octave++;
        }

        note.value (scale[idx].value () + octave * 12);
        note.accidentals (Note.Accidentals.NONE);
    }

    void noteDown(Note note)
    {
        int octave = note.value() / 12;
        if (note.value() < 0 && note.value() % 12 != 0) {
            octave--;
        }

        int idx = noteIdx(note) - 1;
        if (idx < 0) {
            idx = 6;
            octave--;
        }

        note.value (scale[idx].value () + octave * 12);
        note.accidentals (Note.Accidentals.NONE);
    }

    void noteUpHalf(Note note)
    {
        Note.Accidentals scaleAccidentals = scale[noteIdx(note)].accidentals();
        if (scaleAccidentals == note.accidentals()) {
            note.accidentals(NONE);
        }

        switch (note.accidentals()) {
            case FLAT:
                switch (scaleAccidentals) {
                    case NONE:
                    case FLAT:
                        note.accidentals(NONE);
                        return;
                    case SHARP:
                        note.accidentals(RELEASE);
                        return;
                }
            case SHARP:
                note.accidentals(NONE);
                noteUp(note);
                if (scale[noteIdx(note)].accidentals() == SHARP) {
                    note.accidentals(RELEASE);
                }

                return;
            case NONE:
                switch (scaleAccidentals) {
                    case NONE:
                        note.accidentals(SHARP);
                        return;
                    case FLAT:
                        note.accidentals(RELEASE);
                        return;
                    case SHARP:
                        note.accidentals(NONE);
                        noteUp(note);
                        if (scale[noteIdx(note)].accidentals() == SHARP) {
                            note.accidentals(RELEASE);
                        }

                        return;
                }
            case RELEASE:
                switch (scaleAccidentals) {
                    case NONE:
                    case FLAT:
                        note.accidentals(SHARP);
                        return;
                    case SHARP:
                        note.accidentals(NONE);
                        return;
                }
        }
    }

    int noteAbsoluteValue(Note note)
    {
        int result = note.value ();

        switch (note.accidentals()) {
            case RELEASE:
                return result;
            case SHARP:
                return result+1;
            case FLAT:
                return result-1;
        }

        int idx = noteIdx (note);
        switch (scale[idx].accidentals ()) {
            case SHARP:
                result++;
                break;
            case FLAT:
                result--;
                break;
        }

        return result;
    }


    int noteNameIndex(Note n)
    {
        int i = noteAbsoluteValue(n) % 12;
        while (i < 0) i += 12;
        return i;
    }

    void noteDownHalf(Note note)
    {
        Note.Accidentals scaleAccidentals = scale[noteIdx(note)].accidentals();
        if (scaleAccidentals == note.accidentals()) {
            note.accidentals(NONE);
        }

        switch (note.accidentals()) {
            case SHARP:
                switch (scaleAccidentals) {
                    case NONE:
                    case SHARP:
                        note.accidentals(NONE);
                        return;
                    case FLAT:
                        note.accidentals(RELEASE);
                        return;
                }
            case FLAT:
                note.accidentals(NONE);
                noteDown(note);
                if (scale[noteIdx(note)].accidentals() == FLAT) {
                    note.accidentals(RELEASE);
                }

                return;
            case NONE:
                switch (scaleAccidentals) {
                    case NONE:
                        note.accidentals(FLAT);
                        return;
                    case SHARP:
                        note.accidentals(RELEASE);
                        return;
                    case FLAT:
                        note.accidentals(NONE);
                        noteDown(note);
                        if (scale[noteIdx(note)].accidentals() == FLAT) {
                            note.accidentals(RELEASE);
                        }

                        return;
                }
            case RELEASE:
                switch (scaleAccidentals) {
                    case NONE:
                    case SHARP:
                        note.accidentals(FLAT);
                        return;
                    case FLAT:
                        note.accidentals(NONE);
                        return;
                }
        }
    }

    Note.Accidentals noteAccidentals(Note note)
    {
        return Note.Accidentals.NONE;
    }

    int noteToFrequency(Note note) {
        int idx = noteNameIndex(note);
        int freq100 = frequencies[idx];
        idx = noteAbsoluteValue(note);
        while (idx >= 12) {
            freq100 *= 2;
            idx -= 12;
        }

        while (idx < 0) {
            freq100 /= 2;
            idx += 12;
        }
        return freq100;
    }

    Note frequencyNearestNote(int freq100)
    {
        int octave = 0;
        int abs_value = 0;
        int temp_freq = freq100;

        if (freq100 < 2000) {
            return null;
        }

        while (temp_freq < frequencies[0]) {
            temp_freq *= 2;
            --octave;
        }
        while (temp_freq > frequencies[11]) {
            temp_freq /= 2;
            ++octave;
        }

        if (temp_freq < frequencies[0]) {
            abs_value = 0;
        } else if (temp_freq > frequencies[11]) {
            abs_value = 11;
        } else {
            for (int i = 0; i < 11; i++) {
                if (temp_freq >= frequencies[i] && temp_freq <= frequencies[i + 1]) {
                    int d1 = temp_freq - frequencies[i];
                    int d2 = frequencies[i + 1] - temp_freq;
                    abs_value = (d1 < d2) ? i : i + 1;
                    break;
                }
            }
        }
        abs_value += 12 * octave;
        Note n = new Note(Note.c4 + 12 * octave, NONE, false);
        while (noteAbsoluteValue(n) < abs_value) {
            noteUpHalf(n);
        }
        while (noteAbsoluteValue(n) > abs_value) {
            noteDownHalf(n);
        }
        return n;
    }
}
