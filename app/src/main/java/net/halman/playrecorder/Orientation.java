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

public class Orientation {
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;

    public static int normalize(int orientation)
    {
        while (orientation < UP) {
            orientation += 4;
        }
        return orientation % 4;
    }

    public static int inc(int orientation)
    {
        return normalize(++orientation);
    }

    public static int dec(int orientation)
    {
        return normalize(--orientation);
    }

    public static int add(int orientation, int add)
    {
        return normalize(orientation + add);
    }

    public static boolean vertical(int orientation)
    {
        int o = normalize(orientation);
        return (o == UP || o == DOWN);
    }
}
