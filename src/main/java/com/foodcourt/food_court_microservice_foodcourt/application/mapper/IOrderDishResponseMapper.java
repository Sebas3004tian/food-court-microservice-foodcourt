package com.foodcourt.food_court_microservice_foodcourt.application.mapper;

import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.OrderDishResponseDto;
import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderDish;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {IDishResponseMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IOrderDishResponseMapper {

    OrderDishResponseDto toResponse(OrderDish orderDish);

    List<OrderDishResponseDto> toResponseList(List<OrderDish> orderDishList);
}