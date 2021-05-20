# Real Time Database aka RTDB

This project uses Quarkus, the Supersonic Subatomic Java Framework.

Real Time Database is a project to expose live database changes.

This project exposes apis to configure a project and its collections. It relies on MongoDB.

The data modifications will be visible in live a la firebase. Clients should subscribe to a websocket to get the
modifications.

## Stack

- Quarkus
- MongoDB
- Kafka
    - Kafka Connect
    - Debezium Connector for CDC (Change Data Capture)
- Websockets

## Database structure

#### Projects

Collection name: `rtdb-projects`

| Field         | Type            |
| :--------     | :-------        |
| `_id`         | `ObjectId`      |
| `name`        | `String`        |
| `apikey`      | `String`        |
| `active`      | `Boolean`       |
| `collections` | `List<String>`  |
| `createdAt`   | `Long`          |

A project has a global apikey.

The project name and collections names should respect
the [MongoDB restrictions names](https://docs.mongodb.com/manual/reference/limits/#std-label-restrictions-on-db-names).

## Features

- Create a project
- Create tables associated to a project

## API Reference

#### Create a project

```http
  POST /admin/api/projects
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `name` | `string` | **Required**. The project name |

#### Create a table

```http
  POST /admin/api/projects/{projectId}/collections
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `name`      | `string` | **Required**. The table name |

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory. Be aware that it’s not an _über-jar_ as
the dependencies are copied into the `target/quarkus-app/lib/` directory.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/db-realtime-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html
.
