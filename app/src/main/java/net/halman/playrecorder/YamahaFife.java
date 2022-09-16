package net.halman.playrecorder;

import static net.halman.playrecorder.Hole.CLOSE;
import static net.halman.playrecorder.Hole.HALFOPEN;
import static net.halman.playrecorder.Hole.OPEN;
import static net.halman.playrecorder.Orientation.DOWN;
import static net.halman.playrecorder.Orientation.UP;

public class YamahaFife extends MusicalInstrument {
    public YamahaFife(int type) {
        super(type);

        holes(8);
        switch (type()) {
            default:
            case Constants.FIFE:
                realLowestNote(new Note(Note.c5, Note.Accidentals.RELEASE, false));
                realHighestNote(new Note(Note.e7, Note.Accidentals.RELEASE, false));
                break;
        }

        scoreOffset(-12);
        setFingering();
        setHoles();
    }

    private void setHoles() {
        hole(0, 230, 0.8);
        hole(8, 200, 0.8);
        hole(0, 170, 1.0);
        hole(0, 140, 1.0);
        // -----
        hole(0, 90, 1.0);
        hole(0, 60, 0.8);
        hole(0, 30, 1.0);
        hole(8, 0, 0.8);
    }

    private Grip fifeGrip(int h0, int h1, int h2, int h3,
                                int h4, int h5, int h6, int h7)
    {
        int [] gripArray = new int [] {h0, h1, h2, h3, h4, h5, h6, h7};

        return new Grip(gripArray);
    }

    private void setFingering() {
        grips(null);
        int offset = new Scale(0).noteAbsoluteValue(realLowestNote());

        // C
        addGrip(offset, fifeGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE));
        // C#
        addGrip(1 + offset, fifeGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, HALFOPEN));
        // D
        addGrip(2 + offset, fifeGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN));
        // D#
        addGrip(3 + offset, fifeGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, HALFOPEN, OPEN));
        // E
        addGrip(4 + offset, fifeGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, CLOSE));
        // F
        addGrip(5 + offset, fifeGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, CLOSE));
        // F#
        addGrip(6 + offset, fifeGrip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE));
        // G
        addGrip(7 + offset, fifeGrip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, CLOSE));
        // G#
        addGrip(8 + offset, fifeGrip(CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, CLOSE));
        // A
        addGrip(9 + offset, fifeGrip(CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, CLOSE));
        // A#
        addGrip(10 + offset, fifeGrip(CLOSE, CLOSE, OPEN, CLOSE, CLOSE, CLOSE, OPEN, CLOSE));
        // B
        addGrip(11 + offset, fifeGrip(CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, CLOSE));
        // C
        addGrip(12 + offset, fifeGrip(CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, CLOSE));
        // C#
        addGrip(13 + offset, fifeGrip(OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, CLOSE));
        // D
        addGrip(14 + offset, fifeGrip(OPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN));
        // D#
        addGrip(15 + offset, fifeGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, HALFOPEN, OPEN));
        // E
        addGrip(16 + offset, fifeGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, CLOSE));
        // F
        addGrip(17 + offset, fifeGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, CLOSE));
        // F#
        addGrip(18 + offset, fifeGrip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, CLOSE, OPEN, CLOSE));
        // G
        addGrip(19 + offset, fifeGrip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, CLOSE));
        // G#
        addGrip(20 + offset, fifeGrip(CLOSE, CLOSE, CLOSE, OPEN, CLOSE, OPEN, OPEN, CLOSE));
        // A
        addGrip(21 + offset, fifeGrip(CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, CLOSE));
        // A#
        addGrip(22 + offset, fifeGrip(CLOSE, CLOSE, OPEN, CLOSE, OPEN, OPEN, OPEN, CLOSE));
        // B
        addGrip(23 + offset, fifeGrip(CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, CLOSE));
        // C
        addGrip(24 + offset, fifeGrip(CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, CLOSE));
        // C#
        addGrip(25 + offset, fifeGrip(OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, OPEN, CLOSE));
        // D
        addGrip(26 + offset, fifeGrip(OPEN, CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN, CLOSE));
        // D# can't be find the grip
        // E
        addGrip(28 + offset, fifeGrip(CLOSE, CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN, CLOSE));
    }

}
