package de.dhbw.chess.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PieceColorTest {

    @Test
    void oppositeOfWhiteIsBlack() {
        assertEquals(PieceColor.BLACK, PieceColor.WHITE.opposite());
    }

    @Test
    void oppositeOfBlackIsWhite() {
        assertEquals(PieceColor.WHITE, PieceColor.BLACK.opposite());
    }

    @Test
    void oppositeIsInvolution() {
        assertEquals(PieceColor.WHITE, PieceColor.WHITE.opposite().opposite());
        assertEquals(PieceColor.BLACK, PieceColor.BLACK.opposite().opposite());
    }

    @Test
    void isWhiteAndIsBlackBehaveAsExpected() {
        assertTrue(PieceColor.WHITE.isWhite());
        assertFalse(PieceColor.WHITE.isBlack());
        assertTrue(PieceColor.BLACK.isBlack());
        assertFalse(PieceColor.BLACK.isWhite());
    }
}
