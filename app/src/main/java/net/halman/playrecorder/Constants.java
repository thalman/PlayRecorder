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

public class Constants {

    // instrument types
    static final int RECORDER_SOPRANINO_BAROQUE = 0;
    static final int RECORDER_SOPRANO_BAROQUE = 1;
    static final int RECORDER_ALT_BAROQUE = 2;
    static final int RECORDER_TENOR_BAROQUE = 3;
    static final int RECORDER_BASS_BAROQUE = 4;

    static final int RECORDER_SOPRANINO_GERMAN = 8;
    static final int RECORDER_SOPRANO_GERMAN = 9;
    static final int RECORDER_ALT_GERMAN = 10;
    static final int RECORDER_TENOR_GERMAN = 11;
    static final int RECORDER_BASS_GERMAN = 12;

    static final int TIN_WHISTLE_D = 20;
    static final int TIN_WHISTLE_G = 21;

    static boolean isRecorder(int type) {
        return (type >= RECORDER_SOPRANINO_BAROQUE && type <= RECORDER_BASS_GERMAN);
    }

    static boolean isBaroqueRecorder(int type) {
        return (type >= RECORDER_SOPRANINO_BAROQUE && type <= RECORDER_BASS_BAROQUE);
    }

    static boolean isGermanRecorder(int type) {
        return (type >= RECORDER_SOPRANINO_GERMAN && type <= RECORDER_BASS_GERMAN);
    }

    static boolean isTinWhistle(int type) {
        return (type >= TIN_WHISTLE_D && type <= TIN_WHISTLE_G);
    }
}
