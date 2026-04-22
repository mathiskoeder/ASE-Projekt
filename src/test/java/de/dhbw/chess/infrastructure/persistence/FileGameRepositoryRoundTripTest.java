package de.dhbw.chess.infrastructure.persistence;

import de.dhbw.chess.application.factory.GameFactory;
import de.dhbw.chess.domain.entity.Game;
import de.dhbw.chess.domain.entity.Player;
import de.dhbw.chess.domain.valueobject.GameStatus;
import de.dhbw.chess.domain.valueobject.Move;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileGameRepositoryRoundTripTest {

    @Test
    void savedGameRoundTripsThroughFile(@TempDir Path tmp) {
        FileGameRepository repo = new FileGameRepository(tmp);
        Player white = new Player(UUID.randomUUID(), "Max", PieceColor.WHITE);
        Player black = new Player(UUID.randomUUID(), "Mathis", PieceColor.BLACK);
        Game game = GameFactory.createNewGame(white, black);
        game.makeMove(new Move(Position.fromAlgebraic("e2"), Position.fromAlgebraic("e4")));
        repo.save(game);

        Optional<Game> reloaded = repo.findById(game.id());
        assertTrue(reloaded.isPresent());
        Game restored = reloaded.orElseThrow();
        assertEquals(game.id(), restored.id());
        assertEquals(PieceColor.BLACK, restored.activeColor());
        assertEquals(GameStatus.IN_PROGRESS, restored.status());
        assertNotNull(restored.board().pieceAt(Position.fromAlgebraic("e4")));
    }
}
