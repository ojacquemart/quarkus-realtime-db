package com.github.ojacquemart.realtime.websocket

import javax.websocket.CloseReason

enum class CloseCodes(private val code: Int) : CloseReason.CloseCode {
  UNAUTHORIZED(4_001),
  FORBIDDEN(4_003)
  ;

  override fun getCode(): Int {
    return this.code
  }

  companion object {
    fun cannotAccept() = CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, CloseReason.CloseCodes.CANNOT_ACCEPT.name)

    fun unauthorized() = CloseReason(UNAUTHORIZED, UNAUTHORIZED.name)
    fun forbidden() = CloseReason(FORBIDDEN, FORBIDDEN.name)
  }

}
