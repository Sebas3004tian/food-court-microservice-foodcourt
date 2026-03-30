package com.foodcourt.food_court_microservice_foodcourt.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderDishResponseDto {
    private Long id;
    private DishResponseDto dish;
    private Integer amount;
    private BigDecimal price;
}
