package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.adapter;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IDishPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.DishEntity;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper.IDishEntityMapper;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;

    private final IDishEntityMapper dishEntityMapper;

    @Override
    public Optional<Dish> findOneById(Long id) {
        return dishRepository.findById(id)
                .map(dishEntityMapper::toDish);
    }

    @Override
    public Dish createDish(Dish dish) {
        return saveDish(dish);
    }

    @Override
    public Dish updateDish(Dish dish) {
        return saveDish(dish);
    }

    @Override
    public Optional<Dish> findOneByName(String name) {
        return dishRepository.findOneByName(name)
                .map(dishEntityMapper::toDish);
    }

    private Dish saveDish(Dish dish) {
        DishEntity dishEntity = dishRepository.save(dishEntityMapper.toEntity(dish));
        return dishEntityMapper.toDish(dishEntity);
    }
}
