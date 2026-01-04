package com.example.carrepairshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "clients", schema = "car_service")
public class Client {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String password;
}
