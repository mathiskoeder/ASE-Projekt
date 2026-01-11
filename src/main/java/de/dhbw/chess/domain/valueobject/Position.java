package de.dhbw.chess.domain.valueobject;

import java.util.Objects;

/**
 * Koordinate auf dem Schachbrett (file 0–7 = a–h, rank 0–7 = 1–8).
 * Reines Value Object: unveränderlich, gleichheit über (file, rank).
 */
public final class Position {

    private static final int MIN = 0;
    private static final int MAX = 7;

    private final int file;
    private final int rank;

    public Position(int file, int rank) {
        if (file < MIN || file > MAX || rank < MIN || rank > MAX) {
            throw new IllegalArgumentException(
                    "Position außerhalb des Bretts: file=" + file + ", rank=" + rank);
        }
        this.file = file;
        this.rank = rank;
    }

    public static Position of(int file, int rank) {
        return new Position(file, rank);
    }

    /** Erzeugt eine Position aus algebraischer Notation, z. B. "e4". */
    public static Position fromAlgebraic(String algebraic) {
        Objects.requireNonNull(algebraic, "algebraic");
        if (algebraic.length() != 2) {
            throw new IllegalArgumentException("Ungültige Notation: " + algebraic);
        }
        char fileChar = Character.toLowerCase(algebraic.charAt(0));
        char rankChar = algebraic.charAt(1);
        int file = fileChar - 'a';
        int rank = rankChar - '1';
        return new Position(file, rank);
    }

    public int file() {
        return file;
    }

    public int rank() {
        return rank;
    }

    public int fileDelta(Position other) {
        return other.file - this.file;
    }

    public int rankDelta(Position other) {
        return other.rank - this.rank;
    }

    public String toAlgebraic() {
        return "" + (char) ('a' + file) + (char) ('1' + rank);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position other)) return false;
        return file == other.file && rank == other.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, rank);
    }

    @Override
    public String toString() {
        return toAlgebraic();
    }
}
