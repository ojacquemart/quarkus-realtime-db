package com.github.ojacquemart.realtime.db

import com.github.ojacquemart.realtime.websocket.MessageType
import com.mongodb.BasicDBObject
import com.mongodb.MongoWriteException
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.ReplaceOptions
import io.quarkus.vertx.ConsumeEvent
import io.vertx.core.eventbus.EventBus
import org.bson.BsonDocument
import org.bson.BsonDocumentWriter
import org.bson.BsonValue
import org.bson.codecs.EncoderContext
import org.bson.types.ObjectId
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.BadRequestException
import javax.ws.rs.InternalServerErrorException

@ApplicationScoped
class GenericMongoCollections(
  val eventBus: EventBus,
  val mongoClient: MongoClient,
) {

  private val logger = Logger.getLogger(GenericMongoCollections::class.java)

  @ConsumeEvent("ws-data")
  fun onDataMessage(operation: MongoOperation) {
    when (operation.type) {
      "CREATE" -> persist(operation)
      "READ" -> read(operation)
      "UPDATE" -> persistOrUpdate(operation)
      "DELETE" -> delete(operation)
    }
  }

  fun persist(operation: MongoOperation) {
    val collection = getMongoCollection(operation)

    persist(collection, operation)
  }

  fun persistOrUpdate(operation: MongoOperation) {
    val data = operation.data ?: throw BadRequestException()
    val collection = getMongoCollection(operation)
    val document = getBsonDocument(collection, data)
    val id = document["_id"] ?: return persist(collection, operation)

    update(collection, id, operation)
  }

  private fun persist(
    collection: MongoCollection<JsonDocument>,
    operation: MongoOperation
  ) {
    logOperation(operation)

    val data = operation.data ?: throw BadRequestException()

    try {
      collection.insertOne(data)
    } catch (e: Exception) {
      when (e) {
        is MongoWriteException -> {
          logger.error("Error while writing document {errorCode: ${e.code}}")

          when (e.code) {
            11_000 -> publishError(operation, reason = "DUPLICATE")
            else -> publishError(operation)
          }
        }
        else -> {
          logger.error("Error while writing document", e)

          publishError(operation)
        }
      }
    }
  }

  private fun update(
    collection: MongoCollection<JsonDocument>,
    id: BsonValue,
    operation: MongoOperation
  ) {
    logOperation(operation)

    val data = operation.data ?: throw BadRequestException()

    try {
      collection.replaceOne(
        eq("_id", MongoOperation.id(id.asString().value)),
        data.filterKeys { it != "_id" },
        ReplaceOptions().upsert(true)
      )
    } catch (e: Exception) {
      logger.error("Error while updating {id: $id}", e)

      publishError(operation)
    }
  }

  fun read(operation: MongoOperation) {
    logOperation(operation)

    val data = operation.data ?: throw BadRequestException()
    val id = (data["_id"] as String?) ?: throw BadRequestException()
    val collection = getMongoCollection(operation)

    try {
      val content = when (id) {
        "_all" -> readAll(collection)
        else -> findOne(id, collection)
      }

      eventBus.publish("db-data", operation.copy(data = content))
    } catch (e: Exception) {
      logger.error("Error while reading data", e)

      publishError(operation)
    }
  }

  private fun readAll(collection: MongoCollection<JsonDocument>): JsonDocument {
    logger.trace("Read all collection items")

    val documents = collection.find(BasicDBObject()).toList()
    val items = documents
      .map {
        when (it["_id"] is ObjectId) {
          true -> stringifyObjectId(it)
          else -> it
        }
      }

    return mapOf(
      "_id" to "_all",
      "items" to items
    )
  }

  private fun stringifyObjectId(it: JsonDocument): MutableMap<Any?, Any?> {
    val map = it.toMutableMap()
    val id = it["_id"] as ObjectId
    map["_id"] = id.toString()

    return map
  }

  private fun findOne(
    id: String,
    collection: MongoCollection<JsonDocument>
  ) = collection.find(BasicDBObject(mapOf("_id" to id))).first()

  fun delete(operation: MongoOperation) {
    logger.trace("Delete operation {id: ${operation.id}}")

    val collectionName = operation.collection ?: throw BadRequestException()
    val collection = getMongoDatabase(operation).getCollection(collectionName)

    try {
      collection.deleteOne(MongoOperation.query(operation.id))
    } catch (e: Exception) {
      logger.error("Error while deleting {id: ${operation.id}", e)

      publishOperation(operation)
    }
  }

  private fun publishError(operation: MongoOperation, reason: String = "UNKNOWN") {
    publishOperation(
      operation.copy(
        type = MessageType.ERROR.name,
        data = mapOf("reason" to reason, "source" to operation.data)
      )
    )
  }

  private fun publishOperation(operation: MongoOperation) {
    eventBus.publish("db-data", operation)
  }

  private fun getMongoCollection(operation: MongoOperation): MongoCollection<JsonDocument> {
    val db = this.getMongoDatabase(operation)
    val collection = operation.collection ?: throw BadRequestException()

    return db.getCollection(collection, operation.data?.javaClass)
  }

  private fun getMongoDatabase(operation: MongoOperation): MongoDatabase {
    val db = operation.db ?: throw BadRequestException()

    return mongoClient.getDatabase(db) ?: throw InternalServerErrorException()
  }

  private fun getBsonDocument(collection: MongoCollection<JsonDocument>, entity: JsonDocument): BsonDocument {
    val document = BsonDocument()
    val codec = collection.codecRegistry[entity.javaClass]
    codec.encode(BsonDocumentWriter(document), entity, EncoderContext.builder().build())

    return document
  }

  private fun logOperation(operation: MongoOperation) {
    logger.trace("Database operation: $operation")
  }

}
