package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.MoveRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Sequenzielle Sammlung aller bereits gespielten Züge einer Partie. Entity, da identitäts- und
 * lebenszyklusgebunden an das Aggregat {@code Game}.
 */
public class MoveHistory {

    private final List<MoveRecord> records = new ArrayList<>();

    public void append(MoveRecord record) {
        records.add(Objects.requireNonNull(record, "record"));
    }

    public List<MoveRecord> records() {
        return Collections.unmodifiableList(records);
    }

    public boolean isEmpty() {
        return records.isEmpty();
    }

    public int size() {
        return records.size();
    }

    public MoveRecord last() {
        return records.isEmpty() ? null : records.get(records.size() - 1);
    }

    /**
     * Anzahl der Halbzüge ohne Bauernzug oder Schlag — Grundlage für die 50-Züge-Regel.
     */
    public int halfMovesSinceProgress() {
        int count = 0;
        for (int i = records.size() - 1; i >= 0; i--) {
            MoveRecord r = records.get(i);
            if (r.movedType() == de.dhbw.chess.domain.valueobject.PieceType.PAWN || r.isCapture()) {
                break;
            }
            count++;
        }
        return count;
    }
}
