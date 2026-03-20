package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.Move;
import de.dhbw.chess.domain.valueobject.MoveRecord;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CastlingTest {

    private static Position p(String alg) {
        return Position.fromAlgebraic(alg);
    }

    private Game gameWith(Board board) {
        Player white = new Player(UUID.randomUUID(), "Max", PieceColor.WHITE);
        Player black = new Player(UUID.randomUUID(), "Mathis", PieceColor.BLACK);
        return new Game(UUID.randomUUID(), white, black, board);
    }

    @Test
    void kingsideCastlingMovesKingAndRook() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new Rook(PieceColor.WHITE), p("h1"));
        board.place(new King(PieceColor.BLACK), p("e8"));
        Game game = gameWith(board);
        MoveRecord record = game.makeMove(new Move(p("e1"), p("g1")));
        assertTrue(record.isKingsideCastle());
        assertTrue(board.pieceAt(p("g1")) instanceof King);
        assertTrue(board.pieceAt(p("f1")) instanceof Rook);
    }

    @Test
    void queensideCastlingMovesKingAndRook() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new Rook(PieceColor.WHITE), p("a1"));
        board.place(new King(PieceColor.BLACK), p("e8"));
        Game game = gameWith(board);
        MoveRecord record = game.makeMove(new Move(p("e1"), p("c1")));
        assertTrue(record.isQueensideCastle());
        assertTrue(board.pieceAt(p("c1")) instanceof King);
        assertTrue(board.pieceAt(p("d1")) instanceof Rook);
    }

    @Test
    void castlingForbiddenWhenKingInCheck() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new Rook(PieceColor.WHITE), p("h1"));
        board.place(new King(PieceColor.BLACK), p("e8"));
        board.place(new Rook(PieceColor.BLACK), p("e7"));
        Game game = gameWith(board);
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Move(p("e1"), p("g1"))));
    }

    @Test
    void castlingForbiddenWhenPathAttacked() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new Rook(PieceColor.WHITE), p("h1"));
        board.place(new King(PieceColor.BLACK), p("e8"));
        board.place(new Rook(PieceColor.BLACK), p("f7"));
        Game game = gameWith(board);
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Move(p("e1"), p("g1"))));
    }

    @Test
    void castlingForbiddenAfterKingHasMoved() {
        Board board = new Board();
        King king = new King(PieceColor.WHITE);
        king.markMoved();
        board.place(king, p("e1"));
        board.place(new Rook(PieceColor.WHITE), p("h1"));
        board.place(new King(PieceColor.BLACK), p("e8"));
        Game game = gameWith(board);
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Move(p("e1"), p("g1"))));
    }

    @Test
    void castlingForbiddenWhenSquaresOccupied() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new Rook(PieceColor.WHITE), p("h1"));
        board.place(new Knight(PieceColor.WHITE), p("g1"));
        board.place(new King(PieceColor.BLACK), p("e8"));
        Game game = gameWith(board);
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Move(p("e1"), p("g1"))));
    }

    @Test
    void notationRendersAsOO() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new Rook(PieceColor.WHITE), p("h1"));
        board.place(new King(PieceColor.BLACK), p("e8"));
        Game game = gameWith(board);
        MoveRecord rec = game.makeMove(new Move(p("e1"), p("g1")));
        assertEquals("O-O", rec.notation());
    }

    @Test
    void rookEndsUpOnCorrectSquare() {
        Board board = new Board();
        Rook rook = new Rook(PieceColor.WHITE);
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(rook, p("a1"));
        board.place(new King(PieceColor.BLACK), p("e8"));
        Game game = gameWith(board);
        game.makeMove(new Move(p("e1"), p("c1")));
        assertSame(rook, board.pieceAt(p("d1")));
    }
}
