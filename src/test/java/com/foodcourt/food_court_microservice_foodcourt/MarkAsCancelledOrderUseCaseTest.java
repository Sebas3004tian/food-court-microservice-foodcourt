package com.foodcourt.food_court_microservice_foodcourt;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.ISmsServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.ITraceabilityServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IUserServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderStatus;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IOrderPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.OrderUseCase;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarkAsCancelledOrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;
    @Mock
    private IUserServicePort userServicePort;
    @Mock
    private ISmsServicePort smsServicePort;

    @Mock
    private ITraceabilityServicePort traceabilityServicePort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(100L);
        order.setClientId(1L);
        order.setStatus(OrderStatus.PENDIENTE);
    }

    @Test
    void shouldCancelOrderSuccessfullyWhenStatusIsPending() {
        order.setClientId(1L);
        order.setStatus(OrderStatus.PENDIENTE);

        boolean response = orderUseCase.markOrderAsCanceled(1L, order);

        assertEquals(OrderStatus.CANCELADO, order.getStatus());
        verify(orderPersistencePort).updateOrder(order);
        assertTrue(response);
    }


    @Test
    void shouldThrowExceptionWhenClientIsDifferent() {
        order.setClientId(1L);

        assertThrows(UnauthorizedException.class,
                () -> orderUseCase.markOrderAsCanceled(999L, order));
    }
}