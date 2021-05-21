package com.github.ojacquemart.realtime.db

import com.github.ojacquemart.realtime.db.debezium.DebeziumData
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.ReplaceOptions
import io.quarkus.vertx.ConsumeEvent
import org.bson.BsonDocument
import org.bson.BsonDocumentWriter
import org.bson.BsonValue
import org.bson.codecs.EncoderContext
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.InternalServerErrorException

@ApplicationScoped
class GenericMongoCollections(
  val mongoClient: MongoClient,
) {

  private val logger = Logger.getLogger(GenericMongoCollections::class.java)

  @ConsumeEvent("ws-data")
  fun onDataMessage(data: DebeziumData) {
    persistOrUpdate(
      PersistRequest(
        database = data.db ?: "",
        collection = data.collection ?: "",
        document = data.data
      )
    )
  }


  fun persist(request: PersistRequest): BsonValue? {
    val collection = getMongoCollection(request)

    return persist(collection, request)
  }

  fun persistOrUpdate(request: PersistRequest): BsonValue? {
    val collection = getMongoCollection(request)
    val document = getBsonDocument(collection, request.document)
    val id = document["_id"] ?: return persist(collection, request)

    return update(collection, id, request)
  }

  private fun persist(
    collection: MongoCollection<JsonDocument>,
    request: PersistRequest
  ): BsonValue? {
    logRequest(request)

    return collection.insertOne(request.document).insertedId
  }

  private fun update(
    collection: MongoCollection<JsonDocument>,
    id: BsonValue,
    request: PersistRequest
  ): BsonValue? {
    logRequest(request)

    val query = BsonDocument().append("_id", id)
    val update = collection.replaceOne(query, request.document, ReplaceOptions().upsert(true))

    return update.upsertedId
  }

  private fun getMongoCollection(request: PersistRequest): MongoCollection<JsonDocument> {
    val db = this.getMongoDatabase(request)

    return db.getCollection(request.collection, request.document.javaClass) ?: throw InternalServerErrorException()
  }

  private fun getMongoDatabase(request: PersistRequest): MongoDatabase {
    return mongoClient.getDatabase(request.database) ?: throw InternalServerErrorException()
  }

  private fun getBsonDocument(collection: MongoCollection<JsonDocument>, entity: JsonDocument): BsonDocument {
    val document = BsonDocument()
    val codec = collection.codecRegistry[entity.javaClass]
    codec.encode(BsonDocumentWriter(document), entity, EncoderContext.builder().build())

    return document
  }

  private fun logRequest(request: PersistRequest) {
    logger.trace("Persist data {db=${request.database}, coll=${request.collection}, data=${request.document}}")
  }

}
