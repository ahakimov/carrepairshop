package com.example.carrepairshop.repository;

import com.example.carrepairshop.model.RepairJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairJobRepository extends JpaRepository<RepairJob, Long> {
}
