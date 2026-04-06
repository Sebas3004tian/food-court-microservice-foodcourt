package com.foodcourt.food_court_microservice_foodcourt.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
public class EmployeeEfficiencyResponseDto {

    private Long employeeId;
    private int totalOrders;
    private Duration averageDuration;
}
