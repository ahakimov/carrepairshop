package com.example.carrepairshop.service;

import com.example.carrepairshop.model.Reservation;
import com.example.carrepairshop.model.ReservationFilter;
import com.example.carrepairshop.model.ReservationStatus;
import com.example.carrepairshop.model.RepairJob;
import com.example.carrepairshop.repository.ReservationRepository;
import com.example.carrepairshop.repository.RepairJobRepository;
import com.example.carrepairshop.repository.utils.ReservationSpecification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationsService {

    private final ReservationRepository reservationRepository;
    private final RepairJobRepository repairJobRepository;

    public ReservationsService(ReservationRepository reservationRepository, 
                               RepairJobRepository repairJobRepository) {
        this.reservationRepository = reservationRepository;
        this.repairJobRepository = repairJobRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    @Transactional
    public Reservation updateReservation(Reservation reservation) {
        // Get existing reservation to check status transition
        if (reservation.getId() != null) {
            Optional<Reservation> existingOpt = reservationRepository.findById(reservation.getId());
            if (existingOpt.isPresent()) {
                Reservation existing = existingOpt.get();
                String oldStatus = existing.getStatus();
                String newStatus = reservation.getStatus();
                
                // Validate status transition
                if (!oldStatus.equals(newStatus)) {
                    ReservationStatus oldStatusEnum = ReservationStatus.fromString(oldStatus);
                    ReservationStatus newStatusEnum = ReservationStatus.fromString(newStatus);
                    
                    if (!oldStatusEnum.canTransitionTo(newStatusEnum)) {
                        throw new IllegalArgumentException(
                            String.format("Cannot transition from %s to %s", oldStatus, newStatus)
                        );
                    }
                    
                    // Handle status transitions
                    handleStatusTransition(existing, newStatusEnum);
                }
            }
        }
        
        // Set dateAdded if creating new reservation
        if (reservation.getDateAdded() == null) {
            reservation.setDateAdded(LocalDateTime.now());
        }
        
        // Set default status if not provided
        if (reservation.getStatus() == null || reservation.getStatus().isEmpty()) {
            reservation.setStatus(ReservationStatus.PENDING.getValue());
        }
        
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional
    public Reservation createReservation(Reservation reservation) {
        // Validate required fields
        validateReservation(reservation);
        
        // Check for overlapping reservations
        checkForOverlappingReservations(reservation);
        
        // Set default values
        reservation.setDateAdded(LocalDateTime.now());
        if (reservation.getStatus() == null || reservation.getStatus().isEmpty()) {
            reservation.setStatus(ReservationStatus.PENDING.getValue());
        }
        
        Reservation saved = reservationRepository.save(reservation);
        
        // If status is CONFIRMED, automatically create repair job
        if (saved != null && ReservationStatus.CONFIRMED.getValue().equals(saved.getStatus())) {
            createRepairJobFromReservation(saved);
        }
        
        return saved;
    }

    public List<Reservation> findReservationByVisitDate(LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null)
            return reservationRepository.findByVisitDateTimeBetween(from, to);

        if (from != null)
            return reservationRepository.findByVisitDateTimeAfter(from);

        return reservationRepository.findByVisitDateTimeBefore(to);
    }

    public List<Reservation> filterReservations(ReservationFilter filter) {
        return reservationRepository.findAll(ReservationSpecification.filter(filter));
    }

    /**
     * Check for overlapping reservations for the same mechanic or car
     */
    private void checkForOverlappingReservations(Reservation reservation) {
        if (reservation.getVisitDateTime() == null) {
            throw new IllegalArgumentException("Visit date/time is required");
        }
        
        if (reservation.getMechanic() == null || reservation.getMechanic().getId() == null) {
            throw new IllegalArgumentException("Mechanic is required");
        }
        
        if (reservation.getCar() == null || reservation.getCar().getId() == null) {
            throw new IllegalArgumentException("Car is required");
        }
        
        if (reservation.getService() == null || reservation.getService().getId() == null) {
            throw new IllegalArgumentException("Service is required");
        }
        
        // Calculate end time based on service duration (in minutes)
        Long serviceDuration = reservation.getService().getEstimatedDuration();
        if (serviceDuration == null || serviceDuration <= 0) {
            serviceDuration = 60L; // Default 1 hour
        }
        
        LocalDateTime startTime = reservation.getVisitDateTime();
        LocalDateTime endTime = startTime.plus(serviceDuration, ChronoUnit.MINUTES);
        
        // Check for overlapping reservations with the same mechanic
        List<Reservation> mechanicOverlaps = reservationRepository
            .findOverlappingByMechanic(
                reservation.getMechanic().getId(),
                startTime,
                endTime,
                ReservationStatus.CANCELLED.getValue()
            );
        
        // Exclude current reservation if updating
        if (reservation.getId() != null) {
            mechanicOverlaps = mechanicOverlaps.stream()
                .filter(r -> !r.getId().equals(reservation.getId()))
                .toList();
        }
        
        if (!mechanicOverlaps.isEmpty()) {
            throw new IllegalStateException(
                String.format("Mechanic is already booked at this time. Existing reservation: %s",
                    mechanicOverlaps.get(0).getId())
            );
        }
        
        // Check for overlapping reservations with the same car
        List<Reservation> carOverlaps = reservationRepository
            .findOverlappingByCar(
                reservation.getCar().getId(),
                startTime,
                endTime,
                ReservationStatus.CANCELLED.getValue()
            );
        
        // Exclude current reservation if updating
        if (reservation.getId() != null) {
            carOverlaps = carOverlaps.stream()
                .filter(r -> !r.getId().equals(reservation.getId()))
                .toList();
        }
        
        if (!carOverlaps.isEmpty()) {
            throw new IllegalStateException(
                String.format("Car is already reserved at this time. Existing reservation: %s",
                    carOverlaps.get(0).getId())
            );
        }
    }

    /**
     * Validate reservation has all required fields
     */
    private void validateReservation(Reservation reservation) {
        if (reservation.getClient() == null || reservation.getClient().getId() == null) {
            throw new IllegalArgumentException("Client is required");
        }
        if (reservation.getCar() == null || reservation.getCar().getId() == null) {
            throw new IllegalArgumentException("Car is required");
        }
        if (reservation.getMechanic() == null || reservation.getMechanic().getId() == null) {
            throw new IllegalArgumentException("Mechanic is required");
        }
        if (reservation.getService() == null || reservation.getService().getId() == null) {
            throw new IllegalArgumentException("Service is required");
        }
        if (reservation.getVisitDateTime() == null) {
            throw new IllegalArgumentException("Visit date/time is required");
        }
        
        // Check if visit date is in the future
        if (reservation.getVisitDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Visit date/time must be in the future");
        }
    }

    /**
     * Handle status transitions and trigger appropriate actions
     */
    private void handleStatusTransition(Reservation reservation, ReservationStatus newStatus) {
        switch (newStatus) {
            case PENDING:
                // No action needed for pending status
                break;
            case CONFIRMED:
                // When confirmed, create repair job
                createRepairJobFromReservation(reservation);
                break;
            case IN_PROGRESS:
                // Update repair job status if exists
                updateRepairJobStatus(reservation, "in_progress");
                break;
            case COMPLETED:
                // Update repair job status and set end time
                updateRepairJobStatus(reservation, "completed");
                break;
            case CANCELLED:
                // Cancel associated repair job if exists
                cancelRepairJob(reservation);
                break;
        }
    }

    /**
     * Create a repair job automatically when reservation is confirmed
     */
    private void createRepairJobFromReservation(Reservation reservation) {
        // Check if repair job already exists for this reservation
        List<RepairJob> existingJobs = repairJobRepository.findAll().stream()
            .filter(job -> job.getClient() != null && 
                          job.getClient().getId().equals(reservation.getClient().getId()) &&
                          job.getService() != null &&
                          job.getService().getId().equals(reservation.getService().getId()) &&
                          job.getStartDateTime() != null &&
                          job.getStartDateTime().equals(reservation.getVisitDateTime()))
            .toList();
        
        if (!existingJobs.isEmpty()) {
            return; // Repair job already exists
        }
        
        RepairJob repairJob = new RepairJob();
        repairJob.setClient(reservation.getClient());
        repairJob.setMechanic(reservation.getMechanic());
        repairJob.setService(reservation.getService());
        repairJob.setStartDateTime(reservation.getVisitDateTime());
        
        // Calculate end time based on service duration
        Long serviceDuration = reservation.getService().getEstimatedDuration();
        if (serviceDuration == null || serviceDuration <= 0) {
            serviceDuration = 60L; // Default 1 hour
        }
        repairJob.setEndDateTime(reservation.getVisitDateTime().plus(serviceDuration, ChronoUnit.MINUTES));
        
        repairJob.setStatus("pending");
        repairJob.setAdditionalDetails(reservation.getAdditionalDetails());
        
        repairJobRepository.save(repairJob);
    }

    /**
     * Update repair job status based on reservation status
     */
    private void updateRepairJobStatus(Reservation reservation, String status) {
        List<RepairJob> jobs = repairJobRepository.findAll().stream()
            .filter(job -> job.getClient() != null && 
                          job.getClient().getId().equals(reservation.getClient().getId()) &&
                          job.getService() != null &&
                          job.getService().getId().equals(reservation.getService().getId()) &&
                          job.getStartDateTime() != null &&
                          job.getStartDateTime().equals(reservation.getVisitDateTime()))
            .toList();
        
        if (!jobs.isEmpty()) {
            RepairJob job = jobs.get(0);
            job.setStatus(status);
            if ("completed".equals(status)) {
                job.setEndDateTime(LocalDateTime.now());
            }
            repairJobRepository.save(job);
        }
    }

    /**
     * Cancel repair job when reservation is cancelled
     */
    private void cancelRepairJob(Reservation reservation) {
        List<RepairJob> jobs = repairJobRepository.findAll().stream()
            .filter(job -> job.getClient() != null && 
                          job.getClient().getId().equals(reservation.getClient().getId()) &&
                          job.getService() != null &&
                          job.getService().getId().equals(reservation.getService().getId()) &&
                          job.getStartDateTime() != null &&
                          job.getStartDateTime().equals(reservation.getVisitDateTime()))
            .toList();
        
        if (!jobs.isEmpty()) {
            RepairJob job = jobs.get(0);
            job.setStatus("cancelled");
            repairJobRepository.save(job);
        }
    }
}
