package de.dhbw.chess.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveTest {

    @Test
    void simpleMoveStoresFromAndTo() {
        Move m = new Move(Position.fromAlgebraic("e2"), Position.fromAlgebraic("e4"));
        assertEquals(Position.fromAlgebraic("e2"), m.from());
        assertEquals(Position.fromAlgebraic("e4"), m.to());
        assertFalse(m.isPromotion());
    }

    @Test
    void promotionMoveStoresPromotionPiece() {
        Move m = new Move(Position.fromAlgebraic("e7"), Position.fromAlgebraic("e8"), PieceType.QUEEN);
        assertTrue(m.isPromotion());
        assertEquals(PieceType.QUEEN, m.promotion());
    }

    @Test
    void rejectsIdenticalFromAndTo() {
        Position p = Position.fromAlgebraic("e4");
        assertThrows(IllegalArgumentException.class, () -> new Move(p, p));
    }

    @Test
    void rejectsPromotionToKingOrPawn() {
        Position from = Position.fromAlgebraic("e7");
        Position to = Position.fromAlgebraic("e8");
        assertThrows(IllegalArgumentException.class, () -> new Move(from, to, PieceType.KING));
        assertThrows(IllegalArgumentException.class, () -> new Move(from, to, PieceType.PAWN));
    }

    @Test
    void equalsAndHashCodeBasedOnAllFields() {
        Move a = new Move(Position.fromAlgebraic("e2"), Position.fromAlgebraic("e4"));
        Move b = new Move(Position.fromAlgebraic("e2"), Position.fromAlgebraic("e4"));
        Move c = new Move(Position.fromAlgebraic("d2"), Position.fromAlgebraic("d4"));
        Move d = new Move(Position.fromAlgebraic("e7"), Position.fromAlgebraic("e8"), PieceType.QUEEN);
        Move e = new Move(Position.fromAlgebraic("e7"), Position.fromAlgebraic("e8"), PieceType.ROOK);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(d, e);
    }

    @Test
    void toStringRendersAlgebraicWithDash() {
        Move m = new Move(Position.fromAlgebraic("e2"), Position.fromAlgebraic("e4"));
        assertEquals("e2-e4", m.toString());
    }

    @Test
    void toStringIncludesPromotion() {
        Move m = new Move(Position.fromAlgebraic("e7"), Position.fromAlgebraic("e8"), PieceType.QUEEN);
        assertEquals("e7-e8=Q", m.toString());
    }
}
