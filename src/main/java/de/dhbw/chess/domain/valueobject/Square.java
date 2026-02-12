package de.dhbw.chess.domain.valueobject;

import java.util.Objects;

/**
 * Belegung eines Feldes auf dem Brett: eine Position zusammen mit der dort stehenden Figur
 * (oder leer). Value Object — wird vor allem für Sichten auf das Brett (Renderer, Iteratoren)
 * verwendet.
 */
public final class Square {

    private final Position position;
    private final PieceColor pieceColor;
    private final PieceType pieceType;

    private Square(Position position, PieceColor color, PieceType type) {
        this.position = Objects.requireNonNull(position, "position");
        this.pieceColor = color;
        this.pieceType = type;
    }

    public static Square empty(Position position) {
        return new Square(position, null, null);
    }

    public static Square occupied(Position position, PieceColor color, PieceType type) {
        Objects.requireNonNull(color, "color");
        Objects.requireNonNull(type, "type");
        return new Square(position, color, type);
    }

    public Position position() {
        return position;
    }

    public boolean isEmpty() {
        return pieceColor == null;
    }

    public PieceColor pieceColor() {
        return pieceColor;
    }

    public PieceType pieceType() {
        return pieceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Square s)) return false;
        return position.equals(s.position)
                && pieceColor == s.pieceColor
                && pieceType == s.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, pieceColor, pieceType);
    }
}
