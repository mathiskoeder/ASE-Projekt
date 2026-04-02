package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.GameStatus;
import de.dhbw.chess.domain.valueobject.Move;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EndgameTest {

    private static Position p(String alg) {
        return Position.fromAlgebraic(alg);
    }

    private Game gameWith(Board board) {
        Player white = new Player(UUID.randomUUID(), "Max", PieceColor.WHITE);
        Player black = new Player(UUID.randomUUID(), "Mathis", PieceColor.BLACK);
        return new Game(UUID.randomUUID(), white, black, board);
    }

    @Test
    void backRankMateEndsGameWithWhiteWin() {
        // Back-Rank-Matt: Schwarzer König h8 mit eigenen Bauern f7/g7/h7 als Mauer,
        // weißer Turm zieht auf die 8. Reihe.
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new King(PieceColor.BLACK), p("h8"));
        board.place(new Pawn(PieceColor.BLACK), p("f7"));
        board.place(new Pawn(PieceColor.BLACK), p("g7"));
        board.place(new Pawn(PieceColor.BLACK), p("h7"));
        board.place(new Rook(PieceColor.WHITE), p("a1"));
        Game game = gameWith(board);
        game.makeMove(new Move(p("a1"), p("a8")));
        assertEquals(GameStatus.WHITE_WINS, game.status());
    }

    @Test
    void stalemateEndsGameAsDraw() {
        // Klassische Patt-Stellung: schwarzer König h8, weiße Dame g6, weißer König f7. Schwarz am Zug.
        Board board = new Board();
        board.place(new King(PieceColor.BLACK), p("h8"));
        board.place(new King(PieceColor.WHITE), p("f7"));
        board.place(new Queen(PieceColor.WHITE), p("g5"));
        Game game = gameWith(board);
        // Tempo-Zug Weiß, danach steht Schwarz patt.
        game.makeMove(new Move(p("g5"), p("g6")));
        assertEquals(GameStatus.DRAW_STALEMATE, game.status());
    }

    @Test
    void resignSetsLosingStatus() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new King(PieceColor.BLACK), p("e8"));
        Game game = gameWith(board);
        game.resign(PieceColor.WHITE);
        assertEquals(GameStatus.BLACK_WINS, game.status());
    }

    @Test
    void threefoldRepetitionEndsAsDraw() {
        Board board = new Board();
        board.place(new King(PieceColor.WHITE), p("e1"));
        board.place(new King(PieceColor.BLACK), p("e8"));
        board.place(new Rook(PieceColor.WHITE), p("a1"));
        board.place(new Rook(PieceColor.BLACK), p("h8"));
        Game game = gameWith(board);
        // Türme oszillieren — Stellung wiederholt sich, bis das Spiel als Remis endet.
        Move[] cycle = new Move[] {
                new Move(p("a1"), p("a2")),
                new Move(p("h8"), p("h7")),
                new Move(p("a2"), p("a1")),
                new Move(p("h7"), p("h8"))
        };
        int i = 0;
        while (game.status() == GameStatus.IN_PROGRESS) {
            game.makeMove(cycle[i % cycle.length]);
            i++;
        }
        assertEquals(GameStatus.DRAW_THREEFOLD_REPETITION, game.status());
    }
}
