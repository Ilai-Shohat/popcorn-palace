package com.att.tdp.popcorn_palace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.service.MovieService;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/all")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        Movie createdMovie = movieService.createMovie(movie);
        return ResponseEntity.ok(createdMovie);
    }

    @PostMapping("/update/{movieTitle}")
    public ResponseEntity<?> updateMovie(@PathVariable String movieTitle, @RequestBody Movie movie) {
        boolean updated = movieService.updateMovie(movieTitle, movie);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{movieTitle}")
    public ResponseEntity<?> deleteMovie(@PathVariable String movieTitle) {
        boolean deleted = movieService.deleteMovie(movieTitle);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
