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

    protected int number_of_holes;
    protected int instrument_type;
    private Note lowest_note;
    private Note highest_note;
    private int score_offset;
    private SparseArray<ArrayList<Grip>> trill_grips = null;
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

    public int holes() {
        return number_of_holes;
    }

    public int type() {
        return instrument_type;
    }

    public boolean canPlay(Scale scale, Note realNote) {
        return (grips(scale, realNote) != null);
    }

    abstract public ArrayList<Grip> grips(Scale scale, Note realNote);

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
        return lowest_note;
    }

    void realHighestNote(Note n) {
        highest_note = new Note(n);
    }

    Note apparentLowestNote () {
        if (lowest_note == null) {
            return null;
        }
        return new Note(lowest_note.value() + score_offset, lowest_note.accidentals());
    }

    Note apparentHighestNote () {
        if (highest_note == null) {
            return null;
        }
        return new Note(highest_note.value() + score_offset, highest_note.accidentals());
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
        return new Note(n.value() - score_offset, n.accidentals());
    }

    Note realNoteToApparentNote(Note n)
    {
        return new Note(n.value() + score_offset, n.accidentals());
    }

    boolean hasTrills()
    {
        return trill_grips != null;
    }

    void addTrillGrip(int baseNoteValue, int higherNoteValue, Grip grip)
    {
        if (trill_grips == null) {
            trill_grips = new SparseArray<>();
        }
        int key = baseNoteValue + 100 * higherNoteValue;
        ArrayList<Grip> grips = trill_grips.get(key);
        if (grips == null) {
            grips = new ArrayList<>();
            trill_grips.put(key, grips);
        }
        grips.add(grip);
    }

    ArrayList<Grip> getTrillGrip(int baseNoteValue, int higherNoteValue) {
        int key = baseNoteValue + 100 * higherNoteValue;
        return trill_grips.get(key);
    }

    void clearTrillGrips()
    {
        trill_grips = null;
    }
}
