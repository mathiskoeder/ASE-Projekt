package de.dhbw.chess.presentation.command;

/**
 * Beendet die Hauptschleife des CLIs.
 */
public class QuitCmd implements Command {

    @Override
    public void execute(CliSession session, String[] args) {
        session.out().println("Auf Wiedersehen!");
        session.stop();
    }
}
