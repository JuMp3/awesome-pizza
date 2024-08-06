package com.example.awesome_pizza.repository;

import com.example.awesome_pizza.entity.PizzaOrder;
import com.example.awesome_pizza.enumz.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<PizzaOrder, Long> {

    Optional<PizzaOrder> findByOrderCode(String orderCode);

    List<PizzaOrder> findByStatus(OrderStatus status);
}
