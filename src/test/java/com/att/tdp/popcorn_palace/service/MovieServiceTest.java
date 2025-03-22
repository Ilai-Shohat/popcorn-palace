package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.exception.MovieAlreadyExistsException;
import com.att.tdp.popcorn_palace.exception.MovieNotFoundException;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testCreateMoviePositive() {
        Movie movie = new Movie("Test Movie", "Drama", 100, 7.5, 2022);

        when(movieRepository.findByTitle("Test Movie")).thenReturn(Collections.emptyList());
        when(movieRepository.save(movie)).thenReturn(movie);

        Movie result = movieService.createMovie(movie);
        
        assertNotNull(result);
        assertEquals("Test Movie", result.getTitle());
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    public void testUpdateMoviePositive() {
        Movie existingMovie = new Movie("Old Title", "Drama", 90, 7.0, 2021);
        existingMovie.setId(1L);
        Movie updatedMovie = new Movie("New Title", "Comedy", 95, 8.0, 2022);

        when(movieRepository.findByTitle("Old Title")).thenReturn(Collections.singletonList(existingMovie));
        when(movieRepository.findByTitle("New Title")).thenReturn(Collections.emptyList());
        when(movieRepository.save(any(Movie.class))).thenReturn(updatedMovie);

        movieService.updateMovie("Old Title", updatedMovie);
        verify(movieRepository, times(1)).save(existingMovie);
        assertEquals("New Title", existingMovie.getTitle());
    }

    @Test
    public void testDeleteMoviePositive() {
        Movie movie = new Movie("Delete Movie", "Action", 110, 8.5, 2020);
        movie.setId(1L);
        when(movieRepository.findByTitle("Delete Movie")).thenReturn(Collections.singletonList(movie));

        movieService.deleteMovie("Delete Movie");
        verify(movieRepository, times(1)).delete(movie);
    }

    @Test
    public void testGetAllMoviesPositive() {
        List<Movie> movieList = new ArrayList<>();
        movieList.add(new Movie("Movie 1", "Action", 120, 8.0, 2021));
        movieList.add(new Movie("Movie 2", "Comedy", 95, 7.5, 2022));

        when(movieRepository.findAll()).thenReturn(movieList);

        List<Movie> result = movieService.getAllMovies();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    public void testGetMovieByTitlePositive() {
        Movie movie = new Movie("Test Movie", "Drama", 110, 8.5, 2020);
        when(movieRepository.findByTitle("Test Movie")).thenReturn(Collections.singletonList(movie));

        Movie result = movieService.getMovieByTitle("Test Movie");

        assertNotNull(result);
        assertEquals("Test Movie", result.getTitle());
        verify(movieRepository, times(1)).findByTitle("Test Movie");
    }

    @Test
    public void testGetMovieByIdPositive() {
        Movie movie = new Movie("Test Movie", "Drama", 110, 8.5, 2020);
        movie.setId(1L);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        Movie result = movieService.getMovieById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Movie", result.getTitle());
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllMoviesEmptyList() {
        when(movieRepository.findAll()).thenReturn(Collections.emptyList());

        List<Movie> result = movieService.getAllMovies();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCreateMovieAlreadyExists() {
        Movie movie = new Movie("Existing Movie", "Action", 120, 8.0, 2021);
        when(movieRepository.findByTitle("Existing Movie")).thenReturn(Collections.singletonList(movie));

        MovieAlreadyExistsException exception = assertThrows(
                MovieAlreadyExistsException.class,
                () -> movieService.createMovie(movie));

        assertTrue(exception.getMessage().contains("already exists"));
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    public void testGetMovieByTitleNotFound() {
        when(movieRepository.findByTitle("Nonexistent Movie")).thenReturn(Collections.emptyList());

        MovieNotFoundException exception = assertThrows(
                MovieNotFoundException.class,
                () -> movieService.getMovieByTitle("Nonexistent Movie"));

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    public void testGetMovieByIdNotFound() {
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        MovieNotFoundException exception = assertThrows(
                MovieNotFoundException.class,
                () -> movieService.getMovieById(999L));

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    public void testUpdateMovieNotFound() {
        Movie updatedMovie = new Movie("New Title", "Comedy", 95, 8.0, 2022);
        when(movieRepository.findByTitle("Nonexistent Movie")).thenReturn(Collections.emptyList());

        MovieNotFoundException exception = assertThrows(
                MovieNotFoundException.class,
                () -> movieService.updateMovie("Nonexistent Movie", updatedMovie));

        assertTrue(exception.getMessage().contains("not found"));
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    public void testUpdateMovieTitleAlreadyExists() {
        // Existing movie we want to update
        Movie existingMovie = new Movie("Old Title", "Drama", 90, 7.0, 2021);
        existingMovie.setId(1L);

        // Another existing movie with the title we want to change to
        Movie conflictingMovie = new Movie("New Title", "Comedy", 100, 8.5, 2022);
        conflictingMovie.setId(2L);

        // Movie with updated data
        Movie updatedMovie = new Movie("New Title", "Action", 95, 8.0, 2022);

        when(movieRepository.findByTitle("Old Title")).thenReturn(Collections.singletonList(existingMovie));
        when(movieRepository.findByTitle("New Title")).thenReturn(Collections.singletonList(conflictingMovie));

        MovieAlreadyExistsException exception = assertThrows(
                MovieAlreadyExistsException.class,
                () -> movieService.updateMovie("Old Title", updatedMovie));

        assertTrue(exception.getMessage().contains("already exists"));
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    public void testDeleteMovieNotFound() {
        when(movieRepository.findByTitle("Nonexistent Movie")).thenReturn(Collections.emptyList());

        MovieNotFoundException exception = assertThrows(
                MovieNotFoundException.class,
                () -> movieService.deleteMovie("Nonexistent Movie"));

        assertTrue(exception.getMessage().contains("not found"));
        verify(movieRepository, never()).delete(any(Movie.class));
    }

    @Test
    public void testUpdateMovieNoChanges() {
        Movie existingMovie = new Movie("Test Movie", "Drama", 90, 7.0, 2021);
        existingMovie.setId(1L);

        // Same movie details
        Movie updatedMovie = new Movie("Test Movie", "Drama", 90, 7.0, 2021);

        when(movieRepository.findByTitle("Test Movie")).thenReturn(Collections.singletonList(existingMovie));

        movieService.updateMovie("Test Movie", updatedMovie);

        // Verify that save was called but no field values should change
        verify(movieRepository, times(1)).save(existingMovie);
    }

    @Test
    public void testUpdateMoviePartialChanges() {
        Movie existingMovie = new Movie("Test Movie", "Drama", 90, 7.0, 2021);
        existingMovie.setId(1L);

        // Only genre and duration change
        Movie updatedMovie = new Movie("Test Movie", "Comedy", 110, 7.0, 2021);

        when(movieRepository.findByTitle("Test Movie")).thenReturn(Collections.singletonList(existingMovie));

        movieService.updateMovie("Test Movie", updatedMovie);

        assertEquals("Comedy", existingMovie.getGenre());
        assertEquals(110, existingMovie.getDuration());
        verify(movieRepository, times(1)).save(existingMovie);
    }

    @Test
    public void testGetMovieByTitleMultipleResults() {
        // This tests the behavior when multiple movies with the same title exist (edge
        // case)
        // The service should return the first one
        Movie movie1 = new Movie("Duplicate Title", "Drama", 100, 7.5, 2021);
        movie1.setId(1L);
        Movie movie2 = new Movie("Duplicate Title", "Action", 110, 8.0, 2022);
        movie2.setId(2L);

        List<Movie> movies = new ArrayList<>();
        movies.add(movie1);
        movies.add(movie2);

        when(movieRepository.findByTitle("Duplicate Title")).thenReturn(movies);

        Movie result = movieService.getMovieByTitle("Duplicate Title");

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }
}
