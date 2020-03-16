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

import android.util.SparseArray;
import java.util.ArrayList;

public abstract class MusicalInstrument {

    private int number_of_holes;
    private int instrument_type;
    private Note lowest_note;
    private Note highest_note;
    private int score_offset;
    private SparseArray<ArrayList<Grip>> instrument_grips = null;
    private SparseArray<ArrayList<Grip>> instrument_trill_grips = null;
    private ArrayList<ArrayList<Hole>> holes_positions = null;

    public MusicalInstrument(int type)
    {
        instrument_type = type;
        number_of_holes = 0;
        holes_positions = new ArrayList<ArrayList<Hole>>();
        holes_positions.add(new ArrayList<Hole>());
        holes_positions.add(new ArrayList<Hole>());
        lowest_note = null;
        highest_note = null;
        score_offset = 0;
    }

    public int holes()
    {
        return number_of_holes;
    }

    public void holes(int holes)
    {
        number_of_holes = holes;
    }

    public int type()
    {
        return instrument_type;
    }

    public void type(int atype)
    {
        instrument_type = atype;
    }

    public boolean canPlay(Scale scale, Note realNote) {
        if (lowest_note == null || highest_note == null) {
            return false;
        }

        int n = scale.noteAbsoluteValue(realNote);
        int min = scale.noteAbsoluteValue(realLowestNote());
        int max = scale.noteAbsoluteValue(realHighestNote());
        return (n >= min) && (n <= max);
    }

    Hole hole(int orientation, int index) {
        if (orientation < 0 || orientation > 1) {
            return null;
        }
        return holes_positions.get(orientation).get(index);
    }

    void hole(int orientation, int x, int y, double zoom)
    {
        ArrayList<Hole> list = holes_positions.get(orientation);
        if (list != null) {
            list.add(new Hole(x, y, zoom));
        }
    }

    Note realLowestNote() {
        return lowest_note;
    }

    void realLowestNote(Note n) {
        lowest_note = new Note(n);
    }

    Note realHighestNote() {
        return highest_note;
    }

    void realHighestNote(Note n) {
        highest_note = new Note(n);
    }

    Note apparentLowestNote () {
        if (lowest_note == null) {
            return null;
        }
        return new Note(lowest_note.value() + score_offset, lowest_note.accidentals(), false);
    }

    Note apparentHighestNote () {
        if (highest_note == null) {
            return null;
        }
        return new Note(highest_note.value() + score_offset, highest_note.accidentals(), false);
    }

    int scoreOffset()
    {
        return score_offset;
    }

    void scoreOffset(int offset)
    {
        score_offset = offset;
    }

    Note apparentNoteToRealNote(Note n)
    {
        return new Note(n.value() - score_offset, n.accidentals(), n.trill());
    }

    Note realNoteToApparentNote(Note n)
    {
        return new Note(n.value() + score_offset, n.accidentals(), n.trill());
    }

    void addGrip(int noteValue, Grip grip)
    {
        if (grip == null) {
            return;
        }

        if (instrument_grips == null) {
            instrument_grips = new SparseArray<>();
        }

        ArrayList<Grip> grips = instrument_grips.get(noteValue);
        if (grips == null) {
            grips = new ArrayList<>();
            instrument_grips.put(noteValue, grips);
        }

        grips.add(grip);
    }

    ArrayList<Grip> getGrips(int noteValue) {
        return instrument_grips.get(noteValue);
    }

    public ArrayList<Grip> grips(Scale scale, Note realNote)
    {
        int idx = scale.noteAbsoluteValue(realNote);
        return getGrips(idx);
    }

    void grips(SparseArray<ArrayList<Grip>> array_of_grips)
    {
        instrument_grips = array_of_grips;
    }

    boolean hasTrills()
    {
        return instrument_trill_grips != null;
    }

    void addTrillGrip(int baseNoteValue, int higherNoteValue, Grip grip)
    {
        if (instrument_trill_grips == null) {
            instrument_trill_grips = new SparseArray<>();
        }

        int key = baseNoteValue + 100 * higherNoteValue;
        ArrayList<Grip> grips = instrument_trill_grips.get(key);

        if (grips == null) {
            grips = new ArrayList<>();
            instrument_trill_grips.put(key, grips);
        }

        grips.add(grip);
    }

    public ArrayList<Grip> getTrillGrip(int baseNoteValue, int higherNoteValue) {
        int key = baseNoteValue + 100 * higherNoteValue;
        return instrument_trill_grips.get(key);
    }

    public ArrayList<Grip> trillGrips(Scale scale, Note realNote)
    {
        Note upper = new Note(realNote);
        scale.noteUp(upper);
        int idx = scale.noteAbsoluteValue(realNote);
        int idx_upper = scale.noteAbsoluteValue(upper);
        if (idx == idx_upper) {
            scale.noteUp(upper);
            idx_upper = scale.noteAbsoluteValue(upper);
        }

        return getTrillGrip(idx, idx_upper);
    }

    void trillGrips(SparseArray<ArrayList<Grip>> array_of_grips)
    {
        instrument_trill_grips = array_of_grips;
    }
}
