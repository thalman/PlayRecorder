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

import static net.halman.playrecorder.Grip.Hole.CLOSE;
import static net.halman.playrecorder.Grip.Hole.OPEN;
import static net.halman.playrecorder.Grip.Hole.HALFOPEN;

public class Recorder {
    enum Fingering {
        BAROQUE,
        GERMAN
    }

    enum Tuning {
        C,
        F
    }

    private Fingering recorderFingering;
    private Tuning recorderTuning;
    private ArrayList<ArrayList<Grip>> grips = null;

    Recorder(Fingering f, Tuning t)
    {
        fingering(f);
        tuning(t);
    }

    Recorder()
    {
        fingering(Fingering.BAROQUE);
        tuning(Tuning.C);
    }

    Tuning tuning()
    {
        return recorderTuning;
    }

    void tuning(Tuning tuning)
    {
        recorderTuning = tuning;
    }

    Fingering fingering()
    {
        return recorderFingering;
    }

    void fingering(Fingering f)
    {
        recorderFingering = f;
        switch (f) {
            case BAROQUE:
                setBaroqueGrips();
                break;
            case GERMAN:
                setGermanGrips();
                break;
        }
    }

    private void setBaroqueGrips()
    {
        grips = new ArrayList<ArrayList<Grip>>();

        // C/F
        ArrayList<Grip> grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN));
        grips.add(grip);

        // C#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN));
        grips.add(grip);

        // D
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grips.add(grip);

        // D#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // E
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // F
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, OPEN));
        grips.add(grip);

        // F#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grips.add(grip);

        // G
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // G#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // A
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // A#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, OPEN, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grip.add(new Grip(CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // B
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grip.add(new Grip(CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // C
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grip.add(new Grip(OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // C#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grip.add(new Grip(OPEN, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grip.add(new Grip(OPEN, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // D
        grip = new ArrayList<Grip>();
        grip.add(new Grip(OPEN, OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // D#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(OPEN, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grips.add(grip);

        // E
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // F
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grips.add(grip);

        // F#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, CLOSE, HALFOPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, CLOSE, HALFOPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // G
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // G#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // A
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // A#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN));
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, OPEN, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, OPEN, OPEN, CLOSE, OPEN, OPEN, CLOSE, CLOSE, OPEN));
        grips.add(grip);

        // B
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // C
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, OPEN, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // C#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, CLOSE));
        grip.add(new Grip(CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, CLOSE, CLOSE, OPEN));
        grip.add(new Grip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, CLOSE, CLOSE, CLOSE));
        grips.add(grip);

        // D
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grip.add(new Grip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN));
        grips.add(grip);

        // D#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grips.add(grip);

        // E
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, CLOSE));
        grips.add(grip);

        // F
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, CLOSE));
        grips.add(grip);

        // F#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // G
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, OPEN, OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

    }

    private void setGermanGrips()
    {
        grips = new ArrayList<ArrayList<Grip>>();

        // C/F
        ArrayList<Grip> grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN));
        grips.add(grip);

        // C#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN));
        grips.add(grip);

        // D
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grips.add(grip);

        // D#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // E
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // F
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // F#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN));
        grips.add(grip);

        // G
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // G#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // A
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // A#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, OPEN, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grip.add(new Grip(CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // B
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grip.add(new Grip(CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // C
        grip = new ArrayList<Grip>();
        grip.add(new Grip(CLOSE, OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grip.add(new Grip(OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // C#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grip.add(new Grip(OPEN, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grip.add(new Grip(OPEN, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // D
        grip = new ArrayList<Grip>();
        grip.add(new Grip(OPEN, OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // D#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(OPEN, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grips.add(grip);

        // E
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // F
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // F#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN));
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, OPEN, OPEN, CLOSE, CLOSE, OPEN));
        grips.add(grip);

        // G
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // G#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN));
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, HALFOPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // A
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // A#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN));
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, OPEN, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, OPEN, OPEN, CLOSE, OPEN, OPEN, CLOSE, CLOSE, OPEN));
        grips.add(grip);

        // B
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // C
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, OPEN, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // C#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, CLOSE));
        grip.add(new Grip(CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, CLOSE, CLOSE, OPEN));
        grip.add(new Grip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, CLOSE, CLOSE, CLOSE));
        grip.add(new Grip(CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE));
        grips.add(grip);

        // D
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN));
        grip.add(new Grip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grips.add(grip);

        // D#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grips.add(grip);

        // E
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, CLOSE));
        grips.add(grip);

        // F
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, CLOSE));
        grips.add(grip);

        // F#
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // G
        grip = new ArrayList<Grip>();
        grip.add(new Grip(HALFOPEN, CLOSE, OPEN, OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

    }

    ArrayList<Grip> grips(Note note, Scale scale)
    {
        int idx = scale.noteAbsoluteValue(note);
        if (tuning() == Tuning.F) {
            idx = idx - 5;
        }

        if ((idx >= 0) && (idx < grips.size())) {
            return grips.get(idx);
        }
        return null;
    }

    boolean canPlay(Scale scale, Note note) {
        return (grips(note, scale) != null);
    }

}
