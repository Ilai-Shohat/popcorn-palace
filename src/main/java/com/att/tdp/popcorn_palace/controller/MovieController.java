package com.att.tdp.popcorn_palace.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.att.tdp.popcorn_palace.dto.MovieDTO;
import com.att.tdp.popcorn_palace.mapper.MovieMapper;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.service.MovieService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieMapper movieMapper;

    @GetMapping("/all")
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        List<MovieDTO> dtos = movies.stream()
                .map(movieMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody MovieDTO movieDTO) {
        Movie movie = movieMapper.fromDTO(movieDTO);
        Movie createdMovie = movieService.createMovie(movie);

        return ResponseEntity.ok(movieMapper.toDTO(createdMovie));
    }

    @PostMapping("/update/{movieTitle}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable String movieTitle,
            @Valid @RequestBody MovieDTO movieDTO) {
        Movie movie = movieMapper.fromDTO(movieDTO);
        movieService.updateMovie(movieTitle, movie);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{movieTitle}")
    public ResponseEntity<?> deleteMovie(@PathVariable String movieTitle) {
        movieService.deleteMovie(movieTitle);

        return ResponseEntity.ok().build();
    }
}
