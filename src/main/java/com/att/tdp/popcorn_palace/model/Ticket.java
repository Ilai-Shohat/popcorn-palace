package com.att.tdp.popcorn_palace.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "tickets")
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    @NotNull(message = "Showtime is required")
    private Showtime showtime;

    @Column(name = "seat_number")
    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @Column(name = "user_id")
    @NotNull(message = "User ID is required")
    private UUID userId;

    @Column(name = "booking_id")
    private UUID bookingId;

    public Ticket(Showtime showtime, String seatNumber, UUID userId) {
        super();
        this.showtime = showtime;
        this.seatNumber = seatNumber;
        this.userId = userId;
        this.bookingId = UUID.randomUUID();
    }
}
