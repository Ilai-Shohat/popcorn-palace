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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
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
    private Integer duration;

    @Column(name = "rating")
    @NotNull(message = "Rating is required")
    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 10, message = "Rating must be at most 10")
    private Double rating;

    @Column(name = "release_year")
    @NotNull(message = "Release year is required")
    @Max(value = 2025, message = "Release year cannot be in the future")
    private Integer releaseYear;

    // Custom constructor without id parameter
    public Movie(String title, String genre, Integer duration, Double rating, Integer releaseYear) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.releaseYear = releaseYear;
    }
}
