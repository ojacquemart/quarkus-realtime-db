package com.github.ojacquemart.realtime.websocket

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class MessageEncoderTest {

  private val encoder = MessageEncoder()

  @Test
  fun encode() {
    val json = encoder.encode(
      Message(type = MessageType.CREATE, content = """{"foo": "bar"}""")
    )

    Assertions.assertEquals("""{"type":"CREATE","content":"{\"foo\": \"bar\"}"}""", json)
  }

}