# Chess

Konsolen-Schach in Java — Programmentwurf an der DHBW Karlsruhe (Kurs Anwendungsorientiertes
Software Engineering).

## Überblick

Vollständiges Schachspiel im Hot-Seat-Modus, gespielt im Terminal:

- alle 6 Figurentypen mit kompletten Zugregeln
- Sonderzüge: kurze/lange Rochade, En passant, Bauernumwandlung
- Endspielerkennung: Schachmatt, Patt, 50-Züge-Regel, dreifache Stellungswiederholung, Aufgabe
- Speichern und Laden laufender Partien (FEN + PGN-light)
- Eingaben in algebraischer Zugnotation (z. B. `e2-e4`, `e7-e8=Q`, `O-O`)

## Architektur

Das Projekt folgt einer Clean-Architecture-Aufteilung mit vier Schichten:

```
de.dhbw.chess
├── domain          ← Entities, Value Objects, Domain Services, Repository-Interfaces
├── application     ← Use-Cases, Factories, DTOs
├── infrastructure  ← Datei- und In-Memory-Repositories, FEN-/PGN-Serialisierung
└── presentation    ← Konsolen-UI, Eingabe-Parser, Command-Pattern
```

Abhängigkeitsrichtung: `presentation → application → domain ← infrastructure`. Die Domain-Schicht
hat keine Imports aus den anderen Paketen.

## Domain-Driven Design

- **Aggregate Root:** `Game` kapselt das Brett, die Zughistorie, beide Spieler, Status und ziehende
  Farbe; alle regelrelevanten Änderungen laufen über dieses Aggregat.
- **Entities:** `Game`, `Board`, `Player`, `Piece` (Subtypen `King`, `Queen`, …).
- **Value Objects:** `Position`, `Move`, `MoveRecord`, `PieceColor`, `PieceType`, `GameStatus`,
  `Square`.
- **Domain Services:** `MoveValidator`, `CheckDetector`, `GameStateEvaluator`, `CastlingRules`,
  `EnPassantRules`.
- **Repositories:** `GameRepository`, `PlayerRepository` als Interfaces in `domain`,
  Implementierungen in `infrastructure`.

## Entwurfsmuster

- **Strategy:** `Piece` als abstrakte Basis, jede Figur kapselt ihre Zugregeln in einer eigenen
  Subklasse.
- **Command:** CLI-Befehle als Command-Objekte (`MoveCmd`, `SaveCmd`, …) hinter einem gemeinsamen
  Interface.
- **Factory:** `GameFactory.createNewGame` setzt das Standard-Brett auf.

## Refactorings (im Verlauf der Git-History)

1. **Replace Conditional with Polymorphism:** zuerst `Piece.possibleMoves()` mit `switch(type)`,
   dann zu Strategy-Subklassen umgebaut (siehe Phase 2 → 3 in der Commit-History).
2. **Extract Class:** Validierung und Schach-Erkennung wurden aus `Game` in `MoveValidator` und
   `CheckDetector` extrahiert; gleichzeitig DRY-Auflösung doppelter Schach-Prüflogik (Phase 5 → 6).

## Build & Run

Voraussetzung: JDK 17 oder neuer (Wrapper liefert Gradle 9.0.0). Mockito-Tests laufen mit
`net.bytebuddy.experimental=true`, das Flag ist im Build bereits gesetzt.

```bash
./gradlew build           # kompiliert + Tests
./gradlew test            # nur Tests
./gradlew jacocoTestReport
./gradlew run             # startet das Spiel
```

## Bedienung

```
DHBW Schach — 'help' für Befehle.
> new
Neue Partie: 4f3c…
> e2-e4
> e7-e5
> save                  # speichert nach ~/.chess-saves/<id>.chess
> load 4f3c…
> resign
> quit
```

## Tests

Die JUnit-5-Suite umfasst u. a. Value-Object-Validierung, Figurenzüge, Rochade, En passant,
Promotion, Schach-/Matt-/Patt-Erkennung, dreifache Wiederholung, Application-Service-Tests mit
Mockito sowie Round-Trip-Tests für die FEN- und Datei-Persistenz.

```bash
./gradlew jacocoTestReport
open build/reports/jacoco/test/html/index.html
```

## Dokumentation

Die ausführliche Programmentwurf-Dokumentation (Clean Architecture, SOLID, GRASP/DRY, DDD,
Refactorings, Entwurfsmuster, Unit-Tests) liegt in `docs/Programmentwurf_Dokumentation.tex`
mit gebauter PDF unter `docs/Programmentwurf_Dokumentation.pdf`.

## Autoren

- Max Katzenberger
- Mathis Koeder
