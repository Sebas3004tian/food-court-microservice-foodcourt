package com.foodcourt.food_court_microservice_foodcourt.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DishResponseDto {
    private String name;
    private BigDecimal price;
    private String description;
    private String urlImage;

    private CategoryResponseDto category;

}
