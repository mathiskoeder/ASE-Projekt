package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.PieceType;

public final class Bishop extends SlidingPiece {

    private static final int[][] DIRS = {{1,1},{1,-1},{-1,1},{-1,-1}};

    public Bishop(PieceColor color) {
        super(PieceType.BISHOP, color);
    }

    @Override
    protected int[][] directions() {
        return DIRS;
    }
}
