package de.dhbw.chess.domain.service;

import de.dhbw.chess.domain.entity.Board;
import de.dhbw.chess.domain.entity.Piece;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;

/**
 * Domain Service: erkennt, ob der König einer gegebenen Farbe von einer feindlichen Figur
 * angegriffen wird.
 */
public class CheckDetector {

    public boolean isInCheck(Board board, PieceColor color) {
        Position kingPos = board.findKing(color);
        if (kingPos == null) {
            return false;
        }
        return isAttacked(board, kingPos, color.opposite());
    }

    /** Liefert true, wenn das Feld von mindestens einer Figur der angreifenden Farbe bedroht ist. */
    public boolean isAttacked(Board board, Position target, PieceColor attacker) {
        for (int f = 0; f < Board.SIZE; f++) {
            for (int r = 0; r < Board.SIZE; r++) {
                Position from = Position.of(f, r);
                Piece p = board.pieceAt(from);
                if (p == null || p.color() != attacker) continue;
                if (p.possibleMoves(board, from).contains(target)) {
                    return true;
                }
            }
        }
        return false;
    }
}
