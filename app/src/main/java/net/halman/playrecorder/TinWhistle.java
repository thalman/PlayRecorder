package net.halman.playrecorder;

import java.util.ArrayList;

import static net.halman.playrecorder.Hole.BELLOPEN;
import static net.halman.playrecorder.Hole.CLOSE;
import static net.halman.playrecorder.Hole.HALFOPEN;
import static net.halman.playrecorder.Hole.OPEN;
import static net.halman.playrecorder.Orientation.DOWN;
import static net.halman.playrecorder.Orientation.UP;

public class TinWhistle extends MusicalInstrument {

    public TinWhistle(int type)
    {
        super(type);
        if (!Constants.isTinWhistle(type)) {
            instrument_type = Constants.TIN_WHISTLE_D;
        }

        number_of_holes = 6;
        switch (type) {
            default:
            case Constants.TIN_WHISTLE_D:
                realLowestNote(new Note(Note.d5, Note.Accidentals.RELEASE));
                realHighestNote(new Note(Note.e7, Note.Accidentals.RELEASE));
                break;
            case Constants.TIN_WHISTLE_G:
                realLowestNote(new Note(Note.g5, Note.Accidentals.RELEASE));
                realHighestNote(new Note(Note.a6 + 12, Note.Accidentals.RELEASE));
                break;

        }

        scoreOffset(-12);
        setFingering();
        setHoles();
    }

    private void setHoles() {
        hole(UP, 0, 130, 1.2);
        hole(UP, 0, 165, 1.2);
        hole(UP, 0, 200, 1.2);
        hole(UP, 0, 250, 1.2);
        hole(UP, 0, 285, 1.2);
        hole(UP, 0, 320, 1.2);

        hole(DOWN, 0, 320, 1.2);
        hole(DOWN, 0, 285, 1.2);
        hole(DOWN, 0, 250, 1.2);
        hole(DOWN, 0, 200, 1.2);
        hole(DOWN, 0, 165, 1.2);
        hole(DOWN, 0, 130, 1.2);
    }

    private Grip tinWhistleGrip(int h0, int h1, int h2, int h3,
                              int h4, int h5)
    {
        int [] gripArray = new int [] {h0, h1, h2, h3, h4, h5};

        return new Grip(gripArray);
    }

    private void setFingering() {
        grips(null);
        int offset = 0;
        if (instrument_type == Constants.TIN_WHISTLE_G) {
            offset = 5;
        }
        // D
        addGrip(14 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE));
        // D#
        addGrip(15 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, HALFOPEN));
        // E
        addGrip(16 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN));
        // F
        addGrip(17 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, HALFOPEN, OPEN));
        // F#
        addGrip(18 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN));
        // G
        addGrip(19 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        // G#
        addGrip(20 + offset, tinWhistleGrip(CLOSE, CLOSE, HALFOPEN, OPEN, OPEN, OPEN));
        // A
        addGrip(21 + offset, tinWhistleGrip(CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN));
        // A#
        addGrip(22 + offset, tinWhistleGrip(CLOSE, HALFOPEN, OPEN, OPEN, OPEN, OPEN));
        // B
        addGrip(23 + offset, tinWhistleGrip(CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        // C
        addGrip(24 + offset, tinWhistleGrip(OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        addGrip(24 + offset, tinWhistleGrip(HALFOPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        // C#
        addGrip(25 + offset, tinWhistleGrip(OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        addGrip(25 + offset, tinWhistleGrip(OPEN, OPEN, OPEN, OPEN, OPEN, CLOSE));
        // D
        addGrip(26 + offset, tinWhistleGrip(OPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE));
        addGrip(26 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE));
        // D#
        addGrip(27 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, HALFOPEN));
        // E
        addGrip(28 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN));
        // F
        addGrip(29 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, HALFOPEN, OPEN));
        // F#
        addGrip(30 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN));
        // G
        addGrip(31 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        // G#
        addGrip(32 + offset, tinWhistleGrip(CLOSE, CLOSE, HALFOPEN, OPEN, OPEN, OPEN));
        // A
        addGrip(33 + offset, tinWhistleGrip(CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN));
        // A#
        addGrip(34 + offset, tinWhistleGrip(CLOSE, HALFOPEN, OPEN, OPEN, OPEN, OPEN));
        // B
        addGrip(35 + offset, tinWhistleGrip(CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        // C
        addGrip(36 + offset, tinWhistleGrip(HALFOPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        addGrip(36 + offset, tinWhistleGrip(OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN));
        // C#
        addGrip(37 + offset, tinWhistleGrip(OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        addGrip(37 + offset, tinWhistleGrip(OPEN, OPEN, OPEN, OPEN, OPEN, CLOSE));
        // D
        addGrip(38 + offset, tinWhistleGrip(OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        // D# can't be find the grip
        addGrip(39 + offset, tinWhistleGrip(BELLOPEN, BELLOPEN, BELLOPEN, BELLOPEN, BELLOPEN, BELLOPEN));
        // E
        addGrip(40 + offset, tinWhistleGrip(CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN));
    }
}
