package de.dhbw.chess.domain.service;

import de.dhbw.chess.domain.entity.Board;
import de.dhbw.chess.domain.entity.Piece;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;

/**
 * Domain Service: ermittelt, ob die Partie für die am Zug befindliche Farbe entschieden ist.
 * Schachmatt = König im Schach, kein legaler Zug. Patt = König nicht im Schach, kein legaler Zug.
 */
public class GameStateEvaluator {

    private final CheckDetector checkDetector;

    public GameStateEvaluator(CheckDetector checkDetector) {
        this.checkDetector = checkDetector;
    }

    public GameStateEvaluator() {
        this(new CheckDetector());
    }

    public boolean isCheckmate(Board board, PieceColor toMove) {
        return checkDetector.isInCheck(board, toMove) && !hasAnyLegalMove(board, toMove);
    }

    public boolean isStalemate(Board board, PieceColor toMove) {
        return !checkDetector.isInCheck(board, toMove) && !hasAnyLegalMove(board, toMove);
    }

    private boolean hasAnyLegalMove(Board board, PieceColor color) {
        for (int f = 0; f < Board.SIZE; f++) {
            for (int r = 0; r < Board.SIZE; r++) {
                Position from = Position.of(f, r);
                Piece p = board.pieceAt(from);
                if (p == null || p.color() != color) continue;
                for (Position to : p.possibleMoves(board, from)) {
                    Board probe = board.copy();
                    probe.move(from, to);
                    if (!checkDetector.isInCheck(probe, color)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
