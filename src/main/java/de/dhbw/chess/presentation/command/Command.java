package de.dhbw.chess.presentation.command;

/**
 * Command-Pattern (GoF): jeder CLI-Befehl wird als eigenes Objekt gekapselt. Macht die
 * Hauptschleife frei von langen if/else-Ketten und erlaubt Erweiterungen ohne Änderungen am
 * bestehenden Dispatcher (Open-Closed-Prinzip).
 */
public interface Command {
    void execute(CliSession session, String[] args);
}
