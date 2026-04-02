package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.service.CheckDetector;
import de.dhbw.chess.domain.service.GameStateEvaluator;
import de.dhbw.chess.domain.service.MoveValidator;
import de.dhbw.chess.domain.valueobject.GameStatus;
import de.dhbw.chess.domain.valueobject.Move;
import de.dhbw.chess.domain.valueobject.MoveRecord;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.PieceType;

import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate Root einer Schachpartie. Hält {@link Board}, {@link MoveHistory}, beide Spieler,
 * den Status und die Farbe am Zug. Konsistenzgrenze: alle regelrelevanten Änderungen am Brett
 * laufen über diese Klasse.
 *
 * <p>Refactoring 2 (Extract Class): Validierung und Schach-Erkennung wurden in {@link MoveValidator}
 * bzw. {@link CheckDetector} ausgelagert. Damit entfällt die zuvor doppelt vorhandene Schach-Logik
 * (DRY). Das Aggregat orchestriert nur noch.</p>
 */
public class Game {

    private final UUID id;
    private final Player white;
    private final Player black;
    private final Board board;
    private final MoveHistory history = new MoveHistory();
    private final MoveValidator validator;
    private final CheckDetector checkDetector;
    private final GameStateEvaluator stateEvaluator;
    private PieceColor activeColor = PieceColor.WHITE;
    private GameStatus status = GameStatus.IN_PROGRESS;

    public Game(UUID id, Player white, Player black, Board board) {
        this(id, white, black, board, new CheckDetector(), null);
    }

    public Game(UUID id, Player white, Player black, Board board,
                CheckDetector checkDetector, MoveValidator validator) {
        this.id = Objects.requireNonNull(id, "id");
        this.white = Objects.requireNonNull(white, "white");
        this.black = Objects.requireNonNull(black, "black");
        this.board = Objects.requireNonNull(board, "board");
        this.checkDetector = Objects.requireNonNull(checkDetector, "checkDetector");
        this.validator = validator != null ? validator : new MoveValidator(this.checkDetector);
        this.stateEvaluator = new GameStateEvaluator(this.checkDetector);
        if (white.color() != PieceColor.WHITE || black.color() != PieceColor.BLACK) {
            throw new IllegalArgumentException("Spieler-Farben widersprechen den Argumenten");
        }
    }

    public UUID id() { return id; }
    public Player white() { return white; }
    public Player black() { return black; }
    public Board board() { return board; }
    public MoveHistory history() { return history; }
    public PieceColor activeColor() { return activeColor; }
    public GameStatus status() { return status; }

    protected void switchActiveColor() {
        this.activeColor = activeColor.opposite();
    }

    protected void setStatus(GameStatus status) {
        this.status = Objects.requireNonNull(status, "status");
    }

    public MoveRecord makeMove(Move move) {
        Objects.requireNonNull(move, "move");
        if (status.isFinal()) {
            throw new IllegalStateException("Partie ist beendet");
        }
        validator.validate(board, move, activeColor, history);

        Piece moving = board.pieceAt(move.from());
        boolean castling = moving instanceof King
                && validator.castlingRules().isCastlingMove(board, move);
        boolean enPassant = moving instanceof Pawn
                && validator.enPassantRules().isEnPassantMove(board, move, history, activeColor);

        Piece captured = board.pieceAt(move.to());
        PieceType capturedType = captured != null ? captured.type() : null;
        if (enPassant) {
            board.remove(validator.enPassantRules().capturedSquare(move));
            capturedType = PieceType.PAWN;
        }
        board.move(move.from(), move.to());
        if (castling) {
            board.move(validator.castlingRules().rookFrom(move),
                    validator.castlingRules().rookTo(move));
        }
        if (move.isPromotion()) {
            board.remove(move.to());
            board.place(promote(move.promotion(), moving.color()), move.to());
        }

        boolean givesCheck = checkDetector.isInCheck(board, activeColor.opposite());
        MoveRecord.Builder b = MoveRecord.builder(move, moving.color(), moving.type())
                .captured(capturedType)
                .check(givesCheck);
        if (castling) {
            if (validator.castlingRules().isKingside(move)) {
                b.kingsideCastle(true);
            } else {
                b.queensideCastle(true);
            }
        }
        if (enPassant) {
            b.enPassant(true);
        }
        MoveRecord record = b.build();
        history.append(record);
        switchActiveColor();
        updateStatus();
        return record;
    }

    private void updateStatus() {
        if (stateEvaluator.isCheckmate(board, activeColor)) {
            status = activeColor.isWhite() ? GameStatus.BLACK_WINS : GameStatus.WHITE_WINS;
        } else if (stateEvaluator.isStalemate(board, activeColor)) {
            status = GameStatus.DRAW_STALEMATE;
        } else if (history.halfMovesSinceProgress() >= 100) {
            // 50 Vollzüge = 100 Halbzüge ohne Bauernzug oder Schlag.
            status = GameStatus.DRAW_FIFTY_MOVE_RULE;
        }
    }

    public void resign(PieceColor color) {
        if (status.isFinal()) {
            throw new IllegalStateException("Partie ist beendet");
        }
        status = color.isWhite() ? GameStatus.BLACK_WINS : GameStatus.WHITE_WINS;
    }

    private static Piece promote(PieceType target, PieceColor color) {
        return switch (target) {
            case QUEEN -> new Queen(color);
            case ROOK -> new Rook(color);
            case BISHOP -> new Bishop(color);
            case KNIGHT -> new Knight(color);
            default -> throw new IllegalArgumentException("Promotion zu " + target + " unzulässig");
        };
    }
}
