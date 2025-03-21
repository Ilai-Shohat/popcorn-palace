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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "showtimes")
@Data
@NoArgsConstructor
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
    private Double price;

    // Custom constructor without id parameter
    public Showtime(Movie movie, String theater, Instant startTime, Instant endTime, Double price) {
        this.movie = movie;
        this.theater = theater;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }
}
