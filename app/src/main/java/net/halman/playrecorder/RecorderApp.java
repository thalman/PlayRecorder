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

import java.util.ArrayList;

import static net.halman.playrecorder.Scale.*;
import static net.halman.playrecorder.Scale.Clefs.G;

public class RecorderApp {
    public Scale scale = new Scale(0);
    public Note note = new Note(Note.c1, Note.Accidentals.NONE);
    public Recorder recorder = new Recorder();

    int notePosition()
    {
        return scale.notePosition(note);
    }

    void noteByPosition(int position)
    {
        note = scale.noteByPosition(position);
        if (! canPlay()) {
            if (position < 5) {
                lowestNote();
            } else {
                highestNote();
            }
        }
    }

    void lowestNote()
    {
        switch(recorder.tuning()) {
            case C:
                note.set(Note.c1,Note.Accidentals.NONE);
                break;
            case F:
                note.set(Note.f1,Note.Accidentals.NONE);
                break;
        }
    }

    void highestNote()
    {
        switch(recorder.tuning()) {
            case C:
                note.set(Note.g3, Note.Accidentals.NONE);
                break;
            case F:
                note.set(Note.c4, Note.Accidentals.NONE);
                break;
        }
    }

    void noteUp()
    {
        scale.noteUp(note);
        if (!canPlay()) {
            lowestNote();
        }
    }

    void noteDown()
    {
        scale.noteDown(note);
        if (!canPlay()) {
            highestNote();
        }
    }

    void noteUpHalf()
    {
        scale.noteUpHalf(note);
        if (!canPlay()) {
            lowestNote();
        }
    }

    void noteDownHalf()
    {
        scale.noteDownHalf(note);
        if (!canPlay()) {
            highestNote();
        }
    }

    void signatureUp()
    {
        int sig = scale.signature() + 1;
        if (sig > 7) {
            sig = -7;
        }

        scale.signature(sig);
    }

    void signatureDown()
    {
        int sig = scale.signature() - 1;
        if (sig < -7) {
            sig = 7;
        }

        scale.signature(sig);
    }

    int signature() {
        return scale.signature();
    }

    void signature(int sig) {
        scale.signature(sig);
    }

    ArrayList<Grip> grips() {
        return recorder.grips(note, scale);
    }

    int noteNameIndex()
    {
        return scale.noteNameIndex(note);
    }

    boolean canPlay()
    {
        return recorder.canPlay(scale, note);
    }

    void recordertuning(Recorder.Tuning T)
    {
        recorder.tuning(T);
        if (! canPlay()) {
            switch(T){
                case C:
                    note.set(Note.c1, Note.Accidentals.NONE);
                    break;
                case F:
                    note.set(Note.f1, Note.Accidentals.NONE);
                    break;
            }
        }
    }

    Recorder.Tuning recordertuning()
    {
        return recorder.tuning();
    }

    void recorderFingering(Recorder.Fingering F)
    {
        recorder.fingering(F);
        if (! canPlay()) {
            switch(recorder.tuning()){
                case C:
                    note.set(Note.c1, Note.Accidentals.NONE);
                    break;
                case F:
                    note.set(Note.f1, Note.Accidentals.NONE);
                    break;
            }
        }
    }

    int fingeringAsInt()
    {
        return recorder.fingering() == Recorder.Fingering.BAROQUE ? 0 : 1;
    }

    void fingering(int F)
    {
        recorder.fingering(F == 0 ? Recorder.Fingering.BAROQUE : Recorder.Fingering.GERMAN);
    }

    public Recorder.Fingering fingering()
    {
        return recorder.fingering();
    }

    public Clefs clef()
    {
        return scale.clef();
    }

    public void clef(Clefs C)
    {
        scale.clef(C);
    }

    public int clefAsInt()
    {
        return scale.clef() == G ? 0 : 1;
    }

    public void clef(int c)
    {
        scale.clef( c == 0 ? Clefs.G : Clefs.F);
    }

    public int tuningAsInt()
    {
        return recorder.tuning() == Recorder.Tuning.C ? 0 : 1;
    }

    public void tuning(int t)
    {
        recorder.tuning(t == 0 ? Recorder.Tuning.C : Recorder.Tuning.F);
    }

    int noteValue()
    {
        return note.value();
    }

    int noteAccidentalsAsInt()
    {
        switch(note.accidentals()) {
            case NONE:
                return 0;
            case RELEASE:
                return 1;
            case SHARP:
                return 2;
            case FLAT:
                return 3;
            default:
                return -1;
        }
    }

    void note(int value, int acc)
    {
        Note.Accidentals a;

        switch (acc) {
            default:
            case 0:
                a = Note.Accidentals.NONE;
                break;
            case 1:
                a = Note.Accidentals.RELEASE;
                break;
            case 2:
                a = Note.Accidentals.SHARP;
                break;
            case 3:
                a = Note.Accidentals.FLAT;
                break;
        }
        note.set(value, a);
    }
}
