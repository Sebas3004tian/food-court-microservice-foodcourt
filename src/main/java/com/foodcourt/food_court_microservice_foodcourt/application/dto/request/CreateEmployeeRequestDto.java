package com.foodcourt.food_court_microservice_foodcourt.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEmployeeRequestDto {

    @NotNull(message="The user id cannot be empty")
    @Positive(message="The user id cannot be empty be negative")
    private Long userId;

    @NotNull(message="The restaurant id cannot be empty")
    @Positive(message="The restaurant id cannot be empty be negative")
    private Long restaurantId;
}
