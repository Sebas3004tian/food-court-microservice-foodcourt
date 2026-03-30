package com.foodcourt.food_court_microservice_foodcourt.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkOrderAsReadyResponseDto {
    private String orderStatus;
    private String smsStatus;
    private String message;
}
