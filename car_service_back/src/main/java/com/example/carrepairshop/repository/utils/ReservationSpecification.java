package com.example.carrepairshop.repository.utils;

import com.example.carrepairshop.model.Reservation;
import com.example.carrepairshop.model.ReservationFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ReservationSpecification {

    public static Specification<Reservation> filter(final ReservationFilter reservationFilter) {
        return (Root<Reservation> reservation, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (reservationFilter.getDateFrom() != null)
                predicates.add(cb.greaterThan(reservation.get("visitDateTime"), reservationFilter.getDateFrom()));

            if (reservationFilter.getDateTo() != null)
                predicates.add(cb.lessThan(reservation.get("visitDateTime"), reservationFilter.getDateTo()));

            if (reservationFilter.getClientId() != null)
                predicates.add(cb.equal(reservation.get("client").get("id"), reservationFilter.getClientId()));

            if (reservationFilter.getCarId() != null)
                predicates.add(cb.equal(reservation.get("car").get("id"), reservationFilter.getCarId()));

            if (reservationFilter.getServiceId() != null)
                predicates.add(cb.equal(reservation.get("service").get("id"), reservationFilter.getServiceId()));

            if (reservationFilter.getStatusId() != null)
                predicates.add(cb.equal(reservation.get("status").get("id"), reservationFilter.getStatusId()));

            if (reservationFilter.getMechanicId() != null)
                predicates.add(cb.equal(reservation.get("mechanic").get("id"), reservationFilter.getMechanicId()));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }
}
