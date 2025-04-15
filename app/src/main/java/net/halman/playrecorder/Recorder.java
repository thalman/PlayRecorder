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

import static net.halman.playrecorder.Hole.BELLCLOSE;
import static net.halman.playrecorder.Hole.BELLOPEN;
import static net.halman.playrecorder.Hole.CLOSE;
import static net.halman.playrecorder.Hole.HALFOPEN;
import static net.halman.playrecorder.Hole.OPEN;
import static net.halman.playrecorder.Hole.TRILL;
import static net.halman.playrecorder.Hole.TRILLONCE;
import static net.halman.playrecorder.Orientation.DOWN;
import static net.halman.playrecorder.Orientation.UP;

public class Recorder extends MusicalInstrument {

    public final static int BAROQUE = 0;
    public final static int GERMAN = 1;

    private int recorder_fingering;

    public Recorder(int atype)
    {
        super(atype);
        if (!Constants.isRecorder(atype)) {
            type(Constants.RECORDER_SOPRANO_BAROQUE);
        }

        setHoles();
        setLimits();
        fingering();
    }

    public Recorder()
    {
        super(Constants.RECORDER_SOPRANO_BAROQUE);
        setHoles();
        setLimits();
        fingering();
    }

    private void setHoles() {
        hole(0, 0, 1.0);
        hole(0, 50, 1.0);
        hole(0, 80, 1.0);
        hole(0, 110, 1.0);
        hole(0, 160, 1.0);
        hole(0, 190, 1.0);
        hole(-12, 220, 0.9);
        hole(+12, 220, 0.9);
        hole(-12, 250, 0.9);
        hole(+12, 250, 0.9);
        hole(0, 280, 1.0);
    }

    private void setLimits() {
        switch (type()) {
            case Constants.RECORDER_SOPRANINO_BAROQUE:
            case Constants.RECORDER_SOPRANINO_GERMAN:
                realLowestNote(new Note(Note.f5, Note.Accidentals.RELEASE, false));
                realHighestNote(new Note(Note.c8, Note.Accidentals.RELEASE, false));
                scoreOffset(-12);
                break;
            case Constants.RECORDER_SOPRANO_BAROQUE:
            case Constants.RECORDER_SOPRANO_GERMAN:
                realLowestNote(new Note(Note.c5, Note.Accidentals.RELEASE, false));
                realHighestNote(new Note(Note.g7, Note.Accidentals.RELEASE, false));
                scoreOffset(-12);
                break;
            case Constants.RECORDER_ALT_BAROQUE:
            case Constants.RECORDER_ALT_GERMAN:
                realLowestNote(new Note(Note.f4, Note.Accidentals.RELEASE, false));
                realHighestNote(new Note(Note.c7, Note.Accidentals.RELEASE, false));
                scoreOffset(0);
                break;
            case Constants.RECORDER_TENOR_BAROQUE:
            case Constants.RECORDER_TENOR_GERMAN:
                realLowestNote(new Note(Note.c4, Note.Accidentals.RELEASE, false));
                realHighestNote(new Note(Note.g6, Note.Accidentals.RELEASE, false));
                scoreOffset(0);
                break;
            case Constants.RECORDER_BASS_BAROQUE:
            case Constants.RECORDER_BASS_GERMAN:
                realLowestNote(new Note(Note.f3, Note.Accidentals.RELEASE, false));
                realHighestNote(new Note(Note.c6, Note.Accidentals.RELEASE, false));
                scoreOffset(12);
                break;
        }
    }

    private void fingering()
    {
        if (type() <= Constants.RECORDER_BASS_BAROQUE ) {
            recorder_fingering = BAROQUE;
        } else {
            recorder_fingering = GERMAN;
        }

        switch (recorder_fingering) {
            case BAROQUE:
                setBaroqueGrips();
                setBaroqueTrillGrips();
                break;
            case GERMAN:
                setGermanGrips();
                setGermanTrillGrips();
                break;
        }
    }

    private Grip recorderGrip(int h0, int h1, int h2, int h3,
                              int h4, int h5, int h6, int h7,
                              int h8, int h9, int h10)
    {
        int [] gripArray = new int [] {h0, h1, h2, h3, h4, h5, h6, h7, h8, h9, h10};

        return new Grip(gripArray);
    }

