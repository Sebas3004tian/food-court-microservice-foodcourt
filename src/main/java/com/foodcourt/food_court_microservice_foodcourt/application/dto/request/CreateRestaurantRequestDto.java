package com.foodcourt.food_court_microservice_foodcourt.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRestaurantRequestDto {

    @NotBlank(message="The name cannot be empty")
    @Pattern(regexp = "^(?!\\d+$)[a-zA-Z0-9 ]+$", message = "The name cannot be only numbers")
    private String name;

    @NotNull(message="The nit cannot be empty")
    @Positive(message="The nit cannot be negative")
    private Long nit;

    @NotBlank(message="The address cannot be empty")
    private String address;

    @NotBlank(message="The restaurant phone number cannot be empty")
    @Pattern(regexp = "^\\+?\\d{1,13}$", message = "Invalid phone number")
    private String phoneNumberRestaurant;

    @NotBlank(message="The url logo cannot be empty")
    private String urlLogo;

    @NotNull(message="The ower id cannot be empty")
    @Positive(message="The ower id cannot be empty be negative")
    private Long ownerId;

}
