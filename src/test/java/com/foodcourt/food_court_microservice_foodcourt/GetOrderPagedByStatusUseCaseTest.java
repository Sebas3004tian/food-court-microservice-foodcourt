package com.foodcourt.food_court_microservice_foodcourt;



import com.foodcourt.food_court_microservice_foodcourt.domain.model.Employee;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderStatus;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IEmployeePersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IJwtServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IOrderPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.OrderUseCase;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.NoDataFoundException;
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

    @Test
    void shouldReturnOrdersSuccessfully() {
        String status = "PENDIENTE";
        int page = 0;
        int size = 10;

        Long userId = 10L;
        Long restaurantId = 1L;

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Employee employee = new Employee();
        employee.setRestaurant(restaurant);

        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDIENTE);

        List<Order> expectedOrders = List.of(order);

        when(jwtServicePort.getAuthenticatedUserId()).thenReturn(userId);

        when(employeePersistencePort.findOneByUserId(userId))
                .thenReturn(Optional.of(employee));

        when(orderPersistencePort.findByRestaurantIdAndStatusPaged(
                restaurantId,
                OrderStatus.PENDIENTE,
                page,
                size
        )).thenReturn(expectedOrders);

        List<Order> result = orderUseCase.getOrderPagedByStatus(status, page, size);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(OrderStatus.PENDIENTE, result.get(0).getStatus());

        verify(orderPersistencePort).findByRestaurantIdAndStatusPaged(
                restaurantId,
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
        Long restaurantId = 1L;

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Employee employee = new Employee();
        employee.setRestaurant(restaurant);

        when(jwtServicePort.getAuthenticatedUserId()).thenReturn(userId);

        when(employeePersistencePort.findOneByUserId(userId))
                .thenReturn(Optional.of(employee));

        when(orderPersistencePort.findByRestaurantIdAndStatusPaged(
                restaurantId,
                OrderStatus.PENDIENTE,
                page,
                size
        )).thenThrow(new NoDataFoundException("No orders found"));

        assertThrows(NoDataFoundException.class, () ->
                orderUseCase.getOrderPagedByStatus(status, page, size)
        );

        verify(orderPersistencePort).findByRestaurantIdAndStatusPaged(
                restaurantId,
                OrderStatus.PENDIENTE,
                page,
                size
        );
    }
}
