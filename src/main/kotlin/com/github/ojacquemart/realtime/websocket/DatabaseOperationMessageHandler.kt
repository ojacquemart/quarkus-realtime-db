package com.github.ojacquemart.realtime.websocket

import com.github.ojacquemart.realtime.db.JsonDocument
import com.github.ojacquemart.realtime.db.MongoOperation
import io.vertx.core.eventbus.EventBus
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DatabaseOperationMessageHandler(
  val eventBus: EventBus,
  val sessionStore: SessionStore
) : MessageHandler {

  private val logger = Logger.getLogger(DatabaseOperationMessageHandler::class.java)

  override fun willHandle(messageType: MessageType) = TYPES.contains(messageType)

  override fun handle(sessionMessage: SessionMessage) {
    logger.trace("Handle database operation message {type: ${sessionMessage.message.type}}")

    if (!sessionStore.isPresent(sessionMessage.session)) {
      logger.warn("Could not handle message without known session attached")

      return sessionMessage.session.close(CloseCodes.unauthorized())
    }

    eventBus.publish(
      "ws-data",
      MongoOperation(
        data = sessionMessage.message.content as JsonDocument,
        collection = sessionMessage.collection,
        db = sessionMessage.project,
        id = sessionMessage.id,
        type = sessionMessage.message.type.name
      )
    )
  }

  companion object {
    val TYPES = listOf(
      MessageType.CREATE, MessageType.UPDATE, MessageType.READ, MessageType.DELETE
    )
  }

}
