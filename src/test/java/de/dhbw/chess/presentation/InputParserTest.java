package de.dhbw.chess.presentation;

import de.dhbw.chess.domain.valueobject.Move;
import de.dhbw.chess.domain.valueobject.PieceType;
import de.dhbw.chess.domain.valueobject.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputParserTest {

    private final InputParser parser = new InputParser();

    @Test
    void parsesSimpleMove() {
        Move move = parser.parseMove("e2-e4");
        assertEquals(Position.fromAlgebraic("e2"), move.from());
        assertEquals(Position.fromAlgebraic("e4"), move.to());
    }

    @Test
    void parsesPromotion() {
        Move move = parser.parseMove("e7-e8=Q");
        assertEquals(PieceType.QUEEN, move.promotion());
        assertTrue(move.isPromotion());
    }

    @Test
    void rejectsMalformedInput() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseMove("e2e4"));
        assertThrows(IllegalArgumentException.class, () -> parser.parseMove(""));
        assertThrows(NullPointerException.class, () -> Position.fromAlgebraic(null));
    }

    @Test
    void rejectsNullInput() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseMove(null));
    }

    @Test
    void shortcutKingsideCastlingThrowsShortcutException() {
        InputParser.ShortcutMoveException ex = assertThrows(
                InputParser.ShortcutMoveException.class,
                () -> parser.parseMove("O-O"));
        assertTrue(ex.isKingside());
    }

    @Test
    void shortcutQueensideCastlingThrowsShortcutException() {
        InputParser.ShortcutMoveException ex = assertThrows(
                InputParser.ShortcutMoveException.class,
                () -> parser.parseMove("O-O-O"));
        assertTrue(!ex.isKingside());
    }
}
