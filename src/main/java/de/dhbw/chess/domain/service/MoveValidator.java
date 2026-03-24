package de.dhbw.chess.domain.service;

import de.dhbw.chess.domain.entity.Board;
import de.dhbw.chess.domain.entity.King;
import de.dhbw.chess.domain.entity.MoveHistory;
import de.dhbw.chess.domain.entity.Pawn;
import de.dhbw.chess.domain.entity.Piece;
import de.dhbw.chess.domain.valueobject.Move;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;

import java.util.List;

/**
 * Domain Service: prüft, ob ein Zug für die aktuelle Stellung regelkonform ist. Berücksichtigt
 * Standardzüge, Rochade und En passant.
 */
public class MoveValidator {

    private final CheckDetector checkDetector;
    private final CastlingRules castlingRules;
    private final EnPassantRules enPassantRules;

    public MoveValidator(CheckDetector checkDetector) {
        this.checkDetector = checkDetector;
        this.castlingRules = new CastlingRules(checkDetector);
        this.enPassantRules = new EnPassantRules();
    }

    public MoveValidator() {
        this(new CheckDetector());
    }

    public void validate(Board board, Move move, PieceColor activeColor, MoveHistory history) {
        Piece moving = board.pieceAt(move.from());
        if (moving == null) {
            throw new IllegalArgumentException("Kein Stück auf " + move.from());
        }
        if (moving.color() != activeColor) {
            throw new IllegalArgumentException("Falsche Farbe am Zug");
        }

        if (moving instanceof King && castlingRules.isCastlingMove(board, move)) {
            castlingRules.verify(board, move, activeColor);
            return;
        }

        if (moving instanceof Pawn
                && enPassantRules.isEnPassantMove(board, move, history, activeColor)) {
            verifyEnPassantDoesNotExposeKing(board, move, activeColor);
            return;
        }

        List<Position> targets = moving.possibleMoves(board, move.from());
        if (!targets.contains(move.to())) {
            throw new IllegalArgumentException("Zug nicht erlaubt für " + moving.type());
        }
        verifyPromotion(moving, move, activeColor);
        Board probe = board.copy();
        probe.move(move.from(), move.to());
        if (checkDetector.isInCheck(probe, activeColor)) {
            throw new IllegalArgumentException("Zug lässt eigenen König im Schach");
        }
    }

    private void verifyPromotion(Piece moving, Move move, PieceColor color) {
        boolean reachesLastRank = moving instanceof Pawn
                && move.to().rank() == (color.isWhite() ? 7 : 0);
        if (reachesLastRank && !move.isPromotion()) {
            throw new IllegalArgumentException("Bauernumwandlung erforderlich");
        }
        if (!reachesLastRank && move.isPromotion()) {
            throw new IllegalArgumentException("Promotion ist nur auf der letzten Reihe erlaubt");
        }
    }

    private void verifyEnPassantDoesNotExposeKing(Board board, Move move, PieceColor color) {
        Board probe = board.copy();
        probe.remove(enPassantRules.capturedSquare(move));
        probe.move(move.from(), move.to());
        if (checkDetector.isInCheck(probe, color)) {
            throw new IllegalArgumentException("En passant lässt eigenen König im Schach");
        }
    }

    public CastlingRules castlingRules() { return castlingRules; }
    public EnPassantRules enPassantRules() { return enPassantRules; }
}
