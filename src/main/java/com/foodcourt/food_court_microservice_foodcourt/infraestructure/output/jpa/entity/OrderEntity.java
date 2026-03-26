package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.jpa.entity;

import com.foodcourt.food_court_microservice_foodcourt.domain.model.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id",nullable = false)
    private Long clientId;

    @Column(name = "restaurant_id",nullable = false)
    private Long restaurantId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "security_pin")
    private String securityPin;

    @Column(name = "creation_date",nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "update_date")
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        this.creationDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}