package com.github.ojacquemart.realtime.db.debezium

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ChangeEventPayloadToJsonDocumentConverterTest {

  private val converter = ChangeEventPayloadToJsonDocumentConverter()

  @Test
  fun `should convert a create event with a simple _id`() {
    val map = converter.convert(
      ChangeEvent(
        payload = ChangeEvent.Payload(
          op = "c",
          after = """{"_id": "foobarqix","foo": "bar"}"""
        )
      )
    )

    Assertions.assertEquals("foobarqix", map["_id"])
    Assertions.assertEquals("bar", map["foo"])
  }

  @Test
  fun `should convert a create event with a ObjectId structure`() {
    val map = converter.convert(
      ChangeEvent(
        payload = ChangeEvent.Payload(
          op = "c",
          after = """{"_id": {"${'$'}oid": "609e98b06d42ec08cad4ed3a"},"foo": "bar"}"""
        )
      )
    )

    Assertions.assertEquals("609e98b06d42ec08cad4ed3a", map["_id"])
  }

  @Test
  fun `should convert a delete event with a ObjectId structure`() {
    val map = converter.convert(
      ChangeEvent(
        payload = ChangeEvent.Payload(
          op = "d",
          filter = """{"_id": {"${'$'}oid": "609e98b06d42ec08cad4ed3a"}}"""
        )
      )
    )

    Assertions.assertEquals("609e98b06d42ec08cad4ed3a", map["_id"])
  }

  @Test
  fun `should convert a update event id with a ObjectId structure`() {
    val map = converter.convert(
      ChangeEvent(
        payload = ChangeEvent.Payload(
          op = "u",
          after = """{"foo": "bar"}""",
          filter = """{"_id": {"${'$'}oid": "609e98b06d42ec08cad4ed3a"}}"""
        )
      )
    )

    Assertions.assertEquals("609e98b06d42ec08cad4ed3a", map["_id"])
    Assertions.assertEquals("bar", map["foo"])
  }

}