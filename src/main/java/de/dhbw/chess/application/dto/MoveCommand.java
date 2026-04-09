package de.dhbw.chess.application.dto;

import de.dhbw.chess.domain.valueobject.Move;
import de.dhbw.chess.domain.valueobject.PieceType;
import de.dhbw.chess.domain.valueobject.Position;

import java.util.Objects;
import java.util.UUID;

/**
 * Eingabe-DTO für die Anwendung — beschreibt einen vom Spieler intendierten Zug. Wird im
 * {@code MoveService} in einen Domain-{@link Move} übersetzt.
 */
public final class MoveCommand {

    private final UUID gameId;
    private final Position from;
    private final Position to;
    private final PieceType promotion;

    public MoveCommand(UUID gameId, Position from, Position to, PieceType promotion) {
        this.gameId = Objects.requireNonNull(gameId, "gameId");
        this.from = Objects.requireNonNull(from, "from");
        this.to = Objects.requireNonNull(to, "to");
        this.promotion = promotion;
    }

    public MoveCommand(UUID gameId, Position from, Position to) {
        this(gameId, from, to, null);
    }

    public UUID gameId() { return gameId; }
    public Position from() { return from; }
    public Position to() { return to; }
    public PieceType promotion() { return promotion; }

    public Move toMove() {
        return promotion == null ? new Move(from, to) : new Move(from, to, promotion);
    }
}
