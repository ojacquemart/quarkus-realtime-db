package com.github.ojacquemart.realtime.db.debezium

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer

class DebeziumChangeEventDeserializer : ObjectMapperDeserializer<DebeziumChangeEvent>(DebeziumChangeEvent::class.java)
