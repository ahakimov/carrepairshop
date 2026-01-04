package com.example.carrepairshop.repository;

import com.example.carrepairshop.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String>, JpaSpecificationExecutor<Reservation> {
    List<Reservation> findByVisitDateTimeBefore(LocalDateTime before);
    List<Reservation> findByVisitDateTimeAfter(LocalDateTime after);
    List<Reservation> findByVisitDateTimeBetween(LocalDateTime from, LocalDateTime to);
}
