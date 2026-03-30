package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.adapter;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Category;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.ICategoryPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper.ICategoryEntityMapper;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CategoryJpaAdapter implements ICategoryPersistencePort {


    private final ICategoryRepository categoryRepository;

    private final ICategoryEntityMapper categoryEntityMapper;
    
    @Override
    public Optional<Category> findOneById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryEntityMapper::toCategory);
    }
}
