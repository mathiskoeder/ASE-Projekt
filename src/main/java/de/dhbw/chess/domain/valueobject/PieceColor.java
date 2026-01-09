package de.dhbw.chess.domain.valueobject;

/**
 * Farbe einer Spielfigur. Value Object — die beiden Konstanten sind unveränderlich
 * und teilen sich pro Farbe genau eine Instanz (Enum-Garantie).
 */
public enum PieceColor {

    WHITE,
    BLACK;

    public PieceColor opposite() {
        return this == WHITE ? BLACK : WHITE;
    }

    public boolean isWhite() {
        return this == WHITE;
    }

    public boolean isBlack() {
        return this == BLACK;
    }
}
