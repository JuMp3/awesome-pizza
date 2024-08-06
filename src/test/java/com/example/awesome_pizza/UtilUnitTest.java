package com.example.awesome_pizza;

import com.example.awesome_pizza.data.OrderDto;
import com.example.awesome_pizza.entity.PizzaOrder;
import com.example.awesome_pizza.enumz.OrderStatus;
import com.example.awesome_pizza.enumz.PizzaType;
import com.example.awesome_pizza.util.JsonUtils;
import com.example.awesome_pizza.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UtilUnitTest {

    @InjectMocks
    private MapperUtil mapperUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void json() throws IOException {

        String json = JsonUtils.stringify(OrderDto.builder()
                .pizzaType(PizzaType.MARGHERITA)
                .build());

        assertNotNull(json);

        OrderDto orderDto = JsonUtils.asPojo(json, OrderDto.class);
        assertNotNull(orderDto);
        assertEquals(PizzaType.MARGHERITA, orderDto.getPizzaType());

        orderDto = JsonUtils.asPojo(json.getBytes(StandardCharsets.UTF_8), OrderDto.class);
        assertNotNull(orderDto);
        assertEquals(PizzaType.MARGHERITA, orderDto.getPizzaType());
    }

    @Test
    void mapper() {

        PizzaOrder pizzaOrder = mapperUtil.fromDtoToEntity(OrderDto.builder()
                .pizzaType(PizzaType.MARGHERITA)
                .build());
        assertNotNull(pizzaOrder);
        assertEquals(PizzaType.MARGHERITA, pizzaOrder.getPizzaType());

        pizzaOrder.setStatus(OrderStatus.IN_PROGRESS);

        OrderDto dto = mapperUtil.fromEntityToDto(pizzaOrder);
        assertNotNull(dto);
        assertEquals(OrderStatus.IN_PROGRESS, dto.getStatus());

        List<OrderDto> dtos = mapperUtil.fromEntityListToDtoList(List.of(PizzaOrder.builder()
                .pizzaType(PizzaType.FOUR_CHEESES)
                .orderCode("1234")
                .status(OrderStatus.NEW)
                .build()));

        assertNotNull(dtos);
        assertEquals(1, dtos.size());
    }
}
