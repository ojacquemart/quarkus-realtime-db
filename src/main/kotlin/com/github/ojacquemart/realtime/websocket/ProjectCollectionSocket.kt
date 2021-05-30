package com.github.ojacquemart.realtime.websocket

import com.github.ojacquemart.realtime.db.MongoOperation
import io.quarkus.vertx.ConsumeEvent
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Instance
import javax.websocket.*
import javax.websocket.server.PathParam
import javax.websocket.server.ServerEndpoint

@ServerEndpoint(
  value = "/{project}/{collection}/{id}",
  encoders = [MessageEncoder::class],
  decoders = [MessageDecoder::class]
)
@ApplicationScoped
class ProjectCollectionSocket(
  val handlers: Instance<MessageHandler>,
  val sessionStore: SessionStore
) {

  private val logger = Logger.getLogger(ProjectCollectionSocket::class.java)

  @OnOpen
  fun onOpen(
    session: Session,
    @PathParam("project") project: String,
    @PathParam("collection") collection: String,
    @PathParam("id") id: String? = null
  ) {
    logger.trace("Session opened {sessionId: ${session.id}}")
  }

  @OnClose
  fun onClose(
    session: Session,
    @PathParam("project") project: String,
    @PathParam("collection") collection: String,
    @PathParam("id") id: String
  ) {
    logger.trace("Session closed {sessionId: ${session.id}}")

    sessionStore.remove(session)
  }

  @OnError
  fun onError(
    session: Session,
    @PathParam("project") project: String,
    @PathParam("collection") collection: String,
    @PathParam("id") id: String,
    throwable: Throwable
  ) {
    logger.error("Session error {sessionId: ${session.id}, project=$project, coll=$collection, id=$id}", throwable)

    sessionStore.remove(session)
  }

  @OnMessage
  fun onMessage(
    session: Session,
    message: Message,
    @PathParam("project") project: String,
    @PathParam("collection") collection: String,
    @PathParam("id") id: String = ""
  ) {
    logger.trace("New message {sessionId: ${session.id}, type: ${message.type}}")

    when (val handler = handlers.find { it.willHandle(message.type) }) {
      null -> session.close(CloseCodes.cannotAccept())
      else -> handler.handle(SessionMessage(session, message, project, collection, getId(message, id)))
    }
  }

  /**
   * Gets the object id from the message if present, otherwise keep the id from the ws url
   */
  private fun getId(message: Message, id: String): String? {
    return when (message.type) {
      MessageType.HELLO -> id
      else -> {
        val map = message.content as Map<*, Any?>

        return map["_id"] as String? ?: id
      }
    }
  }

  @ConsumeEvent("db-data")
  fun onDataMessage(data: MongoOperation) {
    logger.trace("Receive message from the database")


    sessionStore.notifyClients(data)
  }

}
