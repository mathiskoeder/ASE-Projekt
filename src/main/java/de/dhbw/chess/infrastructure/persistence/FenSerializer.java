package de.dhbw.chess.infrastructure.persistence;

import de.dhbw.chess.domain.entity.Bishop;
import de.dhbw.chess.domain.entity.Board;
import de.dhbw.chess.domain.entity.King;
import de.dhbw.chess.domain.entity.Knight;
import de.dhbw.chess.domain.entity.Pawn;
import de.dhbw.chess.domain.entity.Piece;
import de.dhbw.chess.domain.entity.Queen;
import de.dhbw.chess.domain.entity.Rook;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;

import java.util.Objects;

/**
 * Serialisiert / deserialisiert nur das Brett-Layout im FEN-Stil. Für unsere Speicherdatei genügt
 * dieser Teil; Rochaderechte, En-passant-Feld und Halbzug-Zähler werden separat (oder gar nicht)
 * abgelegt — siehe Speicherformat-Beschreibung.
 */
public final class FenSerializer {

    private FenSerializer() {}

    public static String serializePlacement(Board board) {
        Objects.requireNonNull(board, "board");
        StringBuilder sb = new StringBuilder(64);
        for (int r = Board.SIZE - 1; r >= 0; r--) {
            int empties = 0;
            for (int f = 0; f < Board.SIZE; f++) {
                Piece piece = board.pieceAt(Position.of(f, r));
                if (piece == null) {
                    empties++;
                } else {
                    if (empties > 0) {
                        sb.append(empties);
                        empties = 0;
                    }
                    sb.append(piece.type().fenSymbol(piece.color()));
                }
            }
            if (empties > 0) sb.append(empties);
            if (r > 0) sb.append('/');
        }
        return sb.toString();
    }

    public static Board parsePlacement(String fenPlacement) {
        Objects.requireNonNull(fenPlacement, "fenPlacement");
        String[] ranks = fenPlacement.split("/");
        if (ranks.length != Board.SIZE) {
            throw new IllegalArgumentException("FEN benötigt " + Board.SIZE + " Reihen, hat: " + ranks.length);
        }
        Board board = new Board();
        for (int i = 0; i < ranks.length; i++) {
            int rank = Board.SIZE - 1 - i;
            String row = ranks[i];
            int file = 0;
            for (int idx = 0; idx < row.length(); idx++) {
                char c = row.charAt(idx);
                if (Character.isDigit(c)) {
                    file += c - '0';
                } else {
                    Piece piece = pieceFromSymbol(c);
                    board.place(piece, Position.of(file, rank));
                    file++;
                }
            }
            if (file != Board.SIZE) {
                throw new IllegalArgumentException("FEN-Reihe hat falsche Breite: " + row);
            }
        }
        return board;
    }

    private static Piece pieceFromSymbol(char c) {
        PieceColor color = Character.isUpperCase(c) ? PieceColor.WHITE : PieceColor.BLACK;
        return switch (Character.toLowerCase(c)) {
            case 'p' -> new Pawn(color);
            case 'r' -> new Rook(color);
            case 'n' -> new Knight(color);
            case 'b' -> new Bishop(color);
            case 'q' -> new Queen(color);
            case 'k' -> new King(color);
            default -> throw new IllegalArgumentException("Unbekanntes FEN-Symbol: " + c);
        };
    }
}
