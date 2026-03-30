package com.foodcourt.food_court_microservice_foodcourt.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderDishRequestDto {

    @NotNull(message="The dish id cannot be empty")
    @Positive(message="The dish id cannot be empty be negative")
    private Long dishId;

    @NotNull(message = "The amount is required")
    @Min(value = 1, message = "The amount must be at least 1")
    private Integer amount;

}
