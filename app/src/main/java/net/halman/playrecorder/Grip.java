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


public class Grip {
    private int number_of_holes;
    private int[] holes = null;

    public Grip()
    {
        number_of_holes = 11; // recorder default
        holes = new int[number_of_holes];

        for (int i = 0; i < number_of_holes; ++i) {
             holes[i] = Hole.OPEN;
        }
    }

    public Grip(int[] h)
    {
        number_of_holes = h.length;
        set(h);
    }

    int holes()
    {
        return number_of_holes;
    }

    int get(int idx)
    {
        if (idx >= 0 && idx < number_of_holes) {
            return holes[idx];
        }
        return Hole.OPEN;
    }

    void set(int idx, int hole)
    {
        if (idx >= 0 && idx < number_of_holes) {
            holes[idx] = hole;
        }
    }

    void set(int[] holelist)
    {
        if (holelist == null) {
            return;
        }

        holes = new int[number_of_holes];
        for(int a = 0; a < holelist.length && a < number_of_holes; ++a) {
            holes[a] = holelist[a];
        }
    }
}
