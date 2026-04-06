package com.foodcourt.food_court_microservice_foodcourt.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderEfficiencyResponseDto {
    private Long orderId;
    private Long employeeId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Duration duration;
}
