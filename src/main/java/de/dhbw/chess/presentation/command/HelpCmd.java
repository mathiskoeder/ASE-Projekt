package de.dhbw.chess.presentation.command;

/**
 * Listet die verfügbaren Befehle auf.
 */
public class HelpCmd implements Command {

    @Override
    public void execute(CliSession session, String[] args) {
        session.out().println("Verfügbare Befehle:");
        session.out().println("  new                    Neue Partie starten");
        session.out().println("  move <from-to[=Q]>     Zug ausführen, z. B. e2-e4 oder e7-e8=Q");
        session.out().println("  O-O / O-O-O            Kurze / lange Rochade");
        session.out().println("  save                   Aktuelle Partie speichern");
        session.out().println("  load <id>              Partie aus Repository laden");
        session.out().println("  resign                 Aufgeben");
        session.out().println("  show                   Brett ausgeben");
        session.out().println("  help                   Diese Hilfe");
        session.out().println("  quit                   Beenden");
    }
}
