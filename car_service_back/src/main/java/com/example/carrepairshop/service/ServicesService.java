package com.example.carrepairshop.service;

import com.example.carrepairshop.model.Service;
import com.example.carrepairshop.repository.ServicesRepository;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServicesService {
    private final ServicesRepository servicesRepository;

    public ServicesService(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    public List<Service> getAllServices() {
        return servicesRepository.findAll();
    }

    public Optional<Service> findById(String id) {
        return servicesRepository.findById(id);
    }

    public Service updateService(Service service) {
        return servicesRepository.save(service);
    }

    public void deleteService(String id) {
        servicesRepository.deleteById(id);
    }

    public Service createService(Service service) {
        return servicesRepository.save(service);
    }
}
