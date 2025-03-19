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

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "booking_id")
    private UUID bookingId;

    public Ticket() {
    }

    public Ticket(Showtime showtime, String seatNumber, UUID userId) {
        super();
        this.showtime = showtime;
        this.seatNumber = seatNumber;
        this.userId = userId;
        this.bookingId = UUID.randomUUID();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
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

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    @Override
    public String toString() {
        return "Ticket [id=" + id + ", showtime=" + showtime + ", seatNumber=" + seatNumber +
                ", userId=" + userId + ", bookingId=" + bookingId + "]";
    }
}
