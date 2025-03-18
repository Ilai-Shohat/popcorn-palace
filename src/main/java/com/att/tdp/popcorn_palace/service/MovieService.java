package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.model.Movie;

import java.util.List;

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

    public void createMovie(Movie movie) {
        movieRepository.save(movie);
    }

    public void updateMovie(String movieTitle, Movie movie) {
        List<Movie> existingMovies = movieRepository.findByTitle(movieTitle);
        if (!existingMovies.isEmpty()) {
            Movie existingMovie = existingMovies.get(0);
            // existingMovie.setMovieTitle(movie.getMovieTitle());
            // existingMovie.setMovieDescription(movie.getMovieDescription());
            // existingMovie.setMovieGenre(movie.getMovieGenre());
            // existingMovie.setMovieRating(movie.getMovieRating());
            // existingMovie.setMovieDuration(movie.getMovieDuration());
            // existingMovie.setMovieReleaseDate(movie.getMovieReleaseDate());
            movieRepository.save(existingMovie);
        }
    }

    public void deleteMovie(String movieTitle) {
        List<Movie> movies = movieRepository.findByTitle(movieTitle);
        if (!movies.isEmpty()) {
            Movie movie = movies.get(0);
            movieRepository.delete(movie);
        }
    }
}
