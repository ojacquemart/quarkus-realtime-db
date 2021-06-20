package com.github.ojacquemart.realtime.db.debezium

import com.github.ojacquemart.realtime.db.DbDataClient
import com.github.ojacquemart.realtime.RealtimeDbTestLifeCycleManager
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.reactive.messaging.connectors.InMemoryConnector
import org.awaitility.Awaitility.await
import org.awaitility.kotlin.untilNotNull
import org.awaitility.kotlin.untilNull
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.enterprise.inject.Any
import javax.inject.Inject


@QuarkusTest
@QuarkusTestResource(RealtimeDbTestLifeCycleManager::class)
internal class ChangeEventConsumerTest {

  @Inject
  @field:Any
  lateinit var connector: InMemoryConnector

  @Test
  fun `should forward a message`() {
    val changeEvent = ChangeEvent(
      payload = ChangeEvent.Payload(
        op = "r",
        filter = """{"_id": "foo"}""",
        after = """{"_id": "foo", "name": "foobar"}""",
        source = ChangeEvent.Source(
          version = "1.0.0",
          connector = "foo-connector",
          db = "db-foo",
          name = "foo",
          collection = "foobarqix"
        )
      )
    )
    sendEvent(changeEvent)

    val operation = await().untilNotNull { DbDataClient.MESSAGES.poll() }
    Assertions.assertEquals("db-foo", operation.db)
    Assertions.assertEquals("foobarqix", operation.collection)
    Assertions.assertEquals("r", operation.type)
    Assertions.assertEquals("foo", operation.id)
    Assertions.assertEquals(mapOf("_id" to "foo", "name" to "foobar"), operation.data)
  }

  @Test
  fun `should forward a message with ObjectId`() {
    val changeEvent = ChangeEvent(
      payload = ChangeEvent.Payload(
        op = "c",
        after = """{"_id": {"${'$'}oid": "609e98b06d42ec08cad4ed3a"}, "foo": "bar"}"""
      )
    )
    sendEvent(changeEvent)

    val operation = await().untilNotNull { DbDataClient.MESSAGES.poll() }
    Assertions.assertEquals("c", operation.type)
    Assertions.assertEquals(mapOf("_id" to "609e98b06d42ec08cad4ed3a", "foo" to "bar"), operation.data)
    Assertions.assertEquals("609e98b06d42ec08cad4ed3a", operation.id)
  }

  @Test
  fun `should handle a message with an empty id map`() {
    val changeEvent = ChangeEvent(
      payload = ChangeEvent.Payload(
        op = "c",
        after = """{"_id": {"foo": "???"}, "foo": "bar"}"""
      )
    )
    sendEvent(changeEvent)

    val operation = await().untilNotNull { DbDataClient.MESSAGES.poll() }
    Assertions.assertEquals("c", operation.type)
    Assertions.assertEquals(mapOf("_id" to "", "foo" to "bar"), operation.data)
    Assertions.assertEquals("", operation.id)
  }

  @Test
  fun `should handle a message with an invalid json`() {
    val changeEvent = ChangeEvent(
      payload = ChangeEvent.Payload(
        op = "c",
        after = "{"
      )
    )
    sendEvent(changeEvent)

    val operation = await().untilNotNull { DbDataClient.MESSAGES.poll() }
    Assertions.assertEquals("c", operation.type)
    Assertions.assertTrue(operation.data?.isEmpty() ?: false)
    Assertions.assertNull(operation.id)
  }

  @Test
  fun `should not forward a null message`() {
    val changeEvent = ChangeEvent(
      payload = null
    )
    sendEvent(changeEvent)

    await().untilNull { DbDataClient.MESSAGES.poll() }
  }

  private fun sendEvent(changeEvent: ChangeEvent) {
    connector.source<ChangeEvent>("rtdb").send(changeEvent)
  }

}
