package com.foodcourt.food_court_microservice_foodcourt.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateDishRequestDto {

    @NotNull(message = "The price cannot be null")
    @PositiveOrZero(message = "The price cannot be negative")
    private BigDecimal price;

    @NotBlank(message="The description cannot be empty")
    private String description;
}
