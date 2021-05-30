package com.github.ojacquemart.realtime.db

import com.mongodb.BasicDBObject
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.bson.Document
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject

@QuarkusTest
@QuarkusTestResource(MongoTestLifeCycleManager::class)
internal class GenericMongoCollectionsTest {

  val databaseName = "realtime"
  val collectionName = "coll-foobarqix"

  @Inject
  lateinit var mongoClient: MongoClient

  @Inject
  lateinit var genericMongoCollections: GenericMongoCollections

  @BeforeEach
  fun setUp() {
    val collection = getCollection()
    collection.deleteMany(BasicDBObject())
    genericMongoCollections.persist(
      MongoOperation(
        db = databaseName, collection = collectionName, type = "CREATE",
        data = mapOf("_id" to "foo", "name" to "bar")
      )
    )
  }

  @Test
  fun `should persist a document`() {
    genericMongoCollections.persist(
      MongoOperation(
        type = "CREATE",
        db = databaseName, collection = collectionName,
        data = mapOf(
          "_id" to "foo-bar-qix",
          "name" to "foobarqix"
        ),
      )
    )

    val query = BasicDBObject()
    query["_id"] = "foo-bar-qix"

    val collection = getCollection()
    val document = collection.find(query).first()

    Assertions.assertNotNull(document)
    Assertions.assertEquals("foo-bar-qix", document?.getString("_id"))
    Assertions.assertEquals("foobarqix", document?.getString("name"))
  }

  @Test
  fun `should delete a document by its id`() {
    genericMongoCollections.delete(
      MongoOperation(
        type = "DELETE",
        db = databaseName, collection = collectionName, id = "foo",
      )
    )

    val collection = getCollection()
    Assertions.assertEquals(0, collection.countDocuments())
  }

  private fun getCollection(): MongoCollection<Document> {
    return mongoClient
      .getDatabase(databaseName)
      .getCollection(collectionName)
  }

}
