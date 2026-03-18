# ikgrcscore

[![codecov](https://codecov.io/gh/takanori-ugai/ikgrcscore/graph/badge.svg?token=VBE17UHRP3)](https://codecov.io/gh/takanori-ugai/ikgrcscore)

`ikgrcscore` is a Kotlin 17 service built with Javalin. It exposes REST endpoints for submitting answers to questions `Q1` through `Q8`, retrieving scenario metadata, and reading a simple ranking view. The project also serves static HTML assets and publishes OpenAPI, Swagger UI, and ReDoc documentation from the running application.

## Current implementation status

The repository is currently a scaffolded scoring API rather than a full scoring engine:

- `POST /Q1` through `POST /Q8` validate request bodies and return a fixed success payload with score `0.3` and ranking `3`.
- `GET /Scenario/list` returns a fixed list: `["test1", "test2"]`.
- `GET /Scenario/{id}` returns a fixed scenario payload regardless of the `id`.
- `GET /Ranking` returns one hard-coded ranking entry.
- `GET /Ranking/{id}` echoes the requested `id` and returns fixed ranking data.
- `GET /` redirects to `assets/Test0.html`.

One implementation detail matters for local runs: `Q1` opens the bundled SQLite database `test.db` and executes `select * from table1` before returning its response. That database file is included in the repository.

## Requirements

- Java 17
- No separate database setup is required for the checked-in development database

## Run locally

Start the application with Gradle:

```bash
./gradlew run
```

On Windows:

```powershell
.\gradlew.bat run
```

The application starts on `http://localhost:7000`.

Useful URLs:

- App root: `http://localhost:7000/`
- OpenAPI JSON: `http://localhost:7000/openapi`
- Swagger UI: `http://localhost:7000/swagger-ui`
- ReDoc: `http://localhost:7000/redoc`
- Static assets: `http://localhost:7000/assets/...`

## API summary

### Scoring endpoints

All scoring endpoints require `name`, `scenario`, and `answers`. Invalid or blank values return HTTP `400`.

| Endpoint | `answers` shape |
| --- | --- |
| `POST /Q1` | `[{ "name": "Kitchen", "number": 2 }]` |
| `POST /Q2` | `[{ "name": "WALK", "number": 1 }]` |
| `POST /Q3` | `["WALK", "GRAB"]` |
| `POST /Q4` | Same payload shape as `Q3` |
| `POST /Q5` | `[{ "time": "2022-01-01T00:00:20.005", "room": "LivingRoom", "obj": ["Cup"] }]` |
| `POST /Q6` | `"Grab"` |
| `POST /Q7` | `[{ "obj1": "Table", "obj2": "Cup", "relation": "ON" }]` |
| `POST /Q8` | `[{ "name": "Table", "change": [{ "place": [1.1, 2.5, 3.2], "status": ["ON", "CLEAN"] }] }]` |

Successful scoring responses currently look like this for every `Q1` to `Q8` endpoint:

```json
{
  "statusCode": 200,
  "method": "POST",
  "message": "Succeed",
  "data": {
    "score": 0.3,
    "ranking": 3
  }
}
```

Example request:

```bash
curl -X POST http://localhost:7000/Q3 \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice","scenario":"Scenario1","answers":["WALK","GRAB"]}'
```

### Scenario endpoints

- `GET /Scenario/list`
- `GET /Scenario/{id}`

Example scenario response:

```json
{
  "statusCode": 200,
  "method": "GET",
  "message": "Succeed",
  "data": {
    "id": "Scenario1",
    "title": "Scenario1",
    "scene": 1,
    "activities": ["Test"]
  }
}
```

### Ranking endpoints

- `GET /Ranking`
- `GET /Ranking/{id}`

Example ranking list response:

```json
{
  "rankings": [
    {
      "id": "TeamB",
      "rank": 3,
      "score": 0.3
    }
  ]
}
```

## Tests

Run the automated tests with:

```bash
./gradlew test
```

The test suite covers:

- request validation for blank and whitespace-only inputs
- scenario endpoints
- answer and DTO serialization behavior
- `Q7` and `Q8` payload handling

## Project layout

- `src/main/kotlin/com/fujitsu/ikgrcscore`: Javalin app, handlers, and request/response models
- `src/main/resources/public`: static HTML, CSS, JS, and media assets
- `src/test/kotlin/com/fujitsu/ikgrcscore`: JUnit 5 tests
- `config/detekt.yml`: Detekt configuration
- `docs/openapi.yml`: checked-in OpenAPI document

## Notes

- The runtime-generated `/openapi` document is the closest representation of the current code.
- `docs/openapi.yml` exists in the repository, but some field names and types in that file differ from the current Kotlin DTOs. For example, the `Q5` DTO uses an `obj` field (`List<String>`), while the YAML defines an `object` field (`String`). For `Q7`, the DTOs use `obj1`/`obj2` while the YAML uses `object1`/`object2`.
