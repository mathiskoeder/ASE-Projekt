package de.dhbw.chess.domain.service;

import de.dhbw.chess.domain.entity.Board;
import de.dhbw.chess.domain.entity.King;
import de.dhbw.chess.domain.entity.Knight;
import de.dhbw.chess.domain.entity.Pawn;
import de.dhbw.chess.domain.entity.Rook;
import de.dhbw.chess.domain.valueobject.Move;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveValidatorTest {

    private static Position p(String alg) {
        return Position.fromAlgebraic(alg);
    }

    @Test
    void detectsCheckByEnemyRook() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new Rook(PieceColor.BLACK), p("e7"));
        assertTrue(new CheckDetector().isInCheck(board, PieceColor.WHITE));
    }

    @Test
    void noCheckWhenRookBlocked() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new Pawn(PieceColor.WHITE), p("e3"));
        board.place(new Rook(PieceColor.BLACK), p("e7"));
        assertFalse(new CheckDetector().isInCheck(board, PieceColor.WHITE));
    }

    @Test
    void validatorAcceptsLegalKnightJump() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("a1"));
        board.place(new King(PieceColor.BLACK), p("h8"));
        board.place(new Knight(PieceColor.WHITE), p("b1"));
        MoveValidator v = new MoveValidator();
        assertDoesNotThrow(() ->
                v.validate(board, new Move(p("b1"), p("c3")), PieceColor.WHITE));
    }

    @Test
    void validatorRejectsMoveLeavingKingInCheck() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new King(PieceColor.BLACK), p("e8"));
        board.place(new Knight(PieceColor.WHITE), p("e2"));
        board.place(new Rook(PieceColor.BLACK), p("e7"));
        MoveValidator v = new MoveValidator();
        assertThrows(IllegalArgumentException.class, () ->
                v.validate(board, new Move(p("e2"), p("g3")), PieceColor.WHITE));
    }

    @Test
    void validatorRejectsWrongOwner() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("a1"));
        board.place(new King(PieceColor.BLACK), p("h8"));
        board.place(new Pawn(PieceColor.BLACK), p("e7"));
        MoveValidator v = new MoveValidator();
        assertThrows(IllegalArgumentException.class, () ->
                v.validate(board, new Move(p("e7"), p("e6")), PieceColor.WHITE));
    }
}
