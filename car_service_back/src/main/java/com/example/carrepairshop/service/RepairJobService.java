package com.example.carrepairshop.service;

import com.example.carrepairshop.model.RepairJob;
import com.example.carrepairshop.model.Reservation;
import com.example.carrepairshop.repository.RepairJobRepository;
import com.example.carrepairshop.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepairJobService {

    private final RepairJobRepository repairJobRepository;

    public RepairJobService(RepairJobRepository repairJobRepository) {
        this.repairJobRepository = repairJobRepository;
    }

    public List<RepairJob> getAllRepairJobs() {
        return repairJobRepository.findAll();
    }

    public Optional<RepairJob> findById(Long id) {
        return repairJobRepository.findById(id);
    }

    public RepairJob updateRepairJob(RepairJob repairJob) {
        return repairJobRepository.save(repairJob);
    }

    public void deleteRepairJob(Long id) {
        repairJobRepository.deleteById(id);
    }

    public RepairJob createRepairJob(RepairJob repairJob) {
        return repairJobRepository.save(repairJob);
    }

}
