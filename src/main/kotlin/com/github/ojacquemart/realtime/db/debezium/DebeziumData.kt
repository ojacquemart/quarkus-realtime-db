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
    private val OBJECT_ID_PARSER = MongoObjectIdMapParser()

    fun from(changeEvent: DebeziumChangeEvent?): DebeziumData? {
      val payload = changeEvent?.payload ?: return null
      val after = payload.after ?: return null

      val data = OBJECT_ID_PARSER.parse(after)

      return DebeziumData(
        data = data,
        collection = payload.source?.collection,
        db = payload.source?.db,
        id = data["_id"] as String?,
        type = payload.op
      )
    }

  }

}