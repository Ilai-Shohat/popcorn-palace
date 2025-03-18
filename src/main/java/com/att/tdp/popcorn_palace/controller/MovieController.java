package com.att.tdp.popcorn_palace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.service.MovieService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @PostMapping
    public String createMovie(@RequestBody Movie movie) {
        movieService.createMovie(movie);
        return "Movie created successfully";
    }

    @PutMapping("/update/{movieTitle}")
    public String updateMovie(@PathVariable String movieTitle, @RequestBody Movie movie) {
        movieService.updateMovie(movieTitle, movie);
        return "Movie updated successfully";
    }

    @DeleteMapping("/{movieTitle}")
    public String deleteMovie(@PathVariable String movieTitle) {
        movieService.deleteMovie(movieTitle);
        return "Movie deleted successfully";
    }
}
