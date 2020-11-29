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

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ScaleUnitTest {
    @Test
    public void scaleName ()
    {
        Scale s = new Scale (0);
        assertEquals (s.name (0), "C");
        assertEquals (s.name (-7), "Cb");
        assertEquals (s.name (+7), "C#");
        assertEquals (s.name (-1), "F");
        assertEquals (s.name (+1), "G");
        assertEquals (s.name (+8), "");
        assertEquals (s.name (-8), "");
    }

    @Test
    public void noteUp ()
    {
        Note n = new Note (4, Note.Accidentals.NONE, false);
        Scale s = new Scale (1);

        s.noteUp (n);
        assertEquals (5, n.value ());
        assertEquals ( Note.Accidentals.NONE, n.accidentals ());
        s.noteUp (n);
        assertEquals (7, n.value ());
        assertEquals (Note.Accidentals.NONE, n.accidentals ());

        n = new Note (4+12, Note.Accidentals.NONE, false);
        s.noteUp (n);
        assertEquals (5+12, n.value ());
        assertEquals ( Note.Accidentals.NONE, n.accidentals ());
        s.noteUp (n);
        assertEquals (7+12, n.value ());
        assertEquals (Note.Accidentals.NONE, n.accidentals ());


        n.set(Note.c4 - 3*12, Note.Accidentals.NONE, false);
        s.noteUp(n);
        assertEquals(Note.d4 - 3*12, n.value());

        n.set(Note.f3, Note.Accidentals.NONE, false);
        s.noteUp(n);
        assertEquals(Note.g3, n.value());
    }

    @Test
    public void noteDown ()
    {
        Note n = new Note (0, Note.Accidentals.NONE, false);
        Scale s = new Scale (-2);

        s.noteDown (n);
        assertEquals (-1, n.value ());
        assertEquals ( Note.Accidentals.NONE, n.accidentals ());
        s.noteDown (n);
        assertEquals (-3, n.value ());
        assertEquals (Note.Accidentals.NONE, n.accidentals ());

        n.set(Note.c4 - 3*12, Note.Accidentals.NONE, false);
        s.noteDown (n);
        assertEquals (Note.b4 - 4*12, n.value ());
    }

    @Test
    public void noteHalfUp ()
    {
        Note n = new Note (Note.d4, Note.Accidentals.NONE, false);
        Scale s = new Scale (1);

        s.noteUpHalf (n);
        assertEquals (Note.d4, n.value ());
        assertEquals (Note.Accidentals.SHARP, n.accidentals ());
        s.noteUpHalf (n);
        assertEquals (Note.e4, n.value ());
        assertEquals (Note.Accidentals.NONE, n.accidentals ());
        s.noteUpHalf (n);
        assertEquals (Note.e4, n.value ());
        assertEquals ( Note.Accidentals.SHARP, n.accidentals ());
        s.noteUpHalf (n);
        assertEquals (Note.f4, n.value ());
        assertEquals (Note.Accidentals.RELEASE, n.accidentals ());
        s.noteUpHalf (n);
        assertEquals (Note.f4, n.value ());
        assertEquals (Note.Accidentals.NONE, n.accidentals ());
    }

    @Test
    public void noteHalfDown ()
    {
        Note n = new Note (Note.a4, Note.Accidentals.NONE, false);
        Scale s = new Scale (1);

        s.noteDownHalf (n);
        assertEquals (Note.a4, n.value ());
        assertEquals (Note.Accidentals.FLAT, n.accidentals ());
        s.noteDownHalf (n);
        assertEquals (Note.g4, n.value ());
        assertEquals (Note.Accidentals.NONE, n.accidentals ());
        s.noteDownHalf (n);
        assertEquals (Note.g4, n.value ());
        assertEquals (Note.Accidentals.FLAT, n.accidentals ());
        s.noteDownHalf (n);
        assertEquals (Note.f4, n.value ());
        assertEquals (Note.Accidentals.NONE, n.accidentals ());
        s.noteDownHalf (n);
        assertEquals (Note.f4, n.value ());
        assertEquals (Note.Accidentals.RELEASE, n.accidentals ());
        s.noteDownHalf (n);
        assertEquals (Note.f4, n.value ());
        assertEquals (Note.Accidentals.FLAT, n.accidentals ());
        s.noteDownHalf (n);
        assertEquals (Note.e4, n.value ());
        assertEquals (Note.Accidentals.NONE, n.accidentals ());
    }

    @Test
    public void noteAbsoluteValue() {
        Scale s = new Scale (-1);

        Note n = new Note (Note.b4, Note.Accidentals.NONE, false);
        assertEquals(10, s.noteAbsoluteValue(n));

        n.set(Note.b4, Note.Accidentals.FLAT, false);
        assertEquals(10, s.noteAbsoluteValue(n));

        n.set(Note.b4, Note.Accidentals.RELEASE, false);
        assertEquals(11, s.noteAbsoluteValue(n));

        n.set(Note.b4, Note.Accidentals.SHARP, false);
        assertEquals(12, s.noteAbsoluteValue(n));
    }

    @Test
    public void noteToFrequencyTest()
    {
        Scale s = new Scale (0);

        Note n = new Note (Note.a4, Note.Accidentals.NONE, false);
        assertEquals(44000, s.noteToFrequency(n));

        n.set(Note.a3, Note.Accidentals.NONE, false);
        assertEquals(22000, s.noteToFrequency(n));

        n.set(Note.a5, Note.Accidentals.NONE, false);
        assertEquals(88000, s.noteToFrequency(n));

        n.set(Note.c5, Note.Accidentals.NONE, false);
        assertEquals(26163 * 2, s.noteToFrequency(n));

        s = new Scale(1);

        n.set(Note.f5, Note.Accidentals.NONE, false);
        assertEquals(36999 * 2, s.noteToFrequency(n));

        n.set(Note.f3, Note.Accidentals.NONE, false);
        assertEquals(36999 / 2, s.noteToFrequency(n));
    }

    @Test
    public void frequencyToTest()
    {
        Scale s = new Scale (0);

        Note n = s.frequencyNearestNote (44000);
        assertEquals(n.value(), Note.a4);

        n = s.frequencyNearestNote (44000*2);
        assertEquals(n.value(), Note.a5);

        n = s.frequencyNearestNote (44400*2);
        assertEquals(n.value(), Note.a5);

        n = s.frequencyNearestNote(4785);
        assertEquals(n.value(), Note.g4 - 3*12);
    }

    @Test
    public void midiNoteTest()
    {
        Scale s = new Scale (0);

        Note n = new Note(0, Note.Accidentals.NONE, false);
        assertEquals(60, s.noteMidiValue(n));

        n.set(Note.a3, Note.Accidentals.SHARP, false);
        assertEquals(58, s.noteMidiValue(n));
    }
}

