package com.github.ojacquemart.realtime.websocket

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class MessageDecoderTest {

  private val decoder = MessageDecoder()

  @Test
  fun `should define if the string should be decoded`() {
    Assertions.assertFalse(decoder.willDecode("{}"))
    Assertions.assertFalse(decoder.willDecode("""{"foo": "bar"}"""))
    Assertions.assertFalse(decoder.willDecode("""{"type": "FOO", "content": "???"}"""))
    Assertions.assertTrue(decoder.willDecode("""{"type": "HELLO", "content": "foobarqix"}"""))
  }

  @Test
  fun `should decode a message`() {
    val message = decoder.decode("""{"type": "HELLO", "content": "foobarqix"}""")

    Assertions.assertNotNull(message)
    Assertions.assertEquals(MessageType.HELLO, message.type)
    Assertions.assertEquals("foobarqix", message.content)
  }

}
