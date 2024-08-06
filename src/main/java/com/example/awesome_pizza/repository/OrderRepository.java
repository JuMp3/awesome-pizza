package com.example.awesome_pizza.repository;

import com.example.awesome_pizza.entity.PizzaOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<PizzaOrder, Long> {

    Optional<PizzaOrder> findByOrderCode(String orderCode);
}
