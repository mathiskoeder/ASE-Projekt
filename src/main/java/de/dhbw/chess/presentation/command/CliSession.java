package de.dhbw.chess.presentation.command;

import de.dhbw.chess.application.service.GameService;
import de.dhbw.chess.application.service.MoveService;
import de.dhbw.chess.application.service.PersistenceService;
import de.dhbw.chess.domain.entity.Game;
import de.dhbw.chess.presentation.BoardRenderer;
import de.dhbw.chess.presentation.InputParser;

import java.io.PrintStream;
import java.util.Objects;

/**
 * Sitzungskontext für die CLI. Hält die laufende Partie sowie alle Application Services und
 * Helfer, damit Command-Objekte nicht jeweils einen vollen Konstruktor benötigen.
 */
public class CliSession {

    private final GameService gameService;
    private final MoveService moveService;
    private final PersistenceService persistenceService;
    private final BoardRenderer renderer;
    private final InputParser parser;
    private final PrintStream out;
    private Game currentGame;
    private boolean running = true;

    public CliSession(GameService gameService, MoveService moveService,
                      PersistenceService persistenceService, BoardRenderer renderer,
                      InputParser parser, PrintStream out) {
        this.gameService = Objects.requireNonNull(gameService, "gameService");
        this.moveService = Objects.requireNonNull(moveService, "moveService");
        this.persistenceService = Objects.requireNonNull(persistenceService, "persistenceService");
        this.renderer = Objects.requireNonNull(renderer, "renderer");
        this.parser = Objects.requireNonNull(parser, "parser");
        this.out = Objects.requireNonNull(out, "out");
    }

    public GameService gameService() { return gameService; }
    public MoveService moveService() { return moveService; }
    public PersistenceService persistenceService() { return persistenceService; }
    public BoardRenderer renderer() { return renderer; }
    public InputParser parser() { return parser; }
    public PrintStream out() { return out; }

    public Game currentGame() { return currentGame; }
    public void setCurrentGame(Game game) { this.currentGame = game; }

    public boolean isRunning() { return running; }
    public void stop() { this.running = false; }
}
