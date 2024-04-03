package com.example.orderservice.service;

import com.example.orderservice.event.OrderEvent;
import com.example.orderservice.event.OrderStatusEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Value("${app.kafka.kafkaOrderStatusEventTopic}")
    private String topicName;

    private final KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;

    public void handleOrderEvent(OrderEvent orderEvent) {
        kafkaTemplate.send(topicName, new OrderStatusEvent("PROCESS", Instant.now()));
    }
}
