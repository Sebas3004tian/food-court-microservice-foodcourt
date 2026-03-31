package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.adapter;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Dish;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IDishPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.DishNotFoundException;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.DishEntity;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper.IDishEntityMapper;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
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

    @Override
    public Page<Dish> findByRestaurantPaged(Long restaurantId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<DishEntity> dishEntityPage = dishRepository.findByRestaurant(restaurantId, pageable);

        if (dishEntityPage.isEmpty()) {
            throw new DishNotFoundException("");
        }

        List<Dish> dishList = dishEntityMapper.toDishList(dishEntityPage.getContent());

        return new PageImpl<>(
                dishList,
                dishEntityPage.getPageable(),
                dishEntityPage.getTotalElements()
        );
    }

    @Override
    public Page<Dish> findByRestaurantAndCategoryPaged(Long restaurantId, Long categoryId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<DishEntity> dishEntityPage =
                dishRepository.findByRestaurantAndCategoryPaged(restaurantId, categoryId, pageable);

        if (dishEntityPage.isEmpty()) {
            throw new DishNotFoundException("");
        }

        List<Dish> dishList = dishEntityMapper.toDishList(dishEntityPage.getContent());

        return new PageImpl<>(
                dishList,
                dishEntityPage.getPageable(),
                dishEntityPage.getTotalElements()
        );
    }

    private Dish saveDish(Dish dish) {
        DishEntity dishEntity = dishRepository.save(dishEntityMapper.toEntity(dish));
        return dishEntityMapper.toDish(dishEntity);
    }
}
