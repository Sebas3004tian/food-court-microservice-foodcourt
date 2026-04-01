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
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
    private IEmployeePersistencePort employeePersistencePort;

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

    private Employee employee;
    private Order order;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);

        employee = new Employee();
        employee.setId(1L);
        employee.setUserId(1L);
        employee.setRestaurant(restaurant);

        order = new Order();
        order.setId(100L);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.EN_PREPARACION);
        order.setEmployeeId(1L);
        order.setClientId(1L);
    }

    @Test
    void shouldMarkOrderAsReadyAndSendSmsSuccessfully() {

        when(employeePersistencePort.findOneByUserId(1L)).thenReturn(Optional.of(employee));
        when(orderPersistencePort.findOneById(100L)).thenReturn(Optional.of(order));
        when(userServicePort.getPhone(1L)).thenReturn("+573001234567");
        when(smsServicePort.sendSms(anyString(), anyString())).thenReturn("SMS SENT");
        when(userServicePort.getEmail(anyLong())).thenReturn("test@mail.com");
        doNothing().when(traceabilityServicePort).saveOrderTraceability(any());

        String response = orderUseCase.markOrderAsReady(1L,100L);

        assertEquals(OrderStatus.LISTO, order.getStatus());
        assertNotNull(order.getSecurityPin());

        verify(orderPersistencePort).updateOrder(order);
        verify(smsServicePort).sendSms(anyString(), contains("PIN"));

        assertTrue(response.contains("SMS sent successfully"));
    }

    @Test
    void shouldThrowUnauthorizedWhenDifferentRestaurant() {
        Restaurant anotherRestaurant = new Restaurant();
        anotherRestaurant.setId(2L);
        order.setRestaurant(anotherRestaurant);

        when(employeePersistencePort.findOneByUserId(1L)).thenReturn(Optional.of(employee));
        when(orderPersistencePort.findOneById(100L)).thenReturn(Optional.of(order));

        assertThrows(UnauthorizedException.class,
                () -> orderUseCase.markOrderAsReady(1L,100L));
    }

    @Test
    void shouldThrowExceptionWhenStatusIsNotInPreparation() {
        order.setStatus(OrderStatus.PENDIENTE);

        when(employeePersistencePort.findOneByUserId(1L)).thenReturn(Optional.of(employee));
        when(orderPersistencePort.findOneById(100L)).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderStatusException.class,
                () -> orderUseCase.markOrderAsReady(1L,100L));
    }

    @Test
    void shouldThrowUnauthorizedWhenEmployeeNotAssigned() {
        order.setEmployeeId(999L);

        when(employeePersistencePort.findOneByUserId(1L)).thenReturn(Optional.of(employee));
        when(orderPersistencePort.findOneById(100L)).thenReturn(Optional.of(order));

        assertThrows(NotAssignedException.class,
                () -> orderUseCase.markOrderAsReady(1L,100L));
    }

    @Test
    void shouldReturnMessageWhenSmsFails() {
        when(employeePersistencePort.findOneByUserId(1L)).thenReturn(Optional.of(employee));
        when(orderPersistencePort.findOneById(100L)).thenReturn(Optional.of(order));
        when(userServicePort.getPhone(1L)).thenReturn("+573001234567");
        when(smsServicePort.sendSms(anyString(), anyString())).thenReturn(null);
        when(userServicePort.getEmail(anyLong())).thenReturn("test@mail.com");
        doNothing().when(traceabilityServicePort).saveOrderTraceability(any());

        String response = orderUseCase.markOrderAsReady(1L,100L);

        assertEquals(OrderStatus.LISTO, order.getStatus());
        assertTrue(response.contains("failed"));
    }
}