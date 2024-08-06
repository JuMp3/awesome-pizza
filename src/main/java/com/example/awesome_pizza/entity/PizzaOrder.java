package com.example.awesome_pizza.entity;

import com.example.awesome_pizza.enumz.OrderStatus;
import com.example.awesome_pizza.enumz.PizzaType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "PIZZA_ORDER")
public class PizzaOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ORDER", nullable = false)
    private Long id;

    @Column(name = "PIZZA_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private PizzaType pizzaType;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "ORDER_CODE", nullable = false)
    private String orderCode;
}
