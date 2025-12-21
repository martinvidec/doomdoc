# DoomDoc Workflow

## Übersicht

Dieser Workflow integriert JIRA, Confluence und GitHub für eine strukturierte Entwicklung von DoomDoc.

**JIRA Projekt-Key:** `DOOM`

## Komponenten

### 1. Vision und Dokumentation (Confluence)
- **Zweck**: Langfristige Vision, Architektur-Entscheidungen, User Guides
- **Inhalt**:
  - Product Vision und Roadmap
  - Architektur-Dokumentation
  - Technische Spezifikationen
  - User Documentation
  - Meeting Notes und Entscheidungen

### 2. Task und Issue Management (JIRA)
- **Zweck**: Sprint Planning, Bug Tracking, Task Management
- **Issue Types**:
  - **Story**: Neue Features oder größere Änderungen
  - **Task**: Technische Aufgaben
  - **Bug**: Fehlerbehebungen
  - **Improvement**: Verbesserungen bestehender Features

### 3. Source Code Management (GitHub)
- **Repository**: doomdoc
- **Branch-Strategie**: Feature-Branch-Workflow mit JIRA-Integration

## Workflow-Prozess

### Session Start

**1. JIRA-Task auswählen**
```bash
# Rufen Sie JIRA auf und wählen Sie den nächsten offenen Task aus Ihrem Sprint/Backlog
# Notieren Sie sich die Issue-Nummer, z.B. DOOM-123
```

**2. Branch erstellen**
```bash
# Format: <type>/<JIRA-KEY>-<kurze-beschreibung>
# Beispiele:
git checkout -b feature/DOOM-123-add-search-functionality
git checkout -b bugfix/DOOM-124-fix-tree-collapse
git checkout -b improvement/DOOM-125-optimize-rendering
```

**Branch-Naming-Konventionen:**
- `feature/DOOM-XXX-description` - Neue Features
- `bugfix/DOOM-XXX-description` - Bug Fixes
- `improvement/DOOM-XXX-description` - Verbesserungen
- `docs/DOOM-XXX-description` - Dokumentations-Änderungen
- `refactor/DOOM-XXX-description` - Code-Refactoring

### Entwicklung

**1. Code-Änderungen durchführen**
- Implementieren Sie die Aufgabe gemäß JIRA-Ticket
- Folgen Sie den Coding-Standards in CLAUDE.md

**2. Commits erstellen**
```bash
# Commit-Message-Format:
# <JIRA-KEY>: <Kurzbeschreibung>
#
# <Längere Beschreibung (optional)>

git commit -m "DOOM-123: Add search highlighting functionality

Implements real-time search with result highlighting in the tree view.
Automatically expands parent nodes when search matches are found."
```

**3. Validierung**
```bash
# Build and Test
mvn clean compile package -DskipTests

# Generate Documentation
javadoc -doclet at.videc.DoomDoclet \
  -docletpath target/classes:target/dependencies/gson-2.8.9.jar \
  -classpath target/dependencies/gson-2.8.9.jar \
  -sourcepath ./src/main/java \
  -subpackages at.videc

# Verify output
ls -la output.html
```

### Pull Request

**1. Branch pushen**
```bash
git push -u origin feature/DOOM-123-add-search-functionality
```

**2. Pull Request erstellen**
- **Titel-Format**: `[DOOM-XXX] Kurzbeschreibung`
- **Beispiel**: `[DOOM-123] Add search highlighting functionality`

**PR-Beschreibung Template:**
```markdown
## JIRA Ticket
[DOOM-123](https://your-jira-instance.atlassian.net/browse/DOOM-123)

## Änderungen
- Beschreibung der Hauptänderungen
- Liste der modifizierten Komponenten

## Testing
- [ ] Build erfolgreich (`mvn clean compile package`)
- [ ] Dokumentation generiert (`output.html` erstellt)
- [ ] Manuelle Tests durchgeführt

## Screenshots (falls UI-Änderungen)
[Screenshots hier einfügen]

## Bemerkungen
Zusätzliche Informationen oder bekannte Einschränkungen
```

**3. JIRA-Status aktualisieren**
- Verschieben Sie das Ticket zu "In Review"
- Fügen Sie den PR-Link zum JIRA-Ticket hinzu

### Review und Merge

**1. Code Review**
- Reviewer prüft Code gemäß Standards
- Feedback wird in PR-Kommentaren gegeben
- Änderungen werden bei Bedarf vorgenommen

**2. Merge**
```bash
# Nach Approval:
git checkout main
git merge --no-ff feature/DOOM-123-add-search-functionality
git push origin main
```

**3. JIRA abschließen**
- Status zu "Done" verschieben
- Resolution auf "Fixed/Done" setzen
- Fix Version eintragen (falls vorhanden)

### Cleanup

```bash
# Lokalen Branch löschen
git branch -d feature/DOOM-123-add-search-functionality

# Remote Branch löschen
git push origin --delete feature/DOOM-123-add-search-functionality
```

## Automatisierung

### Git Hooks
Das Repository enthält Git Hooks für:
- **Branch-Name-Validierung**: Stellt sicher, dass Branches dem DOOM-XXX-Format folgen
- **Commit-Message-Validierung**: Prüft, ob JIRA-Key in Commit-Messages vorhanden ist

**Installation:**
```bash
./scripts/setup-hooks.sh
```

### GitHub Actions
Automatische Integration:
- PR-Titel-Validierung (JIRA-Key erforderlich)
- Automatische Build-Validierung
- JIRA-Status-Updates bei PR-Events
- Automatische Verlinkung zwischen GitHub und JIRA

## Best Practices

### Branch-Management
- Ein Branch pro JIRA-Ticket
- Branches kurz leben lassen (max. 1-2 Wochen)
- Regelmäßig von `main` mergen, um Konflikte zu vermeiden

### Commits
- Atomare Commits (eine logische Änderung pro Commit)
- Aussagekräftige Commit-Messages
- Immer JIRA-Key in der ersten Zeile

### Pull Requests
- Kleine, fokussierte PRs (< 500 Zeilen bevorzugt)
- Selbst-Review vor PR-Erstellung
- Tests und Build müssen erfolgreich sein

### JIRA
- Tickets detailliert beschreiben
- Acceptance Criteria definieren
- Zeitschätzungen pflegen
- Status aktuell halten

## Checkliste für neue Session

- [ ] JIRA öffnen und nächsten offenen Task auswählen
- [ ] Task-Details und Acceptance Criteria lesen
- [ ] Branch mit korrektem Format erstellen (`<type>/DOOM-XXX-description`)
- [ ] JIRA-Status auf "In Progress" setzen
- [ ] Entwicklung durchführen
- [ ] Build und Tests validieren
- [ ] PR mit JIRA-Key erstellen
- [ ] JIRA-Status auf "In Review" setzen
- [ ] Nach Merge: JIRA auf "Done" setzen und Branch aufräumen

## Tools und Links

- **JIRA**: [Link zu Ihrer JIRA-Instanz]
- **Confluence**: [Link zu Ihrem Confluence Space]
- **GitHub**: https://github.com/[your-username]/doomdoc
- **Git Hook Setup**: `./scripts/setup-hooks.sh`

## Support

Bei Fragen zum Workflow:
1. Confluence-Dokumentation prüfen
2. Team-Channel kontaktieren
3. WORKFLOW.md aktualisieren, wenn Prozess sich ändert
