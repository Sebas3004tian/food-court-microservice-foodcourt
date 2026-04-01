package com.foodcourt.food_court_microservice_foodcourt.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderTraceability {

    private Long orderId;
    private Long clientId;
    private String clientEmail;

    private String previousStatus;
    private String newStatus;

    private Long employeeId;
    private String employeeEmail;
}