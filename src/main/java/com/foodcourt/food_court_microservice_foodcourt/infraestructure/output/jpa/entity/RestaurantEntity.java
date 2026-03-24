package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "restaurants")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private Long nit;

    @Column(name = "phone_number_restaurant", nullable = false, unique = true)
    private String phoneNumberRestaurant;

    @Column(name = "url_logo", nullable = false, unique = true)
    private String urlLogo;

    @Column(name = "owner_id",unique = true, nullable = false)
    private Long ownerId;
}
