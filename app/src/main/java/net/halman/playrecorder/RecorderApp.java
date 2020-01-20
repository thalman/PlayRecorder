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

public class RecorderApp implements Serializable {
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
        switch(recorder.tunning()) {
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
        switch(recorder.tunning()) {
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

    void recorderTunning(Recorder.Tunning T)
    {
        recorder.tunning(T);
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

    Recorder.Tunning recorderTunning()
    {
        return recorder.tunning();
    }

    void recorderFingering(Recorder.Fingering F)
    {
        recorder.fingering(F);
        if (! canPlay()) {
            switch(recorder.tunning()){
                case C:
                    note.set(Note.c1, Note.Accidentals.NONE);
                    break;
                case F:
                    note.set(Note.f1, Note.Accidentals.NONE);
                    break;
            }
        }
    }

    Recorder.Fingering recorderFingering()
    {
        return recorder.fingering();
    }

    public Scale.Clefs clef()
    {
        return scale.clef();
    }

    public void clef(Scale.Clefs C)
    {
        scale.clef(C);
    }
}
