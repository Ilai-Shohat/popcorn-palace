package com.att.tdp.popcorn_palace.dto;

import java.time.Instant;

public class ShowtimeDTO {
    private Long id;
    private double price;
    private Long movieId;
    private String theater;
    private Instant startTime;
    private Instant endTime;

    public ShowtimeDTO() {
    }

    public ShowtimeDTO(Long id, double price, Long movieId, String theater, Instant startTime, Instant endTime) {
        this.id = id;
        this.price = price;
        this.movieId = movieId;
        this.theater = theater;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
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
}
