package com.foodcourt.food_court_microservice_foodcourt;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderStatus;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.api.IJwtServicePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IOrderPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.OrderUseCase;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.OrderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

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
    private IRestaurantPersistencePort restaurantPersistencePort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private Restaurant restaurant;
    private Order order;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);

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

        Page<Order> pageResult = new PageImpl<>(List.of(order));

        when(orderPersistencePort.findByRestaurantIdAndStatusPaged(
                1L,
                OrderStatus.PENDIENTE,
                page,
                size
        )).thenReturn(pageResult);

        when(restaurantPersistencePort.findOneById(1L)).thenReturn(Optional.ofNullable(restaurant));

        Page<Order> result = orderUseCase.getOrderPagedByStatus(userId, 1L, status, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(OrderStatus.PENDIENTE, result.getContent().get(0).getStatus());

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
        Long restaurantId = restaurant.getId();

        when(orderPersistencePort.findByRestaurantIdAndStatusPaged(
                restaurantId,
                OrderStatus.PENDIENTE,
                page,
                size
        )).thenThrow(new OrderNotFoundException(" "));

        when(restaurantPersistencePort.findOneById(restaurantId)).thenReturn(Optional.ofNullable(restaurant));
        assertThrows(OrderNotFoundException.class, () ->
                orderUseCase.getOrderPagedByStatus(userId,restaurantId,status, page, size)
        );

        verify(orderPersistencePort).findByRestaurantIdAndStatusPaged(
                restaurant.getId(),
                OrderStatus.PENDIENTE,
                page,
                size
        );
    }
}