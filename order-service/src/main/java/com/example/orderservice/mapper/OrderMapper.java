package com.example.orderservice.mapper;

import com.example.orderservice.event.OrderEvent;
import com.example.orderservice.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    OrderEvent orderToOrderEvent(Order order);

}
