package com.github.ojacquemart.realtime.db.debezium

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class MongoObjectIdMapParserTest {

  private val parser = MongoObjectIdMapParser()

  @Test
  fun parse() {
    val map = parser.parse("""{"_id": "foobarqix","foo": "bar"}""")

    Assertions.assertEquals("foobarqix", map["_id"])
  }

  @Test
  fun parse_withObjectId() {
    val map = parser.parse("""{"_id": {"${'$'}oid": "609e98b06d42ec08cad4ed3a"},"foo": "bar"}""")

    Assertions.assertEquals("609e98b06d42ec08cad4ed3a", map["_id"])
  }

}