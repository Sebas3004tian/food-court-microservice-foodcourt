package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderDish;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.OrderDishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IOrderDishEntityMapper {

    OrderDishEntity toEntity(OrderDish orderDish);
    List<OrderDishEntity> toEntityList(List<OrderDish> orderDishList);

    OrderDish toOrderDish(OrderDishEntity orderDishEntity);
    List<OrderDish> toOrderDishList(List<OrderDishEntity> orderDishEntityList);
}