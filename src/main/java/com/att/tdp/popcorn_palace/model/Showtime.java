package com.att.tdp.popcorn_palace.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

@Entity
@Table(name = "showtimes")
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    Movie movie;

    @Column(name = "theater")
    String theater;

    @Column(name = "start_time")
    Date start_time;

    @Column(name = "end_time")
    Date end_time;

    @Column(name = "price")
    double price;

    public Showtime() {
    }

    public Showtime(Movie movie, String theater, Date start_time, Date end_time, double price) {
        super();
        this.movie = movie;
        this.theater = theater;
        this.start_time = start_time;
        this.end_time = end_time;
        this.price = price;
    }
}
