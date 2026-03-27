package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.adapter;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderStatus;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IOrderPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.NoDataFoundException;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.DishEntity;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.OrderDishEntity;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.OrderEntity;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper.IOrderEntityMapper;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.IOrderDishRepository;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderDishRepository orderDishRepository;

    private final IOrderEntityMapper orderEntityMapper;

    @Override
    public Order createOrder(Order order) {
        return saveOrder(order);
    }

    @Override
    public boolean existsByClientIdAndStatusIn(Long clientId, List<OrderStatus> statusList) {
        return orderRepository.existsByClientIdAndStatusIn(clientId, statusList);
    }

    @Override
    public List<Order> findByRestaurantIdAndStatusPaged(Long restaurantId, OrderStatus status, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        List<OrderEntity> orderEntityList =
                orderRepository.findByRestaurantIdAndStatus(restaurantId, status, pageable);

        if (orderEntityList.isEmpty()) {
            throw new NoDataFoundException("No orders found for this restaurant with this status");
        }

        List<Long> orderIds = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntityList) {
            orderIds.add(orderEntity.getId());
        }

        List<OrderDishEntity> orderDishEntityList =
                orderDishRepository.findByOrderIdsWithDish(orderIds);

        for (OrderEntity orderEntity : orderEntityList) {

            List<OrderDishEntity> dishesForOrder = new ArrayList<>();

            for (OrderDishEntity orderDishEntity : orderDishEntityList) {
                if (orderDishEntity.getOrder().getId().equals(orderEntity.getId())) {
                    dishesForOrder.add(orderDishEntity);
                }
            }

            orderEntity.setOrderDishes(dishesForOrder);
        }

        return orderEntityMapper.toOrderList(orderEntityList);
    }

    @Override
    public Optional<Order> findOneById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderEntityMapper::toOrder);
    }

    @Override
    public Order updateOrder(Order order) {
        return saveOrder(order);
    }

    private Order saveOrder(Order order) {
        OrderEntity orderEntity = orderRepository.save(orderEntityMapper.toEntity(order));
        return orderEntityMapper.toOrder(orderEntity);
    }
}
