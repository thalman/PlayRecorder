package net.halman.playrecorder;

import java.util.ArrayList;

import static net.halman.playrecorder.Hole.BELLOPEN;
import static net.halman.playrecorder.Hole.CLOSE;
import static net.halman.playrecorder.Hole.HALFOPEN;
import static net.halman.playrecorder.Hole.OPEN;
import static net.halman.playrecorder.Orientation.DOWN;
import static net.halman.playrecorder.Orientation.UP;

public class TinWhistle extends MusicalInstrument {

    public TinWhistle(int atype)
    {
        super(atype);
        if (!Constants.isTinWhistle(atype)) {
            type(Constants.TIN_WHISTLE_D);
        }

        holes(6);
        switch (type()) {
            default:
            case Constants.TIN_WHISTLE_D:
                realLowestNote(new Note(Note.d5, Note.Accidentals.RELEASE, false));
                realHighestNote(new Note(Note.e7, Note.Accidentals.RELEASE, false));
                break;
            case Constants.TIN_WHISTLE_G:
                realLowestNote(new Note(Note.g5, Note.Accidentals.RELEASE, false));
                realHighestNote(new Note(Note.a6 + 12, Note.Accidentals.RELEASE, false));
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
        int offset = new Scale(0).noteAbsoluteValue(realLowestNote());

        // D
        addGrip(offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE));
        // D#
        addGrip(1 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, HALFOPEN));
        // E
        addGrip(2 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN));
        // F
        addGrip(3 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, HALFOPEN, OPEN));
        // F#
        addGrip(4 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN));
        // G
        addGrip(5 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        // G#
        addGrip(6 + offset, tinWhistleGrip(CLOSE, CLOSE, HALFOPEN, OPEN, OPEN, OPEN));
        // A
        addGrip(7 + offset, tinWhistleGrip(CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN));
        // A#
        addGrip(8 + offset, tinWhistleGrip(CLOSE, HALFOPEN, OPEN, OPEN, OPEN, OPEN));
        // B
        addGrip(9 + offset, tinWhistleGrip(CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        // C
        addGrip(10 + offset, tinWhistleGrip(OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        addGrip(10 + offset, tinWhistleGrip(HALFOPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        // C#
        addGrip(11 + offset, tinWhistleGrip(OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        addGrip(11 + offset, tinWhistleGrip(OPEN, OPEN, OPEN, OPEN, OPEN, CLOSE));
        // D
        addGrip(12 + offset, tinWhistleGrip(OPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE));
        addGrip(12 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE));
        // D#
        addGrip(13 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, HALFOPEN));
        // E
        addGrip(14 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN));
        // F
        addGrip(15 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, HALFOPEN, OPEN));
        // F#
        addGrip(16 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN));
        // G
        addGrip(17 + offset, tinWhistleGrip(CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        // G#
        addGrip(18 + offset, tinWhistleGrip(CLOSE, CLOSE, HALFOPEN, OPEN, OPEN, OPEN));
        // A
        addGrip(19 + offset, tinWhistleGrip(CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN));
        // A#
        addGrip(20 + offset, tinWhistleGrip(CLOSE, HALFOPEN, OPEN, OPEN, OPEN, OPEN));
        // B
        addGrip(21 + offset, tinWhistleGrip(CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        // C
        addGrip(22 + offset, tinWhistleGrip(HALFOPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        addGrip(22 + offset, tinWhistleGrip(OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN));
        // C#
        addGrip(23 + offset, tinWhistleGrip(OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        addGrip(23 + offset, tinWhistleGrip(OPEN, OPEN, OPEN, OPEN, OPEN, CLOSE));
        // D
        addGrip(24 + offset, tinWhistleGrip(OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        // D# can't be find the grip
        // E
        addGrip(26 + offset, tinWhistleGrip(CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN));
    }
}
