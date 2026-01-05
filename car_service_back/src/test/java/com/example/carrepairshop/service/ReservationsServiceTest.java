package com.example.carrepairshop.service;

import com.example.carrepairshop.model.*;
import com.example.carrepairshop.repository.RepairJobRepository;
import com.example.carrepairshop.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationsServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RepairJobRepository repairJobRepository;

    @InjectMocks
    private ReservationsService reservationsService;

    private Client testClient;
    private Car testCar;
    private Mechanic testMechanic;
    private Service testService;
    private Reservation testReservation;

    @BeforeEach
    void setUp() {
        // Setup test data
        testClient = new Client();
        testClient.setId(1L);
        testClient.setName("John Doe");

        testCar = new Car();
        testCar.setId(1L);
        testCar.setModel("Toyota Camry");
        testCar.setOwner(testClient);

        testMechanic = new Mechanic();
        testMechanic.setId(1L);
        testMechanic.setName("Mike Smith");

        testService = new Service();
        testService.setId(1L);
        testService.setServiceName("Oil Change");
        testService.setEstimatedDuration(60L); // 60 minutes

        testReservation = new Reservation();
        testReservation.setClient(testClient);
        testReservation.setCar(testCar);
        testReservation.setMechanic(testMechanic);
        testReservation.setService(testService);
        testReservation.setVisitDateTime(LocalDateTime.now().plusDays(1));
    }

    @Test
    void testCreateReservation_Success() {
        // Given
        when(reservationRepository.findOverlappingByMechanic(any(), any(), any(), any()))
            .thenReturn(new ArrayList<>());
        when(reservationRepository.findOverlappingByCar(any(), any(), any(), any()))
            .thenReturn(new ArrayList<>());
        when(reservationRepository.save(any(Reservation.class)))
            .thenAnswer(invocation -> {
                Reservation r = invocation.getArgument(0);
                r.setId(1L);
                return r;
            });

        // When
        Reservation result = reservationsService.createReservation(testReservation);

        // Then
        assertNotNull(result);
        assertEquals(ReservationStatus.PENDING.getValue(), result.getStatus());
        assertNotNull(result.getDateAdded());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void testCreateReservation_WithOverlappingMechanic_ShouldFail() {
        // Given
        Reservation existingReservation = new Reservation();
        existingReservation.setId(2L);
        existingReservation.setMechanic(testMechanic);
        existingReservation.setVisitDateTime(testReservation.getVisitDateTime());

        when(reservationRepository.findOverlappingByMechanic(any(), any(), any(), any()))
            .thenReturn(List.of(existingReservation));

        // When & Then
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> reservationsService.createReservation(testReservation)
        );

        assertTrue(exception.getMessage().contains("Mechanic is already booked"));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void testCreateReservation_WithOverlappingCar_ShouldFail() {
        // Given
        Reservation existingReservation = new Reservation();
        existingReservation.setId(2L);
        existingReservation.setCar(testCar);
        existingReservation.setVisitDateTime(testReservation.getVisitDateTime());

        when(reservationRepository.findOverlappingByMechanic(any(), any(), any(), any()))
            .thenReturn(new ArrayList<>());
        when(reservationRepository.findOverlappingByCar(any(), any(), any(), any()))
            .thenReturn(List.of(existingReservation));

        // When & Then
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> reservationsService.createReservation(testReservation)
        );

        assertTrue(exception.getMessage().contains("Car is already reserved"));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void testCreateReservation_WithConfirmedStatus_CreatesRepairJob() {
        // Given
        testReservation.setStatus(ReservationStatus.CONFIRMED.getValue());

        when(reservationRepository.findOverlappingByMechanic(any(), any(), any(), any()))
            .thenReturn(new ArrayList<>());
        when(reservationRepository.findOverlappingByCar(any(), any(), any(), any()))
            .thenReturn(new ArrayList<>());
        when(reservationRepository.save(any(Reservation.class)))
            .thenAnswer(invocation -> {
                Reservation r = invocation.getArgument(0);
                r.setId(1L);
                return r;
            });
        when(repairJobRepository.findAll()).thenReturn(new ArrayList<>());
        when(repairJobRepository.save(any(RepairJob.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Reservation result = reservationsService.createReservation(testReservation);

        // Then
        assertNotNull(result);
        assertEquals(ReservationStatus.CONFIRMED.getValue(), result.getStatus());
        verify(repairJobRepository).save(any(RepairJob.class));
    }

    @Test
    void testCreateReservation_WithPastDate_ShouldFail() {
        // Given
        testReservation.setVisitDateTime(LocalDateTime.now().minusDays(1));

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> reservationsService.createReservation(testReservation)
        );

        assertTrue(exception.getMessage().contains("must be in the future"));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void testUpdateReservation_ValidStatusTransition() {
        // Given
        Reservation existing = new Reservation();
        existing.setId(1L);
        existing.setStatus(ReservationStatus.PENDING.getValue());
        existing.setClient(testClient);
        existing.setCar(testCar);
        existing.setMechanic(testMechanic);
        existing.setService(testService);
        existing.setVisitDateTime(LocalDateTime.now().plusDays(1));

        Reservation updated = new Reservation();
        updated.setId(1L);
        updated.setStatus(ReservationStatus.CONFIRMED.getValue());
        updated.setClient(testClient);
        updated.setCar(testCar);
        updated.setMechanic(testMechanic);
        updated.setService(testService);
        updated.setVisitDateTime(LocalDateTime.now().plusDays(1));

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(updated);
        when(repairJobRepository.findAll()).thenReturn(new ArrayList<>());
        when(repairJobRepository.save(any(RepairJob.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Reservation result = reservationsService.updateReservation(updated);

        // Then
        assertNotNull(result);
        assertEquals(ReservationStatus.CONFIRMED.getValue(), result.getStatus());
        verify(repairJobRepository).save(any(RepairJob.class));
    }

    @Test
    void testUpdateReservation_InvalidStatusTransition_ShouldFail() {
        // Given
        Reservation existing = new Reservation();
        existing.setId(1L);
        existing.setStatus(ReservationStatus.PENDING.getValue());

        Reservation updated = new Reservation();
        updated.setId(1L);
        updated.setStatus(ReservationStatus.COMPLETED.getValue()); // Invalid: skipping CONFIRMED

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(existing));

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> reservationsService.updateReservation(updated)
        );

        assertTrue(exception.getMessage().contains("Cannot transition"));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void testCreateReservation_MissingRequiredFields_ShouldFail() {
        // Given
        Reservation invalid = new Reservation();
        // Missing client, car, mechanic, service, visitDateTime

        // When & Then
        assertThrows(
            IllegalArgumentException.class,
            () -> reservationsService.createReservation(invalid)
        );
    }
}

