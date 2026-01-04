package com.example.carrepairshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "services", schema = "car_service")
public class Service {
    @Id
    @GeneratedValue
    private Long id;
    private String serviceName;
    private String description;
    private Double price;
    private Long estimatedDuration;
}
