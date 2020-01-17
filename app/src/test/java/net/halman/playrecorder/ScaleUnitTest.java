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
        Note n = new Note (4, Note.Accidentals.NONE);
        Scale s = new Scale (1);

        s.noteUp (n);
        assertEquals (5, n.value ());
        assertEquals ( Note.Accidentals.NONE, n.accidentals ());
        s.noteUp (n);
        assertEquals (7, n.value ());
        assertEquals (Note.Accidentals.NONE, n.accidentals ());

        n = new Note (4+12, Note.Accidentals.NONE);
        s.noteUp (n);
        assertEquals (5+12, n.value ());
        assertEquals ( Note.Accidentals.NONE, n.accidentals ());
        s.noteUp (n);
        assertEquals (7+12, n.value ());
        assertEquals (Note.Accidentals.NONE, n.accidentals ());
    }

    @Test
    public void noteDown ()
    {
        Note n = new Note (0, Note.Accidentals.NONE);
        Scale s = new Scale (-2);

        s.noteDown (n);
        assertEquals (-1, n.value ());
        assertEquals ( Note.Accidentals.NONE, n.accidentals ());
        s.noteDown (n);
        assertEquals (-3, n.value ());
        assertEquals (Note.Accidentals.NONE, n.accidentals ());
    }

    @Test
    public void noteHalfUp ()
    {
        Note n = new Note (2, Note.Accidentals.NONE);
        Scale s = new Scale (1);

        s.noteUpHalf (n);
        assertEquals (2, n.value ());
        assertEquals ( Note.Accidentals.SHARP, n.accidentals ());
        s.noteUpHalf (n);
        assertEquals (4, n.value ());
        assertEquals (Note.Accidentals.NONE, n.accidentals ());
        s.noteUpHalf (n);
        assertEquals (4, n.value ());
        assertEquals ( Note.Accidentals.SHARP, n.accidentals ());
        s.noteUpHalf (n);
        assertEquals (5, n.value ());
        assertEquals (Note.Accidentals.NONE, n.accidentals ());
        s.noteUpHalf (n);
        assertEquals (7, n.value ());
        assertEquals (Note.Accidentals.NONE, n.accidentals ());
    }

    @Test
    public void noteHalfDown ()
    {
        Note n = new Note (9, Note.Accidentals.NONE);
        Scale s = new Scale (1);

        s.noteDownHalf (n);
        assertEquals (9, n.value ());
        assertEquals (Note.Accidentals.FLAT, n.accidentals ());
        s.noteDownHalf (n);
        assertEquals (7, n.value ());
        assertEquals ( Note.Accidentals.NONE, n.accidentals ());
        s.noteDownHalf (n);
        assertEquals (5, n.value ());
        assertEquals (Note.Accidentals.NONE, n.accidentals ());
        s.noteDownHalf (n);
        assertEquals (5, n.value ());
        assertEquals ( Note.Accidentals.FLAT, n.accidentals ());
        s.noteDownHalf (n);
        assertEquals (4, n.value ());
        assertEquals (Note.Accidentals.NONE, n.accidentals ());
    }

    @Test
    public void noteAbsoluteValue() {
        Scale s = new Scale (-1);

        Note n = new Note (Note.b1, Note.Accidentals.NONE);
        assertEquals(10, s.noteAbsoluteValue(n));

        n.set(Note.b1, Note.Accidentals.FLAT);
        assertEquals(10, s.noteAbsoluteValue(n));

        n.set(Note.b1, Note.Accidentals.RELEASE);
        assertEquals(11, s.noteAbsoluteValue(n));

        n.set(Note.b1, Note.Accidentals.SHARP);
        assertEquals(12, s.noteAbsoluteValue(n));
    }
}

