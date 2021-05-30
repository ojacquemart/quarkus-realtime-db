package com.github.ojacquemart.realtime.websocket

import javax.websocket.Session

data class SessionMessage(
  val session: Session,
  val message: Message,
  val project: String,
  val collection: String,
  val id: String?
)
