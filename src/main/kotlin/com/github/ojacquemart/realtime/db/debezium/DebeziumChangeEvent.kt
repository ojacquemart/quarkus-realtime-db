package com.github.ojacquemart.realtime.db.debezium

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class DebeziumChangeEvent(
  val payload: Payload? = null
) {

  data class Payload(
    @JsonProperty("op")
    val op: String,
    val after: String? = null,
    val source: Source? = null
  )

  data class Source(
    val version: String? = null,
    val connector: String? = null,
    val name: String? = null,
    @JsonProperty("db")
    val db: String,
    @JsonProperty("collection")
    val collection: String,
  )

}
