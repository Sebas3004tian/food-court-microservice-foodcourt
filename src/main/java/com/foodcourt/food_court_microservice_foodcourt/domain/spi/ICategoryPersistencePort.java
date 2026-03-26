package com.foodcourt.food_court_microservice_foodcourt.domain.spi;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Category;

import java.util.Optional;

public interface ICategoryPersistencePort {
    Optional<Category> findOneById(Long id);
}
