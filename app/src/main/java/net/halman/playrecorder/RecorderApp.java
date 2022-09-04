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

import android.util.Pair;

import java.util.ArrayList;

import static net.halman.playrecorder.Scale.*;
import static net.halman.playrecorder.Scale.Clefs.G;

public class RecorderApp {
    public Scale scale = new Scale(0);
    public Note apparent_note = new Note(Note.c4, Note.Accidentals.NONE, false);
    private MusicalInstrument musical_instrument = new Recorder();
    private int last_recorder_fingering = Recorder.BAROQUE;

    int notePosition()
    {
        return scale.notePosition(apparent_note);
    }

    void noteByPosition(int position)
    {
        boolean tr = apparent_note.trill();
        apparent_note = scale.noteByPosition(position);
        apparent_note.trill(tr);
        if (! canPlay()) {
            if (position < 5) {
                apparentLowestNote();
            } else {
                apparentHighestNote();
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
        if (Constants.isFife(type)) {
            musical_instrument = new YamahaFife(type);
            return;
        }
        musical_instrument = new Recorder(Constants.RECORDER_SOPRANO_BAROQUE);
        lastRecorderFingering(Recorder.BAROQUE);
    }

    void apparentLowestNote()
    {
        boolean tr = noteTrill();
        Note lowest = musical_instrument.apparentLowestNote();
        Note tmp = new Note(lowest.value(), Note.Accidentals.NONE, false);
        if (scale.noteAbsoluteValue(tmp) == scale.noteAbsoluteValue(lowest)) {
            apparent_note.set(tmp);
        } else {
            apparent_note.set(lowest);
        }
        apparent_note.trill(tr);
    }

    int instrumentHighestFreq100() {
        Note n = musical_instrument.realHighestNote();
        return scale.noteToFrequency(n);
    }

    void apparentHighestNote()
    {
        boolean tr = noteTrill();
        Note highest = musical_instrument.apparentHighestNote();
        Note tmp = new Note(highest.value(), Note.Accidentals.NONE, false);
        if (scale.noteAbsoluteValue(tmp) == scale.noteAbsoluteValue(highest)) {
            apparent_note.set(tmp);
        } else {
            apparent_note.set(highest);
        }
        apparent_note.trill(tr);
    }

    void noteUp()
    {
        scale.noteUp(apparent_note);
        if (!canPlay()) {
            apparentLowestNote();
        }
    }

    void noteDown()
    {
        scale.noteDown(apparent_note);
        if (!canPlay()) {
            apparentHighestNote();
        }
    }

    void noteUpHalf()
    {
        scale.noteUpHalf(apparent_note);
        if (!canPlay()) {
            apparentLowestNote();
        }
    }

    void noteDownHalf()
    {
        scale.noteDownHalf(apparent_note);
        if (!canPlay()) {
            apparentHighestNote();
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
        if (apparent_note.trill()) {
            return musical_instrument.trillGrips(scale, musical_instrument.apparentNoteToRealNote(apparent_note));
        } else {
            return musical_instrument.grips(scale, musical_instrument.apparentNoteToRealNote(apparent_note));
        }
    }

    int noteNameIndex()
    {
        return scale.noteNameIndex(apparent_note);
    }

    boolean canPlay()
    {
        return musical_instrument.canPlay(scale, musical_instrument.apparentNoteToRealNote(apparent_note));
    }

    boolean canPlay(Note n)
    {
        return musical_instrument.canPlay(scale, n);
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
        return apparent_note.value();
    }

    int noteAccidentalsAsInt()
    {
        switch(apparent_note.accidentals()) {
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

    void realNote(Note real)
    {
        apparent_note.set(musical_instrument.realNoteToApparentNote(real));
    }

    void apparentNote(Note apparent)
    {
        apparent_note.set(apparent);
    }

    void apparentNote(int value, int acc, boolean t)
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
        apparent_note.set(value, a, t);
    }

    void realNote(int value, int acc, boolean tr)
    {
        apparentNote(value, acc, tr);
        apparent_note = musical_instrument.realNoteToApparentNote(apparent_note);
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

    boolean noteTrill()
    {
        return apparent_note.trill();
    }

    void noteTrill(boolean t)
    {
        apparent_note.trill(t && musical_instrument.hasTrills());
    }

    boolean canPlayTrills()
    {
        return musical_instrument.hasTrills();
    }

    void checkLimits()
    {
        if (apparent_note.trill() && !musical_instrument.hasTrills()) {
            apparent_note.trill(false);
        }

        if (!musical_instrument.canPlay(scale, musical_instrument.apparentNoteToRealNote(apparent_note))) {
            int idx = scale.noteAbsoluteValue(apparent_note);
            if (idx < 10) {
                apparent_note.set(musical_instrument.apparentLowestNote());
            } else {
                apparent_note.set(musical_instrument.apparentHighestNote());
            }
        }
    }

    int getMidiNote() {
        return scale.noteMidiValue(musical_instrument.apparentNoteToRealNote(apparent_note));
    }

    Pair<Integer, Integer> getTrillMidiNotes() {
        Pair<Note, Note> trill = musical_instrument.trillNotes(scale, musical_instrument.apparentNoteToRealNote(apparent_note));
        return new Pair<>(scale.noteMidiValue(trill.first), scale.noteMidiValue(trill.second));
    }
}
