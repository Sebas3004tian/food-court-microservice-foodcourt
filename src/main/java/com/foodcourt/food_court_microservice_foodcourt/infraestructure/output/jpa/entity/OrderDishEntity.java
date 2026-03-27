package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "orders_dishes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer amount;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private DishEntity dish;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;
}
