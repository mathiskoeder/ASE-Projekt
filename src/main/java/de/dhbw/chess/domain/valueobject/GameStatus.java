package de.dhbw.chess.domain.valueobject;

/**
 * Aktueller Status einer Schachpartie.
 */
public enum GameStatus {

    IN_PROGRESS,
    WHITE_WINS,
    BLACK_WINS,
    DRAW_STALEMATE,
    DRAW_FIFTY_MOVE_RULE,
    DRAW_THREEFOLD_REPETITION,
    DRAW_INSUFFICIENT_MATERIAL,
    RESIGNED;

    public boolean isFinal() {
        return this != IN_PROGRESS;
    }

    public boolean isDraw() {
        return this == DRAW_STALEMATE
                || this == DRAW_FIFTY_MOVE_RULE
                || this == DRAW_THREEFOLD_REPETITION
                || this == DRAW_INSUFFICIENT_MATERIAL;
    }
}
