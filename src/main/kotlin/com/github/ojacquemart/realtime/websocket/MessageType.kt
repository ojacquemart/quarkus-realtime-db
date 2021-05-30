package com.github.ojacquemart.realtime.websocket

enum class MessageType {
  HELLO,
  CREATE,
  READ,
  UPDATE,
  DELETE,
  ERROR,
  NOT_SUPPORTED
}
