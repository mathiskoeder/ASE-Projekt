package de.dhbw.chess.domain.service;

import de.dhbw.chess.domain.entity.Board;
import de.dhbw.chess.domain.entity.Piece;
import de.dhbw.chess.domain.valueobject.Move;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;

import java.util.List;

/**
 * Domain Service: prüft, ob ein Zug für die aktuelle Stellung regelkonform ist.
 *
 * <p>Das war zuvor inline im {@code Game}-Aggregat enthalten und wurde im Refactoring 2
 * (Extract Class) hierher verschoben. Damit erhält das Aggregat eine klare Schnittstelle
 * (delegieren) und der Service kann eigenständig getestet sowie erweitert werden
 * (z. B. um Sonderzüge wie Rochade, En passant, Promotion).</p>
 */
public class MoveValidator {

    private final CheckDetector checkDetector;

    public MoveValidator(CheckDetector checkDetector) {
        this.checkDetector = checkDetector;
    }

    public MoveValidator() {
        this(new CheckDetector());
    }

    /**
     * Prüft Eigentümerschaft, pseudo-legale Zielfelder und Eigenschach. Wirft eine
     * {@code IllegalArgumentException}, falls der Zug nicht zulässig ist.
     */
    public void validate(Board board, Move move, PieceColor activeColor) {
        Piece moving = board.pieceAt(move.from());
        if (moving == null) {
            throw new IllegalArgumentException("Kein Stück auf " + move.from());
        }
        if (moving.color() != activeColor) {
            throw new IllegalArgumentException("Falsche Farbe am Zug");
        }
        List<Position> targets = moving.possibleMoves(board, move.from());
        if (!targets.contains(move.to())) {
            throw new IllegalArgumentException("Zug nicht erlaubt für " + moving.type());
        }
        Board probe = board.copy();
        probe.move(move.from(), move.to());
        if (checkDetector.isInCheck(probe, activeColor)) {
            throw new IllegalArgumentException("Zug lässt eigenen König im Schach");
        }
    }
}
