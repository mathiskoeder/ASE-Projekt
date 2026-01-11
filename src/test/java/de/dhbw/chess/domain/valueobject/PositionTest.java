package de.dhbw.chess.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PositionTest {

    @Test
    void acceptsAllSquaresInsideBoard() {
        for (int f = 0; f < 8; f++) {
            for (int r = 0; r < 8; r++) {
                Position p = Position.of(f, r);
                assertEquals(f, p.file());
                assertEquals(r, p.rank());
            }
        }
    }

    @Test
    void rejectsFileBelowZero() {
        assertThrows(IllegalArgumentException.class, () -> Position.of(-1, 0));
    }

    @Test
    void rejectsFileAboveSeven() {
        assertThrows(IllegalArgumentException.class, () -> Position.of(8, 0));
    }

    @Test
    void rejectsRankBelowZero() {
        assertThrows(IllegalArgumentException.class, () -> Position.of(0, -1));
    }

    @Test
    void rejectsRankAboveSeven() {
        assertThrows(IllegalArgumentException.class, () -> Position.of(0, 8));
    }

    @Test
    void parsesAlgebraicNotation() {
        assertEquals(Position.of(0, 0), Position.fromAlgebraic("a1"));
        assertEquals(Position.of(4, 3), Position.fromAlgebraic("e4"));
        assertEquals(Position.of(7, 7), Position.fromAlgebraic("h8"));
    }

    @Test
    void rejectsMalformedAlgebraicNotation() {
        assertThrows(IllegalArgumentException.class, () -> Position.fromAlgebraic("e9"));
        assertThrows(IllegalArgumentException.class, () -> Position.fromAlgebraic("z3"));
        assertThrows(IllegalArgumentException.class, () -> Position.fromAlgebraic("e44"));
    }

    @Test
    void roundTripsAlgebraicNotation() {
        assertEquals("e4", Position.of(4, 3).toAlgebraic());
        assertEquals("a1", Position.of(0, 0).toAlgebraic());
        assertEquals("h8", Position.of(7, 7).toAlgebraic());
    }

    @Test
    void deltaMethodsReturnSignedDifference() {
        Position a = Position.of(2, 3);
        Position b = Position.of(5, 1);
        assertEquals(3, a.fileDelta(b));
        assertEquals(-2, a.rankDelta(b));
    }

    @Test
    void equalsAndHashCodeBasedOnCoordinates() {
        Position a = Position.of(3, 4);
        Position b = Position.of(3, 4);
        Position c = Position.of(4, 3);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}
