package com.github.ojacquemart.realtime.admin.project

import org.bson.types.ObjectId
import org.jboss.logging.Logger
import java.sql.Timestamp
import java.time.Instant
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.BadRequestException

@ApplicationScoped
class ProjectService(
  val projectRepository: ProjectRepository
) {

  private val logger = Logger.getLogger(ProjectService::class.java)

  fun create(payload: NewProjectRequest): ApikeyResponse {
    logger.debug("Try to persist project '${payload.name}'")

    throwExceptionIfNameExists(payload)

    return doPersist(payload)
  }

  private fun doPersist(payload: NewProjectRequest): ApikeyResponse {
    val now = Timestamp.from(Instant.now())
    val project = ProjectEntity(
      id = ObjectId(),
      name = payload.name,
      apikey = UUID.randomUUID().toString(),
      createdAt = now.time
    )
    projectRepository.persist(project)

    return ApikeyResponse(apikey = project.apikey)
  }

  private fun throwExceptionIfNameExists(payload: NewProjectRequest) {
    if (projectRepository.existsByName(payload.name)) {
      throw BadRequestException("Project with name '${payload.name} already exists'")
    }
  }

  fun createCollection(projectName: String, payload: NewCollectionRequest) {
    val collectionName = payload.name.trim()
    logger.debug("Attach collection $collectionName to project $projectName")

    val project = projectRepository.findByName(projectName)
      ?: throw BadRequestException("Project '$projectName' not found")

    if (project.hasCollection(collectionName)) {
      throw BadRequestException("Project '$projectName' already contains collection '$collectionName'")
    }

    project.addCollection(collectionName)
    projectRepository.update(project)
  }

}
