package com.github.ojacquemart.realtime.websocket

import com.github.ojacquemart.realtime.admin.project.ProjectEntity
import com.github.ojacquemart.realtime.admin.project.ProjectRepository
import com.github.ojacquemart.realtime.RealtimeDbTestLifeCycleManager
import com.mongodb.BasicDBObject
import com.mongodb.client.MongoClient
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.common.http.TestHTTPResource
import io.quarkus.test.junit.QuarkusTest
import org.awaitility.Awaitility.await
import org.awaitility.kotlin.untilNotNull
import org.awaitility.kotlin.untilNull
import org.bson.Document
import org.jboss.logging.Logger
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.URI
import java.util.concurrent.LinkedBlockingDeque
import javax.inject.Inject
import javax.websocket.*

@QuarkusTestResource(RealtimeDbTestLifeCycleManager::class)
@QuarkusTest
class ProjectCollectionSocketTest {

  @TestHTTPResource("/$FOOBARQIX_PROJECT/$FOOBARQIX_COLLECTION/_all")
  lateinit var uri: URI

  @Inject
  lateinit var mongoClient: MongoClient

  @Inject
  lateinit var projectRepository: ProjectRepository

  @Inject
  lateinit var sessionStore: SessionStore

  @BeforeEach
  fun setUp() {
    sessionStore.clear()

    projectRepository.deleteAll()
    projectRepository.persist(
      ProjectEntity(
        name = FOOBARQIX_PROJECT,
        apikey = FOOBARQIX_APIKEY,
        collections = mutableListOf(FOOBARQIX_COLLECTION)
      )
    )
  }

  @Test
  @Throws(Exception::class)
  fun `should handle a valid sequence of messages`() {
    val session = getSession()
    session.basicRemote.sendText("""{"type": "HELLO", "content": "$FOOBARQIX_APIKEY"}""")

    val context = SessionContext(FOOBARQIX_PROJECT, FOOBARQIX_COLLECTION, "_all")
    await().until { sessionStore.findAllSessions(context).size == 1 }

    session.basicRemote.sendText("""{"type": "CREATE", "content": {"_id": "foo", "name": "bar"}}""")
    assertDocumentName("bar")

    session.basicRemote.sendText("""{"type": "UPDATE", "content": {"_id": "foo", "name": "foobarqix"}}""")
    assertDocumentName("foobarqix")

    session.basicRemote.sendText("""{"type": "READ", "content": {"_id": "foo"}}""")
    assertMessageReceived(Message(type = MessageType.READ, content = mapOf("_id" to "foo", "name" to "foobarqix")))

    session.basicRemote.sendText("""{"type": "DELETE", "content": {"_id": "foo"}}""")
    assertNullDocument()
  }

  @Test
  fun `should block a hello message with an unknown apikey`() {
    val session = getSession()
    session.basicRemote.sendText("""{"type": "HELLO", "content": "foobarqix"}""")

    await().until { CloseCodes.FORBIDDEN.code == CLOSE_REASONS.poll() }
  }

  @Test
  fun `should block a hello message with an invalid apikey`() {
    val session = getSession()
    session.basicRemote.sendText("""{"type": "HELLO", "content": 1}""")

    await().until { CloseCodes.UNAUTHORIZED.code == CLOSE_REASONS.poll() }
  }

  @Test
  fun `should block a message with no initial hello message`() {
    val session = getSession()
    session.basicRemote.sendText("""{"type": "CREATE", "content": {"_id": "foo", "name": "bar"}}""")

    await().until { CloseCodes.UNAUTHORIZED.code == CLOSE_REASONS.poll() }
  }

  @Test
  fun `should be notified on type 'CREATE' when sending twice the same message`() {
    val session = getSession()

    session.basicRemote.sendText("""{"type": "HELLO", "content": "$FOOBARQIX_APIKEY"}""")
    session.basicRemote.sendText("""{"type": "CREATE", "content": {"_id": "foo", "name": "bar"}}""")
    session.basicRemote.sendText("""{"type": "CREATE", "content": {"_id": "foo", "name": "bar"}}""")

    assertMessageReceived(
      Message(
        type = MessageType.ERROR,
        content = mapOf(
          "reason" to "DUPLICATE",
          "source" to mapOf("_id" to "foo", "name" to "bar")
        )
      )
    )
  }

  private fun assertMessageReceived(expected: Message) {
    await().until { expected == MESSAGES.poll() }
  }

  private fun assertDocumentName(expected: String) {
    val actual = await().untilNotNull { findDocument() }
    Assertions.assertEquals(expected, actual["name"])
  }

  private fun assertNullDocument() {
    await().untilNull { findDocument() }
  }

  private fun getSession() = ContainerProvider
    .getWebSocketContainer()
    .connectToServer(Client::class.java, uri)

  private fun findDocument(): Document? {
    val query = BasicDBObject()
    query["_id"] = "foo"

    return mongoClient.getDatabase(FOOBARQIX_PROJECT).getCollection(FOOBARQIX_COLLECTION).find(query).first()
  }

  @ClientEndpoint(
    encoders = [MessageEncoder::class],
    decoders = [MessageDecoder::class]
  )
  class Client {

    private val logger = Logger.getLogger(Client::class.java)

    @OnOpen
    fun open(session: Session) {
    }

    @OnClose
    fun close(session: Session, reason: CloseReason) {
      logger.debug("Close reason: $reason")
      CLOSE_REASONS.add(reason.closeCode.code)
    }

    @OnMessage
    fun message(message: Message) {
      logger.debug("Message received: $message")
      MESSAGES.add(message)
    }

    @OnError
    fun error(session: Session, reason: Throwable) {
    }
  }

  companion object {
    private val CLOSE_REASONS = LinkedBlockingDeque<Int>()
    private val MESSAGES = LinkedBlockingDeque<Message>()

    private const val FOOBARQIX_PROJECT = "foobarqix"
    private const val FOOBARQIX_APIKEY = "apikey-foobarqix"
    private const val FOOBARQIX_COLLECTION = "demo"
  }

}
