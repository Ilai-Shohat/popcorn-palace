package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class TicketBookingRequest {
    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @NotNull(message = "User ID is required")
    private UUID userId;
}
