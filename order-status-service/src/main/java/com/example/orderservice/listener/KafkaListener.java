package com.example.orderservice.listener;

import com.example.orderservice.event.OrderEvent;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaListener {

    private final OrderService orderService;

    @org.springframework.kafka.annotation.KafkaListener(topics = "${app.kafka.kafkaOrderEventTopic}",
    groupId = "${app.kafka.kafkaOrderGroupId}",
    containerFactory = "kafkaOrderEventConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload OrderEvent orderEvent,
                       @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {

        log.info("Received message: {}", orderEvent);
        log.info("Key: {}; Partition: {}, Topic: {}; Timestamp: {}", key, partition, topic, timestamp);

        orderService.handleOrderEvent(orderEvent);

    }
}
