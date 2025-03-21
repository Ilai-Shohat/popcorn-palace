package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private Long id;

    @NotNull(message = "Title is required")
    private String title;

    @NotNull(message = "Genre is required")
    private String genre;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Integer duration;

    @NotNull(message = "Rating is required")
    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 10, message = "Rating must be at most 10")
    private Double rating;

    @NotNull(message = "Release year is required")
    @Max(value = 2025, message = "Release year cannot be in the future")
    private Integer releaseYear;
}
