package com.github.ojacquemart.realtime.db

import com.mongodb.client.MongoClient
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions
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

  @Test
  fun persist() {
    genericMongoCollections.persist(
      PersistRequest(
        database = databaseName,
        collection = collectionName,
        document = mapOf(
          "_id" to "foo-bar-qix",
          "name" to "foobarqix"
        )
      )
    )

    val collection = mongoClient
      .getDatabase(databaseName)
      .getCollection(collectionName)
    val document = collection.find().first()

    Assertions.assertNotNull(document)
    Assertions.assertEquals("foo-bar-qix", document?.getString("_id"))
    Assertions.assertEquals("foobarqix", document?.getString("name"))
  }

}