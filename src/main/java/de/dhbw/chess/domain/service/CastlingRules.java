package de.dhbw.chess.domain.service;

import de.dhbw.chess.domain.entity.Board;
import de.dhbw.chess.domain.entity.King;
import de.dhbw.chess.domain.entity.Piece;
import de.dhbw.chess.domain.entity.Rook;
import de.dhbw.chess.domain.valueobject.Move;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;

/**
 * Erkennt Rochade-Züge und prüft die zugehörigen Bedingungen:
 * König und Turm haben sich nicht bewegt, Felder zwischen ihnen sind frei,
 * der König steht weder vor noch während noch nach dem Zug im Schach.
 */
public class CastlingRules {

    private final CheckDetector checkDetector;

    public CastlingRules(CheckDetector checkDetector) {
        this.checkDetector = checkDetector;
    }

    public boolean isCastlingMove(Board board, Move move) {
        Piece moving = board.pieceAt(move.from());
        if (!(moving instanceof King)) return false;
        return Math.abs(move.to().file() - move.from().file()) == 2
                && move.from().rank() == move.to().rank();
    }

    public boolean isKingside(Move move) {
        return move.to().file() > move.from().file();
    }

    public Position rookFrom(Move move) {
        int rank = move.from().rank();
        return isKingside(move) ? Position.of(7, rank) : Position.of(0, rank);
    }

    public Position rookTo(Move move) {
        int rank = move.from().rank();
        return isKingside(move) ? Position.of(5, rank) : Position.of(3, rank);
    }

    public void verify(Board board, Move move, PieceColor color) {
        Piece kingPiece = board.pieceAt(move.from());
        if (!(kingPiece instanceof King) || kingPiece.hasMoved()) {
            throw new IllegalArgumentException("Rochade nicht möglich: König hat sich bewegt");
        }
        Piece rookPiece = board.pieceAt(rookFrom(move));
        if (!(rookPiece instanceof Rook) || rookPiece.color() != color || rookPiece.hasMoved()) {
            throw new IllegalArgumentException("Rochade nicht möglich: Turm fehlt oder hat gezogen");
        }
        int dir = isKingside(move) ? 1 : -1;
        int rank = move.from().rank();
        int startFile = move.from().file() + dir;
        int endFile = isKingside(move) ? 6 : 1;
        for (int f = Math.min(startFile, endFile); f <= Math.max(startFile, endFile); f++) {
            if (!board.isEmpty(Position.of(f, rank))) {
                throw new IllegalArgumentException("Rochade nicht möglich: Felder belegt");
            }
        }
        if (checkDetector.isInCheck(board, color)) {
            throw new IllegalArgumentException("Rochade aus dem Schach heraus nicht erlaubt");
        }
        // König darf während der Bewegung nicht über bedrohte Felder ziehen.
        for (int f = move.from().file(); f != move.to().file() + dir; f += dir) {
            Board probe = board.copy();
            probe.move(move.from(), Position.of(f, rank));
            if (checkDetector.isInCheck(probe, color)) {
                throw new IllegalArgumentException("Rochade durch bedrohte Felder nicht erlaubt");
            }
        }
    }
}
