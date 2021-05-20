package com.github.ojacquemart.realtime.admin.project

import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ApikeyVerifier(
  val projectRepository: ProjectRepository
) {

  private val logger = Logger.getLogger(ApikeyVerifier::class.java)

  fun verify(verifyRequest: VerifyRequest): Boolean {
    logger.debug("Verify apikey: $verifyRequest")

    val project = projectRepository.findByName(verifyRequest.project) ?: return false

    return project.hasApikey(verifyRequest.apikey) && project.hasCollection(verifyRequest.collection)
  }

}