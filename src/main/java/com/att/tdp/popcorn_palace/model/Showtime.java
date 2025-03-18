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

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Showtime [id=" + id + ", movie=" + movie + ", theater=" + theater + ", start_time=" + start_time
                + ", end_time=" + end_time + ", price=" + price + "]";
    }
}
