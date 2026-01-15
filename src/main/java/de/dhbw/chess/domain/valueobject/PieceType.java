package de.dhbw.chess.domain.valueobject;

/**
 * Figurentyp. Trägt das Symbol für die FEN-/PGN-Notation und einen rohen Materialwert,
 * den die Domäne (z. B. einfache Bewertung, Promotion-Auswahl) verwenden kann.
 */
public enum PieceType {

    KING('K', 0),
    QUEEN('Q', 9),
    ROOK('R', 5),
    BISHOP('B', 3),
    KNIGHT('N', 3),
    PAWN('P', 1);

    private final char symbol;
    private final int materialValue;

    PieceType(char symbol, int materialValue) {
        this.symbol = symbol;
        this.materialValue = materialValue;
    }

    public char symbol() {
        return symbol;
    }

    public int materialValue() {
        return materialValue;
    }

    /** Symbol in Groß-/Kleinschreibung passend zu FEN (Weiß groß, Schwarz klein). */
    public char fenSymbol(PieceColor color) {
        return color.isWhite() ? Character.toUpperCase(symbol) : Character.toLowerCase(symbol);
    }

    public static PieceType fromSymbol(char symbol) {
        char upper = Character.toUpperCase(symbol);
        for (PieceType type : values()) {
            if (type.symbol == upper) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unbekanntes Figurensymbol: " + symbol);
    }
}
