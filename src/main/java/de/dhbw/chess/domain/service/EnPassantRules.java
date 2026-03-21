package de.dhbw.chess.domain.service;

import de.dhbw.chess.domain.entity.Board;
import de.dhbw.chess.domain.entity.MoveHistory;
import de.dhbw.chess.domain.entity.Pawn;
import de.dhbw.chess.domain.entity.Piece;
import de.dhbw.chess.domain.valueobject.Move;
import de.dhbw.chess.domain.valueobject.MoveRecord;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.PieceType;
import de.dhbw.chess.domain.valueobject.Position;

/**
 * Erkennt En-passant-Schläge anhand der Zughistorie. Ein En passant ist möglich, wenn der
 * unmittelbar vorhergehende gegnerische Zug ein Bauern-Doppelschritt war und der eigene Bauer
 * neben dem geschlagenen Bauern auf der fünften (Weiß) bzw. vierten (Schwarz) Reihe steht.
 */
public class EnPassantRules {

    public boolean isEnPassantMove(Board board, Move move, MoveHistory history, PieceColor color) {
        Piece moving = board.pieceAt(move.from());
        if (!(moving instanceof Pawn)) return false;
        if (!board.isEmpty(move.to())) return false;
        int dir = color.isWhite() ? 1 : -1;
        if (move.to().rank() - move.from().rank() != dir) return false;
        if (Math.abs(move.to().file() - move.from().file()) != 1) return false;
        MoveRecord last = history.last();
        if (last == null) return false;
        if (last.movedType() != PieceType.PAWN) return false;
        Move lastMove = last.move();
        if (Math.abs(lastMove.to().rank() - lastMove.from().rank()) != 2) return false;
        return lastMove.to().file() == move.to().file()
                && lastMove.to().rank() == move.from().rank();
    }

    /** Position des durch En passant geschlagenen Bauern (auf der Reihe des schlagenden Bauern). */
    public Position capturedSquare(Move move) {
        return Position.of(move.to().file(), move.from().rank());
    }
}
