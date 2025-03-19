package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class TicketBookingRequest {
    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @NotNull(message = "User ID is required")
    private UUID userId;

    // Default constructor
    public TicketBookingRequest() {
    }

    // Getters and setters
    public Long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
