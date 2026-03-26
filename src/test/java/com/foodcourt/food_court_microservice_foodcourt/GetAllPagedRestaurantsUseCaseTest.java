package com.foodcourt.food_court_microservice_foodcourt;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.RestaurantUseCase;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.NoDataFoundException;
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
class GetAllPagedRestaurantsUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private List<Restaurant> restaurantList;

    @BeforeEach
    void setUp() {
        restaurantList = List.of(
                new Restaurant(1L, "Burger House",1L,"1b#1","+57", "url1",1L),
                new Restaurant(2L, "Pizza Place",1L, "1b#1","+57","url2",1L)
        );
    }

    @Test
    void shouldReturnPagedRestaurantsSuccessfully() {

        when(restaurantPersistencePort.findAllPaged(0, 2))
                .thenReturn(restaurantList);

        List<Restaurant> result = restaurantUseCase.getAllPagedRestaurants(0, 2);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Burger House", result.get(0).getName());

        verify(restaurantPersistencePort).findAllPaged(0, 2);
    }

    @Test
    void shouldThrowExceptionWhenPageIsNegative() {

        assertThrows(IllegalArgumentException.class,
                () -> restaurantUseCase.getAllPagedRestaurants(-1, 2));

        verifyNoInteractions(restaurantPersistencePort);
    }

    @Test
    void shouldThrowExceptionWhenSizeIsZeroOrNegative() {

        assertThrows(IllegalArgumentException.class,
                () -> restaurantUseCase.getAllPagedRestaurants(0, 0));

        verifyNoInteractions(restaurantPersistencePort);
    }

    @Test
    void shouldThrowExceptionWhenNoRestaurantsFound() {

        when(restaurantPersistencePort.findAllPaged(0, 2))
                .thenThrow(new NoDataFoundException("Empty list"));

        assertThrows(NoDataFoundException.class,
                () -> restaurantUseCase.getAllPagedRestaurants(0, 2));

        verify(restaurantPersistencePort).findAllPaged(0, 2);
    }
}
