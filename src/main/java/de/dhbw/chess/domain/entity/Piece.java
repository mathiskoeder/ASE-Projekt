package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.PieceType;
import de.dhbw.chess.domain.valueobject.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Erste, einfache Modellierung einer Schachfigur.
 *
 * <p><b>Code Smell — bewusst vorhanden:</b> die Zuglogik wird hier zentral über einen
 * {@code switch} auf {@link PieceType} entschieden. Das ist ein klassischer Verstoß gegen das
 * Open-Closed-Prinzip: jede neue Figur erfordert eine Änderung an dieser Klasse. In einer späteren
 * Phase wird das per Refactoring (Replace Conditional with Polymorphism) auf eine
 * Strategy-Hierarchie umgestellt.</p>
 */
public class Piece {

    private final PieceType type;
    private final PieceColor color;
    private boolean hasMoved;

    public Piece(PieceType type, PieceColor color) {
        this.type = Objects.requireNonNull(type, "type");
        this.color = Objects.requireNonNull(color, "color");
    }

    public PieceType type() {
        return type;
    }

    public PieceColor color() {
        return color;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void markMoved() {
        this.hasMoved = true;
    }

    /** Pseudo-legale Zielfelder ohne Berücksichtigung von Schach. Wird in Folge-Commits gefüllt. */
    public List<Position> possibleMoves(Board board, Position from) {
        List<Position> moves = new ArrayList<>();
        switch (type) {
            case PAWN:
                // wird in nächstem Commit befüllt
                break;
            case ROOK:
            case BISHOP:
            case QUEEN:
                // wird in nächstem Commit befüllt
                break;
            case KNIGHT:
            case KING:
                // wird in nächstem Commit befüllt
                break;
            default:
                throw new IllegalStateException("Unbekannter Figurentyp: " + type);
        }
        return moves;
    }
}
