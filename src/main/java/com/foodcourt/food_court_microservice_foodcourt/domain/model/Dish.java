package com.foodcourt.food_court_microservice_foodcourt.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Dish {

    private Long id;
    private String name;
    private String description;
    private String urlImage;
    private boolean active;
    private Restaurant restaurant;
    private Category category;
}
