# Real Time Database aka RTDB

[![Build](https://github.com/ojacquemart/quarkus-realtime-db/actions/workflows/build.yml/badge.svg)](https://github.com/ojacquemart/quarkus-realtime-db/actions/workflows/build.yml) [![codecov](https://codecov.io/gh/ojacquemart/quarkus-realtime-db/branch/develop/graph/badge.svg?token=SVQF7QJN63)](https://codecov.io/gh/ojacquemart/quarkus-realtime-db)
 [![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fojacquemart%2Fquarkus-realtime-db.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fojacquemart%2Fquarkus-realtime-db?ref=badge_shield)

This project uses Quarkus, the Supersonic Subatomic Java Framework.

Real Time Database is a project to notice live database changes.

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

## Features

- Create a project and its collections
- Listen MongoDB events through Kafka Connect and CDC (Change Data Capture) with Debezium

## Demo mode

### Docker network

The docker compose uses a custom network named `rdtb-network`.

To rename this network, use:

```sed -i '/rtdb-network/foobarqix-network/g' docker-compose.yml```

### Run the demo

You can setup the whole environment using docker-compose.

The full docker compose starts the kafka, mongo, backend and frontend parts.

```shell script
# quarkus backend
./mvnw package
docker build -f src/main/docker/Dockerfile.jvm -t realtime-db .

# vue sample ui
cd samples/ui
docker build -t realtime-ui .

cd ../..

# the whole stack
docker network create rtdb-network
docker compose -f docker-compose-full.yml up
```

## Dev mode

You can only start the kafka and mongo parts of the stack when you do backend or frontend stuff.

```
docker compose up
```

### Quarkus backend

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

### Sample UI

Go to the `samples/ui` and run the `nom run dev` command.

Check `http://localhost:3001` to configure your projects and collections.

### Mongo

The MongoDB instance starts in a replica set mode. The docker-compose command does it for you.

### Debezium

Debezium requires a [MongoDB connector](dbz-mongodb-connector.json).

Here are the curl commands to add the MongoDB connector:

```shell script
curl -X DELETE connect:8083/connectors/rtdb-connector
curl -X POST -H "Accept:application/json" connect:8083/connectors/ -d @dbz-mongodb-connector.json
curl -X GET -H "Accept:application/json" -H "Content-Type:application/json" connect:8083/connectors
```

By default, the reserved databases (admin, config and local) are not monitored. The docker-compose command does it for you.

## Kafka

### Consumers config

See [Consumers configuration](https://kafka.apache.org/documentation/#consumerconfigs)

The `metadata.max.age` property is set to 30 seconds. It means that the quarkus app will receive new items
from any new collections after 30 seconds. The default value was 5 minutes.

TODO: see if there is a way to auto-detect the new topics created.

## API Reference

#### Create a project

```http
  POST /admin/api/projects
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `name` | `string` | **Required**. The project name |

#### Get the projects names

```http
  GET /admin/api/projects
```

#### Get a project by its name

```http
  GET /admin/api/projects/:name
```

#### Create a table

```http
  POST /admin/api/projects/{projectId}/collections
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `name`      | `string` | **Required**. The collection name |

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

## Examples

Setup the [debezium connector](#debezium).

### JavaScript

1️⃣ First, you have to create a project. The endpoint returns an apikey. Copy it while it's warm.

```curl
curl -X POST -H "Content-Type: application/json" http://localhost:8080/admin/api/projects -d '{"name": "foobarqix"}'
```

2️⃣ The second endpoint associates a collection to the project.

```curl
curl -X POST -H "Content-Type: application/json" http://localhost:8080/admin/api/projects/foobarqix/collections  -d '{"name": "demo"}'
```

3️⃣ Then, you can initialize the websocket connection. The `_all` parameter means that you will receive all events
from the demo collection. Specify a id if you want to be more specific.

```javascript
var APIKEY = '7b4bde3c-4ab2-495f-b06e-f5f51f684e02'
var socket = new WebSocket('ws://localhost:8080/foobarqix/demo/_all')
socket.addEventListener('open', (event) => {
    console.log('Connect initialized, verifying the apikey...')
    socket.send(JSON.stringify({type: 'HELLO', content: APIKEY}))
})

socket.addEventListener('message', (event) => {
    console.log('Message from rtdb:', event.data)
})

socket.addEventListener('close', (error, reason) => {
    console.log('Connection closed:', error, reason)
})
```

4️⃣ Play with the CRUD operations:

```javascript
socket.send(JSON.stringify({type: 'CREATE', content: {_id: "foo", "name": "bar"}}))
socket.send(JSON.stringify({type: 'READ', content: {_id: "foo"}}))
socket.send(JSON.stringify({type: 'UPDATE', content: {_id: "foo", "name": "foobarqix"}}))
socket.send(JSON.stringify({type: 'DELETE', content: {_id: "foo"}}))
```

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

## Tests

```
mvn clean verify sonar:sonar -Dsonar.host=$SONAR_HOST -Dsonar.login=$SONAR_LOGIN -Dsonar.password=SONAR_PASSWORD -Dsonar.coverage.jacoco.xmlReportPaths=target/jacoco-report/jacoco.xml
```

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

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

## License

[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fojacquemart%2Fquarkus-realtime-db.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Fojacquemart%2Fquarkus-realtime-db?ref=badge_large)
