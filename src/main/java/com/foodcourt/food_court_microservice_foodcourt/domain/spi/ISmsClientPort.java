package com.foodcourt.food_court_microservice_foodcourt.domain.spi;

public interface ISmsClientPort {
    String sendSms(String clientPhone,String sms);
}
