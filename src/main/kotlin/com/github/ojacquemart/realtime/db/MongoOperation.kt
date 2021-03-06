package com.github.ojacquemart.realtime.db

import com.github.ojacquemart.realtime.db.debezium.ChangeEvent
import com.github.ojacquemart.realtime.db.debezium.ChangeEventPayloadToJsonDocumentConverter
import com.mongodb.BasicDBObject
import org.bson.types.ObjectId

data class MongoOperation(
  val type: String,
  val db: String?,
  val collection: String?,
  val data: JsonDocument? = null,
  val id: String? = null,
) {

  companion object {
    private val JSON_DOCUMENT_CONVERTER = ChangeEventPayloadToJsonDocumentConverter()

    fun query(id: String?) = BasicDBObject(mapOf("_id" to id(id)))

    fun id(id: String?) = when (ObjectId.isValid(id)) {
      true -> ObjectId(id)
      else -> id
    }

    fun from(changeEvent: ChangeEvent?): MongoOperation? {
      val payload = changeEvent?.payload ?: return null
      val data = JSON_DOCUMENT_CONVERTER.convert(changeEvent)

      return MongoOperation(
        type = payload.op,
        db = payload.source?.db,
        collection = payload.source?.collection,
        data = data,
        id = data["_id"] as String?,
      )
    }

  }

}
