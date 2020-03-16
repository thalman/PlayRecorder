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

public class Hole {
    public static final int OPEN = 0;
    public static final int CLOSE = 1;
    public static final int HALFOPEN = 2;
    public static final int BELLOPEN = 3;
    public static final int BELLCLOSE = 4;
    public static final int TRILL = 5;
    public static final int TRILLONCE = 6;

    int x;
    int y;
    double zoom;

    Hole(int x, int y, double zoom)
    {
        this.x = x;
        this.y = y;
        this.zoom = zoom;
    }
}
