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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
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
        when(orderPersistencePort.findOneById(100L)).thenReturn(Optional.of(order));

        String response = orderUseCase.markOrderAsCanceled(1L, 100L);

        assertEquals(OrderStatus.CANCELADO, order.getStatus());
        verify(orderPersistencePort).updateOrder(order);
        assertEquals("Orden cancelada", response);
    }

    @Test
    void shouldSendSmsWhenOrderIsNotPending() {
        order.setStatus(OrderStatus.EN_PREPARACION);

        when(orderPersistencePort.findOneById(100L)).thenReturn(Optional.of(order));
        when(userServicePort.getPhone(1L)).thenReturn("+573001234567");
        when(smsServicePort.sendSms(anyString(), anyString())).thenReturn("SMS SENT");

        String response = orderUseCase.markOrderAsCanceled(1L, 100L);

        verify(smsServicePort).sendSms(anyString(), contains("no puede cancelarse"));
        assertEquals("SMS SENT", response);
    }

    @Test
    void shouldReturnMessageWhenPhoneIsNull() {
        order.setStatus(OrderStatus.EN_PREPARACION);

        when(orderPersistencePort.findOneById(100L)).thenReturn(Optional.of(order));
        when(userServicePort.getPhone(1L)).thenReturn(null);

        String response = orderUseCase.markOrderAsCanceled(1L, 100L);

        assertEquals("SMS failed (user service error)", response);
        verify(smsServicePort, never()).sendSms(anyString(), anyString());
    }

    @Test
    void shouldReturnMessageWhenSmsFails() {
        order.setStatus(OrderStatus.EN_PREPARACION);

        when(orderPersistencePort.findOneById(100L)).thenReturn(Optional.of(order));
        when(userServicePort.getPhone(1L)).thenReturn("+573001234567");
        when(smsServicePort.sendSms(anyString(), anyString())).thenReturn(null);

        String response = orderUseCase.markOrderAsCanceled(1L, 100L);

        assertEquals("SMS failed (sms service error)", response);
    }

    @Test
    void shouldThrowExceptionWhenClientIsDifferent() {
        when(orderPersistencePort.findOneById(100L)).thenReturn(Optional.of(order));

        assertThrows(UnauthorizedException.class,
                () -> orderUseCase.markOrderAsCanceled(999L, 100L));
    }
}