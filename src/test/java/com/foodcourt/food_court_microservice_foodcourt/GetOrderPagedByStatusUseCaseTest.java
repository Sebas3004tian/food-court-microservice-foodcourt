package com.foodcourt.food_court_microservice_foodcourt;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Employee;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderStatus;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IEmployeePersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IJwtServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IOrderPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.OrderUseCase;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.OrderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetOrderPagedByStatusUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IJwtServicePort jwtServicePort;

    @Mock
    private IEmployeePersistencePort employeePersistencePort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private Restaurant restaurant;
    private Employee employee;
    private Order order;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);

        employee = new Employee();
        employee.setRestaurant(restaurant);

        order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDIENTE);
    }

    @Test
    void shouldReturnOrdersSuccessfully() {
        String status = "PENDIENTE";
        int page = 0;
        int size = 10;

        Long userId = 10L;

        List<Order> expectedOrders = List.of(order);

        when(employeePersistencePort.findOneByUserId(userId))
                .thenReturn(Optional.of(employee));

        when(orderPersistencePort.findByRestaurantIdAndStatusPaged(
                restaurant.getId(),
                OrderStatus.PENDIENTE,
                page,
                size
        )).thenReturn(expectedOrders);

        List<Order> result = orderUseCase.getOrderPagedByStatus(userId,status, page, size);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(OrderStatus.PENDIENTE, result.get(0).getStatus());

        verify(orderPersistencePort).findByRestaurantIdAndStatusPaged(
                restaurant.getId(),
                OrderStatus.PENDIENTE,
                page,
                size
        );
    }

    @Test
    void shouldThrowExceptionWhenNoOrdersFound() {
        String status = "PENDIENTE";
        int page = 0;
        int size = 10;

        Long userId = 10L;

        when(employeePersistencePort.findOneByUserId(userId))
                .thenReturn(Optional.of(employee));

        when(orderPersistencePort.findByRestaurantIdAndStatusPaged(
                restaurant.getId(),
                OrderStatus.PENDIENTE,
                page,
                size
        )).thenThrow(new OrderNotFoundException(" "));

        assertThrows(OrderNotFoundException.class, () ->
                orderUseCase.getOrderPagedByStatus(userId,status, page, size)
        );

        verify(orderPersistencePort).findByRestaurantIdAndStatusPaged(
                restaurant.getId(),
                OrderStatus.PENDIENTE,
                page,
                size
        );
    }
}