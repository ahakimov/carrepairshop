package com.example.carrepairshop.model;

public enum ReservationStatus {
    PENDING("pending", "Reservation is pending confirmation"),
    CONFIRMED("confirmed", "Reservation is confirmed"),
    IN_PROGRESS("in_progress", "Service is in progress"),
    COMPLETED("completed", "Service is completed"),
    CANCELLED("cancelled", "Reservation is cancelled");

    private final String value;
    private final String description;

    ReservationStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ReservationStatus fromString(String status) {
        for (ReservationStatus rs : ReservationStatus.values()) {
            if (rs.value.equalsIgnoreCase(status)) {
                return rs;
            }
        }
        return PENDING; // Default
    }

    public boolean canTransitionTo(ReservationStatus newStatus) {
        return switch (this) {
            case PENDING -> newStatus == CONFIRMED || newStatus == CANCELLED;
            case CONFIRMED -> newStatus == IN_PROGRESS || newStatus == CANCELLED;
            case IN_PROGRESS -> newStatus == COMPLETED || newStatus == CANCELLED;
            case COMPLETED -> false; // Cannot change from completed
            case CANCELLED -> false; // Cannot change from cancelled
        };
    }
}

