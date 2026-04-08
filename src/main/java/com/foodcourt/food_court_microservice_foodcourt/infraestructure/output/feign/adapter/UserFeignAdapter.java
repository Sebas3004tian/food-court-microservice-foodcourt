package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.adapter;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IUserServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.SmsResultMessage;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.client.IUserFeignClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserFeignAdapter implements IUserServicePort {

    private final IUserFeignClient userFeignClient;

    @Override
    public boolean isUserOwner(Long userId) {
        return "PROPIETARIO".equals(userFeignClient.getUserRole(userId));
    }

    @Override
    public boolean isUserEmployee(Long userId) {
        return "EMPLEADO".equals(userFeignClient.getUserRole(userId));
    }

    @Override
    public String getPhone(Long id) {
        String phoneNumber = userFeignClient.getPhone(id);
        if (phoneNumber == null) {
            return SmsResultMessage.ORDER_READY_USER_ERROR.getMessage();
        }
        return phoneNumber;
    }

    @Override
    public String getEmail(Long id) {
        return userFeignClient.getEmail(id);
    }
}
