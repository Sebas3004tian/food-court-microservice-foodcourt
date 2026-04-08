package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.repository;

import com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    Optional<RestaurantEntity> findOneByName(String name);
    Optional<RestaurantEntity> findOneByNit(Long nit);
    Optional<RestaurantEntity> findOneByPhoneNumberRestaurant(String phoneNumberRestaurant);

    @Query("SELECT r.id FROM RestaurantEntity r WHERE r.ownerId = :ownerId")
    Optional<Long> findIdByOwnerId(Long ownerId);
}
