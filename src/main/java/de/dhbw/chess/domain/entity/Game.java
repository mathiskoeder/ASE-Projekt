package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.GameStatus;
import de.dhbw.chess.domain.valueobject.PieceColor;

import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate Root einer Schachpartie. Hält {@link Board}, {@link MoveHistory}, beide Spieler,
 * den Status und die Farbe am Zug. Konsistenzgrenze: alle regelrelevanten Änderungen am Brett
 * laufen über diese Klasse.
 *
 * <p>Phase 5 des Plans: das Aggregat wird mit minimalem Verhalten angelegt und in den folgenden
 * Commits bewusst mit Inline-Validierung gefüttert (Negativstand). In Phase 6 wird die Validierung
 * dann in eigene Domain-Services extrahiert.</p>
 */
public class Game {

    private final UUID id;
    private final Player white;
    private final Player black;
    private final Board board;
    private final MoveHistory history = new MoveHistory();
    private PieceColor activeColor = PieceColor.WHITE;
    private GameStatus status = GameStatus.IN_PROGRESS;

    public Game(UUID id, Player white, Player black, Board board) {
        this.id = Objects.requireNonNull(id, "id");
        this.white = Objects.requireNonNull(white, "white");
        this.black = Objects.requireNonNull(black, "black");
        this.board = Objects.requireNonNull(board, "board");
        if (white.color() != PieceColor.WHITE || black.color() != PieceColor.BLACK) {
            throw new IllegalArgumentException("Spieler-Farben widersprechen den Argumenten");
        }
    }

    public UUID id() { return id; }
    public Player white() { return white; }
    public Player black() { return black; }
    public Board board() { return board; }
    public MoveHistory history() { return history; }
    public PieceColor activeColor() { return activeColor; }
    public GameStatus status() { return status; }

    protected void switchActiveColor() {
        this.activeColor = activeColor.opposite();
    }

    protected void setStatus(GameStatus status) {
        this.status = Objects.requireNonNull(status, "status");
    }
}
