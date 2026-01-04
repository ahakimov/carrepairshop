package com.example.carrepairshop.controller;

import com.example.carrepairshop.model.Service;
import com.example.carrepairshop.service.ServicesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("services")
public class ServicesController {
    private final ServicesService servicesService;

    public ServicesController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    @GetMapping
    public List<Service> serviceList() {
        return servicesService.getAllServices();
    }

    @GetMapping("{id}")
    public Service getService(@PathVariable String id) {
        return servicesService.findById(id).orElse(null);
    }

    @PutMapping
    public ResponseEntity<Service> updateService(@RequestBody Service service) {
        return ResponseEntity.ok(servicesService.updateService(service));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteService(@PathVariable String id) {
        servicesService.deleteService(id);

        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("new")
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        return ResponseEntity.ok(servicesService.createService(service));
    }
}
