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

import java.io.Serializable;
import java.util.ArrayList;

import static net.halman.playrecorder.Note.Accidentals.FLAT;
import static net.halman.playrecorder.Note.Accidentals.NONE;
import static net.halman.playrecorder.Note.Accidentals.RELEASE;
import static net.halman.playrecorder.Note.Accidentals.SHARP;

public class Scale implements Serializable {

                                    /* c  d  e  f  g  a  b   c */
                                    /* 0  1  2  3  4  5  6   7 */
    private int[] scaleIntervals    = {0, 2, 4, 5, 7, 9, 11};
    private String[] scaleNames     = {"Cb","Gb","Db","Ab","Eb","Bb","F","C","G","D","A","E","B","F#","C#"};
    private int[] scalesAccidentals = { 3,   0,   4,   1,   5,   2,   6, -1,  3,  0,  4,  1,  5,  2,   6};
    private int scaleSignature;
    private static final String [] noteNames = new String[] {"C", "C#/Db", "D", "D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab", "A", "A#/Bb", "B"};

    private Note[] scale;

    Scale(int asignature) {
        signature(asignature);
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


    private void initScale()
    {
        scale = new Note[7];
        for (int i = 0; i < 7; i++) {
            scale[i] = new Note(scaleIntervals[i], Note.Accidentals.NONE);
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
        return idx + octave * 7;
    }

    void noteUp (Note note)
    {
        int octave = note.value() / 12;
        if (note.value() < 0) {
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
        if (note.value() < 0) {
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

    void noteUpHalf (Note note)
    {
        switch (note.accidentals()) {
            case SHARP:
                noteUp (note);
                return;
            case FLAT:
                note.accidentals(Note.Accidentals.NONE);
                return;
            case NONE:
                int idx = noteIdx (note);
                if (scale[idx].accidentals() == FLAT && note.accidentals() == NONE) {
                    note.accidentals(RELEASE);
                    return;
                }

                Note tmp = new Note (note);
                noteUp (tmp);
                note.accidentals (SHARP);
                if (noteAbsoluteValue(tmp) == noteAbsoluteValue(note)) {
                    note.set(tmp);
                }
                return;
            case RELEASE:
                if (scaleSignature > 0) {
                    note.accidentals(Note.Accidentals.NONE);
                }
                if (scaleSignature < 0) {
                    noteUp(note);
                }
                return;
        }
    }

    int noteAbsoluteValue(Note note)
    {
        int result = note.value ();
        if (note.accidentals() == Note.Accidentals.RELEASE) {
            return result;
        }

        switch (note.accidentals()) {
            case SHARP:
                result++;
                break;
            case FLAT:
                result--;
                break;
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


    String noteName(Note n)
    {
        int i = noteAbsoluteValue(n) % 12;
        while (i < 0) i += 12;
        return noteNames[i];
    }

    void noteDownHalf(Note note)
    {
        switch (note.accidentals()) {
            case SHARP:
                note.accidentals(Note.Accidentals.NONE);
                return;
            case FLAT:
                noteDown (note);
                return;
            case NONE:
                int idx = noteIdx (note);
                if (scale[idx].accidentals() == SHARP && note.accidentals() == NONE) {
                    note.accidentals(RELEASE);
                    return;
                }

                Note tmp = new Note(note);
                noteDown(tmp);
                note.accidentals(FLAT);
                if (noteAbsoluteValue(tmp) == noteAbsoluteValue(note)) {
                    note.set(tmp);
                }
                return;
            case RELEASE:
                if (scaleSignature > 0) {
                    noteDown(note);
                }
                if (scaleSignature < 0) {
                    note.accidentals(Note.Accidentals.NONE);
                }
                return;
        }
    }

    Note.Accidentals noteAccidentals(Note note)
    {
        return Note.Accidentals.NONE;
    }
}
