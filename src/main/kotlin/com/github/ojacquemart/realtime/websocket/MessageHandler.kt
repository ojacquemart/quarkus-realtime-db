package com.github.ojacquemart.realtime.websocket

interface MessageHandler {
  fun willHandle(messageType: MessageType): Boolean
  fun handle(sessionMessage: SessionMessage): Unit
}
