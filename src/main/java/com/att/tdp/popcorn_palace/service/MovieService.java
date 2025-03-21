package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.exception.MovieAlreadyExistsException;
import com.att.tdp.popcorn_palace.exception.MovieNotFoundException;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.att.tdp.popcorn_palace.repository.MovieRepository;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie createMovie(Movie movie) {
        List<Movie> existingMovies = movieRepository.findByTitle(movie.getTitle());
        if (!existingMovies.isEmpty()) {
            throw new MovieAlreadyExistsException("Movie with title '" + movie.getTitle() + "' already exists");
        }

        return movieRepository.save(movie);
    }

    public Movie getMovieByTitle(String movieTitle) {
        List<Movie> movies = movieRepository.findByTitle(movieTitle);
        if (movies.isEmpty()) {
            throw new MovieNotFoundException("Movie with title '" + movieTitle + "' not found");
        }

        return movies.get(0);
    }

    public Movie getMovieById(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);

        return movie.orElseThrow(() -> new MovieNotFoundException("Movie with ID " + id + " not found"));
    }

    public Movie updateMovie(String movieTitle, Movie movie) {
        List<Movie> existingMovies = movieRepository.findByTitle(movieTitle);

        if (!existingMovies.isEmpty()) {
            Movie existingMovie = existingMovies.get(0);

            // Check if title is being changed
            if (!movieTitle.equals(movie.getTitle())) {
                // Check if the new title already exists for another movie
                List<Movie> moviesWithNewTitle = movieRepository.findByTitle(movie.getTitle());
                if (!moviesWithNewTitle.isEmpty()) {
                    throw new MovieAlreadyExistsException(
                            "Cannot update: Movie with title '" + movie.getTitle() + "' already exists");
                }
            }

            BeanUtils.copyProperties(movie, existingMovie, "id");
            return movieRepository.save(existingMovie);
        } else {
            throw new MovieNotFoundException("Movie with title '" + movieTitle + "' not found");
        }
    }

    public void deleteMovie(String movieTitle) {
        List<Movie> movies = movieRepository.findByTitle(movieTitle);

        if (!movies.isEmpty()) {
            Movie movie = movies.get(0);
            movieRepository.delete(movie);
        } else {
            throw new MovieNotFoundException("Movie with title '" + movieTitle + "' not found");
        }
    }
}
