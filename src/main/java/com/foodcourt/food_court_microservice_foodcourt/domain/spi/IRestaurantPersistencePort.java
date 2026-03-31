package com.foodcourt.food_court_microservice_foodcourt.domain.spi;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IRestaurantPersistencePort {
    Optional<Restaurant> findOneById(Long id);
    Restaurant createRestaurant(Restaurant restaurant);
    Optional<Restaurant> findOneByName(String name);
    Optional<Restaurant> findOneByNit(Long nit);
    Optional<Restaurant> findOneByPhoneNumber(String phoneNumber);
    Page<Restaurant> findAllPaged(int page, int size);
}
