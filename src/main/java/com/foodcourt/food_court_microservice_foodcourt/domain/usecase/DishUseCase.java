package com.foodcourt.food_court_microservice_foodcourt.domain.usecase;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IDishServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.CategoryNotFoundException;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.DishAlreadyExistsException;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.RestaurantNotFoundException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Category;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.ICategoryPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IDishPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IJwtServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.UnauthorizedException;

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
    public void createDish(Dish dish) {

        Long categoryId = dish.getCategory().getId();
        Category category = categoryPersistencePort.findOneById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id "+categoryId+" not found"));
        dish.setCategory(category);

        Long restaurantId = dish.getRestaurant().getId();
        Restaurant restaurant = restaurantPersistencePort.findOneById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id "+restaurantId+" not found"));
        dish.setRestaurant(restaurant);

        Long userId = jwtServicePort.getAuthenticatedUserId();

        if (!restaurant.getOwnerId().equals(userId)) {
            throw new UnauthorizedException("You are not the owner of this restaurant");
        }

        if (dishPersistencePort.findOneByName(dish.getName()).isPresent()) {
            throw new DishAlreadyExistsException("Dish name already exists");
        }

        dish.setActive(true);

        dishPersistencePort.createDish(dish);
    }
}
