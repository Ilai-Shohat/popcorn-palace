package com.att.tdp.popcorn_palace.model;

import jakarta.persistence.Entity;

@Entity
public class Movie {
    String title;
    String genre;
    int duration;
    double rating;
    int release_year;

    public Movie(String title, String genre, int duration, double rating, int release_year) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.release_year = release_year;
    }
}
