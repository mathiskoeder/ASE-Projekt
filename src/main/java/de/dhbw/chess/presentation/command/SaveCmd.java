package de.dhbw.chess.presentation.command;

import de.dhbw.chess.domain.entity.Game;

/**
 * Speichert die aktuelle Partie über den {@code PersistenceService}. Das Repository entscheidet,
 * wohin geschrieben wird (Datei, In-Memory etc.).
 */
public class SaveCmd implements Command {

    @Override
    public void execute(CliSession session, String[] args) {
        Game game = session.currentGame();
        if (game == null) {
            session.out().println("Keine Partie aktiv.");
            return;
        }
        session.persistenceService().save(game);
        session.out().println("Gespeichert: " + game.id());
    }
}
