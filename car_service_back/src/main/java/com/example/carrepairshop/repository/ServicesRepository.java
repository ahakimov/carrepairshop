package com.example.carrepairshop.repository;

import com.example.carrepairshop.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesRepository extends JpaRepository<Service, String> {
}
