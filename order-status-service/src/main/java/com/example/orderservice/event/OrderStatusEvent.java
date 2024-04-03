package com.example.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class OrderStatusEvent {

    private String status;
    private Instant date;
}
