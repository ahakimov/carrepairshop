package com.example.carrepairshop.repository;

import com.example.carrepairshop.model.Car;
import com.example.carrepairshop.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {
    Optional<Car> findByOwner(Client client);
}
