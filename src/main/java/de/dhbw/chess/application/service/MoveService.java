package de.dhbw.chess.application.service;

import de.dhbw.chess.application.dto.MoveCommand;
import de.dhbw.chess.domain.entity.Game;
import de.dhbw.chess.domain.repository.GameRepository;
import de.dhbw.chess.domain.valueobject.MoveRecord;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Application Service: führt einen Zug auf einer geladenen Partie aus und persistiert diese
 * anschließend. Nimmt {@link MoveCommand}-Eingaben entgegen und gibt das aktualisierte
 * {@link Game} zusammen mit dem {@link MoveRecord} zurück.
 */
public class MoveService {

    private final GameRepository gameRepository;

    public MoveService(GameRepository gameRepository) {
        this.gameRepository = Objects.requireNonNull(gameRepository, "gameRepository");
    }

    public MoveResult execute(MoveCommand command) {
        Objects.requireNonNull(command, "command");
        Game game = gameRepository.findById(command.gameId())
                .orElseThrow(() -> new NoSuchElementException("Partie nicht gefunden: " + command.gameId()));
        MoveRecord record = game.makeMove(command.toMove());
        gameRepository.save(game);
        return new MoveResult(game, record);
    }

    public record MoveResult(Game game, MoveRecord record) {}
}
