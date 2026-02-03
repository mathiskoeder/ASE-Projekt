package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PieceMovesTest {

    private static Position p(String alg) {
        return Position.fromAlgebraic(alg);
    }

    @Test
    void whitePawnHasTwoForwardOptionsFromStart() {
        Board board = new Board();
        Pawn pawn = new Pawn(PieceColor.WHITE);
        board.place(pawn, p("e2"));
        List<Position> moves = pawn.possibleMoves(board, p("e2"));
        assertTrue(moves.contains(p("e3")));
        assertTrue(moves.contains(p("e4")));
        assertEquals(2, moves.size());
    }

    @Test
    void whitePawnAfterFirstMoveOnlySingleStep() {
        Board board = new Board();
        Pawn pawn = new Pawn(PieceColor.WHITE);
        board.place(pawn, p("e3"));
        List<Position> moves = pawn.possibleMoves(board, p("e3"));
        assertEquals(List.of(p("e4")), moves);
    }

    @Test
    void whitePawnCapturesDiagonally() {
        Board board = new Board();
        Pawn pawn = new Pawn(PieceColor.WHITE);
        board.place(pawn, p("e4"));
        board.place(new Pawn(PieceColor.BLACK), p("d5"));
        board.place(new Pawn(PieceColor.BLACK), p("f5"));
        List<Position> moves = pawn.possibleMoves(board, p("e4"));
        assertTrue(moves.contains(p("d5")));
        assertTrue(moves.contains(p("f5")));
        assertTrue(moves.contains(p("e5")));
    }

    @Test
    void rookOnEmptyBoardHas14Moves() {
        Board board = new Board();
        Rook rook = new Rook(PieceColor.WHITE);
        board.place(rook, p("d4"));
        List<Position> moves = rook.possibleMoves(board, p("d4"));
        assertEquals(14, moves.size());
    }

    @Test
    void rookStopsAtFriendlyPiece() {
        Board board = new Board();
        Rook rook = new Rook(PieceColor.WHITE);
        board.place(rook, p("d4"));
        board.place(new Pawn(PieceColor.WHITE), p("d6"));
        List<Position> moves = rook.possibleMoves(board, p("d4"));
        assertTrue(moves.contains(p("d5")));
        assertFalse(moves.contains(p("d6")));
        assertFalse(moves.contains(p("d7")));
    }

    @Test
    void rookCapturesEnemy() {
        Board board = new Board();
        Rook rook = new Rook(PieceColor.WHITE);
        board.place(rook, p("d4"));
        board.place(new Pawn(PieceColor.BLACK), p("d7"));
        List<Position> moves = rook.possibleMoves(board, p("d4"));
        assertTrue(moves.contains(p("d7")));
        assertFalse(moves.contains(p("d8")));
    }

    @Test
    void bishopOnD4HasFullDiagonals() {
        Board board = new Board();
        Bishop bishop = new Bishop(PieceColor.WHITE);
        board.place(bishop, p("d4"));
        List<Position> moves = bishop.possibleMoves(board, p("d4"));
        assertEquals(13, moves.size());
    }

    @Test
    void queenCombinesRookAndBishop() {
        Board board = new Board();
        Queen queen = new Queen(PieceColor.WHITE);
        board.place(queen, p("d4"));
        List<Position> moves = queen.possibleMoves(board, p("d4"));
        assertEquals(27, moves.size());
    }

    @Test
    void knightFromD4HasEightTargets() {
        Board board = new Board();
        Knight knight = new Knight(PieceColor.WHITE);
        board.place(knight, p("d4"));
        List<Position> moves = knight.possibleMoves(board, p("d4"));
        assertEquals(8, moves.size());
    }

    @Test
    void knightFromCornerHasTwoTargets() {
        Board board = new Board();
        Knight knight = new Knight(PieceColor.WHITE);
        board.place(knight, p("a1"));
        List<Position> moves = knight.possibleMoves(board, p("a1"));
        assertEquals(2, moves.size());
    }

    @Test
    void kingHasUpToEightSurroundingTargets() {
        Board board = new Board();
        King king = new King(PieceColor.WHITE);
        board.place(king, p("e4"));
        List<Position> moves = king.possibleMoves(board, p("e4"));
        assertEquals(8, moves.size());
    }
}
