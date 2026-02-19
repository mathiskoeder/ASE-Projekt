package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;
import de.dhbw.chess.domain.valueobject.Square;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Schachbrett — Container für Figuren, ohne Spielregelwissen. Stellt grundlegende Operationen
 * (place, remove, move, scan) bereit. Die Regelhoheit liegt bei den Figuren bzw. später beim
 * {@code MoveValidator}.
 */
public class Board {

    public static final int SIZE = 8;

    private final Piece[][] squares = new Piece[SIZE][SIZE];

    public Piece pieceAt(Position pos) {
        return squares[pos.file()][pos.rank()];
    }

    public void place(Piece piece, Position pos) {
        Objects.requireNonNull(piece, "piece");
        squares[pos.file()][pos.rank()] = piece;
    }

    public Piece remove(Position pos) {
        Piece p = squares[pos.file()][pos.rank()];
        squares[pos.file()][pos.rank()] = null;
        return p;
    }

    /**
     * Verschiebt eine Figur ohne Regelprüfung. Liefert die ggf. geschlagene Figur zurück.
     */
    public Piece move(Position from, Position to) {
        Piece moving = squares[from.file()][from.rank()];
        if (moving == null) {
            throw new IllegalStateException("Kein Stück auf " + from);
        }
        Piece captured = squares[to.file()][to.rank()];
        squares[to.file()][to.rank()] = moving;
        squares[from.file()][from.rank()] = null;
        moving.markMoved();
        return captured;
    }

    public boolean isEmpty(Position pos) {
        return pieceAt(pos) == null;
    }

    public boolean isEnemy(Position pos, PieceColor own) {
        Piece p = pieceAt(pos);
        return p != null && p.color() != own;
    }

    public boolean isFriendly(Position pos, PieceColor own) {
        Piece p = pieceAt(pos);
        return p != null && p.color() == own;
    }

    /** Liefert die Position des Königs der gegebenen Farbe oder {@code null}. */
    public Position findKing(PieceColor color) {
        for (int f = 0; f < SIZE; f++) {
            for (int r = 0; r < SIZE; r++) {
                Piece p = squares[f][r];
                if (p instanceof King && p.color() == color) {
                    return Position.of(f, r);
                }
            }
        }
        return null;
    }

    /** Belegung als unveränderliche Liste (nützlich für Renderer und Tests). */
    public List<Square> snapshot() {
        List<Square> list = new ArrayList<>(SIZE * SIZE);
        for (int r = SIZE - 1; r >= 0; r--) {
            for (int f = 0; f < SIZE; f++) {
                Piece p = squares[f][r];
                Position pos = Position.of(f, r);
                list.add(p == null
                        ? Square.empty(pos)
                        : Square.occupied(pos, p.color(), p.type()));
            }
        }
        return list;
    }

    /** Tiefe Kopie des Bretts, etwa zum Probespielen während der Schach-Prüfung. */
    public Board copy() {
        Board copy = new Board();
        for (int f = 0; f < SIZE; f++) {
            for (int r = 0; r < SIZE; r++) {
                copy.squares[f][r] = squares[f][r];
            }
        }
        return copy;
    }
}
