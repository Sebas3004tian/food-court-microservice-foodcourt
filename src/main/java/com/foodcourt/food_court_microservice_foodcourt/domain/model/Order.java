package com.foodcourt.food_court_microservice_foodcourt.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private Long id;
    private Long clientId;
    private Restaurant restaurant;
    private OrderStatus status;
    private Long employeeId;
    private String securityPin;

}