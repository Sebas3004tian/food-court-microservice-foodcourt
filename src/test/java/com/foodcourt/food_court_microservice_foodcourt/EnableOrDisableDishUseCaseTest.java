package com.foodcourt.food_court_microservice_foodcourt;

import com.foodcourt.food_court_microservice_foodcourt.domain.exception.DishStatusAlreadySetException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IDishPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IJwtServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.DishUseCase;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.NoDataFoundException;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnableOrDisableDishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IJwtServicePort jwtServicePort;

    @InjectMocks
    private DishUseCase dishUseCase;

    private Dish dish;
    private Restaurant restaurant;

    private static final Long DISH_ID = 1L;
    private static final Long RESTAURANT_ID = 10L;
    private static final Long OWNER_ID = 100L;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(RESTAURANT_ID);
        restaurant.setOwnerId(OWNER_ID);

        dish = new Dish();
        dish.setId(DISH_ID);
        dish.setActive(false);
        dish.setRestaurant(restaurant);
    }

    @Test
    void shouldEnableDishSuccessfully() {
        dish.setActive(false);

        when(dishPersistencePort.findOneById(DISH_ID)).thenReturn(Optional.of(dish));
        when(restaurantPersistencePort.findOneById(RESTAURANT_ID)).thenReturn(Optional.of(restaurant));

        dishUseCase.enableOrDisableDish(OWNER_ID,DISH_ID, true);

        assertTrue(dish.isActive());
        verify(dishPersistencePort).updateDish(dish);
    }

    @Test
    void shouldThrowExceptionWhenDishNotFound() {
        when(dishPersistencePort.findOneById(DISH_ID)).thenReturn(Optional.empty());

        assertThrows(NoDataFoundException.class,
                () -> dishUseCase.enableOrDisableDish(1L,DISH_ID, true));

        verify(dishPersistencePort, never()).updateDish(any());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        when(dishPersistencePort.findOneById(DISH_ID)).thenReturn(Optional.of(dish));
        when(restaurantPersistencePort.findOneById(RESTAURANT_ID)).thenReturn(Optional.empty());

        assertThrows(NoDataFoundException.class,
                () -> dishUseCase.enableOrDisableDish(1L,DISH_ID, true));

        verify(dishPersistencePort, never()).updateDish(any());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        when(dishPersistencePort.findOneById(DISH_ID)).thenReturn(Optional.of(dish));
        when(restaurantPersistencePort.findOneById(RESTAURANT_ID)).thenReturn(Optional.of(restaurant));

        assertThrows(UnauthorizedException.class,
                () -> dishUseCase.enableOrDisableDish(1L,DISH_ID, true));

        verify(dishPersistencePort, never()).updateDish(any());
    }

    @Test
    void shouldThrowExceptionWhenDishAlreadyEnabled() {
        dish.setActive(true);

        when(dishPersistencePort.findOneById(DISH_ID)).thenReturn(Optional.of(dish));
        when(restaurantPersistencePort.findOneById(RESTAURANT_ID)).thenReturn(Optional.of(restaurant));

        assertThrows(DishStatusAlreadySetException.class,
                () -> dishUseCase.enableOrDisableDish(OWNER_ID,DISH_ID, true));

        verify(dishPersistencePort, never()).updateDish(any());
    }

    @Test
    void shouldThrowExceptionWhenDishAlreadyDisabled() {
        dish.setActive(false);

        when(dishPersistencePort.findOneById(DISH_ID)).thenReturn(Optional.of(dish));
        when(restaurantPersistencePort.findOneById(RESTAURANT_ID)).thenReturn(Optional.of(restaurant));

        assertThrows(DishStatusAlreadySetException.class,
                () -> dishUseCase.enableOrDisableDish(OWNER_ID,DISH_ID, false));

        verify(dishPersistencePort, never()).updateDish(any());
    }
}