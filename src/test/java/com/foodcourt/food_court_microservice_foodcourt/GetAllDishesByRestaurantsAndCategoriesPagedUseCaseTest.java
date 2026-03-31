package com.foodcourt.food_court_microservice_foodcourt;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IDishPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.DishUseCase;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.DishNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllDishesByRestaurantsAndCategoriesPagedUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @InjectMocks
    private DishUseCase dishUseCase;

    private static final Long RESTAURANT_ID = 1L;
    private static final Long CATEGORY_ID = 2L;
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    private Dish dish;

    @BeforeEach
    void setUp() {
        dish = new Dish();
        dish.setId(1L);
        dish.setName("Pizza");
    }

    @Test
    void shouldReturnDishesWhenCategoryIsProvided() {

        when(dishPersistencePort.findByRestaurantAndCategoryPaged(
                RESTAURANT_ID, CATEGORY_ID, PAGE, SIZE))
                .thenReturn(List.of(dish));

        List<Dish> result = dishUseCase.getDishesPagedByRestaurant(
                RESTAURANT_ID, CATEGORY_ID, PAGE, SIZE);

        assertEquals(1, result.size());

        verify(dishPersistencePort).findByRestaurantAndCategoryPaged(
                RESTAURANT_ID, CATEGORY_ID, PAGE, SIZE);
    }

    @Test
    void shouldReturnDishesWhenCategoryIsNull() {

        when(dishPersistencePort.findByRestaurantPaged(
                RESTAURANT_ID, PAGE, SIZE))
                .thenReturn(List.of(dish));

        List<Dish> result = dishUseCase.getDishesPagedByRestaurant(
                RESTAURANT_ID, null, PAGE, SIZE);

        assertEquals(1, result.size());

        verify(dishPersistencePort).findByRestaurantPaged(
                RESTAURANT_ID, PAGE, SIZE);
    }

    @Test
    void shouldThrowExceptionWhenNoDishesFoundWithCategory() {

        when(dishPersistencePort.findByRestaurantAndCategoryPaged(
                RESTAURANT_ID, CATEGORY_ID, PAGE, SIZE))
                .thenThrow(new DishNotFoundException(""));

        assertThrows(DishNotFoundException.class, () ->
                dishUseCase.getDishesPagedByRestaurant(
                        RESTAURANT_ID, CATEGORY_ID, PAGE, SIZE)
        );
    }

    @Test
    void shouldThrowExceptionWhenNoDishesFoundWithoutCategory() {

        when(dishPersistencePort.findByRestaurantPaged(
                RESTAURANT_ID, PAGE, SIZE))
                .thenThrow(new DishNotFoundException("No dishes"));

        assertThrows(DishNotFoundException.class, () ->
                dishUseCase.getDishesPagedByRestaurant(
                        RESTAURANT_ID, null, PAGE, SIZE)
        );
    }

    @Test
    void shouldThrowExceptionWhenPaginationParamsAreInvalid() {

        assertThrows(IllegalArgumentException.class, () ->
                dishUseCase.getDishesPagedByRestaurant(
                        RESTAURANT_ID, CATEGORY_ID, -1, SIZE)
        );

        assertThrows(IllegalArgumentException.class, () ->
                dishUseCase.getDishesPagedByRestaurant(
                        RESTAURANT_ID, CATEGORY_ID, PAGE, 0)
        );
    }
}