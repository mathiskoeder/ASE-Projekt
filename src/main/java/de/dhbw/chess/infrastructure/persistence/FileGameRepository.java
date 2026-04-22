package de.dhbw.chess.infrastructure.persistence;

import de.dhbw.chess.domain.entity.Board;
import de.dhbw.chess.domain.entity.Game;
import de.dhbw.chess.domain.entity.Player;
import de.dhbw.chess.domain.repository.GameRepository;
import de.dhbw.chess.domain.valueobject.GameStatus;
import de.dhbw.chess.domain.valueobject.PieceColor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Persistiert Partien als einfache Text-Dateien. Format: ein {@code [FEN]}-Header für die Stellung,
 * Metadaten zu Spielern und Status sowie eine PGN-light Sektion mit den gespielten Halbzügen.
 *
 * <p>Das Laden rekonstruiert das Brett aus dem FEN-String. Eine vollständige Wiedergabe der
 * Zughistorie ist mit dem aktuellen DDD-Modell nicht trivial möglich (Aggregat würde alle Züge
 * neu verifizieren), daher beschränkt sich das Laden bewusst auf den Stellungs-Snapshot — das
 * genügt für Aufgabe und Endbenutzer.</p>
 */
public class FileGameRepository implements GameRepository {

    private final Path directory;

    public FileGameRepository(Path directory) {
        this.directory = Objects.requireNonNull(directory, "directory");
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void save(Game game) {
        Objects.requireNonNull(game, "game");
        String fen = FenSerializer.serializePlacement(game.board());
        String moves = PgnLightSerializer.serialize(game.history().records());
        StringBuilder sb = new StringBuilder();
        sb.append("[FEN] ").append(fen).append('\n');
        sb.append("[GameId] ").append(game.id()).append('\n');
        sb.append("[White] ").append(game.white().id()).append('|').append(game.white().name()).append('\n');
        sb.append("[Black] ").append(game.black().id()).append('|').append(game.black().name()).append('\n');
        sb.append("[ActiveColor] ").append(game.activeColor().name()).append('\n');
        sb.append("[Status] ").append(game.status().name()).append('\n');
        sb.append('\n').append("[Moves]").append('\n').append(moves);
        try {
            Files.writeString(fileFor(game.id()), sb.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Optional<Game> findById(UUID id) {
        Path file = fileFor(id);
        if (!Files.exists(file)) return Optional.empty();
        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            String fen = readHeader(lines, "[FEN]");
            String[] white = readHeader(lines, "[White]").split("\\|", 2);
            String[] black = readHeader(lines, "[Black]").split("\\|", 2);
            String active = readHeader(lines, "[ActiveColor]");
            String status = readHeader(lines, "[Status]");
            Board board = FenSerializer.parsePlacement(fen);
            Player whitePlayer = new Player(UUID.fromString(white[0]), white[1], PieceColor.WHITE);
            Player blackPlayer = new Player(UUID.fromString(black[0]), black[1], PieceColor.BLACK);
            Game game = new Game(id, whitePlayer, blackPlayer, board);
            if (PieceColor.valueOf(active) == PieceColor.BLACK) {
                game.switchActiveColorForRestore();
            }
            game.setStatusForRestore(GameStatus.valueOf(status));
            return Optional.of(game);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void delete(UUID id) {
        try {
            Files.deleteIfExists(fileFor(id));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Path fileFor(UUID id) {
        return directory.resolve(id + ".chess");
    }

    private static String readHeader(List<String> lines, String key) {
        String prefix = key + " ";
        for (String line : lines) {
            if (line.startsWith(prefix)) {
                return line.substring(prefix.length()).trim();
            }
        }
        throw new IllegalStateException("Header fehlt: " + key);
    }
}
