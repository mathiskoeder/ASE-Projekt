package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.PieceType;
import de.dhbw.chess.domain.valueobject.Position;

import java.util.ArrayList;
import java.util.List;

public final class Knight extends Piece {

    private static final int[][] STEPS = {
            {1,2},{2,1},{-1,2},{-2,1},{1,-2},{2,-1},{-1,-2},{-2,-1}
    };

    public Knight(PieceColor color) {
        super(PieceType.KNIGHT, color);
    }

    @Override
    public List<Position> possibleMoves(Board board, Position from) {
        List<Position> moves = new ArrayList<>();
        for (int[] s : STEPS) {
            int f = from.file() + s[0];
            int r = from.rank() + s[1];
            if (f < 0 || f > 7 || r < 0 || r > 7) continue;
            Position p = Position.of(f, r);
            if (board.isEmpty(p) || board.isEnemy(p, color())) {
                moves.add(p);
            }
        }
        return moves;
    }
}
