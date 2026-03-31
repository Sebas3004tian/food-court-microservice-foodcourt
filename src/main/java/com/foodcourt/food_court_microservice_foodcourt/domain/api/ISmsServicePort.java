package com.foodcourt.food_court_microservice_foodcourt.domain.api;

public interface ISmsServicePort {
    String sendSms(String clientPhone,String sms);
}
