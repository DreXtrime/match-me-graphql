# MatchMe Graphql Showcase

Graphql api attached to a Social matching backend for a demo project. built with Spring Boot.

## Pre-requisites
- Java 21
- Maven

## Setup

create a `.env` in `server/`, recommended copying the example:

```bash
cd server/
cp .env.example .env
```
You can leave the values as the default values are setup to work out of the box (Not for production use!).

## Running the server

Make sure you are in the `server/` directory and run it with:
```bash
mvn spring-boot:run
```
If you want to use the graphql playground run it with the flag:
```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=dev"
```

## Notes
- This project uses a H2 database, so you don't have to set anything up manually
- 200 users will be created (amount can be modified in the .env)
- 2 test users will be created for your convenience with these credentials:
    - email: "tester1@tester.com" password: "password"
    - email: "tester2@tester.com" password: "password"  
    (they are set up to appear in each other's recommendations)

## Testing
GraphQL API endpoint: `http://localhost:8080/graphql`  
GraphQL playground (dev mode only): `http://localhost:8080/graphiql`
- If the cursor is misaligned in the web-ui try changing page zoom level.
- From the left side of the interface open the explorer, you will be able to see the api laid out to you.
- Everything except login and register will need an auth token. When you login or register the token will be returned and at the bottom of the screen, click "Headers" and add this:
```bash
{
  "Authorization": "Bearer your_user_token_here"
}
```
All request will now use authentication.

- Subscriptions use WebSocket and the token must be Authorization. Enable the subscription and you should see a loading icon, in another window send a connection request and the subscription details should pop up in real time.

## API Limitations
- `user(id)`, `bio(id)`, and `profile(id)` only work for users who appear in your recommendations, have a pending connection request, or are connected
- `recommendations` requires your profile and bio to be complete before returning results
- Email is only returned if the target is current user otherwise it is null


## File Paths
- GraphQL schema: `server/src/main/resources/graphql/schema.graphqls`
- GraphQL resolvers: `server/src/main/java/com/matchme/server/graphql/`

## Credits
[tanelerikneitov](https://gitea.kood.tech/tanelerikneitov)  
<img src="https://media1.tenor.com/m/7qJXeMvRL_4AAAAd/putting-in-work-locking-in.gif" width="100">