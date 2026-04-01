package com.foodcourt.food_court_microservice_foodcourt;

import com.foodcourt.food_court_microservice_foodcourt.domain.api.IJwtServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.ISmsServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.ITraceabilityServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IUserServicePort;
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
    private ISmsServicePort smsServicePort;

    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private ITraceabilityServicePort traceabilityServicePort;

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
        restaurant.setId(10L);

        employee = new Employee();
        employee.setId(1L);
        employee.setRestaurant(restaurant);

        order = new Order();
        order.setId(1L);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PENDIENTE);
    }

    @Test
    void shouldAssignOrderAndSetStatusInPreparation() {

        when(employeePersistencePort.findOneByUserId(employee.getUserId()))
                .thenReturn(Optional.of(employee));
        when(orderPersistencePort.findOneById(order.getId())).thenReturn(Optional.of(order));

        orderUseCase.assignOrder(employee.getUserId(),order.getId());

        assertEquals(employee.getUserId(), order.getEmployeeId());
        assertEquals(OrderStatus.EN_PREPARACION, order.getStatus());

        verify(orderPersistencePort).updateOrder(order);
    }

    @Test
    void shouldThrowIfOrderNotFound() {
        Long userId = 100L;

        when(jwtServicePort.getAuthenticatedUserId()).thenReturn(userId);
        when(employeePersistencePort.findOneByUserId(userId)).thenReturn(Optional.of(employee));
        when(orderPersistencePort.findOneById(order.getId())).thenReturn(Optional.empty());

        Long orderId = order.getId();
        assertThrows(RuntimeException.class, () -> orderUseCase.assignOrder(1L,orderId));
    }

    @Test
    void shouldThrowIfEmployeeNotInSameRestaurant() {
        Long userId = 100L;

        Restaurant anotherRestaurant = new Restaurant();
        anotherRestaurant.setId(99L);
        employee.setRestaurant(anotherRestaurant);

        when(jwtServicePort.getAuthenticatedUserId()).thenReturn(userId);
        when(employeePersistencePort.findOneByUserId(userId)).thenReturn(Optional.of(employee));
        when(orderPersistencePort.findOneById(order.getId())).thenReturn(Optional.of(order));

        Long orderId = order.getId();
        assertThrows(RuntimeException.class, () -> orderUseCase.assignOrder(1L,orderId));
    }

    @Test
    void shouldThrowIfOrderAlreadyInPreparation() {
        Long userId = 100L;
        order.setStatus(OrderStatus.EN_PREPARACION);

        when(jwtServicePort.getAuthenticatedUserId()).thenReturn(userId);
        when(employeePersistencePort.findOneByUserId(userId)).thenReturn(Optional.of(employee));
        when(orderPersistencePort.findOneById(order.getId())).thenReturn(Optional.of(order));

        Long orderId = order.getId();
        assertThrows(RuntimeException.class, () -> orderUseCase.assignOrder(1L,orderId));
    }
}