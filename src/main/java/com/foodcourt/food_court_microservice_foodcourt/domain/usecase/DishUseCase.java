package com.foodcourt.food_court_microservice_foodcourt.domain.usecase;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IDishServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.AlreadyExistsException;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.CategoryNotFoundException;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.DishNotFoundException;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.RestaurantNotFoundException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Category;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.ICategoryPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IDishPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.validator.DishValidator;

import java.math.BigDecimal;
import java.util.List;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;

    public DishUseCase(IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, ICategoryPersistencePort categoryPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.categoryPersistencePort = categoryPersistencePort;
    }

    @Override
    public void updateDish(Long userId,Long dishId, BigDecimal dishPrice, String dishDescription){

        Dish dish = dishPersistencePort.findOneById(dishId)
                .orElseThrow(() -> new DishNotFoundException(dishId.toString()));

        DishValidator.validateOwnership(dish.getRestaurant(),userId);

        dish.setPrice(dishPrice);
        dish.setDescription(dishDescription);

        dishPersistencePort.updateDish(dish);
    }

    @Override
    public void enableOrDisableDish(Long userId,Long dishId, boolean active) {

        Dish dish = dishPersistencePort.findOneById(dishId)
                .orElseThrow(() -> new DishNotFoundException(dishId.toString()));

        Long restaurantId = dish.getRestaurant().getId();
        Restaurant restaurant = restaurantPersistencePort.findOneById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId.toString()));

        DishValidator.validateOwnership(restaurant,userId);
        DishValidator.validateStatusNotAlreadySet(dish.isActive(),active);

        dish.setActive(active);

        dishPersistencePort.updateDish(dish);
    }

    @Override
    public List<Dish> getDishesPagedByRestaurant(Long restaurantId, Long categoryId, int page, int size) {

        DishValidator.validatePaginationParams(page, size);

        if (categoryId != null) {
            return dishPersistencePort.findByRestaurantAndCategoryPaged(restaurantId, categoryId, page, size);
        }

        return dishPersistencePort.findByRestaurantPaged(restaurantId, page, size);
    }

    @Override
    public void createDish(Long userId, Dish dish) {

        Long categoryId = dish.getCategory().getId();
        Category category = categoryPersistencePort.findOneById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId.toString()));
        dish.setCategory(category);

        Long restaurantId = dish.getRestaurant().getId();
        Restaurant restaurant = restaurantPersistencePort.findOneById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId.toString()));
        dish.setRestaurant(restaurant);

        DishValidator.validateOwnership(restaurant, userId);

        if (dishPersistencePort.findOneByName(dish.getName()).isPresent()) {
            throw new AlreadyExistsException("Dish name");
        }

        dish.activate();

        dishPersistencePort.createDish(dish);
    }
}
