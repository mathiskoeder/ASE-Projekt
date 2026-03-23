package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.Move;
import de.dhbw.chess.domain.valueobject.MoveRecord;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnPassantTest {

    private static Position p(String alg) {
        return Position.fromAlgebraic(alg);
    }

    private Game gameWith(Board board) {
        Player white = new Player(UUID.randomUUID(), "Max", PieceColor.WHITE);
        Player black = new Player(UUID.randomUUID(), "Mathis", PieceColor.BLACK);
        return new Game(UUID.randomUUID(), white, black, board);
    }

    @Test
    void capturesAdjacentPawnAfterDoubleStep() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new King(PieceColor.BLACK), p("e8"));
        board.place(new Pawn(PieceColor.WHITE), p("e5"));
        board.place(new Pawn(PieceColor.BLACK), p("d7"));
        Game game = gameWith(board);

        // Schwarz ist eigentlich am Zug erst nach Weiß. Wir simulieren einen Weiß-Tempo-Zug
        // (Königszug), damit Schwarz den Doppelschritt machen darf.
        game.makeMove(new Move(p("e1"), p("e2")));
        game.makeMove(new Move(p("d7"), p("d5")));
        // Jetzt darf Weiß en passant schlagen.
        MoveRecord rec = game.makeMove(new Move(p("e5"), p("d6")));
        assertTrue(rec.isEnPassant());
        assertNull(board.pieceAt(p("d5")));
        assertSame(rec.movedType(), de.dhbw.chess.domain.valueobject.PieceType.PAWN);
    }

    @Test
    void enPassantNotAllowedAfterIntermediateMove() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new King(PieceColor.BLACK), p("e8"));
        board.place(new Pawn(PieceColor.WHITE), p("e5"));
        board.place(new Pawn(PieceColor.BLACK), p("d7"));
        Game game = gameWith(board);
        game.makeMove(new Move(p("e1"), p("e2")));
        game.makeMove(new Move(p("d7"), p("d5")));
        game.makeMove(new Move(p("e2"), p("e1")));
        game.makeMove(new Move(p("e8"), p("d8")));
        // Doppelschritt liegt nicht mehr unmittelbar vorher — En passant unzulässig.
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Move(p("e5"), p("d6"))));
    }
}
