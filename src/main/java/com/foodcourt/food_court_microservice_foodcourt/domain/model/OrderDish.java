package com.foodcourt.food_court_microservice_foodcourt.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDish {

    private Long id;
    private Order order;
    private Dish dish;
    private Integer amount;
    private BigDecimal price;


    public BigDecimal calculateTotal() {
        if (amount == null || dish.getPrice() == null) {
            return BigDecimal.ZERO;
        }
        return dish.getPrice().multiply(BigDecimal.valueOf(amount));
    }
}
