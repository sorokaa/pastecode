# PasteCode

PasteCode is a unique project that allows you to share your code snippets among your friends, parents, pets, and even
your colleagues!

It works pretty simply — you write your code, hit save, and it's done! Just copy link and send it to whoever you
want!

Have a sensitive code? No problem, simply enter your password, and your code will be secured (probably). Also, you can
add code expiration time, and no one will get access to this code after this time.

## API Documentation

API is documented with OpenAPI.

Link: ****http(-s):/{host}/swagger-ui/index.html****

## Stack

* Java 17
* Spring Boot, Spring Data JPA, Spring Security
* Keycloak
* PostgreSQL 14
* Liquibase
* Gradle
* Docker (for testcontainers)
* Lombok, Mapstruct
* JUnit, RestAssured, Testcontainers

## Project environment variables

Database variables:

| Variable          | Description                                                  |
|-------------------|--------------------------------------------------------------|
| POSTGRES_VERSION  | PostgreSQL image version                                     |
| POSTGRES_HOST     | IP or host where PostgreSQL located                          |
| POSTGRES_USER     | Username (will be created such user in PostgreSQL container) |
| POSTGRES_PASSWORD | Password of user                                             |
| POSTGRES_DB       | Name of database                                             |
| POSTGRES_PORT     | External PostgreSQL port                                     |

Keycloak variables:

| Variable                   | Description                                                           |
|----------------------------|-----------------------------------------------------------------------|
| KEYCLOAK_DATABASE_HOST     | IP or host where Keycloak database is located                         |
| KEYCLOAK_DATABASE_USERNAME | Username (will be created such user in Keycloak PostgreSQL container) |
| KEYCLOAK_DATABASE_PASSWORD | Password of user                                                      |
| KEYCLOAK_DATABASE_NAME     | Keycloak Database name                                                |
| KEYCLOAK_DATABASE_PORT     | External Keycloak PostgreSQL port                                     |
| KEYCLOAK_VERSION           | Version of Keycloak image                                             |
| KEYCLOAK_ADMIN_USER        | Username of admin user                                                |
| KEYCLOAK_ADMIN_PASSWORD    | Password of admin user                                                |
| KEYCLOAK_HOST              | IP or host where Keycloak located                                     |
| KEYCLOAK_PORT              | External Keycloak port                                                |
| KEYCLOAK_URL               | Final Keycloak URL with host and port                                 |
| KEYCLOAK_REALM             | Realm that project uses                                               |
| KEYCLOAK_CLIENT_ID         | Admin CLI client id                                                   |
| KEYCLOAK_CLIENT_SECRET     | Admin CLI client secret                                               |

## Running project

### Installing components

First, you need to have a database. You can either create it via docker-compose.yaml in the project root or run your own
RDBMS.
After a successful database run, create a "pastecode" there (I'm too lazy to do it automatically).
Then, you need to have a Keycloak instance. You can run it with docker-compose.yaml or use your own.

### Keycloak settings

1. Login in Administrator Console with admin credentials
2. Create realm, put this realm name into KEYCLOAK_REALM variable, switch to this realm in Keycloak
3. Open Clients tab, then open "admin-cli" client.
4. Find **Client authentication** toggle, make it active. Tab **Credentials** must appear
5. Open **Credentials** tab and copy value from **Client secret** input.
6. Put this value into **KEYCLOAK_REALM** variable.

### Running backend

Finally, after all of these steps, you can run the backend. First, you need to update .env file in the project root.
Then use Gradle CLI to run the project (or just use IDE).

## Diagram

![image](documentation%2Fpastecode.png)