package de.dhbw.chess.presentation;

import de.dhbw.chess.domain.valueobject.Move;
import de.dhbw.chess.domain.valueobject.PieceType;
import de.dhbw.chess.domain.valueobject.Position;

/**
 * Übersetzt Eingaben des Users (z. B. {@code e2-e4}, {@code e7-e8=Q}, {@code O-O}) in
 * Domain-{@link Move}s. Bewusst minimalistisch — keine algebraische Disambiguierung, dafür
 * eindeutiges From-To-Format.
 */
public class InputParser {

    public Move parseMove(String input) {
        if (input == null) throw new IllegalArgumentException("Eingabe leer");
        String s = input.trim();
        if (s.equalsIgnoreCase("O-O") || s.equalsIgnoreCase("0-0")) {
            // Kurze Rochade — wird in der Hauptschleife mit aktiver Farbe aufgelöst.
            throw new ShortcutMoveException(true);
        }
        if (s.equalsIgnoreCase("O-O-O") || s.equalsIgnoreCase("0-0-0")) {
            throw new ShortcutMoveException(false);
        }
        String[] parts = s.split("=", 2);
        String[] squares = parts[0].split("-", 2);
        if (squares.length != 2 || squares[0].length() != 2 || squares[1].length() != 2) {
            throw new IllegalArgumentException("Erwartet z. B. e2-e4, gegeben: " + input);
        }
        Position from = Position.fromAlgebraic(squares[0]);
        Position to = Position.fromAlgebraic(squares[1]);
        if (parts.length == 2) {
            PieceType promotion = PieceType.fromSymbol(parts[1].charAt(0));
            return new Move(from, to, promotion);
        }
        return new Move(from, to);
    }

    /** Wird ausgelöst, wenn der User eine kurze Rochade-Notation eingibt. */
    public static final class ShortcutMoveException extends RuntimeException {
        private final boolean kingside;
        public ShortcutMoveException(boolean kingside) {
            super(kingside ? "O-O" : "O-O-O");
            this.kingside = kingside;
        }
        public boolean isKingside() { return kingside; }
    }
}
