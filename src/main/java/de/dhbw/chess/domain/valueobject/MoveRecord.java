package de.dhbw.chess.domain.valueobject;

import java.util.Objects;

/**
 * Eintrag in der Zughistorie. Hält den ausgeführten Zug, ziehende Farbe, ggf. geschlagenes
 * Material und Sondermerkmale (Rochade, En passant). Value Object — unveränderlich, gleichheit
 * über alle Felder.
 */
public final class MoveRecord {

    private final Move move;
    private final PieceColor mover;
    private final PieceType movedType;
    private final PieceType capturedType;
    private final boolean enPassant;
    private final boolean kingsideCastle;
    private final boolean queensideCastle;
    private final boolean check;
    private final boolean checkmate;

    private MoveRecord(Builder b) {
        this.move = Objects.requireNonNull(b.move, "move");
        this.mover = Objects.requireNonNull(b.mover, "mover");
        this.movedType = Objects.requireNonNull(b.movedType, "movedType");
        this.capturedType = b.capturedType;
        this.enPassant = b.enPassant;
        this.kingsideCastle = b.kingsideCastle;
        this.queensideCastle = b.queensideCastle;
        this.check = b.check;
        this.checkmate = b.checkmate;
    }

    public Move move() { return move; }
    public PieceColor mover() { return mover; }
    public PieceType movedType() { return movedType; }
    public PieceType capturedType() { return capturedType; }
    public boolean isCapture() { return capturedType != null; }
    public boolean isEnPassant() { return enPassant; }
    public boolean isKingsideCastle() { return kingsideCastle; }
    public boolean isQueensideCastle() { return queensideCastle; }
    public boolean isCastle() { return kingsideCastle || queensideCastle; }
    public boolean isCheck() { return check; }
    public boolean isCheckmate() { return checkmate; }

    /**
     * Notation in PGN-light Form: {@code from-to} bzw. {@code O-O} / {@code O-O-O} für Rochaden,
     * mit Promotion-Suffix {@code =X} und Schach-Markierungen {@code +} / {@code #}.
     */
    public String notation() {
        String core;
        if (kingsideCastle) {
            core = "O-O";
        } else if (queensideCastle) {
            core = "O-O-O";
        } else {
            core = move.from().toAlgebraic() + "-" + move.to().toAlgebraic();
            if (move.isPromotion()) {
                core += "=" + move.promotion().symbol();
            }
        }
        if (checkmate) return core + "#";
        if (check) return core + "+";
        return core;
    }

    public static Builder builder(Move move, PieceColor mover, PieceType movedType) {
        return new Builder(move, mover, movedType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoveRecord r)) return false;
        return enPassant == r.enPassant
                && kingsideCastle == r.kingsideCastle
                && queensideCastle == r.queensideCastle
                && check == r.check
                && checkmate == r.checkmate
                && move.equals(r.move)
                && mover == r.mover
                && movedType == r.movedType
                && capturedType == r.capturedType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(move, mover, movedType, capturedType,
                enPassant, kingsideCastle, queensideCastle, check, checkmate);
    }

    @Override
    public String toString() {
        return notation();
    }

    public static final class Builder {
        private final Move move;
        private final PieceColor mover;
        private final PieceType movedType;
        private PieceType capturedType;
        private boolean enPassant;
        private boolean kingsideCastle;
        private boolean queensideCastle;
        private boolean check;
        private boolean checkmate;

        private Builder(Move move, PieceColor mover, PieceType movedType) {
            this.move = move;
            this.mover = mover;
            this.movedType = movedType;
        }

        public Builder captured(PieceType type) { this.capturedType = type; return this; }
        public Builder enPassant(boolean v) { this.enPassant = v; return this; }
        public Builder kingsideCastle(boolean v) { this.kingsideCastle = v; return this; }
        public Builder queensideCastle(boolean v) { this.queensideCastle = v; return this; }
        public Builder check(boolean v) { this.check = v; return this; }
        public Builder checkmate(boolean v) { this.checkmate = v; return this; }

        public MoveRecord build() {
            return new MoveRecord(this);
        }
    }
}
