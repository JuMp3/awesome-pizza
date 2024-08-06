package com.example.awesome_pizza;

import com.example.awesome_pizza.data.OrderDto;
import com.example.awesome_pizza.entity.PizzaOrder;
import com.example.awesome_pizza.enumz.OrderStatus;
import com.example.awesome_pizza.enumz.PizzaType;
import com.example.awesome_pizza.exceptions.GenericConflictException;
import com.example.awesome_pizza.service.OrderService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class AwesomePizzaApplicationTests {

	@Autowired
	private OrderService orderService;

	private OrderDto createNewOrder(OrderDto order) {
		return orderService.createOrder(order);
	}

	@BeforeEach
	public void init() {
		orderService.deleteAllOrders();
	}

	@Test
	void testCreateOrder() {

		OrderDto createdOrder = createNewOrder(OrderDto.builder()
				.pizzaType(PizzaType.MARGHERITA)
				.build());

		assertNotNull(createdOrder);
		assertEquals(OrderStatus.NEW, createdOrder.getStatus());
		assertNotNull(createdOrder.getOrderCode());
	}

	@Test
	void testUpdateOrderStatus() {

		OrderDto createdOrder = createNewOrder(OrderDto.builder()
				.pizzaType(PizzaType.MARGHERITA)
				.build());

		OrderDto updatedOrder = orderService.updateOrderStatus(createdOrder.getOrderCode(), OrderStatus.DELIVERED);

		assertNotNull(updatedOrder);
		assertEquals(OrderStatus.DELIVERED, updatedOrder.getStatus());
	}

	@Test
	void testTakeOrder() {

		OrderDto createdOrder = createNewOrder(OrderDto.builder()
				.pizzaType(PizzaType.PEPPERONI)
				.build());

		OrderDto takenOrder = orderService.takeOrder(createdOrder.getOrderCode());

		assertNotNull(takenOrder);
		assertEquals(OrderStatus.IN_PROGRESS, takenOrder.getStatus());
	}

	@Test
	void testTakeOrderKoForOthersInProgress() {

		testTakeOrder();

		OrderDto createdOrder = createNewOrder(OrderDto.builder()
				.pizzaType(PizzaType.SEAFOOD)
				.build());

		GenericConflictException e = assertThrows(GenericConflictException.class, () ->
				orderService.takeOrder(createdOrder.getOrderCode()));

		assertEquals("There is another order in progress", e.getMessage());
	}

	@Test
	void testCompleteOrder() {

		OrderDto createdOrder = createNewOrder(OrderDto.builder()
				.pizzaType(PizzaType.FOUR_CHEESES)
				.build());

		orderService.takeOrder(createdOrder.getOrderCode());

		OrderDto completedOrder = orderService.completeOrder(createdOrder.getOrderCode());

		assertNotNull(completedOrder);
		assertEquals(OrderStatus.DELIVERED, completedOrder.getStatus());
	}

	@Test
	void testCancelOrder() {

		OrderDto createdOrder = createNewOrder(OrderDto.builder()
				.pizzaType(PizzaType.BBQ_CHICKEN)
				.build());

		orderService.takeOrder(createdOrder.getOrderCode());

		OrderDto completedOrder = orderService.cancelOrder(createdOrder.getOrderCode());

		assertNotNull(completedOrder);
		assertEquals(OrderStatus.CANCELLED, completedOrder.getStatus());
	}
}
