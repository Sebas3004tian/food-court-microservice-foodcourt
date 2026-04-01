package com.foodcourt.food_court_microservice_foodcourt;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.ISmsServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.ITraceabilityServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IUserServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.InvalidOrderStatusException;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.NotAssignedException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Employee;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderStatus;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.*;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarkAsDeliveredOrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;
    @Mock
    private IEmployeePersistencePort employeePersistencePort;

    @Mock
    private ITraceabilityServicePort traceabilityServicePort;

    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private ISmsServicePort smsServicePort;

    @Mock
    private IOrderDishPersistencePort orderDishPersistencePort;

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

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
        employee.setId(10L);
        employee.setUserId(10L);
        employee.setRestaurant(restaurant);

        order = new Order();
        order.setId(100L);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.LISTO);
        order.setEmployeeId(10L);
        order.setClientId(1L);
        order.setSecurityPin("111111");
    }

    @Test
    void shouldMarkOrderAsDeliveredAndSendSmsSuccessfully() {

        when(employeePersistencePort.findOneByUserId(1L)).thenReturn(Optional.of(employee));
        when(orderPersistencePort.findOneById(100L)).thenReturn(Optional.of(order));

        when(userServicePort.getEmail(anyLong())).thenReturn("test@mail.com");
        doNothing().when(traceabilityServicePort).saveOrderTraceability(any());

        orderUseCase.markOrderAsDelivered(1L,100L,"111111");

        assertEquals(OrderStatus.ENTREGADO, order.getStatus());
    }

    @Test
    void shouldThrowUnauthorizedWhenDifferentRestaurant() {
        Restaurant anotherRestaurant = new Restaurant();
        anotherRestaurant.setId(2L);
        order.setRestaurant(anotherRestaurant);

        when(employeePersistencePort.findOneByUserId(1L)).thenReturn(Optional.of(employee));
        when(orderPersistencePort.findOneById(100L)).thenReturn(Optional.of(order));

        assertThrows(UnauthorizedException.class,
                () -> orderUseCase.markOrderAsDelivered(1L,100L,"111111"));
    }

    @Test
    void shouldThrowExceptionWhenStatusIsNotReady() {
        order.setStatus(OrderStatus.PENDIENTE);

        when(employeePersistencePort.findOneByUserId(1L)).thenReturn(Optional.of(employee));
        when(orderPersistencePort.findOneById(100L)).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderStatusException.class,
                () -> orderUseCase.markOrderAsDelivered(1L,100L,"111111"));
    }

    @Test
    void shouldThrowUnauthorizedWhenEmployeeNotAssigned() {
        order.setEmployeeId(999L);

        when(employeePersistencePort.findOneByUserId(1L)).thenReturn(Optional.of(employee));
        when(orderPersistencePort.findOneById(100L)).thenReturn(Optional.of(order));

        assertThrows(NotAssignedException.class,
                () -> orderUseCase.markOrderAsDelivered(1L,100L,"111111"));
    }
}
