package com.example.carrepairshop.controller;

import com.example.carrepairshop.model.Reservation;
import com.example.carrepairshop.model.ReservationFilter;
import com.example.carrepairshop.service.ReservationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("reservations")
public class ReservationsController {

    private final ReservationsService reservationsService;

    public ReservationsController(ReservationsService reservationsService) {
        this.reservationsService = reservationsService;
    }

    @GetMapping
    public List<Reservation> reservationList() {
        return reservationsService.getAllReservations();
    }

    @GetMapping("{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        return reservationsService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<?> updateReservation(@RequestBody Reservation reservation) {
        try {
            return ResponseEntity.ok(reservationsService.updateReservation(reservation));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage()); // Conflict
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        reservationsService.deleteReservation(id);

        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("new")
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        try {
            return ResponseEntity.ok(reservationsService.createReservation(reservation));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage()); // Conflict
        }
    }

    @GetMapping("schedule")
    public ResponseEntity<List<Reservation>> getReservationsSchedule(@RequestParam LocalDateTime from, @RequestParam LocalDateTime to) {
        return ResponseEntity.ok(reservationsService.findReservationByVisitDate(from, to));
    }

    @GetMapping("filter")
    public ResponseEntity<List<Reservation>> filterReservations(@RequestBody ReservationFilter filter) {
        return ResponseEntity.ok(reservationsService.filterReservations(filter));
    }
}
