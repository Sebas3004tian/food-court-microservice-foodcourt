package com.foodcourt.food_court_microservice_foodcourt.application.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantResponseDto {
    @NotBlank(message="The name cannot be empty")
    private String name;

    @NotBlank(message="The url logo cannot be empty")
    private String urlLogo;
}
