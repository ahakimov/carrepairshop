package com.example.carrepairshop.controller;

import com.example.carrepairshop.model.Car;
import com.example.carrepairshop.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cars")
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<Car> carList() {
        return carService.getAllCars();
    }

    @GetMapping("{id}")
    public Car getCar(@PathVariable String id) {
        return carService.findById(id).orElse(null);
    }

    @PutMapping
    public ResponseEntity<Car> updateCar(@RequestBody Car car) {
        return ResponseEntity.ok(carService.updateCar(car));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCar(@PathVariable String id) {
        carService.deleteCar(id);

        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("new")
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        return ResponseEntity.ok(carService.createCar(car));
    }
}
