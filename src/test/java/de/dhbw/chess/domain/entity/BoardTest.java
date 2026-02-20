package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {

    private static Position p(String alg) {
        return Position.fromAlgebraic(alg);
    }

    @Test
    void emptyByDefault() {
        Board board = new Board();
        assertTrue(board.isEmpty(p("e4")));
        assertNull(board.pieceAt(p("e4")));
    }

    @Test
    void placeAndRetrieve() {
        Board board = new Board();
        Pawn pawn = new Pawn(PieceColor.WHITE);
        board.place(pawn, p("e2"));
        assertSame(pawn, board.pieceAt(p("e2")));
        assertFalse(board.isEmpty(p("e2")));
    }

    @Test
    void enemyAndFriendlyDetection() {
        Board board = new Board();
        board.place(new Pawn(PieceColor.WHITE), p("e2"));
        board.place(new Pawn(PieceColor.BLACK), p("e7"));
        assertTrue(board.isFriendly(p("e2"), PieceColor.WHITE));
        assertTrue(board.isEnemy(p("e7"), PieceColor.WHITE));
        assertFalse(board.isFriendly(p("e7"), PieceColor.WHITE));
    }

    @Test
    void moveCapturesAndUpdatesHasMoved() {
        Board board = new Board();
        Pawn white = new Pawn(PieceColor.WHITE);
        Pawn black = new Pawn(PieceColor.BLACK);
        board.place(white, p("e4"));
        board.place(black, p("d5"));
        Piece captured = board.move(p("e4"), p("d5"));
        assertSame(black, captured);
        assertSame(white, board.pieceAt(p("d5")));
        assertNull(board.pieceAt(p("e4")));
        assertTrue(white.hasMoved());
    }

    @Test
    void findKingReturnsItsPosition() {
        Board board = new Board();
        King king = new King(PieceColor.WHITE);
        board.place(king, p("e1"));
        assertEquals(p("e1"), board.findKing(PieceColor.WHITE));
        assertNull(board.findKing(PieceColor.BLACK));
    }

    @Test
    void copyIsIndependent() {
        Board board = new Board();
        board.place(new Pawn(PieceColor.WHITE), p("e2"));
        Board copy = board.copy();
        copy.remove(p("e2"));
        assertNotNull(board.pieceAt(p("e2")));
        assertNull(copy.pieceAt(p("e2")));
    }

    @Test
    void snapshotContainsAllSquares() {
        Board board = new Board();
        assertEquals(64, board.snapshot().size());
    }
}
