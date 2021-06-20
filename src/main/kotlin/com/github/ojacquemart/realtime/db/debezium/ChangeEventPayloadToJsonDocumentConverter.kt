package com.github.ojacquemart.realtime.db.debezium

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.ojacquemart.realtime.db.JsonDocument
import org.jboss.logging.Logger

class ChangeEventPayloadToJsonDocumentConverter {

  companion object {
    private val logger = Logger.getLogger(ChangeEventPayloadToJsonDocumentConverter::class.java)

    private val MAPPER = ObjectMapper()
    private val MAP_TYPE_REF: TypeReference<HashMap<String, Any>> = object : TypeReference<HashMap<String, Any>>() {}
  }

  /**
   * Transform an actual DebeziumChangeEvent to a JSON and replace any $oid occurrence by its String value if necessary.
   * Combine the filter, after and patch according to the change event.
   *
   * @see <a href="https://debezium.io/documentation/reference/connectors/mongodb.html#mongodb-change-events-value">Change Event Values</a>
   */
  fun convert(event: ChangeEvent?): JsonDocument {
    return convert(event?.payload?.filter) + convert(event?.payload?.after) + convert(event?.payload?.patch)
  }

  private fun convert(maybeData: String?): Map<String, Any> {
    val data = maybeData ?: return emptyMap()

    return try {
      val map: MutableMap<String, Any> = MAPPER.readValue(data, MAP_TYPE_REF)
      if (shouldParseObjectId(map)) {
        map["_id"] = parseObjectId(map)
      }

      map
    } catch (e: Exception) {
      logger.error("Error while parsing the payload data: '$data'")

      emptyMap()
    }
  }

  private fun shouldParseObjectId(map: Map<String, Any>): Boolean {
    val id = map["_id"]

    return id is Map<*, *>
  }

  private fun parseObjectId(map: Map<String, Any>): String {
    val id = getId(map)

    return (id?.get("\$oid") as String?) ?: ""
  }

  private fun getId(map: Map<String, Any>) = map["_id"] as Map<*, *>?

}
