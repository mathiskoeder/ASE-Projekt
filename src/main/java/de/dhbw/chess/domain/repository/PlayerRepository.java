package de.dhbw.chess.domain.repository;

import de.dhbw.chess.domain.entity.Player;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository für {@link Player}. In dieser einfachen Implementierung dient es vor allem dazu,
 * Spielerprofile zwischen Sitzungen wiederzufinden.
 */
public interface PlayerRepository {

    void save(Player player);

    Optional<Player> findById(UUID id);

    Optional<Player> findByName(String name);
}
