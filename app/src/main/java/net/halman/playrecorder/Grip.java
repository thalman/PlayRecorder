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

public class Grip {
    enum Hole {
        OPEN,
        CLOSE,
        HALFOPEN
    }

    private int number_of_holes = 11;
    private Hole[] holes = null;

    public Grip()
    {
        number_of_holes = 11; // recorder default
        holes = new Hole[number_of_holes];

        for (int i = 0; i < number_of_holes; ++i) {
             holes[i] = Hole.OPEN;
        }
    }

    public Grip(ArrayList<Hole> h)
    {
        number_of_holes = h.size();
        holes = new Hole[number_of_holes];

        for(int i = 0; i < h.size(); ++i) {
            holes[i] = h.get(i);
        }
    }

    int holes()
    {
        return number_of_holes;
    }

    Hole get(int idx)
    {
        if (idx >= 0 && idx < number_of_holes) {
            return holes[idx];
        }
        return Hole.OPEN;
    }

    void set(int idx, Hole hole)
    {
        if (idx >= 0 && idx < number_of_holes) {
            holes[idx] = hole;
        }
    }
    
    void set(ArrayList<Hole> holelist)
    {
        for(int a = 0; a < holelist.size() && a < number_of_holes; ++a) {
            holes[a] = holelist.get(a);
        }
    }
}
