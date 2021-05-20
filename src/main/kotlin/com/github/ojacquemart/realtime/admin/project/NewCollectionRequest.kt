package com.github.ojacquemart.realtime.admin.project

import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class NewCollectionRequest(
  @field:Size(min = 1, max = 256)
  @field:Pattern(regexp = """^(?!system\.)[^${'$'}]+${'$'}""")
  val name: String = ""
)
