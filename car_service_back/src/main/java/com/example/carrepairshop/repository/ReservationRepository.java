package com.example.carrepairshop.repository;

import com.example.carrepairshop.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {
    List<Reservation> findByVisitDateTimeBefore(LocalDateTime before);
    List<Reservation> findByVisitDateTimeAfter(LocalDateTime after);
    List<Reservation> findByVisitDateTimeBetween(LocalDateTime from, LocalDateTime to);
    
    // Check for overlapping reservations for a mechanic
    List<Reservation> findByMechanicIdAndVisitDateTimeBetweenAndStatusNot(
        Long mechanicId, 
        LocalDateTime start, 
        LocalDateTime end, 
        String status
    );
    
    // Check for overlapping reservations for a car
    List<Reservation> findByCarIdAndVisitDateTimeBetweenAndStatusNot(
        Long carId, 
        LocalDateTime start, 
        LocalDateTime end, 
        String status
    );
    
    // Alternative methods using entity relationships (if above don't work)
    @Query("SELECT r FROM Reservation r WHERE r.mechanic.id = :mechanicId " +
           "AND r.visitDateTime BETWEEN :start AND :end " +
           "AND r.status != :status")
    List<Reservation> findOverlappingByMechanic(
        @Param("mechanicId") Long mechanicId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        @Param("status") String status
    );
    
    @Query("SELECT r FROM Reservation r WHERE r.car.id = :carId " +
           "AND r.visitDateTime BETWEEN :start AND :end " +
           "AND r.status != :status")
    List<Reservation> findOverlappingByCar(
        @Param("carId") Long carId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        @Param("status") String status
    );
}
