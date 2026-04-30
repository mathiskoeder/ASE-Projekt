package de.dhbw.chess.presentation.command;

import de.dhbw.chess.domain.entity.Game;

/**
 * Erlaubt der ziehenden Seite aufzugeben — die Partie endet zugunsten der Gegenseite.
 */
public class ResignCmd implements Command {

    @Override
    public void execute(CliSession session, String[] args) {
        Game game = session.currentGame();
        if (game == null) {
            session.out().println("Keine Partie aktiv.");
            return;
        }
        try {
            game.resign(game.activeColor());
            session.persistenceService().save(game);
            session.out().println("Aufgabe akzeptiert. Status: " + game.status());
        } catch (RuntimeException e) {
            session.out().println("Aufgabe nicht möglich: " + e.getMessage());
        }
    }
}
