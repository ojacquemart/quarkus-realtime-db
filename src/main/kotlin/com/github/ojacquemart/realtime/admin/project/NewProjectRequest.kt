package com.github.ojacquemart.realtime.admin.project

import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class NewProjectRequest(
  @field:Size(min = 1, max = 64)
  @field:Pattern(regexp = """^[^/\\. "${'$'}*<>:|?]+${'$'}""")
  val name: String = ""
)
