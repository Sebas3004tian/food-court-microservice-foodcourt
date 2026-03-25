package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.adapter;

import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IUserExternalPort;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.client.IUserFeignClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserFeignAdapter implements IUserExternalPort {

    private final IUserFeignClient userFeignClient;

    @Override
    public boolean isUserOwner(Long userId) {
        return "OWNER".equals(userFeignClient.getUserRole(userId));
    }
}
