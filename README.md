# MatchMe Graphql showcase

Graphql attached to a MatchMe project backend.

## Pre-requisites
- Java 21,
- Spring Boot

## Setup

create a `/env` in `server/`:

```bash
cd server/
cp .env.example .env
```
You can leave the values as the default values will work.

## Running the server

Make sure you are in the `server/` directory and run it with:
```bash
mvn spring-boot:run
```

## Notes
- This project uses a H2 database, so you don't have to set anything up manually
- 200 users will be created
- 2 test users will be created for your convenience with these credentials:
    - email: "tester1@tester.com" password: "password"
    - email: "tester2@tester.com" password: "password"

## Testing
Graphiql Web-UI is on http://localhost:8080 by default


