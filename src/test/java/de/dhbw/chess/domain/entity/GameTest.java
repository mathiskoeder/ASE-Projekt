package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.Move;
import de.dhbw.chess.domain.valueobject.MoveRecord;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameTest {

    private static Position p(String alg) {
        return Position.fromAlgebraic(alg);
    }

    private Game minimalGame(Board board) {
        Player white = new Player(UUID.randomUUID(), "Max", PieceColor.WHITE);
        Player black = new Player(UUID.randomUUID(), "Mathis", PieceColor.BLACK);
        return new Game(UUID.randomUUID(), white, black, board);
    }

    @Test
    void whiteMovesFirst() {
        Board board = new Board();
        board.place(new Pawn(PieceColor.WHITE), p("e2"));
        Game game = minimalGame(board);
        assertEquals(PieceColor.WHITE, game.activeColor());
    }

    @Test
    void executingMoveAdvancesActiveColor() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new King(PieceColor.BLACK), p("e8"));
        board.place(new Pawn(PieceColor.WHITE), p("e2"));
        Game game = minimalGame(board);
        MoveRecord rec = game.makeMove(new Move(p("e2"), p("e4")));
        assertNotNull(rec);
        assertEquals(PieceColor.BLACK, game.activeColor());
        assertSame(rec, game.history().last());
    }

    @Test
    void wrongColorRejected() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new King(PieceColor.BLACK), p("e8"));
        board.place(new Pawn(PieceColor.BLACK), p("e7"));
        Game game = minimalGame(board);
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Move(p("e7"), p("e5"))));
    }

    @Test
    void illegalDestinationRejected() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new King(PieceColor.BLACK), p("e8"));
        board.place(new Pawn(PieceColor.WHITE), p("e2"));
        Game game = minimalGame(board);
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Move(p("e2"), p("e5"))));
    }

    @Test
    void moveLeavingOwnKingInCheckIsRejected() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new King(PieceColor.BLACK), p("e8"));
        // Weißer Springer auf e2 ist gepinnt durch schwarzen Turm auf e8 (deckt e1).
        board.place(new Knight(PieceColor.WHITE), p("e2"));
        board.place(new Rook(PieceColor.BLACK), p("e7"));
        Game game = minimalGame(board);
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Move(p("e2"), p("g3"))));
    }
}
