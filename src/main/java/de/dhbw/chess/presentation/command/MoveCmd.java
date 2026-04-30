package de.dhbw.chess.presentation.command;

import de.dhbw.chess.application.dto.MoveCommand;
import de.dhbw.chess.application.service.MoveService;
import de.dhbw.chess.domain.entity.Game;
import de.dhbw.chess.domain.entity.King;
import de.dhbw.chess.domain.valueobject.Move;
import de.dhbw.chess.domain.valueobject.PieceColor;
import de.dhbw.chess.domain.valueobject.Position;
import de.dhbw.chess.presentation.InputParser;

/**
 * Verarbeitet einen Spielzug. Übersetzt Eingaben wie {@code e2-e4} oder {@code O-O} in einen
 * {@link MoveCommand} und delegiert an den {@code MoveService}.
 */
public class MoveCmd implements Command {

    @Override
    public void execute(CliSession session, String[] args) {
        Game game = session.currentGame();
        if (game == null) {
            session.out().println("Keine Partie aktiv. 'new' startet eine neue Partie.");
            return;
        }
        if (args.length < 1) {
            session.out().println("Bitte einen Zug angeben, z. B. e2-e4.");
            return;
        }
        try {
            Move move;
            try {
                move = session.parser().parseMove(args[0]);
            } catch (InputParser.ShortcutMoveException shortcut) {
                move = resolveCastle(game, shortcut.isKingside());
            }
            MoveCommand cmd = new MoveCommand(game.id(), move.from(), move.to(), move.promotion());
            MoveService.MoveResult result = session.moveService().execute(cmd);
            session.setCurrentGame(result.game());
            session.out().println(result.record().notation());
        } catch (RuntimeException e) {
            session.out().println("Zug abgelehnt: " + e.getMessage());
        }
    }

    private Move resolveCastle(Game game, boolean kingside) {
        PieceColor color = game.activeColor();
        Position kingPos = findKing(game, color);
        int rank = color.isWhite() ? 0 : 7;
        Position to = Position.of(kingside ? 6 : 2, rank);
        return new Move(kingPos, to);
    }

    private Position findKing(Game game, PieceColor color) {
        for (int f = 0; f < 8; f++) {
            for (int r = 0; r < 8; r++) {
                Position p = Position.of(f, r);
                if (game.board().pieceAt(p) instanceof King k && k.color() == color) {
                    return p;
                }
            }
        }
        throw new IllegalStateException("Kein König auf dem Brett");
    }
}
