package de.dhbw.chess.application.service;

import de.dhbw.chess.application.factory.GameFactory;
import de.dhbw.chess.domain.entity.Game;
import de.dhbw.chess.domain.entity.Player;
import de.dhbw.chess.domain.repository.GameRepository;
import de.dhbw.chess.domain.valueobject.PieceColor;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PersistenceServiceTest {

    @Test
    void saveDelegatesToRepository() {
        GameRepository repo = mock(GameRepository.class);
        PersistenceService service = new PersistenceService(repo);
        Player white = new Player(UUID.randomUUID(), "Max", PieceColor.WHITE);
        Player black = new Player(UUID.randomUUID(), "Mathis", PieceColor.BLACK);
        Game game = GameFactory.createNewGame(white, black);
        service.save(game);
        verify(repo).save(game);
    }

    @Test
    void loadReturnsRepositoryEntry() {
        GameRepository repo = mock(GameRepository.class);
        PersistenceService service = new PersistenceService(repo);
        Player white = new Player(UUID.randomUUID(), "Max", PieceColor.WHITE);
        Player black = new Player(UUID.randomUUID(), "Mathis", PieceColor.BLACK);
        Game game = GameFactory.createNewGame(white, black);
        when(repo.findById(game.id())).thenReturn(Optional.of(game));
        assertSame(game, service.load(game.id()));
    }

    @Test
    void loadThrowsWhenMissing() {
        GameRepository repo = mock(GameRepository.class);
        PersistenceService service = new PersistenceService(repo);
        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.load(id));
    }
}
