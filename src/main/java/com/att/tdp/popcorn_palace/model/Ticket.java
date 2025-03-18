package com.att.tdp.popcorn_palace.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "showtime_id")
    private String showtimeId;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "user_id")
    private String userId;

    public Ticket() {
    }

    public Ticket(String showtimeId, String seatNumber, String userId) {
        super();
        this.showtimeId = showtimeId;
        this.seatNumber = seatNumber;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(String showtimeId) {
        this.showtimeId = showtimeId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Ticket [id=" + id + ", showtimeId=" + showtimeId + ", seatNumber=" + seatNumber + ", userId=" + userId
                + "]";
    }
}
