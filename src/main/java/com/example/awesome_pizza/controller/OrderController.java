package com.example.awesome_pizza.controller;

import com.example.awesome_pizza.data.OrderDto;
import com.example.awesome_pizza.enumz.OrderStatus;
import com.example.awesome_pizza.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @GetMapping
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderCode}")
    public OrderDto getOrderById(@PathVariable String orderCode) {
        return orderService.getOrderByOrderCode(orderCode);
    }

    @PutMapping("/{orderCode}/status/{status}")
    public OrderDto updateOrderStatus(@PathVariable String orderCode, @PathVariable OrderStatus status) {
        return orderService.updateOrderStatus(orderCode, status);
    }

    @PutMapping("/{orderCode}/take")
    public OrderDto takeOrder(@PathVariable String orderCode) {
        return orderService.takeOrder(orderCode);
    }

    @PutMapping("/{orderCode}/complete")
    public OrderDto completeOrder(@PathVariable String orderCode) {
        return orderService.completeOrder(orderCode);
    }

    @PutMapping("/{orderCode}/cancel")
    public OrderDto cancelOrder(@PathVariable String orderCode) {
        return orderService.cancelOrder(orderCode);
    }
}
