package net.halman.playrecorder;

import java.util.ArrayList;

import static net.halman.playrecorder.Hole.BELLOPEN;
import static net.halman.playrecorder.Hole.CLOSE;
import static net.halman.playrecorder.Hole.HALFOPEN;
import static net.halman.playrecorder.Hole.OPEN;
import static net.halman.playrecorder.Orientation.DOWN;
import static net.halman.playrecorder.Orientation.UP;

public class TinWhistle extends MusicalInstrument {
    private ArrayList<ArrayList<Grip>> grips = null;

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
        hole(UP, 0, 170, 1.5);
        hole(UP, 0, 220, 1.5);
        hole(UP, 0, 270, 1.5);
        hole(UP, 0, 340, 1.5);
        hole(UP, 0, 390, 1.5);
        hole(UP, 0, 440, 1.5);

        hole(DOWN, 0, 440, 1.5);
        hole(DOWN, 0, 390, 1.5);
        hole(DOWN, 0, 340, 1.5);
        hole(DOWN, 0, 270, 1.5);
        hole(DOWN, 0, 220, 1.5);
        hole(DOWN, 0, 170, 1.5);
    }

    private Grip tinWhistleGrip(int h0, int h1, int h2, int h3,
                              int h4, int h5)
    {
        int [] gripArray = new int [] {h0, h1, h2, h3, h4, h5};

        return new Grip(gripArray);
    }

    private void setFingering() {
        grips = new ArrayList<>();
        ArrayList<Grip> grip;

        // D
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE));
        grips.add(grip);

        // D#
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, HALFOPEN));
        grips.add(grip);

        // E
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN));
        grips.add(grip);

        // F
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, HALFOPEN, OPEN));
        grips.add(grip);

        // F#
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN));
        grips.add(grip);

        // G
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grips.add(grip);

        // G#
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, CLOSE, HALFOPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // A
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // A#
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, HALFOPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // B
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // C
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grip.add(tinWhistleGrip(HALFOPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // C#
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grip.add(tinWhistleGrip(OPEN, OPEN, OPEN, OPEN, OPEN, CLOSE));
        grips.add(grip);

        // D
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(OPEN, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE));
        grip.add(tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, CLOSE));
        grips.add(grip);

        // D#
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, HALFOPEN));
        grips.add(grip);

        // E
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, CLOSE, OPEN));
        grips.add(grip);

        // F
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, HALFOPEN, OPEN));
        grips.add(grip);

        // F#
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, CLOSE, CLOSE, CLOSE, OPEN, OPEN));
        grips.add(grip);

        // G
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grips.add(grip);

        // G#
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, CLOSE, HALFOPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // A
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, CLOSE, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // A#
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, HALFOPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // B
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, OPEN, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // C
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(HALFOPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grip.add(tinWhistleGrip(OPEN, CLOSE, OPEN, OPEN, OPEN, OPEN));
        grips.add(grip);

        // C#
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(OPEN, OPEN, OPEN, OPEN, OPEN, OPEN));
        grip.add(tinWhistleGrip(OPEN, OPEN, OPEN, OPEN, OPEN, CLOSE));
        grips.add(grip);

        // D
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(OPEN, CLOSE, CLOSE, OPEN, OPEN, OPEN));
        grips.add(grip);

        // D# can't be find the grip
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(BELLOPEN, BELLOPEN, BELLOPEN, BELLOPEN, BELLOPEN, BELLOPEN));
        grips.add(grip);

        // E
        grip = new ArrayList<>();
        grip.add(tinWhistleGrip(CLOSE, CLOSE, OPEN, CLOSE, CLOSE, OPEN));
        grips.add(grip);
    }

    @Override
    public ArrayList<Grip> grips(Scale scale, Note realNote)
    {
        int idx = scale.noteAbsoluteValue(realNote) - scale.noteAbsoluteValue(realLowestNote());
        if ((idx >= 0) && (idx < grips.size())) {
            return grips.get(idx);
        }
        return null;
    }
}
