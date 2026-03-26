package com.foodcourt.food_court_microservice_foodcourt;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Category;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.ICategoryPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IDishPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IJwtServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.DishUseCase;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateDishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IJwtServicePort jwtServicePort;

    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    @InjectMocks
    private DishUseCase dishUseCase;

    @Test
    void shouldCreateDishSuccessfully() {

        Long ownerId = 10L;
        Long restaurantId = 1L;

        Category category = new Category(1L, "POSTRES");

        Restaurant restaurant = new Restaurant(
                restaurantId,
                "Restaurante Test",
                123456L,
                "Calle 123",
                "123456789",
                "logo.png",
                ownerId
        );

        Dish dish = new Dish(
                null,
                "Helado",
                new BigDecimal("1000"),
                "Helado de vainilla",
                "url.com/img",
                true,
                restaurant,
                category
        );

        when(jwtServicePort.getAuthenticatedUserId())
                .thenReturn(ownerId);

        when(restaurantPersistencePort.findOneById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        when(categoryPersistencePort.findOneById(1L))
                .thenReturn(Optional.of(category));

        dishUseCase.createDish(dish);

        verify(dishPersistencePort).createDish(dish);
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {

        Long ownerId = 10L;
        Long otherUserId = 99L;
        Long restaurantId = 1L;

        Restaurant restaurant = new Restaurant(
                restaurantId,
                "Restaurante Test",
                123456L,
                "Calle 123",
                "123456789",
                "logo.png",
                ownerId
        );

        Category category = new Category(1L, "POSTRES");

        Dish dish = new Dish();
        dish.setRestaurant(restaurant);
        dish.setCategory(category);

        when(jwtServicePort.getAuthenticatedUserId())
                .thenReturn(otherUserId);

        when(restaurantPersistencePort.findOneById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        when(categoryPersistencePort.findOneById(1L))
                .thenReturn(Optional.of(category));

        assertThrows(UnauthorizedException.class, () ->
                dishUseCase.createDish(dish)
        );

        verify(dishPersistencePort, never()).createDish(any());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        Long restaurantId = 1L;

        Category category = new Category(1L, "POSTRES");

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Dish dish = new Dish();
        dish.setRestaurant(restaurant);
        dish.setCategory(category);

        when(categoryPersistencePort.findOneById(1L))
                .thenReturn(Optional.of(category));

        when(restaurantPersistencePort.findOneById(restaurantId))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                dishUseCase.createDish(dish)
        );

        verify(dishPersistencePort, never()).createDish(any());
    }

}