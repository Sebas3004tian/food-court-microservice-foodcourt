package com.foodcourt.food_court_microservice_foodcourt.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SendSmsRequestDto {
    private String phoneNumber;
    private String message;
}