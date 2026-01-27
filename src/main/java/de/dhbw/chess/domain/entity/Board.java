package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.Position;

/**
 * Vorläufiger Stub des Bretts. In Phase 4 wird die Klasse vollständig ausgebaut. Hier nur das
 * Minimum, das die ersten {@link Piece}-Implementierungen benötigen.
 */
public class Board {

    private final Piece[][] squares = new Piece[8][8];

    public Piece pieceAt(Position pos) {
        return squares[pos.file()][pos.rank()];
    }

    public void place(Piece piece, Position pos) {
        squares[pos.file()][pos.rank()] = piece;
    }

    public boolean isEmpty(Position pos) {
        return pieceAt(pos) == null;
    }

    public boolean isEnemy(Position pos, de.dhbw.chess.domain.valueobject.PieceColor own) {
        Piece p = pieceAt(pos);
        return p != null && p.color() != own;
    }
}
