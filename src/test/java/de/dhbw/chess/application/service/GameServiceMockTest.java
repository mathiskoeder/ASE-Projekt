package de.dhbw.chess.application.service;

import de.dhbw.chess.application.dto.MoveCommand;
import de.dhbw.chess.application.factory.GameFactory;
import de.dhbw.chess.domain.entity.Game;
import de.dhbw.chess.domain.entity.Player;
import de.dhbw.chess.domain.repository.GameRepository;
import de.dhbw.chess.domain.valueobject.MoveRecord;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameServiceMockTest {

    private GameRepository repository;
    private GameService gameService;
    private MoveService moveService;
    private Player white;
    private Player black;

    @BeforeEach
    void setUp() {
        repository = mock(GameRepository.class);
        gameService = new GameService(repository);
        moveService = new MoveService(repository);
        white = new Player(UUID.randomUUID(), "Max", PieceColor.WHITE);
        black = new Player(UUID.randomUUID(), "Mathis", PieceColor.BLACK);
    }

    @Test
    void startNewGamePersistsViaRepository() {
        Game game = gameService.startNewGame(white, black);
        verify(repository, times(1)).save(game);
        assertSame(white, game.white());
        assertSame(black, game.black());
    }

    @Test
    void loadGameReturnsRepositoryEntry() {
        Game game = GameFactory.createNewGame(white, black);
        when(repository.findById(game.id())).thenReturn(Optional.of(game));
        Game loaded = gameService.loadGame(game.id());
        assertSame(game, loaded);
    }

    @Test
    void loadGameThrowsWhenMissing() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> gameService.loadGame(id));
    }

    @Test
    void moveServiceExecutesAndPersists() {
        Game game = GameFactory.createNewGame(white, black);
        when(repository.findById(game.id())).thenReturn(Optional.of(game));
        MoveCommand cmd = new MoveCommand(game.id(),
                Position.fromAlgebraic("e2"), Position.fromAlgebraic("e4"));
        MoveRecord rec = moveService.execute(cmd).record();
        assertEquals(Position.fromAlgebraic("e4"), rec.move().to());
        verify(repository).save(any(Game.class));
    }
}
