package com.github.ojacquemart.realtime.admin

import com.github.ojacquemart.realtime.admin.project.ProjectEntity
import com.github.ojacquemart.realtime.admin.project.ProjectRepository
import com.github.ojacquemart.realtime.db.MongoTestLifeCycleManager
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import javax.inject.Inject

@QuarkusTest
@QuarkusTestResource(MongoTestLifeCycleManager::class)
internal class AdminResourceTest {

  @Inject
  lateinit var projectRepository: ProjectRepository

  /**
   * This test injects by default a project with name `big-foobarqix` and collection `baz`
   */
  @BeforeEach
  fun setUp() {
    projectRepository.deleteAll()
    projectRepository.persist(
      ProjectEntity(
        name = "big-foobarqix",
        apikey = "???",
        collections = mutableListOf("baz"),
        createdAt = 123456L
      )
    )
  }

  @Test
  fun `should create a project`() {
    given()
      .contentType("application/json")
      .body("""{"name": "foobarqix"}""")
      .`when`()
      .post("/admin/api/projects")
      .then()
      .statusCode(200)
      .body("apikey", notNullValue())

    val project = projectRepository.findByName("foobarqix")
    Assertions.assertNotNull(project)
  }

  @ParameterizedTest
  @ValueSource(
    strings = [
      "mongodblimitsdbnameto64charsaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
      "foo/ bar qix ??? *"
    ]
  )
  fun `should not create a project with an invalid name`(name: String) {
    given()
      .contentType("application/json")
      .body("""{"name": "$name"}""")
      .`when`()
      .post("/admin/api/projects")
      .then()
      .statusCode(400)
  }


  @Test
  fun `should not create a project with an already existing name`() {
    given()
      .contentType("application/json")
      .body("""{"name": "big-foobarqix"}""")
      .`when`()
      .post("/admin/api/projects")
      .then()
      .statusCode(400)
  }

  @Test
  fun `should get the projects names`() {
    projectRepository.persist(
      ProjectEntity(
        name = "abc",
        createdAt = 456L
      )
    )

    given()
      .contentType("application/json")
      .`when`()
      .get("/admin/api/projects")
      .then()
      .statusCode(200)
      .body(`is`("""["abc","big-foobarqix"]"""))
  }

  @Test
  fun `should create a collection`() {
    given()
      .contentType("application/json")
      .body("""{"name": "bar"}""")
      .`when`()
      .post("/admin/api/projects/big-foobarqix/collections")
      .then()
      .statusCode(204)
  }

  @ParameterizedTest
  @ValueSource(
    strings = [
      "\$ foobar",
      "system.foo"
    ]
  )
  fun `should not create a collection with an invalid name`(name: String) {
    given()
      .contentType("application/json")
      .body("""{"name": "$name"}""")
      .`when`()
      .post("/admin/api/projects/big-foobarqix/collections")
      .then()
      .statusCode(400)
  }

  @Test
  fun `should not create a collection with an already existing name`() {
    given()
      .contentType("application/json")
      .body("""{"name": "baz"}""")
      .`when`()
      .post("/admin/api/projects/big-foobarqix/collections")
      .then()
      .statusCode(400)
  }

}
