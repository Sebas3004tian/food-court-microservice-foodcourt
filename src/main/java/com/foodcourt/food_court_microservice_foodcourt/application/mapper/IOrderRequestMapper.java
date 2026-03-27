package com.foodcourt.food_court_microservice_foodcourt.application.mapper;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateOrderDishRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.request.CreateOrderRequestDto;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderDish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderRequestMapper {

    @Mapping(source = "restaurantId", target = "restaurant.id")
    Order toOrder(CreateOrderRequestDto createOrderRequestDto);

    @Mapping(source = "dishId", target = "dish.id")
    OrderDish toOrderDish(CreateOrderDishRequestDto createOrderDishRequestDtoList);

    List<OrderDish> toOrderDishList(List<CreateOrderDishRequestDto> createOrderDishRequestDtoList);
}
