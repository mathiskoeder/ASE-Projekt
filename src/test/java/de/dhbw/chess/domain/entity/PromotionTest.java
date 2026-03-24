package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.Move;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.PieceType;
import de.dhbw.chess.domain.valueobject.Position;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PromotionTest {

    private static Position p(String alg) {
        return Position.fromAlgebraic(alg);
    }

    private Game gameWith(Board board) {
        Player white = new Player(UUID.randomUUID(), "Max", PieceColor.WHITE);
        Player black = new Player(UUID.randomUUID(), "Mathis", PieceColor.BLACK);
        return new Game(UUID.randomUUID(), white, black, board);
    }

    private Board boardWithPawnNearPromotion() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("a1"));
        board.place(new King(PieceColor.BLACK), p("h8"));
        board.place(new Pawn(PieceColor.WHITE), p("e7"));
        return board;
    }

    @Test
    void promotionToQueenReplacesPawn() {
        Game game = gameWith(boardWithPawnNearPromotion());
        game.makeMove(new Move(p("e7"), p("e8"), PieceType.QUEEN));
        assertInstanceOf(Queen.class, game.board().pieceAt(p("e8")));
    }

    @Test
    void promotionToRook() {
        Game game = gameWith(boardWithPawnNearPromotion());
        game.makeMove(new Move(p("e7"), p("e8"), PieceType.ROOK));
        assertInstanceOf(Rook.class, game.board().pieceAt(p("e8")));
    }

    @Test
    void promotionToBishop() {
        Game game = gameWith(boardWithPawnNearPromotion());
        game.makeMove(new Move(p("e7"), p("e8"), PieceType.BISHOP));
        assertInstanceOf(Bishop.class, game.board().pieceAt(p("e8")));
    }

    @Test
    void promotionToKnight() {
        Game game = gameWith(boardWithPawnNearPromotion());
        game.makeMove(new Move(p("e7"), p("e8"), PieceType.KNIGHT));
        assertInstanceOf(Knight.class, game.board().pieceAt(p("e8")));
    }

    @Test
    void promotionRequiredOnLastRank() {
        Game game = gameWith(boardWithPawnNearPromotion());
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Move(p("e7"), p("e8"))));
    }

    @Test
    void promotionRejectedWhenNotOnLastRank() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("a1"));
        board.place(new King(PieceColor.BLACK), p("h8"));
        board.place(new Pawn(PieceColor.WHITE), p("e6"));
        Game game = gameWith(board);
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Move(p("e6"), p("e7"), PieceType.QUEEN)));
    }

    @Test
    void promotionMoveRecordedAsPawn() {
        Game game = gameWith(boardWithPawnNearPromotion());
        var rec = game.makeMove(new Move(p("e7"), p("e8"), PieceType.QUEEN));
        assertTrue(rec.move().isPromotion());
    }
}
