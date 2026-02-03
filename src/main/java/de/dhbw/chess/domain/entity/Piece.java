package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.PieceType;
import de.dhbw.chess.domain.valueobject.Position;

import java.util.List;
import java.util.Objects;

/**
 * Abstrakte Schachfigur.
 *
 * <p><b>Refactoring 1 — Replace Conditional with Polymorphism (Strategy Pattern):</b>
 * Die ursprüngliche {@code switch}-basierte Implementierung wurde aufgelöst. Jede Figur kapselt
 * ihre Zuglogik nun in einer eigenen Subklasse. Das schließt die Klasse für Erweiterungen offen
 * (OCP) — neue Figurentypen entstehen durch Hinzufügen einer Subklasse, ohne dass diese Basis-
 * klasse oder bestehende Figuren angefasst werden müssen.</p>
 */
public abstract class Piece {

    private final PieceType type;
    private final PieceColor color;
    private boolean hasMoved;

    protected Piece(PieceType type, PieceColor color) {
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

    /** Pseudo-legale Zielfelder ohne Berücksichtigung von Schach gegen den eigenen König. */
    public abstract List<Position> possibleMoves(Board board, Position from);

    @Override
    public String toString() {
        return color + " " + type;
    }
}
