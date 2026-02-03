package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.PieceType;

public final class Queen extends SlidingPiece {

    private static final int[][] DIRS = {
            {1,0},{-1,0},{0,1},{0,-1},
            {1,1},{1,-1},{-1,1},{-1,-1}
    };

    public Queen(PieceColor color) {
        super(PieceType.QUEEN, color);
    }

    @Override
    protected int[][] directions() {
        return DIRS;
    }
}
