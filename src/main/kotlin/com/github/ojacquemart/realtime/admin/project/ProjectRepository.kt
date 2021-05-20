package com.github.ojacquemart.realtime.admin.project

import io.quarkus.mongodb.panache.PanacheMongoRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProjectRepository : PanacheMongoRepository<ProjectEntity> {

  fun existsByName(name: String?): Boolean {
    return find("name", name).count() > 0
  }

  fun findByName(name: String?): ProjectEntity? {
    return findBy("name", name)
  }

  fun findByApikey(apikey: String?): ProjectEntity? {
    return findBy("apikey", apikey)
  }

  fun findBy(field: String, value: String?): ProjectEntity? {
    return find(field, value).firstResult()
  }

}
