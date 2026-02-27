package de.dhbw.chess.domain.entity;

import de.dhbw.chess.domain.valueobject.PieceColor;

import java.util.Objects;
import java.util.UUID;

/**
 * Spieler einer Schachpartie. Entity, identifiziert durch eine ID, die über mehrere Partien hinweg
 * stabil bleibt (z. B. für Spieler-Statistiken in einer späteren Erweiterung).
 */
public class Player {

    private final UUID id;
    private final String name;
    private final PieceColor color;

    public Player(UUID id, String name, PieceColor color) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.color = Objects.requireNonNull(color, "color");
    }

    public static Player named(String name, PieceColor color) {
        return new Player(UUID.randomUUID(), name, color);
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public PieceColor color() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player p)) return false;
        return id.equals(p.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
