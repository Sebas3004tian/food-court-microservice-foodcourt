package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.adapter;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderStatus;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IOrderPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.OrderNotFoundException;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.OrderDishEntity;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.OrderEntity;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper.IOrderEntityMapper;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.IOrderDishRepository;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


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
    public Page<Order> findByRestaurantIdAndStatusPaged(Long restaurantId, OrderStatus status, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<OrderEntity> orderEntityPage =
                orderRepository.findByRestaurantIdAndStatus(restaurantId, status, pageable);

        if (orderEntityPage.isEmpty()) {
            throw new OrderNotFoundException("");
        }

        List<Long> orderIds = orderEntityPage.getContent()
                .stream()
                .map(OrderEntity::getId)
                .toList();

        List<OrderDishEntity> orderDishEntityList =
                orderDishRepository.findByOrderIdsWithDish(orderIds);

        Map<Long, List<OrderDishEntity>> dishesByOrderId = orderDishEntityList.stream()
                .collect(Collectors.groupingBy(od -> od.getOrder().getId()));

        for (OrderEntity orderEntity : orderEntityPage.getContent()) {
            List<OrderDishEntity> dishes =
                    dishesByOrderId.getOrDefault(orderEntity.getId(), new ArrayList<>());
            orderEntity.setOrderDishes(dishes);
        }

        List<Order> orderList =
                orderEntityMapper.toOrderList(orderEntityPage.getContent());

        return new PageImpl<>(
                orderList,
                orderEntityPage.getPageable(),
                orderEntityPage.getTotalElements()
        );
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
