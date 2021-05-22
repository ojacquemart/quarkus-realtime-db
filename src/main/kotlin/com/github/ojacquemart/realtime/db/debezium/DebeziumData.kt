package com.github.ojacquemart.realtime.db.debezium

import com.github.ojacquemart.realtime.db.JsonDocument

class DebeziumData(
  val data: JsonDocument,
  val collection: String?,
  val db: String?,
  val id: String?,
  val type: String
) {

  companion object {
    private val JSON_DOCUMENT_CONVERTER = ChangeEventPayloadToJsonDocumentConverter()

    fun from(changeEvent: ChangeEvent?): DebeziumData? {
      val payload = changeEvent?.payload ?: return null
      val data = JSON_DOCUMENT_CONVERTER.convert(changeEvent) ?: return null

      return DebeziumData(
        db = payload.source?.db,
        collection = payload.source?.collection,
        type = payload.op,
        data = data,
        id = data["_id"] as String?,
      )
    }

  }

}