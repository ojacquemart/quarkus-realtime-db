package com.github.ojacquemart.realtime.websocket

import javax.websocket.Session

data class SessionContext(
  val project: String?,
  val collection: String?,
  val id: String?
) {

  data class Holder(
    val session: Session,
    val data: SessionContext
  )

  fun matches(other: SessionContext): Boolean {
    val hasSameDbAndCollection = project == other.project && collection == other.collection
    if (id == "_all") {
      return hasSameDbAndCollection
    }

    return hasSameDbAndCollection && id == other.id
  }

}
