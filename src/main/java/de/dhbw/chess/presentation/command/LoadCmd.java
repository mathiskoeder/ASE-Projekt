package de.dhbw.chess.presentation.command;

import de.dhbw.chess.domain.entity.Game;

import java.util.UUID;

/**
 * Lädt eine Partie anhand ihrer ID aus dem Repository.
 */
public class LoadCmd implements Command {

    @Override
    public void execute(CliSession session, String[] args) {
        if (args.length < 1) {
            session.out().println("Bitte eine Partie-ID angeben.");
            return;
        }
        try {
            UUID id = UUID.fromString(args[0]);
            Game loaded = session.persistenceService().load(id);
            session.setCurrentGame(loaded);
            session.out().println("Geladen: " + id);
        } catch (IllegalArgumentException e) {
            session.out().println("Ungültige ID: " + args[0]);
        } catch (RuntimeException e) {
            session.out().println("Laden fehlgeschlagen: " + e.getMessage());
        }
    }
}