    private void setBaroqueGrips()
    {
        grips(null);
        int idx = new Scale(0).noteAbsoluteValue(realLowestNote());

        // C/F
        addGrip(idx, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, BELLOPEN));
        // C#
        addGrip(idx + 1, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, BELLOPEN));
        // D
        addGrip(idx + 2, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        // D#
        addGrip(idx + 3, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, BELLOPEN));
        // E
        addGrip(idx + 4, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // F
        addGrip(idx + 5, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, BELLOPEN));
        // F#
        addGrip(idx + 6, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        // G
        addGrip(idx + 7, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // G#
        addGrip(idx + 8, recorderGrip(CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, BELLOPEN));
        // A
        addGrip(idx + 9, recorderGrip(CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // A#
        addGrip(idx + 10, recorderGrip(CLOSE, CLOSE, OPEN, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 10, recorderGrip(CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // B
        addGrip(idx + 11, recorderGrip(CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 11, recorderGrip(CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // C
        addGrip(idx + 12, recorderGrip(CLOSE, OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 12, recorderGrip(OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // C#
        addGrip(idx + 13, recorderGrip(OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 13, recorderGrip(OPEN, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 13, recorderGrip(OPEN, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // D
        addGrip(idx + 14, recorderGrip(OPEN, OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // D#
        addGrip(idx + 15, recorderGrip(OPEN, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        // E
        addGrip(idx + 16, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // F
        addGrip(idx + 17, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        // F#
        addGrip(idx + 18, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 18, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, HALFOPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 18, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, HALFOPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 18, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, BELLOPEN));
        // G
        addGrip(idx + 19, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // G#
        addGrip(idx + 20, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // A
        addGrip(idx + 21, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // A#
        addGrip(idx + 22, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 22, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 22, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 22, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, OPEN, CLOSE, OPEN, OPEN, CLOSE, CLOSE, BELLOPEN));
        // B
        addGrip(idx + 23, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // C
        addGrip(idx + 24, recorderGrip(HALFOPEN, CLOSE, OPEN, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // C#
        addGrip(idx + 25, recorderGrip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, BELLCLOSE));
        addGrip(idx + 25, recorderGrip(CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, CLOSE, CLOSE, BELLOPEN));
        addGrip(idx + 25, recorderGrip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, CLOSE, CLOSE, BELLCLOSE));
        // D
        addGrip(idx + 26, recorderGrip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 26, recorderGrip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, BELLOPEN));
        // D#
        addGrip(idx + 27, recorderGrip(HALFOPEN, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        // E
        addGrip(idx + 28, recorderGrip(HALFOPEN, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLCLOSE));
        // F
        addGrip(idx + 29, recorderGrip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLCLOSE));
        // F#
        addGrip(idx + 30, recorderGrip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // G
        addGrip(idx + 31, recorderGrip(HALFOPEN, CLOSE, OPEN, OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
    }

    private void setBaroqueTrillGrips() {
        trillGrips(null);
        int idx = new Scale(0).noteAbsoluteValue(realLowestNote());

        // C
        addTrillGrip(idx, idx + 2, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, TRILL, BELLOPEN));
        // C#
        addTrillGrip(idx + 1, idx + 2, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, OPEN, BELLOPEN));
        // D
        addTrillGrip(idx + 2, idx + 3, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, TRILL, TRILL, BELLOPEN));
        addTrillGrip(idx + 2, idx + 4, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, TRILL, OPEN, OPEN, BELLOPEN));
        // D#
        addTrillGrip(idx + 3, idx + 4, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 3, idx + 5, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, CLOSE, OPEN, CLOSE, OPEN, BELLOPEN));
        // E
        addTrillGrip(idx + 4, idx + 5, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, TRILLONCE, TRILLONCE, TRILL, TRILL, BELLOPEN)); //?
        addTrillGrip(idx + 4, idx + 5, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 4, idx + 6, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, TRILLONCE, TRILLONCE, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 4, idx + 6, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, TRILL, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // F
        addTrillGrip(idx + 5, idx + 6, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, TRILL, TRILL, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 5, idx + 7, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, TRILL, OPEN, CLOSE, CLOSE, CLOSE, OPEN, BELLOPEN));
        addTrillGrip(idx + 5, idx + 7, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, TRILL, OPEN, TRILL, TRILL, CLOSE, CLOSE, BELLOPEN));
        addTrillGrip(idx + 5, idx + 7, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, TRILL, OPEN, TRILL, TRILL, OPEN, OPEN, BELLOPEN));
        // F#
        addTrillGrip(idx + 6, idx + 7, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, TRILL, TRILL, TRILL, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 6, idx + 8, recorderGrip(CLOSE, CLOSE, CLOSE, TRILL, TRILLONCE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 6, idx + 8, recorderGrip(CLOSE, CLOSE, CLOSE, TRILL, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        // G
        addTrillGrip(idx + 7, idx + 8, recorderGrip(CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, TRILL, TRILL, TRILL, TRILL, BELLOPEN));
        addTrillGrip(idx + 7, idx + 8, recorderGrip(CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, TRILL, TRILL, CLOSE, CLOSE, BELLOPEN));
        addTrillGrip(idx + 7, idx + 8, recorderGrip(CLOSE, CLOSE, CLOSE, OPEN, CLOSE, TRILL, CLOSE, CLOSE, CLOSE, CLOSE, BELLOPEN));
        addTrillGrip(idx + 7, idx + 9, recorderGrip(CLOSE, CLOSE, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // G#
        addTrillGrip(idx + 8, idx + 9, recorderGrip(CLOSE, CLOSE, CLOSE, OPEN, TRILL, TRILL, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 8, idx + 9, recorderGrip(CLOSE, CLOSE, CLOSE, OPEN, TRILL, TRILL, TRILL, TRILL, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 8, idx + 10, recorderGrip(CLOSE, CLOSE, OPEN, CLOSE, TRILL, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, BELLOPEN));
        addTrillGrip(idx + 8, idx + 10, recorderGrip(CLOSE, CLOSE, TRILL, TRILL, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // A
        addTrillGrip(idx + 9, idx + 10, recorderGrip(CLOSE, CLOSE, OPEN, CLOSE, TRILL, CLOSE, OPEN, OPEN, TRILL, OPEN, BELLOPEN));
        addTrillGrip(idx + 9, idx + 10, recorderGrip(CLOSE, CLOSE, OPEN, CLOSE, CLOSE, TRILL, CLOSE, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 9, idx + 11, recorderGrip(CLOSE, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // A#
        addTrillGrip(idx + 10, idx + 11, recorderGrip(CLOSE, CLOSE, OPEN, TRILL, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 10, idx + 11, recorderGrip(CLOSE, OPEN, CLOSE, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 10, idx + 12, recorderGrip(CLOSE, TRILL, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 10, idx + 12, recorderGrip(CLOSE, TRILL, OPEN, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        // B
        addTrillGrip(idx + 11, idx + 12, recorderGrip(CLOSE, OPEN, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 11, idx + 13, recorderGrip(CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // C
        addTrillGrip(idx + 12, idx + 13, recorderGrip(OPEN, CLOSE, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 12, idx + 13, recorderGrip(CLOSE, OPEN, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 12, idx + 14, recorderGrip(TRILL, OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // C#
        addTrillGrip(idx + 13, idx + 14, recorderGrip(OPEN, TRILL, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 13, idx + 15, recorderGrip(OPEN, TRILL, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 13, idx + 15, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, TRILL, CLOSE, CLOSE, BELLOPEN));
        // D
        addTrillGrip(idx + 14, idx + 15, recorderGrip(OPEN, CLOSE, CLOSE, CLOSE, TRILL, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, BELLOPEN));
        addTrillGrip(idx + 14, idx + 15, recorderGrip(OPEN, CLOSE, CLOSE, CLOSE, TRILL, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, BELLOPEN));
        addTrillGrip(idx + 14, idx + 16, recorderGrip(OPEN, CLOSE, CLOSE, CLOSE, TRILL, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, BELLOPEN));
        addTrillGrip(idx + 14, idx + 16, recorderGrip(OPEN, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, CLOSE, CLOSE, CLOSE, CLOSE, BELLOPEN));
        // D#
        addTrillGrip(idx + 15, idx + 16, recorderGrip(OPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, TRILL, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 15, idx + 16, recorderGrip(OPEN, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, TRILL, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 15, idx + 17, recorderGrip(OPEN, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 15, idx + 17, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, CLOSE, OPEN, OPEN, OPEN, BELLOPEN));
        // E
        addTrillGrip(idx + 16, idx + 17, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, TRILLONCE, TRILLONCE, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 16, idx + 17, recorderGrip(OPEN, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, TRILL, TRILL, BELLOPEN));
        addTrillGrip(idx + 16, idx + 18, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, TRILL, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // F
        addTrillGrip(idx + 17, idx + 18, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, TRILL, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 17, idx + 19, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, TRILL, OPEN, TRILL, TRILL, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 17, idx + 19, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, TRILL, OPEN, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 17, idx + 19, recorderGrip(HALFOPEN, CLOSE, CLOSE, TRILL, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        // F#
        addTrillGrip(idx + 18, idx + 19, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, OPEN, TRILL, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 18, idx + 20, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 18, idx + 20, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, TRILL, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // G
        addTrillGrip(idx + 19, idx + 20, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, OPEN, TRILL, TRILL, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 19, idx + 21, recorderGrip(HALFOPEN, CLOSE, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // G#
        addTrillGrip(idx + 20, idx + 21, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 20, idx + 22, recorderGrip(HALFOPEN, CLOSE, CLOSE, TRILL, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, TRILL, BELLOPEN));
        // A
        addTrillGrip(idx + 21, idx + 22, recorderGrip(HALFOPEN, CLOSE, CLOSE, TRILL, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, BELLOPEN));
        addTrillGrip(idx + 21, idx + 22, recorderGrip(HALFOPEN, CLOSE, CLOSE, HALFOPEN, CLOSE, CLOSE, TRILL, TRILL, CLOSE, OPEN, BELLOPEN));
        addTrillGrip(idx + 21, idx + 22, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, TRILL, TRILL, TRILL, OPEN, BELLOPEN));
        addTrillGrip(idx + 21, idx + 23, recorderGrip(HALFOPEN, CLOSE, CLOSE, HALFOPEN, CLOSE, CLOSE, TRILL, TRILL, TRILL, TRILL, BELLOPEN));
        addTrillGrip(idx + 21, idx + 23, recorderGrip(HALFOPEN, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // A#
        addTrillGrip(idx + 22, idx + 23, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, TRILL, TRILL, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 22, idx + 24, recorderGrip(HALFOPEN, CLOSE, TRILL, OPEN, CLOSE, CLOSE, TRILL, TRILL, OPEN, OPEN, BELLOPEN));
        // B
        addTrillGrip(idx + 23, idx + 24, recorderGrip(HALFOPEN, CLOSE, TRILL, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 23, idx + 24, recorderGrip(HALFOPEN, CLOSE, OPEN, OPEN, TRILL, TRILL, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 23, idx + 25, recorderGrip(HALFOPEN, TRILL, TRILL, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // C
        addTrillGrip(idx + 24, idx + 25, recorderGrip(HALFOPEN, TRILL, OPEN, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 24, idx + 26, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, TRILL, TRILL, OPEN, OPEN, OPEN, OPEN, BELLCLOSE));
    }

    private void setGermanGrips()
    {
        grips(null);
        int idx = new Scale(0).noteAbsoluteValue(realLowestNote());

        // C/F
        addGrip(idx, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, BELLOPEN));
        // C#
        addGrip(idx + 1, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, BELLOPEN));
        // D
        addGrip(idx + 2, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        // D#
        addGrip(idx + 3, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, BELLOPEN));
        // E
        addGrip(idx + 4, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // F
        addGrip(idx + 5, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // F#
        addGrip(idx + 6, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, BELLOPEN));
        // G
        addGrip(idx + 7, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // G#
        addGrip(idx + 8, recorderGrip(CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 8, recorderGrip(CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, BELLOPEN));
        // A
        addGrip(idx + 9, recorderGrip(CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // A#
        addGrip(idx + 10, recorderGrip(CLOSE, CLOSE, OPEN, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 10, recorderGrip(CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // B
        addGrip(idx + 11, recorderGrip(CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 11, recorderGrip(CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // C
        addGrip(idx + 12, recorderGrip(CLOSE, OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 12, recorderGrip(OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // C#
        addGrip(idx + 13, recorderGrip(OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 13, recorderGrip(OPEN, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 13, recorderGrip(OPEN, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // D
        addGrip(idx + 14, recorderGrip(OPEN, OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // D#
        addGrip(idx + 15, recorderGrip(OPEN, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        // E
        addGrip(idx + 16, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // F
        addGrip(idx + 17, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // F#
        addGrip(idx + 18, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 18, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, OPEN, OPEN, CLOSE, CLOSE, BELLOPEN));
        // G
        addGrip(idx + 19, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // G#
        addGrip(idx + 20, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, BELLOPEN));
        addGrip(idx + 20, recorderGrip(HALFOPEN, CLOSE, CLOSE, HALFOPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // A
        addGrip(idx + 21, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // A#
        addGrip(idx + 22, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 22, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 22, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        addGrip(idx + 22, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, OPEN, CLOSE, OPEN, OPEN, CLOSE, CLOSE, BELLOPEN));
        // B
        addGrip(idx + 23, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // C
        addGrip(idx + 24, recorderGrip(HALFOPEN, CLOSE, OPEN, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // C#
        addGrip(idx + 25, recorderGrip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, BELLCLOSE));
        addGrip(idx + 25, recorderGrip(CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, CLOSE, CLOSE, BELLOPEN));
        addGrip(idx + 25, recorderGrip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, CLOSE, CLOSE, BELLCLOSE));
        addGrip(idx + 25, recorderGrip(CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, BELLCLOSE));
        // D
        addGrip(idx + 26, recorderGrip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, BELLOPEN));
        addGrip(idx + 26, recorderGrip(HALFOPEN, CLOSE, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        // D#
        addGrip(idx + 27, recorderGrip(HALFOPEN, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        // E
        addGrip(idx + 28, recorderGrip(HALFOPEN, OPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLCLOSE));
        // F
        addGrip(idx + 29, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLCLOSE));
        // F#
        addGrip(idx + 30, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // G
        addGrip(idx + 31, recorderGrip(HALFOPEN, CLOSE, OPEN, OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
    }

    private void setGermanTrillGrips() {
        trillGrips(null);
        int idx = new Scale(0).noteAbsoluteValue(realLowestNote());

        // C
        addTrillGrip(idx, idx + 2, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, TRILL, BELLOPEN));
        // C#
        addTrillGrip(idx + 1, idx + 2, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, OPEN, BELLOPEN));
        // D
        addTrillGrip(idx + 2, idx + 3, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, TRILL, TRILL, BELLOPEN));
        addTrillGrip(idx + 2, idx + 4, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, TRILL, OPEN, OPEN, BELLOPEN));
        // D#
        addTrillGrip(idx + 3, idx + 4, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 3, idx + 5, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, CLOSE, OPEN, OPEN, OPEN, BELLOPEN));
        // E
        addTrillGrip(idx + 4, idx + 5, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 4, idx + 6, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, TRILL, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // F
        addTrillGrip(idx + 5, idx + 6, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, TRILL, TRILL, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 5, idx + 7, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, TRILL, OPEN, TRILL, TRILL, CLOSE, CLOSE, BELLOPEN));
        addTrillGrip(idx + 5, idx + 7, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // F#
        addTrillGrip(idx + 6, idx + 7, recorderGrip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, TRILL, TRILL, TRILL, CLOSE, CLOSE, BELLOPEN));
        addTrillGrip(idx + 6, idx + 8, recorderGrip(CLOSE, CLOSE, CLOSE, TRILL, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        // G
        addTrillGrip(idx + 7, idx + 8, recorderGrip(CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, TRILL, TRILL, CLOSE, CLOSE, BELLOPEN));
        addTrillGrip(idx + 7, idx + 8, recorderGrip(CLOSE, CLOSE, CLOSE, OPEN, CLOSE, TRILL, CLOSE, CLOSE, CLOSE, CLOSE, BELLOPEN));
        addTrillGrip(idx + 7, idx + 9, recorderGrip(CLOSE, CLOSE, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // G#
        addTrillGrip(idx + 8, idx + 9, recorderGrip(CLOSE, CLOSE, CLOSE, OPEN, TRILL, TRILL, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 8, idx + 9, recorderGrip(CLOSE, CLOSE, CLOSE, OPEN, TRILL, TRILL, TRILL, TRILL, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 8, idx + 10, recorderGrip(CLOSE, CLOSE, TRILL, TRILL, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // A
        addTrillGrip(idx + 9, idx + 11, recorderGrip(CLOSE, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // A#
        addTrillGrip(idx + 10, idx + 11, recorderGrip(CLOSE, OPEN, CLOSE, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 10, idx + 12, recorderGrip(CLOSE, TRILL, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 10, idx + 12, recorderGrip(CLOSE, TRILL, OPEN, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        // B
        addTrillGrip(idx + 11, idx + 12, recorderGrip(CLOSE, OPEN, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 11, idx + 13, recorderGrip(CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // C
        addTrillGrip(idx + 12, idx + 13, recorderGrip(OPEN, CLOSE, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 12, idx + 13, recorderGrip(CLOSE, OPEN, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 12, idx + 14, recorderGrip(TRILL, OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // C#
        addTrillGrip(idx + 13, idx + 14, recorderGrip(OPEN, TRILL, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 13, idx + 15, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, TRILL, CLOSE, CLOSE, BELLOPEN));
        // D - tady
        addTrillGrip(idx + 14, idx + 15, recorderGrip(OPEN, CLOSE, CLOSE, CLOSE, TRILL, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, BELLOPEN));
        addTrillGrip(idx + 14, idx + 15, recorderGrip(OPEN, CLOSE, CLOSE, CLOSE, TRILL, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, BELLOPEN));
        addTrillGrip(idx + 14, idx + 15, recorderGrip(OPEN, TRILL, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, TRILL, BELLOPEN));
        addTrillGrip(idx + 14, idx + 16, recorderGrip(OPEN, CLOSE, CLOSE, CLOSE, TRILL, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, BELLOPEN));
        // D#
        addTrillGrip(idx + 15, idx + 16, recorderGrip(OPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, TRILL, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 15, idx + 16, recorderGrip(OPEN, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, TRILL, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 15, idx + 17, recorderGrip(OPEN, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 15, idx + 17, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, CLOSE, OPEN, OPEN, OPEN, BELLOPEN));
        // E
        addTrillGrip(idx + 16, idx + 17, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 16, idx + 18, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, TRILL, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // F
        addTrillGrip(idx + 17, idx + 18, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, TRILL, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 17, idx + 19, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, TRILL, OPEN, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 17, idx + 19, recorderGrip(HALFOPEN, CLOSE, CLOSE, TRILL, CLOSE, OPEN, CLOSE, CLOSE, OPEN, OPEN, BELLOPEN));
        // F#
        addTrillGrip(idx + 18, idx + 19, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, OPEN, TRILL, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 18, idx + 20, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, TRILL, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // G
        addTrillGrip(idx + 19, idx + 20, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, OPEN, TRILL, TRILL, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 19, idx + 21, recorderGrip(HALFOPEN, CLOSE, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // G#
        addTrillGrip(idx + 20, idx + 21, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 20, idx + 22, recorderGrip(HALFOPEN, CLOSE, CLOSE, TRILL, CLOSE, CLOSE, CLOSE, CLOSE, TRILL, TRILL, BELLOPEN));
        // A
        addTrillGrip(idx + 21, idx + 22, recorderGrip(HALFOPEN, CLOSE, CLOSE, TRILL, OPEN, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, BELLOPEN));
        addTrillGrip(idx + 21, idx + 22, recorderGrip(HALFOPEN, CLOSE, CLOSE, HALFOPEN, CLOSE, CLOSE, TRILL, TRILL, CLOSE, OPEN, BELLOPEN));
        addTrillGrip(idx + 21, idx + 22, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, TRILL, TRILL, TRILL, OPEN, BELLOPEN));
        addTrillGrip(idx + 21, idx + 23, recorderGrip(HALFOPEN, CLOSE, CLOSE, HALFOPEN, CLOSE, CLOSE, TRILL, TRILL, TRILL, TRILL, BELLOPEN));
        addTrillGrip(idx + 21, idx + 23, recorderGrip(HALFOPEN, CLOSE, TRILL, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // A#
        addTrillGrip(idx + 22, idx + 23, recorderGrip(HALFOPEN, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, TRILL, TRILL, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 22, idx + 24, recorderGrip(HALFOPEN, CLOSE, TRILL, OPEN, CLOSE, CLOSE, TRILL, TRILL, OPEN, OPEN, BELLOPEN));
        // B
        addTrillGrip(idx + 23, idx + 24, recorderGrip(HALFOPEN, CLOSE, TRILL, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 23, idx + 24, recorderGrip(HALFOPEN, CLOSE, OPEN, OPEN, TRILL, TRILL, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 23, idx + 25, recorderGrip(HALFOPEN, TRILL, TRILL, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        // C
        addTrillGrip(idx + 24, idx + 25, recorderGrip(HALFOPEN, TRILL, OPEN, OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, BELLOPEN));
        addTrillGrip(idx + 24, idx + 26, recorderGrip(HALFOPEN, CLOSE, CLOSE, CLOSE, TRILL, TRILL, OPEN, OPEN, OPEN, OPEN, BELLCLOSE));
    }
}
