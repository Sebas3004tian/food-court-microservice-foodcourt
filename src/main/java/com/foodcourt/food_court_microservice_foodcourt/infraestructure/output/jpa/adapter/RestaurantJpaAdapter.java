package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.adapter;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.Restaurant;
import com.foodcourt.food_court_microservice_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.food_court_microservice_foodcourt.domain.exception.RestaurantNotFoundException;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.RestaurantEntity;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.mapper.IRestaurantEntityMapper;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;

    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public Optional<Restaurant> findOneById(Long id) {
        return restaurantRepository.findById(id)
                .map(restaurantEntityMapper::toRestaurant);
    }

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = restaurantRepository.save(restaurantEntityMapper.toEntity(restaurant));
        return restaurantEntityMapper.toRestaurant(restaurantEntity);
    }

    @Override
    public Optional<Restaurant> findOneByName(String name) {
        return restaurantRepository.findOneByName(name)
                .map(restaurantEntityMapper::toRestaurant);
    }

    @Override
    public Optional<Restaurant> findOneByNit(Long nit) {
        return restaurantRepository.findOneByNit(nit)
                .map(restaurantEntityMapper::toRestaurant);
    }

    @Override
    public Optional<Restaurant> findOneByPhoneNumber(String phoneNumberRestaurant) {
        return restaurantRepository.findOneByPhoneNumberRestaurant(phoneNumberRestaurant)
                .map(restaurantEntityMapper::toRestaurant);
    }

    @Override
    public List<Restaurant> findAllPaged(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        Page<RestaurantEntity> pageResult = restaurantRepository.findAll(pageable);

        if (pageResult.isEmpty()) {
            throw new RestaurantNotFoundException("");
        }

        List<RestaurantEntity> restaurantEntityList = pageResult.getContent();

        return restaurantEntityMapper.toRestaurantList(restaurantEntityList);
    }

}
