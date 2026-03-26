package com.foodcourt.food_court_microservice_foodcourt.domain.spi;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface IRestaurantPersistencePort {
    Optional<Restaurant> findOneById(Long id);
    Restaurant createRestaurant(Restaurant restaurant);
    Optional<Restaurant> findOneByNit(Long nit);
    Optional<Restaurant> findOneByPhoneNumber(String phoneNumber);
}
