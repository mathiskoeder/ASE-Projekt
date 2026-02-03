package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.PieceType;

public final class Rook extends SlidingPiece {

    private static final int[][] DIRS = {{1,0},{-1,0},{0,1},{0,-1}};

    public Rook(PieceColor color) {
        super(PieceType.ROOK, color);
    }

    @Override
    protected int[][] directions() {
        return DIRS;
    }
}
