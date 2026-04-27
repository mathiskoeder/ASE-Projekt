package de.dhbw.chess.presentation;

import de.dhbw.chess.domain.entity.Board;
import de.dhbw.chess.domain.entity.Piece;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.PieceType;
import de.dhbw.chess.domain.valueobject.Position;

/**
 * Rendert ein {@link Board} als Unicode-ASCII-Brett für die Konsole. Schwarze Felder werden mit
 * Hintergrund-Marker, weiße schlicht dargestellt — funktioniert ohne Terminal-Farbcodes.
 */
public class BoardRenderer {

    public String render(Board board) {
        StringBuilder sb = new StringBuilder();
        sb.append("    a   b   c   d   e   f   g   h\n");
        sb.append("  +---+---+---+---+---+---+---+---+\n");
        for (int r = Board.SIZE - 1; r >= 0; r--) {
            sb.append(r + 1).append(' ');
            for (int f = 0; f < Board.SIZE; f++) {
                Piece p = board.pieceAt(Position.of(f, r));
                sb.append("| ").append(symbol(p)).append(' ');
            }
            sb.append("| ").append(r + 1).append('\n');
            sb.append("  +---+---+---+---+---+---+---+---+\n");
        }
        sb.append("    a   b   c   d   e   f   g   h\n");
        return sb.toString();
    }

    private static char symbol(Piece p) {
        if (p == null) return ' ';
        return unicode(p.type(), p.color());
    }

    private static char unicode(PieceType type, PieceColor color) {
        return switch (type) {
            case KING -> color == PieceColor.WHITE ? '\u2654' : '\u265A';
            case QUEEN -> color == PieceColor.WHITE ? '\u2655' : '\u265B';
            case ROOK -> color == PieceColor.WHITE ? '\u2656' : '\u265C';
            case BISHOP -> color == PieceColor.WHITE ? '\u2657' : '\u265D';
            case KNIGHT -> color == PieceColor.WHITE ? '\u2658' : '\u265E';
            case PAWN -> color == PieceColor.WHITE ? '\u2659' : '\u265F';
        };
    }
}
