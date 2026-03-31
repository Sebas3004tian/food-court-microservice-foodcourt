package com.foodcourt.food_court_microservice_foodcourt.domain.spi;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;


public interface IDishPersistencePort {
    Optional<Dish> findOneById(Long id);
    Dish createDish(Dish dish);
    Dish updateDish(Dish dish);
    Optional<Dish> findOneByName(String name);
    Page<Dish> findByRestaurantPaged(Long restaurantId, int page, int size);
    Page<Dish> findByRestaurantAndCategoryPaged(Long restaurantId, Long categoryId, int page, int size);

}
