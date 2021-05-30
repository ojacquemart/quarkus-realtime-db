package com.github.ojacquemart.realtime.websocket

import com.fasterxml.jackson.annotation.JsonProperty

data class Message(
  @JsonProperty("type")
  val type: MessageType,
  @JsonProperty("content")
  val content: Any?
) {

  companion object {
    private val TYPES = mapOf(
      "c" to MessageType.CREATE,
      "r" to MessageType.READ,
      MessageType.READ.name to MessageType.READ,
      "u" to MessageType.UPDATE,
      "d" to MessageType.DELETE,
      MessageType.ERROR.name to MessageType.ERROR,
    )

    fun resolveType(type: String?) = TYPES[type] ?: MessageType.NOT_SUPPORTED
  }

}
