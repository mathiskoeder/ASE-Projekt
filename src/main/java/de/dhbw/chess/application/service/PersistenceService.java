package de.dhbw.chess.application.service;

import de.dhbw.chess.domain.entity.Game;
import de.dhbw.chess.domain.repository.GameRepository;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

/**
 * Application Service: kapselt Speichern und Laden einer Partie. Versteckt das verwendete
 * {@link GameRepository} hinter einer schmalen Schnittstelle, damit die Presentation-Schicht
 * weder Dateipfade noch Repository-Implementierungen kennen muss.
 */
public class PersistenceService {

    private final GameRepository repository;

    public PersistenceService(GameRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    public void save(Game game) {
        repository.save(Objects.requireNonNull(game, "game"));
    }

    public Game load(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Partie nicht gefunden: " + id));
    }
}
