package com.github.ojacquemart.realtime.admin.project

import com.github.ojacquemart.realtime.db.MongoTestLifeCycleManager
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import javax.inject.Inject

@QuarkusTestResource(MongoTestLifeCycleManager::class)
@QuarkusTest
internal class ApikeyVerifierTest {

  @Inject
  lateinit var projectRepository: ProjectRepository

  @Inject
  lateinit var apikeyVerifier: ApikeyVerifier

  @BeforeEach
  fun setUp() {
    projectRepository.deleteAll()
    projectRepository.persist(
      ProjectEntity(
        name = "db-foo",
        apikey = "apikey-foo",
        collections = mutableListOf("tbl-foo", "tbl-bar")
      )
    )
  }

  @Test
  fun `should verify valid data`() {
    val isValid = apikeyVerifier.verify(
      VerifyRequest(
        apikey = "apikey-foo",
        project = "db-foo",
        collection = "tbl-foo"
      )
    )

    Assertions.assertTrue(isValid)
  }

  @ParameterizedTest
  @CsvSource(
    value = [
      // invalid apikey
      "apikey-bar,db-foo,tbl-foo",
      // invalid collection
      "apikey-foo,db-foo,tbl-baz"
    ]
  )
  fun `should verify invalid data`(apikey: String, project: String, collection: String) {
    val isValid = apikeyVerifier.verify(
      VerifyRequest(
        apikey = apikey,
        project = project,
        collection = collection
      )
    )

    Assertions.assertFalse(isValid)
  }

}