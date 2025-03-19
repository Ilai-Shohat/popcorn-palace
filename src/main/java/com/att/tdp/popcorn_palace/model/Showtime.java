package com.att.tdp.popcorn_palace.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "showtimes")
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    @NotNull(message = "Movie is required")
    private Movie movie;

    @Column(name = "theater")
    @NotNull(message = "Theater is required")
    private String theater;

    @Column(name = "start_time")
    @NotNull(message = "Start time is required")
    private Instant startTime;

    @Column(name = "end_time")
    @NotNull(message = "End time is required")
    private Instant endTime;

    @Column(name = "price")
    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be non-negative")
    private double price;

    public Showtime() {
    }

    public Showtime(Movie movie, String theater, Instant startTime, Instant endTime, double price) {
        super();
        this.movie = movie;
        this.theater = theater;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getTheater() {
        return theater;
    }

    public void setTheater(String theater) {
        this.theater = theater;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Showtime [id=" + id + ", movie=" + movie + ", theater=" + theater + ", startTime=" + startTime
                + ", endTime=" + endTime + ", price=" + price + "]";
    }
}
