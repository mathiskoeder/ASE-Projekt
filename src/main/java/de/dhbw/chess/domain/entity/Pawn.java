package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.PieceType;
import de.dhbw.chess.domain.valueobject.Position;

import java.util.ArrayList;
import java.util.List;

public final class Pawn extends Piece {

    public Pawn(PieceColor color) {
        super(PieceType.PAWN, color);
    }

    @Override
    public List<Position> possibleMoves(Board board, Position from) {
        List<Position> moves = new ArrayList<>();
        int dir = color().isWhite() ? 1 : -1;
        int startRank = color().isWhite() ? 1 : 6;
        int oneStepRank = from.rank() + dir;
        if (oneStepRank < 0 || oneStepRank > 7) {
            return moves;
        }
        Position oneStep = Position.of(from.file(), oneStepRank);
        if (board.isEmpty(oneStep)) {
            moves.add(oneStep);
            int twoStepRank = from.rank() + 2 * dir;
            if (from.rank() == startRank && twoStepRank >= 0 && twoStepRank <= 7) {
                Position twoStep = Position.of(from.file(), twoStepRank);
                if (board.isEmpty(twoStep)) {
                    moves.add(twoStep);
                }
            }
        }
        for (int df : new int[] {-1, 1}) {
            int targetFile = from.file() + df;
            if (targetFile < 0 || targetFile > 7) continue;
            Position target = Position.of(targetFile, oneStepRank);
            if (board.isEnemy(target, color())) {
                moves.add(target);
            }
        }
        return moves;
    }
}
