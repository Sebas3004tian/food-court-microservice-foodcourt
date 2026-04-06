package com.foodcourt.food_court_microservice_foodcourt;

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
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class AssignOrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private Order order;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(10L);

        order = new Order();
        order.setId(1L);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PENDIENTE);
    }

    @Test
    void shouldAssignOrderAndSetStatusInPreparation() {
        when(orderPersistencePort.updateOrder(order)).thenReturn(order);

        orderUseCase.assignOrder(1L, order);

        assertEquals(1L, order.getEmployeeId());
        assertEquals(OrderStatus.EN_PREPARACION, order.getStatus());

        verify(orderPersistencePort).updateOrder(order);
    }

}