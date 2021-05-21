package com.github.ojacquemart.realtime.db.debezium

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.ojacquemart.realtime.db.JsonDocument


class MongoObjectIdMapParser {

  companion object {
    private val MAPPER = ObjectMapper()
  }

  /**
   * Transform an actual String to a JSON and replace any $oid occurrence by its String value if necessary
   */
  fun parse(data: String?): JsonDocument {
    val mapTypeReference: TypeReference<HashMap<String, Any>> = object : TypeReference<HashMap<String, Any>>() {}
    val map: MutableMap<String, Any> = MAPPER.readValue(data, mapTypeReference)
    if (shouldParseObjectId(map)) {
      map["_id"] = parseObjectId(map)
    }

    return map
  }

  private fun shouldParseObjectId(map: Map<String, Any>): Boolean {
    val id = map["_id"]

    return id is Map<*, *>
  }

  private fun parseObjectId(map: Map<String, Any>): String {
    val id = getId(map)

    return id?.get("\$oid") as String
  }

  private fun getId(map: Map<String, Any>) = map["_id"] as Map<*, *>?

}