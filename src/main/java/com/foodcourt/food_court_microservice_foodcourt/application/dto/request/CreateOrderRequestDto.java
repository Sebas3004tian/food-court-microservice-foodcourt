package com.foodcourt.food_court_microservice_foodcourt.application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequestDto {

    @NotNull(message="The restaurant id cannot be empty")
    @Positive(message="The restaurant id cannot be empty be negative")
    private Long restaurantId;

    @NotNull(message = "The dishes list cannot be null")
    @NotEmpty(message = "The order must have at least one dish")
    private List<CreateOrderDishRequestDto> dishes;
}
