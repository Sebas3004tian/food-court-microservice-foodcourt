package com.foodcourt.food_court_microservice_foodcourt;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Category;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.ICategoryPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IDishPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IJwtServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;
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

    private Category category;
    private Restaurant restaurant;
    private Dish dish;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "POSTRES");

        restaurant = new Restaurant(
                1L,
                "Restaurante Test",
                123456L,
                "Calle 123",
                "123456789",
                "logo.png",
                10L
        );

        dish = new Dish(
                null,
                "Helado",
                new BigDecimal("1000"),
                "Helado de vainilla",
                "url.com/img",
                true,
                restaurant,
                category
        );
    }

    @Test
    void shouldCreateDishSuccessfully() {

        Long ownerId = 10L;

        when(restaurantPersistencePort.findOneById(restaurant.getId()))
                .thenReturn(Optional.of(restaurant));

        when(categoryPersistencePort.findOneById(category.getId()))
                .thenReturn(Optional.of(category));

        dishUseCase.createDish(ownerId,dish);

        verify(dishPersistencePort).createDish(dish);
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {

        Long otherUserId = 99L;

        when(restaurantPersistencePort.findOneById(restaurant.getId()))
                .thenReturn(Optional.of(restaurant));

        when(categoryPersistencePort.findOneById(category.getId()))
                .thenReturn(Optional.of(category));

        assertThrows(UnauthorizedException.class, () ->
                dishUseCase.createDish(otherUserId,dish)
        );

        verify(dishPersistencePort, never()).createDish(any());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {

        Long ownerId = 10L;

        dish.setRestaurant(new Restaurant());

        when(categoryPersistencePort.findOneById(category.getId()))
                .thenReturn(Optional.of(category));

        when(restaurantPersistencePort.findOneById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                dishUseCase.createDish(ownerId,dish)
        );

        verify(dishPersistencePort, never()).createDish(any());
    }
}