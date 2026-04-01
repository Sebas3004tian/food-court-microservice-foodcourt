package com.foodcourt.food_court_microservice_foodcourt.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderTraceabilityRequestDto {

    private Long orderId;

    private Long clientId;

    private String clientEmail;

    private String previousStatus;

    private String newStatus;

    private Long employeeId;

    private String employeeEmail;

}
