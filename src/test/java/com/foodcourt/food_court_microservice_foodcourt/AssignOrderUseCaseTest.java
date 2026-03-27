package com.foodcourt.food_court_microservice_foodcourt;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Employee;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignOrderUseCaseTest {
    
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
    private IJwtServicePort jwtServicePort;
    
    @InjectMocks
    private OrderUseCase orderUseCase;

    @BeforeEach
    void setUp() {
        orderPersistencePort = mock(IOrderPersistencePort.class);
        employeePersistencePort = mock(IEmployeePersistencePort.class);
        jwtServicePort = mock(IJwtServicePort.class);

        orderUseCase = new OrderUseCase( orderPersistencePort, orderDishPersistencePort, dishPersistencePort, restaurantPersistencePort, employeePersistencePort, jwtServicePort);
    }

    @Test
    void shouldAssignOrderAndSetStatusInPreparation() {
        Long orderId = 1L;
        Long userId = 100L;

        Employee employee = new Employee();
        employee.setId(1L);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);
        employee.setRestaurant(restaurant);

        Order order = new Order();
        order.setId(orderId);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PENDIENTE);

        when(jwtServicePort.getAuthenticatedUserId()).thenReturn(userId);
        when(employeePersistencePort.findOneByUserId(userId)).thenReturn(Optional.of(employee));
        when(orderPersistencePort.findOneById(orderId)).thenReturn(Optional.of(order));
        when(orderPersistencePort.updateOrder(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        orderUseCase.assignOrder(orderId);

        assertEquals(employee.getId(), order.getEmployeeId());
        assertEquals(OrderStatus.EN_PREPARACION, order.getStatus());

        verify(orderPersistencePort, times(1)).updateOrder(order);
    }

    @Test
    void shouldThrowIfOrderNotFound() {
        Long orderId = 1L;
        Long userId = 100L;

        when(jwtServicePort.getAuthenticatedUserId()).thenReturn(userId);
        when(employeePersistencePort.findOneByUserId(userId)).thenReturn(Optional.of(new Employee()));
        when(orderPersistencePort.findOneById(orderId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderUseCase.assignOrder(orderId));
    }

    @Test
    void shouldThrowIfEmployeeNotInSameRestaurant() {
        Long orderId = 1L;
        Long userId = 100L;

        Employee employee = new Employee();
        employee.setId(1L);
        Restaurant employeeRestaurant = new Restaurant();
        employeeRestaurant.setId(99L);
        employee.setRestaurant(employeeRestaurant);

        Order order = new Order();
        order.setId(orderId);
        Restaurant orderRestaurant = new Restaurant();
        orderRestaurant.setId(10L);
        order.setRestaurant(orderRestaurant);
        order.setStatus(OrderStatus.PENDIENTE);

        when(jwtServicePort.getAuthenticatedUserId()).thenReturn(userId);
        when(employeePersistencePort.findOneByUserId(userId)).thenReturn(Optional.of(employee));
        when(orderPersistencePort.findOneById(orderId)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () -> orderUseCase.assignOrder(orderId));
    }

    @Test
    void shouldThrowIfOrderAlreadyInPreparation() {
        Long orderId = 1L;
        Long userId = 100L;

        Employee employee = new Employee();
        employee.setId(1L);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);
        employee.setRestaurant(restaurant);

        Order order = new Order();
        order.setId(orderId);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.EN_PREPARACION);

        when(jwtServicePort.getAuthenticatedUserId()).thenReturn(userId);
        when(employeePersistencePort.findOneByUserId(userId)).thenReturn(Optional.of(employee));
        when(orderPersistencePort.findOneById(orderId)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () -> orderUseCase.assignOrder(orderId));
    }
}
