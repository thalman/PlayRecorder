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

public abstract class MusicalInstrument {

    protected int number_of_holes;
    protected int instrument_type;
    protected int instrument_tuning;

    protected ArrayList<ArrayList<Hole>> holesPositions = null;

    public MusicalInstrument(int type, int tunning)
    {
        instrument_type = type;
        instrument_tuning = tunning;
        number_of_holes = 0;
        holesPositions = new ArrayList<ArrayList<Hole>>();
        holesPositions.add(new ArrayList<Hole>());
        holesPositions.add(new ArrayList<Hole>());
    }

    public int holes() {
        return number_of_holes;
    }


    public int tuning() {
        return instrument_tuning;
    }

    public int type() {
        return instrument_type;
    }

    public boolean canPlay(Scale scale, Note note) {
        return (grips(scale, note) != null);
    }

    abstract public ArrayList<Grip> grips(Scale scale, Note note);

    Hole hole(int orientation, int index) {
        if (orientation < 0 || orientation > 1) {
            return null;
        }
        return holesPositions.get(orientation).get(index);
    }

    void hole(int orientation, int x, int y, double zoom)
    {
        ArrayList<Hole> list = holesPositions.get(orientation);
        if (list != null) {
            list.add(new Hole(x, y, zoom));
        }
    }
}
