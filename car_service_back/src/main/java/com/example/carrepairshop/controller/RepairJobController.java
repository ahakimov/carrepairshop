package com.example.carrepairshop.controller;

import com.example.carrepairshop.model.RepairJob;
import com.example.carrepairshop.service.RepairJobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("repair-jobs")
public class RepairJobController {

    private final RepairJobService repairJobService;

    public RepairJobController(RepairJobService repairJobService) {
        this.repairJobService = repairJobService;
    }

    @GetMapping
    public List<RepairJob> repairJobList() {
        return repairJobService.getAllRepairJobs();
    }

    @GetMapping("{id}")
    public RepairJob getRepairJob(@PathVariable String id) {
        return repairJobService.findById(Long.valueOf(id)).orElse(null);
    }

    @PutMapping
    public ResponseEntity<RepairJob> updateRepairJob(@RequestBody RepairJob RepairJob) {
        return ResponseEntity.ok(repairJobService.updateRepairJob(RepairJob));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteRepairJob(@PathVariable String id) {
        repairJobService.deleteRepairJob(Long.valueOf(id));

        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("new")
    public ResponseEntity<RepairJob> createRepairJob(@RequestBody RepairJob repairJob) {
        return ResponseEntity.ok(repairJobService.createRepairJob(repairJob));
    }
}
