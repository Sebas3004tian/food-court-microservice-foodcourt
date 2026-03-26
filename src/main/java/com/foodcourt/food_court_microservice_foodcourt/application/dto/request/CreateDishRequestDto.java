package com.foodcourt.food_court_microservice_foodcourt.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDishRequestDto {

    @NotBlank(message="The name cannot be empty")
    private String name;

    @NotNull(message="The price cannot be empty")
    @Positive(message="The price cannot be negative")
    private Double price;

    @NotBlank(message="The description cannot be empty")
    private String description;

    @NotBlank(message="The url image cannot be empty")
    private String urlImage;

    @NotNull(message="The category id cannot be empty")
    @Positive(message="The category id cannot be empty be negative")
    private Long categoryId;

    @NotNull(message="The restaurant id cannot be empty")
    @Positive(message="The restaurant id cannot be empty be negative")
    private Long restaurantId;

}
