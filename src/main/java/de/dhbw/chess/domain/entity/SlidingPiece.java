package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Hilfsbasis für Schiebefiguren (Turm, Läufer, Dame).
 * Die Subklasse liefert die zulässigen Richtungen und beerbt damit die Lauflogik.
 */
abstract class SlidingPiece extends Piece {

    SlidingPiece(de.dhbw.chess.domain.valueobject.PieceType type, PieceColor color) {
        super(type, color);
    }

    protected abstract int[][] directions();

    @Override
    public List<Position> possibleMoves(Board board, Position from) {
        List<Position> moves = new ArrayList<>();
        for (int[] d : directions()) {
            int f = from.file() + d[0];
            int r = from.rank() + d[1];
            while (f >= 0 && f <= 7 && r >= 0 && r <= 7) {
                Position p = Position.of(f, r);
                if (board.isEmpty(p)) {
                    moves.add(p);
                } else {
                    if (board.isEnemy(p, color())) {
                        moves.add(p);
                    }
                    break;
                }
                f += d[0];
                r += d[1];
            }
        }
        return moves;
    }
}
