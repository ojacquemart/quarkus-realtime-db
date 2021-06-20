package com.github.ojacquemart.realtime.websocket

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@QuarkusTest
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
