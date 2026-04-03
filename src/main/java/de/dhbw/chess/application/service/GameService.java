package de.dhbw.chess.application.service;

import de.dhbw.chess.application.factory.GameFactory;
import de.dhbw.chess.domain.entity.Game;
import de.dhbw.chess.domain.entity.Player;
import de.dhbw.chess.domain.repository.GameRepository;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

/**
 * Application Service: orchestriert das Anlegen und Verwalten von Partien. Hält keine Spielregeln,
 * delegiert an {@link Game} und nutzt {@link GameRepository} zur Persistenz. Erlaubt eine klare
 * Trennung zwischen Use-Case-Steuerung (hier) und Domänenlogik (Aggregat {@link Game}).
 */
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = Objects.requireNonNull(gameRepository, "gameRepository");
    }

    public Game startNewGame(Player white, Player black) {
        Game game = GameFactory.createNewGame(white, black);
        gameRepository.save(game);
        return game;
    }

    public Game loadGame(UUID id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Partie nicht gefunden: " + id));
    }

    public void persist(Game game) {
        gameRepository.save(Objects.requireNonNull(game, "game"));
    }
}
