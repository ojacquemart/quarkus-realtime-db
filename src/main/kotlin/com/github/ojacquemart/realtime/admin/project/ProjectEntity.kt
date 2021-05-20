package com.github.ojacquemart.realtime.admin.project

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.mongodb.panache.MongoEntity
import org.bson.types.ObjectId

@MongoEntity(collection = "rtdb-projects")
data class ProjectEntity(
  var id: ObjectId? = ObjectId(),
  var name: String? = null,
  var apikey: String? = null,
  var active: Boolean? = true,
  var collections: MutableList<String> = mutableListOf(),
  var createdAt: Long? = null
) {

  @JsonIgnore
  fun hasApikey(apikey: String?) = this.apikey == apikey

  fun addCollection(collectionName: String) {
    collections.add(collectionName)
  }

  @JsonIgnore
  fun hasCollection(name: String) = collections.contains(name)

}

