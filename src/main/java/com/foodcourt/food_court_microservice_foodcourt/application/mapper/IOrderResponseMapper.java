package com.foodcourt.food_court_microservice_foodcourt.application.mapper;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.OrderResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {IOrderDishResponseMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IOrderResponseMapper {

    OrderResponseDto toResponse(Order order);

    List<OrderResponseDto> toResponseList(List<Order> orderList);
}