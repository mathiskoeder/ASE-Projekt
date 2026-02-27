package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.GameStatus;
import de.dhbw.chess.domain.valueobject.Move;
import de.dhbw.chess.domain.valueobject.MoveRecord;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.PieceType;
import de.dhbw.chess.domain.valueobject.Position;

import java.util.List;
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

    /**
     * Führt einen Zug aus, sofern er regelkonform ist.
     *
     * <p><b>Code Smell — bewusst vorhanden:</b> die komplette Validierungslogik (Eigentümerschaft,
     * pseudo-legale Zielfelder, Eigenschach-Prüfung) hängt direkt in dieser Aggregat-Methode.
     * Dadurch hat das Aggregat zu viele Verantwortungen. In Phase 6 werden {@code MoveValidator}
     * und {@code CheckDetector} als eigene Domain-Services extrahiert.</p>
     */
    public MoveRecord makeMove(Move move) {
        Objects.requireNonNull(move, "move");
        if (status.isFinal()) {
            throw new IllegalStateException("Partie ist beendet");
        }
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

        PieceType captured = board.pieceAt(move.to()) != null
                ? board.pieceAt(move.to()).type() : null;
        board.move(move.from(), move.to());

        MoveRecord record = MoveRecord.builder(move, moving.color(), moving.type())
                .captured(captured)
                .build();
        history.append(record);
        switchActiveColor();
        return record;
    }
}
