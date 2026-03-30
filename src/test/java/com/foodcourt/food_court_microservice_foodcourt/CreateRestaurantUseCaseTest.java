package com.foodcourt.food_court_microservice_foodcourt;

import com.foodcourt.food_court_microservice_foodcourt.domain.exception.InvalidUserRoleException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IUserExternalPort;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.RestaurantUseCase;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.UserServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantUseCaseTest {

    @Mock
    private IUserExternalPort userExternalPort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant(
                1L,
                "pepitotest",
                111L,
                "Cra test # test-test",
                "+1",
                "https://...",
                11L
        );
    }

    @Test
    void shouldCreateRestaurantSuccessfully() {

        Long ownerId = 11L;

        when(userExternalPort.isUserOwner(ownerId))
                .thenReturn(true);

        restaurantUseCase.createRestaurant(restaurant);

        verify(restaurantPersistencePort).createRestaurant(restaurant);
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {

        Long ownerId = 11L;

        when(userExternalPort.isUserOwner(ownerId))
                .thenReturn(false);

        assertThrows(InvalidUserRoleException.class, () ->
                restaurantUseCase.createRestaurant(restaurant)
        );

        verify(restaurantPersistencePort, never()).createRestaurant(any());
    }

    @Test
    void shouldThrowExceptionWhenUserServiceFails() {

        Long ownerId = 11L;

        when(userExternalPort.isUserOwner(ownerId))
                .thenThrow(new UserServiceException("User not found", 404));

        assertThrows(UserServiceException.class, () ->
                restaurantUseCase.createRestaurant(restaurant)
        );
    }
}