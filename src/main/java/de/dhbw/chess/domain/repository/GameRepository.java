package de.dhbw.chess.domain.repository;

import de.dhbw.chess.domain.entity.Game;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository für Schachpartien. Domain-seitige Schnittstelle, Implementierungen liegen in der
 * Infrastructure-Schicht (z. B. {@code FileGameRepository}, {@code InMemoryGameRepository}).
 */
public interface GameRepository {

    void save(Game game);

    Optional<Game> findById(UUID id);

    void delete(UUID id);
}
