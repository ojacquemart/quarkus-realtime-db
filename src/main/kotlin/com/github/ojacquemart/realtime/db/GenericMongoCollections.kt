package com.github.ojacquemart.realtime.db

import com.github.ojacquemart.realtime.websocket.MessageType
import com.mongodb.BasicDBObject
import com.mongodb.MongoWriteException
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.ReplaceOptions
import io.quarkus.vertx.ConsumeEvent
import io.vertx.core.eventbus.EventBus
import org.bson.BsonDocument
import org.bson.BsonDocumentWriter
import org.bson.BsonValue
import org.bson.codecs.EncoderContext
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
      logger.error("Error while persisting", e)

      when (e) {
        is MongoWriteException -> {
          when (e.code) {
            11_000 -> publishError(operation, reason = "DUPLICATE")
            else -> publishError(operation)
          }
        }
        else -> publishError(operation)
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
    val query = BsonDocument().append("_id", id)

    try {
      collection.replaceOne(query, data, ReplaceOptions().upsert(true))
    } catch (e: Exception) {
      logger.error("Error while updating {id: $id}", e)

      publishError(operation)
    }
  }

  private fun read(operation: MongoOperation) {
    logOperation(operation)

    val data = operation.data ?: throw BadRequestException()
    val collection = getMongoCollection(operation)
    val query = BasicDBObject(data)

    try {
      val document = collection.find(query).first()

      eventBus.publish("db-data", operation.copy(data = document))
    } catch (e: Exception) {
      logger.error("Error while reading {query: $query}", e)

      publishError(operation)
    }
  }

  fun delete(operation: MongoOperation) {
    logger.trace("Delete operation {id: ${operation.id}}")

    val collectionName = operation.collection ?: throw BadRequestException()
    val collection = getMongoDatabase(operation).getCollection(collectionName)

    val query = BasicDBObject()
    query["_id"] = operation.id

    try {
      collection.deleteOne(query)
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
