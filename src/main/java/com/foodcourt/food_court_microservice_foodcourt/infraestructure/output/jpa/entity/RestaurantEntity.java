package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private Long nit;

    @Column(name = "phone_number_restaurant", nullable = false, unique = true)
    private String phoneNumberRestaurant;

    @Column(name = "url_logo", nullable = false, columnDefinition = "TEXT")
    private String urlLogo;

    @Column(name = "owner_id", nullable = false, unique = true)
    private Long ownerId;
}
