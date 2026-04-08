package com.foodcourt.food_court_microservice_foodcourt;

import com.foodcourt.food_court_microservice_foodcourt.domain.exception.InvalidOrderStatusException;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.NotAssignedException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderStatus;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.*;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.OrderUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarkAsDeliveredOrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

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
        order.setStatus(OrderStatus.LISTO);
        order.setEmployeeId(10L);
        order.setClientId(1L);
        order.setSecurityPin("111111");
    }

    @Test
    void shouldMarkOrderAsDeliveredSuccessfully() {
        order.setStatus(OrderStatus.LISTO);
        order.setEmployeeId(10L);
        order.setSecurityPin("111111");

        orderUseCase.markOrderAsDelivered(10L, order, "111111");

        assertEquals(OrderStatus.ENTREGADO, order.getStatus());
        verify(orderPersistencePort).updateOrder(order);
    }

    @Test
    void shouldThrowExceptionWhenStatusIsNotReady() {
        order.setStatus(OrderStatus.PENDIENTE);

        assertThrows(InvalidOrderStatusException.class,
                () -> orderUseCase.markOrderAsDelivered(1L, order, "111111"));
    }

    @Test
    void shouldThrowUnauthorizedWhenEmployeeNotAssigned() {
        order.setStatus(OrderStatus.LISTO);
        order.setEmployeeId(999L);

        assertThrows(NotAssignedException.class,
                () -> orderUseCase.markOrderAsDelivered(1L, order, "111111"));
    }
}
