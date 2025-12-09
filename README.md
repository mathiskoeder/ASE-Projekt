# Chess

Konsolen-Schach in Java — Programmentwurf an der DHBW Karlsruhe (Kurs Anwendungsorientiertes
Software Engineering).

## Überblick

Vollständiges Schachspiel im Hot-Seat-Modus, gespielt im Terminal:

- alle 6 Figurentypen mit kompletten Zugregeln
- Sonderzüge: kurze/lange Rochade, En passant, Bauernumwandlung
- Endspielerkennung: Schachmatt, Patt, 50-Züge-Regel, dreifache Stellungswiederholung
- Speichern und Laden laufender Partien (FEN + PGN-light)
- Eingaben in algebraischer Zugnotation (z. B. `e2-e4`, `e7-e8=Q`)

## Architektur

Das Projekt folgt einer Clean-Architecture-Aufteilung mit vier Schichten:

```
de.dhbw.chess
├── domain          ← Entities, Value Objects, Domain Services, Repository-Interfaces
├── application     ← Use-Cases, Factories, DTOs
├── infrastructure  ← Datei- und In-Memory-Repositories, FEN-/PGN-Serialisierung
└── presentation    ← Konsolen-UI, Eingabe-Parser, Command-Pattern
```

Domain-Driven Design liegt zugrunde: `Game` ist Aggregate Root, Brett, Züge und Status werden über
Value Objects modelliert, Persistenz hängt am Repository-Interface.

## Build & Run

Voraussetzung: JDK 17 oder neuer.

```bash
./gradlew build           # kompiliert + Tests
./gradlew test            # nur Tests
./gradlew jacocoTestReport
./gradlew run             # startet das Spiel
```

## Autoren

- Max Katzenberger
- Mathis Koeder
