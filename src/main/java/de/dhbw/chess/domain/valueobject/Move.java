package de.dhbw.chess.domain.valueobject;

import java.util.Objects;

/**
 * Beschreibt einen Zugwunsch von {@code from} nach {@code to}, optional mit Promotion.
 * Value Object: unveränderlich, gleichheit über alle Felder.
 *
 * <p>Die Klasse beschreibt nur die Absicht eines Zuges — ob der Zug regelkonform ist,
 * entscheidet der {@code MoveValidator} bzw. später die jeweilige Figur.</p>
 */
public final class Move {

    private final Position from;
    private final Position to;
    private final PieceType promotion;

    public Move(Position from, Position to) {
        this(from, to, null);
    }

    public Move(Position from, Position to, PieceType promotion) {
        this.from = Objects.requireNonNull(from, "from");
        this.to = Objects.requireNonNull(to, "to");
        if (from.equals(to)) {
            throw new IllegalArgumentException("from und to dürfen nicht identisch sein");
        }
        if (promotion != null && (promotion == PieceType.PAWN || promotion == PieceType.KING)) {
            throw new IllegalArgumentException("Promotion zu " + promotion + " ist nicht erlaubt");
        }
        this.promotion = promotion;
    }

    public Position from() {
        return from;
    }

    public Position to() {
        return to;
    }

    public PieceType promotion() {
        return promotion;
    }

    public boolean isPromotion() {
        return promotion != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move other)) return false;
        return from.equals(other.from)
                && to.equals(other.to)
                && promotion == other.promotion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, promotion);
    }

    @Override
    public String toString() {
        String base = from.toAlgebraic() + "-" + to.toAlgebraic();
        return promotion == null ? base : base + "=" + promotion.symbol();
    }
}
