package com.att.tdp.popcorn_palace.model;

import java.sql.Date;

import jakarta.persistence.Entity;

@Entity
public class Showtime {
    Movie movie;
    String theater;
    Date start_time;
    Date end_time;
    double price;

    public Showtime(Movie movie, String theater, Date start_time, Date end_time, double price) {
        this.movie = movie;
        this.theater = theater;
        this.start_time = start_time;
        this.end_time = end_time;
        this.price = price;
    }
}
