# MatchMe Graphql Showcase ![Java](https://img.shields.io/badge/Java-21-orange) ![Maven](https://img.shields.io/badge/Maven-3.8-red) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green) ![H2](https://img.shields.io/badge/Database-H2-blue) ![GraphQL](https://img.shields.io/badge/GraphQL-pink)

A GraphQL API layer built on top of a social matching backend, implemented with Spring Boot.

## Pre-requisites
- Java 21
- Maven

## Setup

Clone the project:
```bash
git clone https://gitea.kood.tech/tanelerikneitov/graphql.git
cd graphql/
```

Create a `.env` in `server/`, it is recommended to copy the .env.example:  
```bash
cd server/
cp .env.example .env
```
You can leave the values unchanged as the default values are setup to work out of the box (Not for production use!).

## Running the server

Make sure you are in the `server/` directory and run the server in dev mode with:
```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=dev"
```
(Not recommended for testing) To run it without the GraphQL playground omit the optional dev flag:
```bash
mvn spring-boot:run
```

## Notes
- This project uses a H2 embedded database, no manual database setup required. Data is stored in `matchme-db.mv.db` — delete this file to reset the database.
- 200 users will be seeded on first run (amount can be modified in `.env`)
- 2 test users will be created for your convenience that you can use, their credentials are:
    - email: `tester1@tester.com` password: `password`
    - email: `tester2@tester.com` password: `password`  
    (they are set up to appear in each other's recommendations)

## Testing

| Endpoint | URL |
|---|---|
| REST API | `http://localhost:8080` |
| GraphQL API | `http://localhost:8080/graphql` |
| GraphQL Playground (dev mode only) | `http://localhost:8080/graphiql` |

### Testing GraphQL

1. Start the server in dev mode (see above)
2. Open `http://localhost:8080/graphiql` in your browser
3. In the explorer on the left you can browse all available queries, mutations and subscriptions
4. First get an auth token by running the login mutation:
```graphql
mutation {
  login(email: "tester1@tester.com", password: "password") {
    token
  }
}
```
5. Copy the token value from the response
6. Click "Headers" at the bottom of the screen and add:
```json
{
  "Authorization": "Bearer your_token_here"
}
```
7. All subsequent requests will now be authenticated as long the header contains a valid token.
For example:
- Get current users email, name and id:

```graphql
query MyQuery {
  me {
    email
    name
    id
  }
}
```

- Get the name, profile picture url, about me and bio data for all recommendations in a single query:

```graphql
query MyQuery {
  recommendations {
    profilePicture
    profile {
      aboutMe
    }
    name
  }
}
```

### Testing Subscriptions

Subscriptions allow you to receive real-time notifications when someone sends you a connection request through websockets.

1. Open two browser tabs, both at `http://localhost:8080/graphiql`
2. Log in as `tester1@tester.com` in tab 1 and `tester2@tester.com` in tab 2, add their respective tokens to headers in each tab
3. In tab 1 run the subscription:
```graphql
subscription {
  connectionRequestReceived {
    id
    name
  }
}
```
4. You should see a loading spinner indicating it is listening
5. In tab 2 find tester1's ID by running `recommendations { id }`, then send a connection request to their id:
```graphql
mutation {
  sendConnectionRequest(id: "tester1-id-here")  {
    message
  }
}
```
6. Tab 1 should immediately receive the event and display the corresponding data.

### Testing REST APIs

- Use a tool like Postman or the built in IntelliJ Http Client. Get a token via `POST /auth/login` and add it as a Bearer token to subsequent requests.  
- A [Postman collection](REST_API.postman_collection.json) is also available for you to import.
- General documentation for the REST_API for the Match me backend can be found at [REST API Documentation](REST_API.md).

## Troubleshooting

- **If the cursor misaligned in GraphiQL** — try changing the browser zoom level with `Ctrl+-`/`Ctrl+=`

## API Limitations
- `user(id)`, `bio(id)`, and `profile(id)` only work for users who appear in your recommendations, have a pending connection request, or are connected
- `recommendations` requires your profile and bio to be complete before returning results
- `email` is only returned for the currently authenticated user, it is null for all other users

## File Paths
- GraphQL schema: `server/src/main/resources/graphql/schema.graphqls`
- GraphQL resolvers: `server/src/main/java/com/matchme/server/graphql/`

## Credits
[tanelerikneitov](https://gitea.kood.tech/tanelerikneitov)  
<img src="https://media1.tenor.com/m/7qJXeMvRL_4AAAAd/putting-in-work-locking-in.gif" width="100">