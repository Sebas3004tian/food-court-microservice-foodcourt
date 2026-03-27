package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Order;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IOrderEntityMapper {

    @Mapping(source = "restaurant.id", target = "restaurantId")
    OrderEntity toEntity(Order order);

    //@Mapping(target = "creation_date", ignore = true)
    Order toOrder(OrderEntity orderEntity);
}
