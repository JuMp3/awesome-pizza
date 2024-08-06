package com.example.awesome_pizza.util;

import com.example.awesome_pizza.data.OrderDto;
import com.example.awesome_pizza.entity.PizzaOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MapperUtil {

    public OrderDto fromEntityToDto(PizzaOrder order) {
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(order, orderDto);
        return orderDto;
    }

    public PizzaOrder fromDtoToEntity(OrderDto orderDto) {
        PizzaOrder order = new PizzaOrder();
        BeanUtils.copyProperties(orderDto, order);
        return order;
    }

    public List<OrderDto> fromEntityListToDtoList(List<PizzaOrder> orders) {
        return orders.stream()
                .map(this::fromEntityToDto)
                .toList();
    }
}
