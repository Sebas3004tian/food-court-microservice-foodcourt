package com.foodcourt.food_court_microservice_foodcourt.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    private Long id;
    private String name;
    private Long nit;
    private String address;
    private String phoneNumberRestaurant;
    private String urlLogo;
    private Long ownerId;
}