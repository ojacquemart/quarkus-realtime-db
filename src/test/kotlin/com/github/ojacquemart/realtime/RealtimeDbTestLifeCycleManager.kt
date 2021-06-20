package com.github.ojacquemart.realtime

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import io.smallrye.reactive.messaging.connectors.InMemoryConnector
import org.testcontainers.containers.MongoDBContainer

class RealtimeDbTestLifeCycleManager : QuarkusTestResourceLifecycleManager {

  val mongodb = MongoDBContainer("mongo:latest")
    .withExposedPorts(27_017)

  override fun start(): Map<String, String> {
    mongodb.start()

    return mapOf(
      "quarkus.mongodb.connection-string" to "mongodb://${mongodb.containerIpAddress}:${mongodb.firstMappedPort}",
    ) +
      InMemoryConnector.switchIncomingChannelsToInMemory("rtdb") +
      InMemoryConnector.switchOutgoingChannelsToInMemory("db-data")
  }

  override fun stop() {
    mongodb.stop()
    InMemoryConnector.clear();
  }

}
