package com.foodcourt.food_court_microservice_foodcourt.application.handler.impl;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateRestaurantRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.PageResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.RestaurantResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.application.handler.IRestaurantHandler;
import com.foodcourt.food_court_microservice_foodcourt.application.mapper.IRestaurantRequestMapper;
import com.foodcourt.food_court_microservice_foodcourt.application.mapper.IRestaurantResponseMapper;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IJwtServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IRestaurantServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IUserServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.InvalidUserRoleException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;

    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IRestaurantResponseMapper restaurantResponseMapper;

    private final IJwtServicePort jwtServicePort;
    private final IUserServicePort userServicePort;

    @Override
    public void createRestaurant(CreateRestaurantRequestDto restaurantRequestDto){
        Restaurant restaurant = restaurantRequestMapper.toRestaurant(restaurantRequestDto);
        if (!userServicePort.isUserOwner(restaurant.getOwnerId())) {
            throw new InvalidUserRoleException(UserRole.PROPIETARIO.name());
        }
        restaurantServicePort.createRestaurant(restaurant);

    }

    @Override
    public PageResponseDto<RestaurantResponseDto> getAllPagedRestaurants(int page, int size) {
        Page<Restaurant> restaurantPage = restaurantServicePort.getAllPagedRestaurants(page,size);

        PageResponseDto<RestaurantResponseDto> pageResponseDto = new PageResponseDto<>();

        pageResponseDto.setContent(
                restaurantResponseMapper.toResponseList(restaurantPage.getContent())
        );
        pageResponseDto.setPage(restaurantPage.getNumber());
        pageResponseDto.setSize(restaurantPage.getSize());
        pageResponseDto.setTotalElements(restaurantPage.getTotalElements());
        pageResponseDto.setTotalPages(restaurantPage.getTotalPages());
        pageResponseDto.setFirst(restaurantPage.isFirst());
        pageResponseDto.setLast(restaurantPage.isLast());
        return pageResponseDto;
    }

    @Override
    public Long getMyRestaurantId() {
        return restaurantServicePort.getRestaurantId(jwtServicePort.getAuthenticatedUserId());
    }

}
