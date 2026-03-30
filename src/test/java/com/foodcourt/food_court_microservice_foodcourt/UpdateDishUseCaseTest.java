package com.foodcourt.food_court_microservice_foodcourt;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Category;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IDishPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IJwtServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.DishUseCase;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
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

    private Restaurant restaurant;
    private Category category;
    private Dish dish;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant(
                1L, "Test", 123L, "addr", "123", "logo", 10L
        );

        category = new Category(1L, "POSTRES");

        dish = new Dish(
                1L,
                "Old Name",
                new BigDecimal("1000"),
                "Old Desc",
                "img",
                true,
                restaurant,
                category
        );
    }

    @Test
    void shouldUpdateDishSuccessfully() {
        Long dishId = 1L;
        Long ownerId = 10L;

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
        Long otherUserId = 99L;

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