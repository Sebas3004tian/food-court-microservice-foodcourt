package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.DishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IDishEntityMapper {

    DishEntity toEntity(Dish dish);
    Dish toDish(DishEntity dishEntity);
}
