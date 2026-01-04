package com.example.carrepairshop.controller;

import com.example.carrepairshop.model.Mechanic;
import com.example.carrepairshop.service.MechanicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("mechanics")
public class MechanicsController {
    private final MechanicService mechanicService;

    public MechanicsController(MechanicService mechanicService) {
        this.mechanicService = mechanicService;
    }

    @GetMapping
    public List<Mechanic> mechanicList() {
        return mechanicService.getAllMechanics();
    }

    @GetMapping("{id}")
    public Mechanic getMechanic(@PathVariable String id) {
        return mechanicService.findById(id).orElse(null);
    }

    @PutMapping
    public ResponseEntity<Mechanic> updateMechanic(@RequestBody Mechanic mechanic) {
        return ResponseEntity.ok(mechanicService.updateMechanic(mechanic));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteMechanic(@PathVariable String id) {
        mechanicService.deleteMechanic(id);

        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("new")
    public ResponseEntity<Mechanic> createMechanic(@RequestBody Mechanic mechanic) {
        return ResponseEntity.ok(mechanicService.createMechanic(mechanic));
    }
}
