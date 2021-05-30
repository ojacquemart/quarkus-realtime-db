package com.github.ojacquemart.realtime.websocket

import com.github.ojacquemart.realtime.admin.project.ApikeyVerifier
import com.github.ojacquemart.realtime.admin.project.VerifyRequest
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class HelloMessageHandler(
  val apikeyVerifier: ApikeyVerifier,
  val sessionStore: SessionStore
) : MessageHandler {

  private val logger = Logger.getLogger(HelloMessageHandler::class.java)

  override fun willHandle(messageType: MessageType) = MessageType.HELLO == messageType

  override fun handle(sessionMessage: SessionMessage) {
    logger.trace("Handle hello message verification")

    val (session, message, project, collection, id) = sessionMessage
    if (message.content !is String) {
      return session.close(CloseCodes.unauthorized())
    }

    val isApikeyValid = apikeyVerifier.verify(
      VerifyRequest(apikey = message.content, project = project, collection = collection)
    )
    if (!isApikeyValid) {
      logger.warn("Invalid apikey provided")

      return session.close(CloseCodes.forbidden())
    }

    sessionStore.add(
      session,
      SessionContext(project = project, collection = collection, id = id)
    )
  }

}
