package de.dhbw.chess.infrastructure.persistence;

import de.dhbw.chess.domain.entity.Game;
import de.dhbw.chess.domain.repository.GameRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * In-Memory-Implementierung des {@link GameRepository}. Eignet sich für Tests und für den
 * laufenden CLI-Prozess, in dem genau eine Partie aktiv ist. Nicht thread-sicher.
 */
public class InMemoryGameRepository implements GameRepository {

    private final Map<UUID, Game> games = new HashMap<>();

    @Override
    public void save(Game game) {
        Objects.requireNonNull(game, "game");
        games.put(game.id(), game);
    }

    @Override
    public Optional<Game> findById(UUID id) {
        return Optional.ofNullable(games.get(id));
    }

    @Override
    public void delete(UUID id) {
        games.remove(id);
    }
}
