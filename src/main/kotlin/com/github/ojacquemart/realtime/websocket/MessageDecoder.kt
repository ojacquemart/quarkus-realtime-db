package com.github.ojacquemart.realtime.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import javax.websocket.Decoder
import javax.websocket.EndpointConfig

class MessageDecoder : Decoder.Text<Message> {

  companion object {
    val MAPPER = ObjectMapper()
  }

  override fun init(config: EndpointConfig?) {
  }

  override fun destroy() {
  }

  override fun decode(data: String?): Message =
    MAPPER.readValue(data, Message::class.java)

  override fun willDecode(data: String?): Boolean {
    return try {
      val map = MAPPER.readValue(data, Map::class.java)

      isTypeSupported(map) && isContentPresent(map)
    } catch (e: Exception) {
      false
    }
  }

  private fun isTypeSupported(map: Map<*, *>): Boolean {
    return isTypePresent(map) && isTypeValid(map)
  }

  private fun isTypeValid(map: Map<*, *>): Boolean {
    return try {
      MessageType.valueOf(map["type"] as String)

      true
    } catch (e: Exception) {
      false
    }
  }

  private fun isTypePresent(map: Map<*, *>) =
    map.containsKey("type")

  private fun isContentPresent(map: Map<*, *>) =
    map.containsKey("content") && map["content"] != null
}
