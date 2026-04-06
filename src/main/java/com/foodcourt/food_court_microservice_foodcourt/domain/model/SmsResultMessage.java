package com.foodcourt.food_court_microservice_foodcourt.domain.model;

import lombok.Getter;

@Getter
public enum SmsResultMessage {
    ORDER_READY_SUCCESS("Order marked as ready and SMS sent successfully"),
    ORDER_READY_USER_ERROR("Order marked as ready but SMS failed (user service error)"),
    ORDER_READY_SMS_ERROR("Order marked as ready but SMS failed (sms service error)"),

    CANCEL_SMS_USER_ERROR("SMS failed (user service error)"),
    CANCEL_SMS_ERROR("SMS failed (sms service error)");

    private final String message;

    SmsResultMessage(String message) {
        this.message = message;
    }

}
