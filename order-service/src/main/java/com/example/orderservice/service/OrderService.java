package com.example.orderservice.service;

import com.example.orderservice.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void send(String topicName, OrderEvent orderEvent) {
        kafkaTemplate.send(topicName, orderEvent);
    }
}
