package com.att.tdp.popcorn_palace.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", unique = true)
    @NotNull(message = "Title is required")
    private String title;

    @Column(name = "genre")
    @NotNull(message = "Genre is required")
    private String genre;

    @Column(name = "duration")
    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private int duration;

    @Column(name = "rating")
    @NotNull(message = "Rating is required")
    @Min(value = 0, message = "Rating must be at least 1")
    @Max(value = 10, message = "Rating must be at most 10")
    private double rating;

    @Column(name = "release_year")
    @NotNull(message = "Release year is required")
    @Max(value = 2025, message = "Release year cannot be in the future")
    private int releaseYear;

    public Movie() {
    }

    public Movie(String title, String genre, int duration, double rating, int releaseYear) {
        super();
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.releaseYear = releaseYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public String toString() {
        return "Movie [id=" + id + ", title=" + title + ", genre=" + genre + ", duration=" + duration + ", rating="
                + rating + ", releaseYear=" + releaseYear + "]";
    }
}
