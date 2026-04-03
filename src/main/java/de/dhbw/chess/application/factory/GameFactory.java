package de.dhbw.chess.application.factory;

import de.dhbw.chess.domain.entity.Bishop;
import de.dhbw.chess.domain.entity.Board;
import de.dhbw.chess.domain.entity.Game;
import de.dhbw.chess.domain.entity.King;
import de.dhbw.chess.domain.entity.Knight;
import de.dhbw.chess.domain.entity.Pawn;
import de.dhbw.chess.domain.entity.Player;
import de.dhbw.chess.domain.entity.Queen;
import de.dhbw.chess.domain.entity.Rook;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;

import java.util.Objects;
import java.util.UUID;

/**
 * Factory (GoF) für die Standard-Schachaufstellung. Kapselt das wiederholte Bestücken des Bretts
 * mit den 32 Startfiguren und entlastet damit Game/Application-Service.
 */
public final class GameFactory {

    private GameFactory() {}

    public static Game createNewGame(Player white, Player black) {
        Objects.requireNonNull(white, "white");
        Objects.requireNonNull(black, "black");
        Board board = standardBoard();
        return new Game(UUID.randomUUID(), white, black, board);
    }

    public static Board standardBoard() {
        Board board = new Board();
        placeBackRank(board, PieceColor.WHITE, 0);
        placePawnRow(board, PieceColor.WHITE, 1);
        placePawnRow(board, PieceColor.BLACK, 6);
        placeBackRank(board, PieceColor.BLACK, 7);
        return board;
    }

    private static void placeBackRank(Board board, PieceColor color, int rank) {
        board.place(new Rook(color), Position.of(0, rank));
        board.place(new Knight(color), Position.of(1, rank));
        board.place(new Bishop(color), Position.of(2, rank));
        board.place(new Queen(color), Position.of(3, rank));
        board.place(new King(color), Position.of(4, rank));
        board.place(new Bishop(color), Position.of(5, rank));
        board.place(new Knight(color), Position.of(6, rank));
        board.place(new Rook(color), Position.of(7, rank));
    }

    private static void placePawnRow(Board board, PieceColor color, int rank) {
        for (int f = 0; f < Board.SIZE; f++) {
            board.place(new Pawn(color), Position.of(f, rank));
        }
    }
}
