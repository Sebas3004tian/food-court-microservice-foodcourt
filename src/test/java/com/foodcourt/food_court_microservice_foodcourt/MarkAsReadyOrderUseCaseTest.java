package com.foodcourt.food_court_microservice_foodcourt;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IJwtServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.ISmsServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.ITraceabilityServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IUserServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.InvalidOrderStatusException;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.NotAssignedException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.*;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.*;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.OrderUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarkAsReadyOrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;
    @Mock
    private IOrderDishPersistencePort orderDishPersistencePort;
    @Mock
    private IDishPersistencePort dishPersistencePort;
    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private ITraceabilityServicePort traceabilityServicePort;
    @Mock
    private ISmsServicePort smsServicePort;
    @Mock
    private IUserServicePort userServicePort;
    @Mock
    private IJwtServicePort jwtServicePort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private Order order;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);

        order = new Order();
        order.setId(100L);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.EN_PREPARACION);
        order.setEmployeeId(1L);
        order.setClientId(1L);
    }

    @Test
    void shouldMarkOrderAsReadySuccessfully() {
        order.setStatus(OrderStatus.EN_PREPARACION);
        order.setEmployeeId(1L);

        String pin = orderUseCase.markOrderAsReady(1L, order);

        assertEquals(OrderStatus.LISTO, order.getStatus());
        assertNotNull(pin);
        verify(orderPersistencePort).updateOrder(order);
    }

    @Test
    void shouldThrowExceptionWhenStatusIsNotInPreparation() {
        order.setStatus(OrderStatus.PENDIENTE);

        assertThrows(InvalidOrderStatusException.class,
                () -> orderUseCase.markOrderAsReady(1L, order));
    }

    @Test
    void shouldThrowUnauthorizedWhenEmployeeNotAssigned() {
        order.setStatus(OrderStatus.EN_PREPARACION);
        order.setEmployeeId(999L);

        assertThrows(NotAssignedException.class,
                () -> orderUseCase.markOrderAsReady(1L, order));
    }

}