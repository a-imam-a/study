package com.example.orderservice.web.controller;

import com.example.orderservice.event.OrderEvent;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.model.Order;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    @Value("${app.kafka.kafkaOrderEventTopic}")
    private String topicName;

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody Order order) {
        orderService.send(topicName, orderMapper.orderToOrderEvent(order));

        return ResponseEntity.ok("Order sent to Kafka");
    }
}
