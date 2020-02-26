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
    public Note note = new Note(Note.c4, Note.Accidentals.NONE);
    private MusicalInstrument musical_instrument = new Recorder();
    private int last_recorder_fingering = Recorder.BAROQUE;

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

    void instrument(int type)
    {
        if (Constants.isRecorder(type)) {
            musical_instrument = new Recorder(type);
            lastRecorderFingering((Constants.isBaroqueRecorder(type) ? Recorder.BAROQUE : Recorder.GERMAN));
            return;
        }
        if (Constants.isTinWhistle(type)) {
            musical_instrument = new TinWhistle(type);
            return;
        }
        musical_instrument = new Recorder(Constants.RECORDER_SOPRANO_BAROQUE);
        lastRecorderFingering(Recorder.BAROQUE);
    }

    void lowestNote()
    {
        Note lowest = musical_instrument.lowestNote();
        Note tmp = new Note(lowest.value(), Note.Accidentals.NONE);
        if (scale.noteAbsoluteValue(tmp) == scale.noteAbsoluteValue(lowest)) {
            note.set(tmp);
        } else {
            note.set(lowest);
        }
    }

    void highestNote()
    {
        note.set(musical_instrument.highestNote());
        Note highest = musical_instrument.highestNote();
        Note tmp = new Note(highest.value(), Note.Accidentals.NONE);
        if (scale.noteAbsoluteValue(tmp) == scale.noteAbsoluteValue(highest)) {
            note.set(tmp);
        } else {
            note.set(highest);
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
        return musical_instrument.grips(scale, note);
    }

    int noteNameIndex()
    {
        return scale.noteNameIndex(note);
    }

    boolean canPlay()
    {
        return musical_instrument.canPlay(scale, note);
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

    public int instrumentType()
    {
        return musical_instrument.type();
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

    int numberOfHoles()
    {
        return musical_instrument.holes();
    }

    Hole getHole(int orientation, int index)
    {
        return musical_instrument.hole(orientation, index);
    }

    int lastRecorderFingering() {
        return last_recorder_fingering;
    }

    void lastRecorderFingering(int fingering) {
        last_recorder_fingering = fingering;
    }
}
