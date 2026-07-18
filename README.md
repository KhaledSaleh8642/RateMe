# RateMe

RateMe ist eine Webanwendung zur Bewertung von Restaurants, Cafés, Bars und weiteren Points of Interest (POIs) in Zweibrücken.

Benutzer können sich registrieren und anmelden, Orte auf einer interaktiven Karte anzeigen, Bilder hochladen, Bewertungen erstellen und ihre eigenen Bewertungen oder ihr Benutzerkonto löschen.

Das Projekt wurde im Rahmen eines Hochschulprojekts an der Hochschule Kaiserslautern entwickelt.

## Funktionen

- Registrierung, Anmeldung und Abmeldung von Benutzern
- Passwort-Hashing mit Salt
- Authentifizierung mithilfe von UUID-Tokens
- Geschützte Endpunkte über den `Authorization`-Header
- Interaktive Karte mit Leaflet
- Anzeige von Points of Interest (POIs)
- Erstellung und Löschung von Bewertungen
- Anzeige der eigenen Bewertungen
- Anzeige aller Bewertungen eines ausgewählten POIs
- Hochladen und Abrufen von Bildern
- Löschung des eigenen Benutzerkontos
- API-Dokumentation mit OpenAPI und Swagger
- AOP-basiertes Logging
- Automatische Initialisierung der MySQL-Datenbank
- Docker-Compose-Umgebung für Datenbank und Anwendung

## Verwendete Technologien

- Java 21
- Spring Boot 4
- Spring Web MVC
- Jakarta Persistence (JPA)
- Jakarta Validation
- MySQL 8
- Maven
- Docker und Docker Compose
- OpenAPI und Swagger
- HTML, CSS und JavaScript
- Leaflet
- REST und JSON

## Projektstruktur

```text
RateMe
├── appServer
│   └── Dockerfile
├── db
│   ├── conf
│   ├── initdb
│   └── Dockerfile
├── logs
├── src
│   ├── main
│   │   ├── java/de/hs_kl/rateme
│   │   │   ├── api
│   │   │   │   ├── controllers
│   │   │   │   └── dtos
│   │   │   ├── aspect
│   │   │   ├── config
│   │   │   ├── entity
│   │   │   ├── model/dbaccess/util
│   │   │   ├── security
│   │   │   └── RatemeApplication.java
│   │   └── resources
│   │       ├── static
│   │       └── application.properties
│   └── test
├── docker-compose.yml
└── pom.xml
```

## Authentifizierung

Nach einer erfolgreichen Registrierung oder Anmeldung gibt der Server ein UUID-Token im `Authorization`-Header der Antwort zurück.

Bei Aufrufen geschützter Endpunkte muss dieses Token im `Authorization`-Header der Anfrage übermittelt werden.

Die Tokens werden mithilfe einer `ConcurrentHashMap` im Arbeitsspeicher gespeichert. Daher werden alle Tokens ungültig, sobald die Anwendung neu gestartet wird. Dieses Authentifizierungsverfahren wurde für das Hochschulprojekt entwickelt und ist nicht für den produktiven Einsatz vorgesehen.

## Übersicht der API-Endpunkte

| Methode | Endpunkt | Authentifizierung | Beschreibung |
|---|---|---:|---|
| `POST` | `/api/users/register` | Nein | Neuen Benutzer registrieren |
| `POST` | `/api/users/login` | Nein | Benutzer anmelden |
| `POST` | `/api/users/logout` | Ja | Benutzer abmelden und Token ungültig machen |
| `DELETE` | `/api/users/me` | Ja | Eigenes Benutzerkonto löschen |
| `GET` | `/api/pois` | Ja | Alle POIs abrufen |
| `GET` | `/api/pois/{id}` | Ja | Einen bestimmten POI abrufen |
| `POST` | `/api/ratings` | Ja | Eine Bewertung erstellen |
| `GET` | `/api/ratings/me` | Ja | Eigene Bewertungen abrufen |
| `GET` | `/api/ratings/poi/{poiId}` | Ja | Bewertungen eines POIs abrufen |
| `DELETE` | `/api/ratings/{ratingId}` | Ja | Eine eigene Bewertung löschen |
| `POST` | `/api/images` | Ja | Ein Bild hochladen |
| `GET` | `/api/images/{id}` | Nein | Ein Bild abrufen |

