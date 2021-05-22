package com.github.ojacquemart.realtime.db.debezium

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer

class ChangeEventDeserializer : ObjectMapperDeserializer<ChangeEvent>(ChangeEvent::class.java)
