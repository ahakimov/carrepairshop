package com.example.carrepairshop.service;

import com.example.carrepairshop.model.Mechanic;
import com.example.carrepairshop.repository.MechanicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MechanicService {
    private final MechanicRepository mechanicRepository;

    public MechanicService(MechanicRepository mechanicRepository) {
        this.mechanicRepository = mechanicRepository;
    }

    public List<Mechanic> getAllMechanics() {
        return mechanicRepository.findAll();
    }

    public Optional<Mechanic> findById(String id) {
        return mechanicRepository.findById(id);
    }

    public Mechanic updateMechanic(Mechanic mechanic) {
        return mechanicRepository.save(mechanic);
    }

    public void deleteMechanic(String id) {
        mechanicRepository.deleteById(id);
    }

    public Mechanic createMechanic(Mechanic mechanic) {
        return mechanicRepository.save(mechanic);
    }
}
