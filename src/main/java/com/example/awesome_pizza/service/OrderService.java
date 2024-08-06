package com.example.awesome_pizza.service;

import com.example.awesome_pizza.data.OrderDto;
import com.example.awesome_pizza.entity.PizzaOrder;
import com.example.awesome_pizza.enumz.OrderStatus;
import com.example.awesome_pizza.exceptions.GenericConflictException;
import com.example.awesome_pizza.exceptions.GenericNotFoundException;
import com.example.awesome_pizza.repository.OrderRepository;
import com.example.awesome_pizza.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MapperUtil mapperUtil;

    public OrderService(OrderRepository orderRepository, MapperUtil mapperUtil) {
        this.orderRepository = orderRepository;
        this.mapperUtil = mapperUtil;
    }

    public OrderDto createOrder(OrderDto order) {
        PizzaOrder newOrder = new PizzaOrder();
        newOrder.setPizzaType(order.getPizzaType());
        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setOrderCode(UUID.randomUUID().toString());
        return mapperUtil.fromEntityToDto(orderRepository.save(newOrder));
    }

    public List<OrderDto> getAllOrders() {
        return mapperUtil.fromEntityListToDtoList(orderRepository.findAll());
    }

    public OrderDto getOrderByOrderCode(String orderCode) {
        Optional<PizzaOrder> order = orderRepository.findByOrderCode(orderCode);
        if (order.isPresent()) {
            return mapperUtil.fromEntityToDto(order.get());
        }
        throw getNotFoundException(orderCode);
    }

    public OrderDto updateOrderStatus(String orderCode, OrderStatus status) {
        Optional<PizzaOrder> order = orderRepository.findByOrderCode(orderCode);
        if (order.isPresent()) {
            PizzaOrder updatedOrder = order.get();
            updatedOrder.setStatus(status);
            return mapperUtil.fromEntityToDto(orderRepository.save(updatedOrder));
        }
        throw getNotFoundException(orderCode);
    }

    public OrderDto takeOrder(String orderCode) {
        Optional<PizzaOrder> order = orderRepository.findByOrderCode(orderCode);
        if (order.isPresent()) {
            if (!OrderStatus.NEW.equals(order.get().getStatus())) {
                throw new GenericConflictException("PizzaOrder " + orderCode + " already taken");
            }
            PizzaOrder takenOrder = order.get();
            takenOrder.setStatus(OrderStatus.IN_PROGRESS);
            return mapperUtil.fromEntityToDto(orderRepository.save(takenOrder));
        }
        throw getNotFoundException(orderCode);
    }

    public OrderDto completeOrder(String orderCode) {
        Optional<PizzaOrder> order = orderRepository.findByOrderCode(orderCode);
        if (order.isPresent()) {
            if (!OrderStatus.IN_PROGRESS.equals(order.get().getStatus())) {
                throw new GenericConflictException("PizzaOrder " + orderCode + " not yet taken");
            }
            PizzaOrder completedOrder = order.get();
            completedOrder.setStatus(OrderStatus.DELIVERED);
            return mapperUtil.fromEntityToDto(orderRepository.save(completedOrder));
        }
        throw getNotFoundException(orderCode);
    }

    public OrderDto cancelOrder(String orderCode) {
        Optional<PizzaOrder> order = orderRepository.findByOrderCode(orderCode);
        if (order.isPresent()) {
            if (!OrderStatus.IN_PROGRESS.equals(order.get().getStatus())) {
                throw new GenericConflictException("PizzaOrder " + orderCode + " not yet taken");
            }
            PizzaOrder completedOrder = order.get();
            completedOrder.setStatus(OrderStatus.CANCELLED);
            return mapperUtil.fromEntityToDto(orderRepository.save(completedOrder));
        }
        throw getNotFoundException(orderCode);
    }

    private GenericNotFoundException getNotFoundException(String orderCode) {
        return new GenericNotFoundException("PizzaOrder " + orderCode + " not found");
    }
}
