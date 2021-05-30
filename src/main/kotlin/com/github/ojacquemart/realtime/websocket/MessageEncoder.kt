package com.github.ojacquemart.realtime.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import javax.websocket.Encoder
import javax.websocket.EndpointConfig

class MessageEncoder : Encoder.Text<Message> {

  companion object {
    private val MAPPER = ObjectMapper()
  }

  override fun init(config: EndpointConfig?) {
  }

  override fun destroy() {
  }

  override fun encode(message: Message): String {
    return MAPPER.writeValueAsString(message)
  }

}