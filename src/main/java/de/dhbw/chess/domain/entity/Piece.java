package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.PieceType;
import de.dhbw.chess.domain.valueobject.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Erste, einfache Modellierung einer Schachfigur.
 *
 * <p><b>Code Smell — bewusst vorhanden:</b> die Zuglogik wird hier zentral über einen
 * {@code switch} auf {@link PieceType} entschieden. Das ist ein klassischer Verstoß gegen das
 * Open-Closed-Prinzip: jede neue Figur erfordert eine Änderung an dieser Klasse. In einer späteren
 * Phase wird das per Refactoring (Replace Conditional with Polymorphism) auf eine
 * Strategy-Hierarchie umgestellt.</p>
 */
public class Piece {

    private final PieceType type;
    private final PieceColor color;
    private boolean hasMoved;

    public Piece(PieceType type, PieceColor color) {
        this.type = Objects.requireNonNull(type, "type");
        this.color = Objects.requireNonNull(color, "color");
    }

    public PieceType type() {
        return type;
    }

    public PieceColor color() {
        return color;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void markMoved() {
        this.hasMoved = true;
    }

    public List<Position> possibleMoves(Board board, Position from) {
        List<Position> moves = new ArrayList<>();
        switch (type) {
            case PAWN:
                addPawnMoves(board, from, moves);
                break;
            case ROOK:
                addSlidingMoves(board, from, moves, new int[][] {{1,0},{-1,0},{0,1},{0,-1}});
                break;
            case BISHOP:
                addSlidingMoves(board, from, moves, new int[][] {{1,1},{1,-1},{-1,1},{-1,-1}});
                break;
            case QUEEN:
                addSlidingMoves(board, from, moves, new int[][] {
                        {1,0},{-1,0},{0,1},{0,-1},
                        {1,1},{1,-1},{-1,1},{-1,-1}
                });
                break;
            case KNIGHT:
            case KING:
                // wird in nächstem Commit befüllt
                break;
            default:
                throw new IllegalStateException("Unbekannter Figurentyp: " + type);
        }
        return moves;
    }

    private void addPawnMoves(Board board, Position from, List<Position> moves) {
        int dir = color.isWhite() ? 1 : -1;
        int startRank = color.isWhite() ? 1 : 6;
        int oneStepRank = from.rank() + dir;
        if (oneStepRank < 0 || oneStepRank > 7) {
            return;
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
            if (board.isEnemy(target, color)) {
                moves.add(target);
            }
        }
    }

    private void addSlidingMoves(Board board, Position from, List<Position> moves, int[][] dirs) {
        for (int[] d : dirs) {
            int f = from.file() + d[0];
            int r = from.rank() + d[1];
            while (f >= 0 && f <= 7 && r >= 0 && r <= 7) {
                Position p = Position.of(f, r);
                if (board.isEmpty(p)) {
                    moves.add(p);
                } else {
                    if (board.isEnemy(p, color)) {
                        moves.add(p);
                    }
                    break;
                }
                f += d[0];
                r += d[1];
            }
        }
    }
}
