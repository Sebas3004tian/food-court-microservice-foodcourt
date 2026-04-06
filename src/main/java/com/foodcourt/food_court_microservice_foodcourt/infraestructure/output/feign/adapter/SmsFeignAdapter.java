package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.adapter;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.SendSmsRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.ISmsServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.SmsResultMessage;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.client.ISmsFeignClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SmsFeignAdapter implements ISmsServicePort {

    private final ISmsFeignClient smsFeignClient;

    @Override
    public String sendSms(String clientPhone, String sms) {

        SendSmsRequestDto request = new SendSmsRequestDto(
                clientPhone,
                sms
        );

        String smsResponse = smsFeignClient.sendSms(request);
        if (smsResponse == null) {
            return SmsResultMessage.ORDER_READY_SMS_ERROR.getMessage();
        }
        return smsResponse;
    }
}
