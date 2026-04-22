package de.dhbw.chess.infrastructure.persistence;

import de.dhbw.chess.domain.valueobject.MoveRecord;

import java.util.List;
import java.util.Objects;

/**
 * Erzeugt eine vereinfachte PGN-Darstellung der Zugfolge ({@code 1. e2-e4 e7-e5}). Geeignet für
 * unsere Save-Files; kein vollwertiger PGN-Parser. Die Notation pro Halbzug entspricht
 * {@link MoveRecord#notation()}.
 */
public final class PgnLightSerializer {

    private PgnLightSerializer() {}

    public static String serialize(List<MoveRecord> records) {
        Objects.requireNonNull(records, "records");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < records.size(); i++) {
            if (i % 2 == 0) {
                if (i > 0) sb.append('\n');
                sb.append((i / 2) + 1).append(". ");
            } else {
                sb.append(' ');
            }
            sb.append(records.get(i).notation());
        }
        if (!records.isEmpty()) sb.append('\n');
        return sb.toString();
    }
}
