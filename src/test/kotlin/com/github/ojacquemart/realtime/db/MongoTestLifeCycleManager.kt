package com.github.ojacquemart.realtime.db

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.GenericContainer

class MongoTestLifeCycleManager : QuarkusTestResourceLifecycleManager {

  var mongodb: GenericContainer<*> = GenericContainer<Nothing>("mongo:latest")
    .withExposedPorts(27_017)

  override fun start(): Map<String, String> {
    mongodb.start()

    return mapOf(
      "quarkus.mongodb.connection-string" to "mongodb://${mongodb.containerIpAddress}:${mongodb.firstMappedPort}"
    )
  }

  override fun stop() {
    mongodb.stop()
  }

}
