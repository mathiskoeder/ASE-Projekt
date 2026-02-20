package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.Move;
import de.dhbw.chess.domain.valueobject.MoveRecord;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.PieceType;
import de.dhbw.chess.domain.valueobject.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveHistoryTest {

    private static MoveRecord knightMove() {
        return MoveRecord.builder(
                new Move(Position.fromAlgebraic("g1"), Position.fromAlgebraic("f3")),
                PieceColor.WHITE,
                PieceType.KNIGHT
        ).build();
    }

    private static MoveRecord pawnMove() {
        return MoveRecord.builder(
                new Move(Position.fromAlgebraic("e2"), Position.fromAlgebraic("e4")),
                PieceColor.WHITE,
                PieceType.PAWN
        ).build();
    }

    private static MoveRecord captureMove() {
        return MoveRecord.builder(
                new Move(Position.fromAlgebraic("e4"), Position.fromAlgebraic("d5")),
                PieceColor.WHITE,
                PieceType.PAWN
        ).captured(PieceType.PAWN).build();
    }

    @Test
    void newHistoryIsEmpty() {
        MoveHistory h = new MoveHistory();
        assertTrue(h.isEmpty());
        assertEquals(0, h.size());
        assertNull(h.last());
    }

    @Test
    void appendStoresRecords() {
        MoveHistory h = new MoveHistory();
        MoveRecord r = knightMove();
        h.append(r);
        assertEquals(1, h.size());
        assertSame(r, h.last());
    }

    @Test
    void recordsListIsImmutable() {
        MoveHistory h = new MoveHistory();
        h.append(knightMove());
        assertThrows(UnsupportedOperationException.class, () -> h.records().clear());
    }

    @Test
    void halfMovesSinceProgressResetsOnPawnOrCapture() {
        MoveHistory h = new MoveHistory();
        h.append(knightMove());
        h.append(knightMove());
        assertEquals(2, h.halfMovesSinceProgress());
        h.append(pawnMove());
        h.append(knightMove());
        h.append(knightMove());
        assertEquals(2, h.halfMovesSinceProgress());
        h.append(captureMove());
        h.append(knightMove());
        assertEquals(1, h.halfMovesSinceProgress());
    }
}
