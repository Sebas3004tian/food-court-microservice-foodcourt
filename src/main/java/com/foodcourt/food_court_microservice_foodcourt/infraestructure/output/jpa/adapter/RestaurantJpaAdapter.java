package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.adapter;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.RestaurantEntity;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper.IRestaurantEntityMapper;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;

    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = restaurantRepository.save(restaurantEntityMapper.toEntity(restaurant));
        return restaurantEntityMapper.toRestaurant(restaurantEntity);
    }
}
