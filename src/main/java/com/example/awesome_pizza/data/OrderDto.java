package com.example.awesome_pizza.data;

import com.example.awesome_pizza.enumz.OrderStatus;
import com.example.awesome_pizza.enumz.PizzaType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 212219864954285416L;

    @NotNull
    private PizzaType pizzaType;

    private String orderCode;
    private OrderStatus status;
}
