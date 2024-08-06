package com.example.awesome_pizza;

import com.example.awesome_pizza.data.OrderDto;
import com.example.awesome_pizza.entity.PizzaOrder;
import com.example.awesome_pizza.enumz.OrderStatus;
import com.example.awesome_pizza.enumz.PizzaType;
import com.example.awesome_pizza.exceptions.GenericConflictException;
import com.example.awesome_pizza.exceptions.GenericNotFoundException;
import com.example.awesome_pizza.repository.OrderRepository;
import com.example.awesome_pizza.service.OrderService;
import com.example.awesome_pizza.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

class OrderServiceUnitTest {

    @Mock
    private OrderRepository orderRepository;

    @Spy
    private MapperUtil mapperUtil;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        doCallRealMethod().when(mapperUtil).fromEntityToDto(any(PizzaOrder.class));
        doCallRealMethod().when(mapperUtil).fromEntityListToDtoList(any());
    }

    @Test
    void testCreateOrder() {

        PizzaOrder order = new PizzaOrder();
        order.setPizzaType(PizzaType.MARGHERITA);
        order.setStatus(OrderStatus.NEW);
        order.setOrderCode("1248");

        when(orderRepository.save(any(PizzaOrder.class))).thenReturn(order);

        OrderDto createdOrder = orderService.createOrder(OrderDto.builder()
                .pizzaType(PizzaType.MARGHERITA)
                .build());

        assertNotNull(createdOrder);
        assertEquals(OrderStatus.NEW, createdOrder.getStatus());
        assertNotNull(createdOrder.getOrderCode());
    }

    @Test
    void testUpdateOrderStatus() {

        PizzaOrder order = new PizzaOrder();
        order.setId(1L);
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setOrderCode("1234");

        when(orderRepository.findByOrderCode(anyString())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(PizzaOrder.class))).thenReturn(order);

        OrderDto updatedOrder = orderService.updateOrderStatus("1234", OrderStatus.DELIVERED);

        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.DELIVERED, updatedOrder.getStatus());
    }

    @Test
    void testNotFoundOrder() {
        when(orderRepository.findByOrderCode(anyString())).thenReturn(Optional.empty());
        assertThrows(GenericNotFoundException.class, () -> orderService.takeOrder("12345"));
    }

    @Test
    void testConflictOrder() {

        PizzaOrder order = new PizzaOrder();
        order.setId(1L);
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setOrderCode("1234578");
        when(orderRepository.findByOrderCode(anyString())).thenReturn(Optional.of(order));

        assertThrows(GenericConflictException.class, () -> orderService.takeOrder("1234578"));
    }

    @Test
    void testTakeOrder() {

        PizzaOrder order = new PizzaOrder();
        order.setId(1L);
        order.setStatus(OrderStatus.NEW);
        order.setOrderCode("12345");

        when(orderRepository.findByOrderCode(anyString())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(PizzaOrder.class))).thenReturn(order);

        OrderDto takenOrder = orderService.takeOrder("12345");

        assertNotNull(takenOrder);
        assertEquals(OrderStatus.IN_PROGRESS, takenOrder.getStatus());
    }

    @Test
    void testCompleteOrder() {

        PizzaOrder order = new PizzaOrder();
        order.setId(1L);
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setOrderCode("134");

        when(orderRepository.findByOrderCode(anyString())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(PizzaOrder.class))).thenReturn(order);

        OrderDto completedOrder = orderService.completeOrder("134");

        assertNotNull(completedOrder);
        assertEquals(OrderStatus.DELIVERED, completedOrder.getStatus());
    }

    @Test
    void testCancelOrder() {

        PizzaOrder order = new PizzaOrder();
        order.setId(1L);
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setOrderCode("1345");

        when(orderRepository.findByOrderCode(anyString())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(PizzaOrder.class))).thenReturn(order);

        OrderDto completedOrder = orderService.cancelOrder("1345");

        assertNotNull(completedOrder);
        assertEquals(OrderStatus.CANCELLED, completedOrder.getStatus());
    }
}
