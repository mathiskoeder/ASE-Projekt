package de.dhbw.chess.presentation;

import de.dhbw.chess.application.service.GameService;
import de.dhbw.chess.application.service.MoveService;
import de.dhbw.chess.application.service.PersistenceService;
import de.dhbw.chess.domain.entity.Game;
import de.dhbw.chess.domain.entity.Player;
import de.dhbw.chess.domain.repository.GameRepository;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.infrastructure.persistence.FileGameRepository;
import de.dhbw.chess.presentation.command.CliSession;
import de.dhbw.chess.presentation.command.Command;
import de.dhbw.chess.presentation.command.HelpCmd;
import de.dhbw.chess.presentation.command.LoadCmd;
import de.dhbw.chess.presentation.command.MoveCmd;
import de.dhbw.chess.presentation.command.QuitCmd;
import de.dhbw.chess.presentation.command.ResignCmd;
import de.dhbw.chess.presentation.command.SaveCmd;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

/**
 * Einstiegspunkt der CLI. Verdrahtet Repository, Application Services und Command-Dispatcher und
 * betreibt eine einfache Read-Eval-Print-Schleife. Keine Spielregel-Logik; alles wird an die
 * darunterliegenden Schichten delegiert.
 */
public class ConsoleApp {

    public static void main(String[] args) {
        Path saveDir = Paths.get(System.getProperty("user.home"), ".chess-saves");
        GameRepository repo = new FileGameRepository(saveDir);
        GameService gameService = new GameService(repo);
        MoveService moveService = new MoveService(repo);
        PersistenceService persistenceService = new PersistenceService(repo);
        BoardRenderer renderer = new BoardRenderer();
        InputParser parser = new InputParser();
        CliSession session = new CliSession(gameService, moveService, persistenceService,
                renderer, parser, System.out);

        Map<String, Command> commands = new HashMap<>();
        commands.put("move", new MoveCmd());
        commands.put("save", new SaveCmd());
        commands.put("load", new LoadCmd());
        commands.put("resign", new ResignCmd());
        commands.put("help", new HelpCmd());
        commands.put("quit", new QuitCmd());

        session.out().println("DHBW Schach — 'help' für Befehle.");
        try (Scanner scanner = new Scanner(System.in)) {
            while (session.isRunning()) {
                if (session.currentGame() != null) {
                    session.out().println();
                    session.out().print(renderer.render(session.currentGame().board()));
                    session.out().println("Status: " + session.currentGame().status()
                            + ", am Zug: " + session.currentGame().activeColor());
                }
                session.out().print("> ");
                if (!scanner.hasNextLine()) break;
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                dispatch(session, commands, line);
            }
        }
    }

    private static void dispatch(CliSession session, Map<String, Command> commands, String line) {
        String[] parts = line.split("\\s+");
        String name = parts[0].toLowerCase();
        String[] cmdArgs = new String[parts.length - 1];
        System.arraycopy(parts, 1, cmdArgs, 0, cmdArgs.length);

        if (name.equals("new")) {
            startNew(session, cmdArgs);
            return;
        }
        if (name.equals("show")) {
            if (session.currentGame() != null) {
                session.out().print(session.renderer().render(session.currentGame().board()));
            } else {
                session.out().println("Keine Partie aktiv.");
            }
            return;
        }
        if (name.equals("o-o") || name.equals("0-0") || name.equals("o-o-o") || name.equals("0-0-0")
                || name.matches("[a-h][1-8]-[a-h][1-8](=[QRBN])?")) {
            commands.get("move").execute(session, new String[] { name });
            return;
        }
        Command command = commands.get(name);
        if (command == null) {
            session.out().println("Unbekannter Befehl: " + name + ". 'help' zeigt eine Übersicht.");
            return;
        }
        command.execute(session, cmdArgs);
    }

    private static void startNew(CliSession session, String[] args) {
        String whiteName = args.length > 0 ? args[0] : "Weiß";
        String blackName = args.length > 1 ? args[1] : "Schwarz";
        Player white = new Player(UUID.randomUUID(), whiteName, PieceColor.WHITE);
        Player black = new Player(UUID.randomUUID(), blackName, PieceColor.BLACK);
        Game game = session.gameService().startNewGame(white, black);
        session.setCurrentGame(game);
        session.out().println("Neue Partie: " + game.id());
    }
}
