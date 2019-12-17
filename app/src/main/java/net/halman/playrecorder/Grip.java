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

    static final int NUMBER_OF_HOLES = 11;

    private Hole[] holes = new Hole[NUMBER_OF_HOLES];

    public Grip()
    {
        for (int i = 0; i < NUMBER_OF_HOLES; ++i) {
             holes[i] = Hole.OPEN;
        }
    }

    public Grip(Hole h0, Hole h1, Hole h2, Hole h3, Hole h4,
                Hole h5, Hole h6, Hole h7, Hole h8, Hole h9,
                Hole h10)
    {
        set(h0, h1, h2, h3, h4, h5, h6, h7, h8, h9, h10);
    }

    Hole get(int idx)
    {
        if (idx >= 0 && idx < NUMBER_OF_HOLES) {
            return holes[idx];
        }
        return Hole.OPEN;
    }

    void set(int idx, Hole hole)
    {
        if (idx >= 0 && idx < NUMBER_OF_HOLES) {
            holes[idx] = hole;
        }
    }

    void set(Hole h0, Hole h1, Hole h2, Hole h3, Hole h4,
             Hole h5, Hole h6, Hole h7, Hole h8, Hole h9,
             Hole h10)
    {
        holes[0] = h0;
        holes[1] = h1;
        holes[2] = h2;
        holes[3] = h3;
        holes[4] = h4;
        holes[5] = h5;
        holes[6] = h6;
        holes[7] = h7;
        holes[8] = h8;
        holes[9] = h9;
        holes[10] = h10;
    }

    void set(ArrayList<Hole> holelist)
    {
        for(int a = 0; a < holelist.size() && a < NUMBER_OF_HOLES; ++a) {
            holes[a] = holelist.get(a);
        }
    }
}
