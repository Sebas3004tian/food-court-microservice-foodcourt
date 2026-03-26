package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Category;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface ICategoryEntityMapper {

    CategoryEntity toEntity(Category category);
    Category toCategory(CategoryEntity categoryEntity);
}
