package com.foodcourt.food_court_microservice_foodcourt.domain.usecase;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IDishServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.*;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Category;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.ICategoryPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IDishPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IJwtServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.UnauthorizedException;

import java.math.BigDecimal;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IJwtServicePort jwtServicePort;

    public DishUseCase(IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, ICategoryPersistencePort categoryPersistencePort, IJwtServicePort jwtServicePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.categoryPersistencePort = categoryPersistencePort;
        this.jwtServicePort = jwtServicePort;
    }

    @Override
    public void updateDish(Long dishId, BigDecimal dishPrice, String dishDescription){
        Dish dish = dishPersistencePort.findOneById(dishId)
                .orElseThrow(() -> new DishNotFoundException("Not found the Dish with id "+dishId));

        Long userId = jwtServicePort.getAuthenticatedUserId();

        if (!dish.getRestaurant().getOwnerId().equals(userId)) {
            throw new UnauthorizedException("You are not the owner of this restaurant");
        }

        dish.setPrice(dishPrice);
        dish.setDescription(dishDescription);

        dishPersistencePort.updateDish(dish);
    }

    @Override
    public void enableOrDisableDish(Long dishId, boolean active) {

        Dish dish = dishPersistencePort.findOneById(dishId)
                .orElseThrow(() -> new DishNotFoundException("Not found the Dish with id "+dishId));

        Long restaurantId = dish.getRestaurant().getId();
        Restaurant restaurant = restaurantPersistencePort.findOneById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Not found the Restaurant with id "+restaurantId));

        Long userId = jwtServicePort.getAuthenticatedUserId();

        if (!restaurant.getOwnerId().equals(userId)) {
            throw new UnauthorizedException("You are not the owner of this restaurant");
        }

        if (dish.isActive() == active) {
            throw new DishStatusAlreadySetException(
                    "Dish is already " + (active ? "enabled" : "disabled")
            );
        }

        dish.setActive(active);

        dishPersistencePort.updateDish(dish);
    }

    @Override
    public void createDish(Dish dish) {

        Long categoryId = dish.getCategory().getId();
        Category category = categoryPersistencePort.findOneById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Not found the Category with id "+categoryId));
        dish.setCategory(category);

        Long restaurantId = dish.getRestaurant().getId();
        Restaurant restaurant = restaurantPersistencePort.findOneById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Not found the Restaurant with id "+restaurantId));
        dish.setRestaurant(restaurant);

        Long userId = jwtServicePort.getAuthenticatedUserId();

        if (!restaurant.getOwnerId().equals(userId)) {
            throw new UnauthorizedException("You are not the owner of this restaurant");
        }

        if (dishPersistencePort.findOneByName(dish.getName()).isPresent()) {
            throw new DishAlreadyExistsException("Dish name already exists");
        }

        dish.activate();

        dishPersistencePort.createDish(dish);
    }
}
