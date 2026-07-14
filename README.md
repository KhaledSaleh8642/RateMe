# RateMe

A REST-based backend for a location rating platform. Users can register, log in, browse points of interest (POIs), submit ratings, and retrieve ratings associated with their account or a specific location.

The project was developed as a university software project at Hochschule Kaiserslautern. This repository focuses on the Spring Boot backend and database environment.

## Features

- User registration, login, and logout
- Salted password hashing
- UUID-based access tokens
- Protected REST endpoints using the `Authorization` header
- Retrieval of all POIs or an individual POI
- Creation of ratings with a grade from 0 to 5
- Retrieval of the authenticated user's ratings
- Retrieval of all ratings for a selected POI
- DTO-based JSON request and response models
- MySQL database initialized through Docker scripts

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Jakarta Validation
- MySQL 8
- Maven
- Docker
- JSON / REST

## Architecture

The application separates API, persistence, domain, and authentication responsibilities:

```text
src/main/java/de/hs_kl/rateme
├── api
│   ├── Controllers
│   └── DTOs
├── entity
├── model/dbaccess/util
├── security
└── RatemeApplication.java
```

### Main Components

- **Controllers** expose the REST API.
- **DTOs** define the external JSON contract.
- **Entities** represent users, POIs, ratings, and images.
- **DBAccess** contains the database operations.
- **SecurityManager** creates and validates UUID access tokens.
- **PasswordTools** handles password hashing and verification.

## Authentication Flow

1. A user registers or logs in.
2. The API returns a UUID token in the `Authorization` response header.
3. The client sends this token in the `Authorization` request header when calling protected endpoints.

> The token store is held in memory using a `ConcurrentHashMap`. Tokens are therefore removed when the application restarts. This implementation was created for the academic project and is not intended as production authentication.

## API Overview

Base URL:

```text
http://localhost:8080
```

| Method | Endpoint | Authentication | Description |
|---|---|---:|---|
| `POST` | `/api/users/register` | No | Register a user and receive an access token |
| `POST` | `/api/users/login` | No | Log in and receive an access token |
| `POST` | `/api/users/logout` | Yes | Invalidate the current access token |
| `GET` | `/api/pois` | Yes | Retrieve all POIs |
| `GET` | `/api/pois/{id}` | Yes | Retrieve one POI |
| `POST` | `/api/ratings` | Yes | Create a rating |
| `GET` | `/api/ratings/me` | Yes | Retrieve the authenticated user's ratings |
| `GET` | `/api/ratings/poi/{poiId}` | Yes | Retrieve ratings for one POI |

## Example Requests

### Register a user

```http
POST /api/users/register
Content-Type: application/json

{
  "username": "khaled",
  "password": "secure-password",
  "email": "khaled@example.com",
  "firstname": "Khaled",
  "lastname": "Saleh",
  "street": "Example Street",
  "streetNr": "10",
  "zip": "66482",
  "city": "Zweibrücken"
}
```

The access token is returned in the response header:

```text
Authorization: <uuid-token>
```

### Retrieve all POIs

```http
GET /api/pois
Authorization: <uuid-token>
```

### Create a rating

```http
POST /api/ratings
Authorization: <uuid-token>
Content-Type: application/json

{
  "poiId": 933057175,
  "grade": 4,
  "txt": "Great food and friendly service.",
  "imageId": null
}
```

Additional request examples are available in [`requests.http`](requests.http).

## Local Setup

### Prerequisites

- Java 21
- Docker
- Git

### 1. Clone the repository

```bash
git clone https://github.com/KhaledSaleh8642/RateMe.git
cd RateMe
```

### 2. Build the MySQL image

Run the following commands from the `db` directory:

```bash
cd db
docker build -f Dockerfile -t swtp_rateme_image .
```

The image initializes the `swtp_rateme` database using the SQL scripts under `db/initdb`.

### 3. Start the database container

```bash
docker run --name swtpRateme -d -p 3306:3306 swtp_rateme_image
```

For later runs:

```bash
docker start swtpRateme
docker stop swtpRateme
```

### 4. Run the Spring Boot application

Return to the repository root:

```bash
cd ..
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The API starts at:

```text
http://localhost:8080
```

## Database Configuration

The default application configuration expects:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/swtp_rateme
spring.datasource.username=root
spring.datasource.password=
```

The provided Docker image allows an empty local MySQL root password for development. Do not use this configuration in production.

## Testing

Run the Maven test suite:

```bash
./mvnw test
```

You can also execute the sample API requests in [`requests.http`](requests.http) with an HTTP client supported by IntelliJ IDEA, VS Code, or another compatible editor.

## Frontend Integration

The REST API returns JSON and was designed to be consumed by a browser-based client. A separate project version used JavaScript, HTML, CSS, and Leaflet to display POIs on an interactive map and communicate with the backend through `fetch`.

The JavaScript frontend is not included in the current public repository.

## Project Context

This project demonstrates:

- Java and Spring Boot backend development
- REST API design
- DTO-based data exchange
- Relational database access with JPA
- Password hashing and custom token authentication
- Docker-based database initialization
- Client-server integration using JSON

## Author

**Khaled Saleh**

- GitHub: [KhaledSaleh8642](https://github.com/KhaledSaleh8642)
