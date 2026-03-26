package com.foodcourt.food_court_microservice_foodcourt.domain.spi;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;

import java.util.Optional;


public interface IDishPersistencePort {
    Optional<Dish> findOneById(Long id);
    Dish createDish(Dish dish);
    Dish updateDish(Dish dish);
    Optional<Dish> findOneByName(String name);

}