## Anwendung starten

### Voraussetzungen

Folgende Software muss installiert sein:

- Java 21
- Docker Desktop
- Git

### 1. Repository klonen

```bash
git clone https://github.com/KhaledSaleh8642/RateMe.git
cd RateMe
```

### 2. Anwendung kompilieren

Unter Windows:

```powershell
.\mvnw.cmd clean package -DskipTests
```

Unter Linux oder macOS:

```bash
./mvnw clean package -DskipTests
```

Dadurch wird die JAR-Datei erzeugt, die von `appServer/Dockerfile` benötigt wird.

### 3. Anwendung mit Docker Compose starten

Docker Desktop muss gestartet sein. Im Hauptverzeichnis des Projekts wird anschließend folgender Befehl ausgeführt:

```bash
docker compose up --build
```

Um die Container im Hintergrund zu starten:

```bash
docker compose up --build -d
```

Docker Compose startet zwei Container:

- `rateme_db`: MySQL-Datenbank auf Port `3306`
- `rateme_appserver`: Spring-Boot-Anwendung auf Port `8080`

Die Datenbank wird automatisch mithilfe der SQL-Dateien im Verzeichnis `db/initdb` initialisiert.

### 4. Anwendung öffnen

Webanwendung:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI-Spezifikation:

```text
http://localhost:8080/v3/api-docs
```

### 5. Anwendung beenden

Container stoppen, ohne sie zu entfernen:

```bash
docker compose stop
```

Container stoppen und entfernen:

```bash
docker compose down
```

## Anwendung lokal ohne App-Container starten

Die Datenbank kann separat über Docker Compose gestartet werden:

```bash
docker compose up -d dbserver
```

Anschließend kann die Spring-Boot-Anwendung lokal gestartet werden.

Unter Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Unter Linux oder macOS:

```bash
./mvnw spring-boot:run
```

Die lokale Anwendung verwendet die Datenbankkonfiguration aus:

```text
src/main/resources/application.properties
```

## Beispiel: Benutzer registrieren

```http
POST /api/users/register
Content-Type: application/json

{
  "username": "khaled",
  "password": "secure-password",
  "email": "khaled@example.com",
  "firstname": "Khaled",
  "lastname": "Saleh",
  "street": "Musterstraße",
  "streetNr": "10",
  "zip": "66482",
  "city": "Zweibrücken"
}
```

Das UUID-Zugriffstoken wird im `Authorization`-Header der Antwort zurückgegeben.

## Beispiel: Bewertung erstellen

```http
POST /api/ratings
Authorization: <uuid-token>
Content-Type: application/json

{
  "poiId": 933057175,
  "grade": 4,
  "txt": "Gutes Essen und freundlicher Service.",
  "imageId": null
}
```

Die Bewertung muss zwischen `0` und `5` liegen. Der Bewertungstext darf nicht leer sein.

## Logging

Die Aktivitäten der Anwendung werden mithilfe eines Spring-AOP-Aspekts protokolliert.

Beim Start über Docker Compose wird die Logdatei in folgendem Pfad gespeichert:

```text
logs/rateme.log
```

Die generierte Logdatei wird durch `.gitignore` von Git ausgeschlossen.

## Tests

Vor der Ausführung der Tests muss die Datenbank gestartet werden:

```bash
docker compose up -d dbserver
```

Unter Windows:

```powershell
.\mvnw.cmd test
```

Unter Linux oder macOS:

```bash
./mvnw test
```

## Projektinhalte

Das Projekt demonstriert insbesondere:

- Entwicklung einer REST-API mit Spring Boot
- Datenaustausch über DTOs
- Manuelle Datenbankoperationen mit dem `EntityManager`
- Modellierung relationaler Daten
- Passwort-Hashing und selbst entwickelte Token-Authentifizierung
- Speicherung und Bereitstellung von Bildern
- Eingabevalidierung und Fehlerbehandlung
- AOP-basiertes Logging
- API-Dokumentation mit OpenAPI
- Bereitstellung mit Docker
- Integration von Frontend und Backend

## Autor

**Khaled Saleh**

[GitHub-Profil](https://github.com/KhaledSaleh8642)