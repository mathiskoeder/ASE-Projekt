package de.dhbw.chess.infrastructure.persistence;

import de.dhbw.chess.application.factory.GameFactory;
import de.dhbw.chess.domain.entity.Board;
import de.dhbw.chess.domain.entity.King;
import de.dhbw.chess.domain.entity.Pawn;
import de.dhbw.chess.domain.entity.Piece;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.PieceType;
import de.dhbw.chess.domain.valueobject.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class FenSerializerTest {

    private static Position p(String alg) {
        return Position.fromAlgebraic(alg);
    }

    @Test
    void initialPositionFen() {
        Board board = GameFactory.standardBoard();
        String fen = FenSerializer.serializePlacement(board);
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", fen);
    }

    @Test
    void roundTripPreservesPieces() {
        Board original = GameFactory.standardBoard();
        String fen = FenSerializer.serializePlacement(original);
        Board parsed = FenSerializer.parsePlacement(fen);
        for (int f = 0; f < Board.SIZE; f++) {
            for (int r = 0; r < Board.SIZE; r++) {
                Piece a = original.pieceAt(Position.of(f, r));
                Piece b = parsed.pieceAt(Position.of(f, r));
                if (a == null) {
                    assertNull(b);
                } else {
                    assertNotNull(b);
                    assertEquals(a.type(), b.type());
                    assertEquals(a.color(), b.color());
                }
            }
        }
    }

    @Test
    void sparseBoardFen() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new King(PieceColor.BLACK), p("e8"));
        board.place(new Pawn(PieceColor.WHITE), p("d4"));
        String fen = FenSerializer.serializePlacement(board);
        assertEquals("4k3/8/8/8/3P4/8/8/4K3", fen);
    }

    @Test
    void parseSparseFenRestoresPiece() {
        Board board = FenSerializer.parsePlacement("4k3/8/8/8/3P4/8/8/4K3");
        assertEquals(PieceType.PAWN, board.pieceAt(p("d4")).type());
        assertEquals(PieceColor.WHITE, board.pieceAt(p("d4")).color());
    }
}
