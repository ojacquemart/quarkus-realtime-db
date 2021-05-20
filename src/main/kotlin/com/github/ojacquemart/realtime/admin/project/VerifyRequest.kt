package com.github.ojacquemart.realtime.admin.project

data class VerifyRequest(
  val apikey: String,
  val project: String,
  val collection: String
)
