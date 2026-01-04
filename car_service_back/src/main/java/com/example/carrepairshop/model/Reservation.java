package com.example.carrepairshop.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reservations", schema = "car_service")
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Client client;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Car car;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Mechanic mechanic;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Service service;
    private LocalDateTime dateAdded;
    private LocalDateTime visitDateTime;
    private String status;
    private String additionalDetails;
}
