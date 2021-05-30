package com.github.ojacquemart.realtime.db.debezium

import com.github.ojacquemart.realtime.db.MongoOperation
import io.vertx.core.eventbus.EventBus
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ChangeEventConsumer(
  val eventBus: EventBus
) {

  private val logger = Logger.getLogger(ChangeEventConsumer::class.java)

  @Incoming("rtdb")
  fun onMessage(message: ChangeEvent?) {
    try {
      logger.info("Message received: $message")

      val data = MongoOperation.from(message)
      data?.let {
        eventBus.publish("db-data", it)
      }
    } catch (e: Exception) {
      logger.error("Unable to process message", e)
    }
  }

}
