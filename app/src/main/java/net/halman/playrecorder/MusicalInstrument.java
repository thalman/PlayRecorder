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

import android.graphics.Rect;
import android.util.Pair;
import android.util.SparseArray;

import java.util.ArrayList;

public abstract class MusicalInstrument {

    private int instrument_type;
    private Note lowest_note;
    private Note highest_note;
    private int score_offset;
    private SparseArray<ArrayList<Grip>> instrument_grips = null;
    private SparseArray<ArrayList<Grip>> instrument_trill_grips = null;
    private final ArrayList<Hole> holes_positions;
    private final Rect grip_size = new Rect();

    public MusicalInstrument(int type)
    {
        instrument_type = type;
        holes_positions = new ArrayList<>();
        lowest_note = null;
        highest_note = null;
        score_offset = 0;
    }

    public int holes()
    {
        return holes_positions.size();
    }

    private void updateSize() {
        int minx, miny, maxx, maxy;
        if (holes_positions.size() > 0) {
            Hole h = holes_positions.get(0);
            minx = h.x;
            miny = h.y;
            maxx = minx;
            maxy = miny;
        } else {
            minx = 0;
            miny = 0;
            maxx = 0;
            maxy = 0;
        }
        for (Hole h: holes_positions) {
            if (h.x > maxx) { maxx = h.x; }
            if (h.y > maxy) { maxy = h.y; }
            if (h.x < minx) { minx = h.x; }
            if (h.y < miny) { miny = h.y; }
        }

        grip_size.set(minx, miny, maxx, maxy);
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

    private Hole holeTransform(int orientation, Hole h) {
        if (h != null) {
            switch (Orientation.normalize(orientation)) {
                case Orientation.UP:
                    return h;
                case Orientation.DOWN:
                    return new Hole(-h.x, grip_size.top + grip_size.height() - h.y, h.zoom, h.orientation);
                case Orientation.LEFT:
                    return new Hole(h.y, -h.x, h.zoom, h.orientation);
                case Orientation.RIGHT:
                    return new Hole(grip_size.top + grip_size.height() - h.y, grip_size.top + h.x, h.zoom, h.orientation);
            }
        }
        return null;
    }

    Hole hole(int orientation, int index) {
        return holeTransform(orientation, holes_positions.get(index));
    }

    void hole(int x, int y, double zoom, int orientation)
    {
        holes_positions.add(new Hole(x, y, zoom, orientation));
        updateSize();
    }

    void hole(int x, int y, double zoom)
    {
        hole(x, y, zoom, Orientation.UP);
    }

    Rect gripSize()
    {
        return grip_size;
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

    Pair<Note,Note> trillNotes(Scale scale, Note real_note)
    {
        Note base = new Note(real_note);
        Note upper = new Note(base);
        scale.noteUp(upper);

        int idx = scale.noteAbsoluteValue(base);
        int idx_upper = scale.noteAbsoluteValue(upper);
        if (idx == idx_upper) {
            scale.noteUp(upper);
        }

        return new Pair<>(base, upper);
    }
}
