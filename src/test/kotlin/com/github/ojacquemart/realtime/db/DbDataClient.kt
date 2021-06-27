package com.github.ojacquemart.realtime.db

import io.quarkus.vertx.ConsumeEvent
import io.vertx.core.eventbus.EventBus
import java.util.concurrent.LinkedBlockingDeque
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class DbDataClient {

  @Inject
  lateinit var eventBus: EventBus

  @ConsumeEvent("db-data")
  fun onDataMessage(data: MongoOperation) {
    MESSAGES.add(data)
  }

  companion object {
    val MESSAGES = LinkedBlockingDeque<MongoOperation>()

    fun clear() {
      MESSAGES.clear()
    }
  }

}
