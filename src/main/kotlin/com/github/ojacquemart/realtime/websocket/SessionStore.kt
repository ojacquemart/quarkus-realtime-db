package com.github.ojacquemart.realtime.websocket

import com.github.ojacquemart.realtime.db.MongoOperation
import org.jboss.logging.Logger
import java.util.concurrent.ConcurrentHashMap
import javax.enterprise.context.ApplicationScoped
import javax.websocket.Session

@ApplicationScoped
class SessionStore {

  private val logger = Logger.getLogger(SessionStore::class.java)

  val sessions = ConcurrentHashMap<String, SessionContext.Holder>()

  fun isPresent(session: Session): Boolean {
    val present = sessions.containsKey(session.id)
    logger.trace("Check session presence {id: ${session.id}, present: $present}")

    return present
  }

  fun add(session: Session, data: SessionContext) {
    val holder = SessionContext.Holder(session, data)
    logger.debug("Add new session {id: ${session.id}, context=${holder.data}}")

    sessions[session.id] = holder
  }

  fun remove(session: Session) {
    logger.debug("Remove session {id: ${session.id}")

    sessions.remove(session.id)
  }

  fun notifyClients(data: MongoOperation) {
    val context = SessionContext(project = data.db, collection = data.collection, id = data.id)
    val sessions = findAllSessions(context)

    if (sessions.isEmpty()) {
      return logger.error("No session found from context: $context")
    }

    notifyClients(sessions, data)
  }

  private fun notifyClients(sessions: List<Session>, data: MongoOperation) {
    logger.debug("Notify the clients")

    sessions.forEach {
      it.asyncRemote.sendObject(
        Message(
          type = Message.resolveType(data.type),
          content = data.data as Any?
        )
      ) { result ->
        result.exception?.let {
          logger.error("Unable to send message", result.exception)
        }
      }
    }
  }

  fun findAllSessions(context: SessionContext): List<Session> {
    logger.trace("Search sessions attached {context: $context}")

    return sessions
      .filter { it.value.data.matches(context) }
      .map { it.value.session }
  }

  fun clear() {
    logger.trace("Reset sessions")

    sessions.clear()
  }

}

