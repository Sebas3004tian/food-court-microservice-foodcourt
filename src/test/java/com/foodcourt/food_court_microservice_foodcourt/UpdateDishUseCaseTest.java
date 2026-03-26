package com.foodcourt.food_court_microservice_foodcourt;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Category;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IDishPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IJwtServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.DishUseCase;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateDishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IJwtServicePort jwtServicePort;

    @InjectMocks
    private DishUseCase dishUseCase;


    @Test
    void shouldUpdateDishSuccessfully() {
        Long dishId = 1L;
        Long ownerId = 10L;

        Restaurant restaurant = new Restaurant(
                1L, "Test", 123L, "addr", "123", "logo", ownerId
        );

        Category category = new Category(1L, "POSTRES");

        Dish dish = new Dish(
                dishId,
                "Old Name",
                new BigDecimal("1000"),
                "Old Desc",
                "img",
                true,
                restaurant,
                category
        );

        BigDecimal newPrice = new BigDecimal("25000");
        String newDescription = "Updated description";

        when(dishPersistencePort.findOneById(dishId))
                .thenReturn(Optional.of(dish));

        when(jwtServicePort.getAuthenticatedUserId())
                .thenReturn(ownerId);

        dishUseCase.updateDish(dishId, newPrice, newDescription);

        assertEquals(newPrice, dish.getPrice());
        assertEquals(newDescription, dish.getDescription());

        verify(dishPersistencePort).updateDish(dish);
    }

    @Test
    void shouldThrowExceptionWhenDishNotFound() {
        Long dishId = 1L;

        when(dishPersistencePort.findOneById(dishId))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                dishUseCase.updateDish(dishId, BigDecimal.TEN, "desc")
        );

        verify(dishPersistencePort, never()).updateDish(any());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        Long dishId = 1L;
        Long realOwnerId = 10L;
        Long otherUserId = 99L;

        Restaurant restaurant = new Restaurant(
                1L, "Test", 123L, "addr", "123", "logo", realOwnerId
        );

        Dish dish = new Dish();
        dish.setId(dishId);
        dish.setRestaurant(restaurant);

        when(dishPersistencePort.findOneById(dishId))
                .thenReturn(Optional.of(dish));

        when(jwtServicePort.getAuthenticatedUserId())
                .thenReturn(otherUserId);

        assertThrows(UnauthorizedException.class, () ->
                dishUseCase.updateDish(dishId, BigDecimal.TEN, "desc")
        );

        verify(dishPersistencePort, never()).updateDish(any());
    }
}
