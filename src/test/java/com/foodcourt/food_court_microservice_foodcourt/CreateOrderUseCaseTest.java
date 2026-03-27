package com.foodcourt.food_court_microservice_foodcourt;

import com.foodcourt.food_court_microservice_foodcourt.domain.exception.ClientHasActiveOrderException;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderDish;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.*;
import com.foodcourt.food_court_microservice_foodcourt.domain.usecase.OrderUseCase;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.NoDataFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IOrderDishPersistencePort orderDishPersistencePort;

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IJwtServicePort jwtServicePort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private Order order;
    private Restaurant restaurant;
    private Dish dish;
    private OrderDish orderDish;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);

        dish = new Dish();
        dish.setId(10L);
        dish.setPrice(BigDecimal.valueOf(10000));
        dish.setRestaurant(restaurant);

        orderDish = new OrderDish();
        orderDish.setDish(dish);
        orderDish.setAmount(2);

        order = new Order();
        order.setRestaurant(restaurant);
    }

    @Test
    void shouldCreateOrderSuccessfully() {

        when(jwtServicePort.getAuthenticatedUserId()).thenReturn(99L);

        when(orderPersistencePort.existsByClientIdAndStatusIn(anyLong(), anyList()))
                .thenReturn(false);

        when(restaurantPersistencePort.findOneById(1L))
                .thenReturn(Optional.of(restaurant));

        when(dishPersistencePort.findOneById(10L))
                .thenReturn(Optional.of(dish));

        when(orderPersistencePort.createOrder(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order o = invocation.getArgument(0);
                    o.setId(100L);
                    return o;
                });

        orderUseCase.createOrder(order, List.of(orderDish));

        verify(orderPersistencePort).createOrder(any(Order.class));
        verify(orderDishPersistencePort).createOrderDishList(anyList(),any());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {

        when(jwtServicePort.getAuthenticatedUserId()).thenReturn(99L);

        when(orderPersistencePort.existsByClientIdAndStatusIn(anyLong(), anyList()))
                .thenReturn(false);

        when(restaurantPersistencePort.findOneById(1L))
                .thenReturn(Optional.empty());

        List<OrderDish> dishes = List.of(orderDish);

        assertThrows(NoDataFoundException.class,
                () -> orderUseCase.createOrder(order, dishes));

        verify(orderPersistencePort, never()).createOrder(any());
    }

    @Test
    void shouldThrowExceptionWhenDishNotFound() {

        when(jwtServicePort.getAuthenticatedUserId()).thenReturn(99L);

        when(orderPersistencePort.existsByClientIdAndStatusIn(anyLong(), anyList()))
                .thenReturn(false);

        when(restaurantPersistencePort.findOneById(1L))
                .thenReturn(Optional.of(restaurant));

        when(dishPersistencePort.findOneById(10L))
                .thenReturn(Optional.empty());

        when(orderPersistencePort.createOrder(any(Order.class)))
                .thenReturn(order);

        List<OrderDish> dishes = List.of(orderDish);

        assertThrows(NoDataFoundException.class,
                () -> orderUseCase.createOrder(order, dishes));
    }

    @Test
    void shouldThrowExceptionWhenClientHasActiveOrder() {

        when(jwtServicePort.getAuthenticatedUserId()).thenReturn(99L);

        when(orderPersistencePort.existsByClientIdAndStatusIn(anyLong(), anyList()))
                .thenReturn(true);

        List<OrderDish> dishes = List.of(orderDish);

        assertThrows(ClientHasActiveOrderException.class,
                () -> orderUseCase.createOrder(order, dishes));

        verify(orderPersistencePort, never()).createOrder(any());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsInvalid() {

        when(jwtServicePort.getAuthenticatedUserId()).thenReturn(99L);

        when(orderPersistencePort.existsByClientIdAndStatusIn(anyLong(), anyList()))
                .thenReturn(false);

        when(restaurantPersistencePort.findOneById(1L))
                .thenReturn(Optional.of(restaurant));

        orderDish.setAmount(0);

        List<OrderDish> dishes = List.of(orderDish);

        assertThrows(IllegalArgumentException.class,
                () -> orderUseCase.createOrder(order, dishes));
    }
}