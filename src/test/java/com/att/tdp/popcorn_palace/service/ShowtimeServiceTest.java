package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.exception.InvalidTimeRangeException;
import com.att.tdp.popcorn_palace.exception.MovieNotFoundException;
import com.att.tdp.popcorn_palace.exception.OverlappingShowtimeException;
import com.att.tdp.popcorn_palace.exception.ShowtimeNotFoundException;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShowtimeServiceTest {

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private MovieService movieService;

    @InjectMocks
    private ShowtimeService showtimeService;

    private Movie movie;
    private Showtime showtime;
    private final Long showtimeId = 1L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        movie = new Movie("Test Movie", "Action", 120, 8.0, 2022);
        movie.setId(1L);

        showtime = new Showtime(movie, "Theater1",
                Instant.parse("2023-03-01T10:00:00Z"),
                Instant.parse("2023-03-01T12:00:00Z"), 15.0);
        showtime.setId(showtimeId);
    }

    @Test
    public void testCreateShowtimePositive() {
        Showtime showtime = new Showtime(movie, "Theater1",
                Instant.parse("2023-03-01T10:00:00Z"), Instant.parse("2023-03-01T12:00:00Z"), 15.0);

        when(movieService.getMovieById(movie.getId())).thenReturn(movie);
        when(showtimeRepository.findOverlappingShowtimes(eq("Theater1"), any(), any(), isNull()))
                .thenReturn(Collections.emptyList());
        when(showtimeRepository.save(showtime)).thenReturn(showtime);

        Showtime result = showtimeService.createShowtime(showtime);
        assertNotNull(result);
        assertEquals("Theater1", result.getTheater());
        verify(showtimeRepository, times(1)).save(showtime);
    }

    @Test
    public void testUpdateShowtimePositive() {
        Long showtimeId = 1L;
        Showtime existingShowtime = new Showtime(movie, "Theater1",
                Instant.parse("2023-03-01T10:00:00Z"), Instant.parse("2023-03-01T12:00:00Z"), 15.0);
        existingShowtime.setId(showtimeId);

        Showtime updatedShowtime = new Showtime(movie, "Theater2",
                Instant.parse("2023-03-01T13:00:00Z"), Instant.parse("2023-03-01T15:00:00Z"), 20.0);

        when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.of(existingShowtime));
        when(movieService.getMovieById(movie.getId())).thenReturn(movie);
        when(showtimeRepository.findOverlappingShowtimes(eq("Theater2"), any(), any(), eq(showtimeId)))
                .thenReturn(Collections.emptyList());
        when(showtimeRepository.save(existingShowtime)).thenReturn(updatedShowtime);

        Showtime result = showtimeService.updateShowtime(showtimeId, updatedShowtime);
        assertNotNull(result);
        assertEquals("Theater2", result.getTheater());
    }

    @Test
    public void testDeleteShowtimePositive() {
        Long showtimeId = 1L;
        Showtime showtime = new Showtime(movie, "Theater1",
                Instant.parse("2023-03-01T10:00:00Z"), Instant.parse("2023-03-01T12:00:00Z"), 15.0);
        showtime.setId(showtimeId);

        when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.of(showtime));
        doNothing().when(showtimeRepository).delete(showtime);

        showtimeService.deleteShowtime(showtimeId);
        verify(showtimeRepository, times(1)).delete(showtime);
    }

    // Additional positive tests
    @Test
    public void testGetAllShowtimesPositive() {
        List<Showtime> showtimeList = new ArrayList<>();
        showtimeList.add(showtime);

        Showtime anotherShowtime = new Showtime(movie, "Theater2",
                Instant.parse("2023-03-01T14:00:00Z"),
                Instant.parse("2023-03-01T16:00:00Z"), 18.0);
        anotherShowtime.setId(2L);
        showtimeList.add(anotherShowtime);

        when(showtimeRepository.findAll()).thenReturn(showtimeList);

        List<Showtime> result = showtimeService.getAllShowtimes();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(showtimeRepository, times(1)).findAll();
    }

    @Test
    public void testShowtimeExistsPositive() {
        when(showtimeRepository.existsById(showtimeId)).thenReturn(true);

        boolean exists = showtimeService.showtimeExists(showtimeId);

        assertTrue(exists);
        verify(showtimeRepository, times(1)).existsById(showtimeId);
    }

    @Test
    public void testGetShowtimeByIdPositive() {
        when(showtimeRepository.existsById(showtimeId)).thenReturn(true);
        when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.of(showtime));

        Showtime result = showtimeService.getShowtimeById(showtimeId);

        assertNotNull(result);
        assertEquals(showtimeId, result.getId());
        assertEquals("Theater1", result.getTheater());
    }

    // Negative test cases
    @Test
    public void testGetAllShowtimesEmptyList() {
        when(showtimeRepository.findAll()).thenReturn(Collections.emptyList());

        List<Showtime> result = showtimeService.getAllShowtimes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testShowtimeExistsNegative() {
        when(showtimeRepository.existsById(999L)).thenReturn(false);

        boolean exists = showtimeService.showtimeExists(999L);

        assertFalse(exists);
    }

    @Test
    public void testGetShowtimeByIdNotFound() {
        when(showtimeRepository.existsById(999L)).thenReturn(false);

        ShowtimeNotFoundException exception = assertThrows(
                ShowtimeNotFoundException.class,
                () -> showtimeService.getShowtimeById(999L));

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    public void testCreateShowtimeWithNullMovie() {
        Showtime invalidShowtime = new Showtime(null, "Theater1",
                Instant.parse("2023-03-01T10:00:00Z"),
                Instant.parse("2023-03-01T12:00:00Z"), 15.0);

        MovieNotFoundException exception = assertThrows(
                MovieNotFoundException.class,
                () -> showtimeService.createShowtime(invalidShowtime));

        assertTrue(exception.getMessage().contains("required"));
        verify(showtimeRepository, never()).save(any(Showtime.class));
    }

    @Test
    public void testCreateShowtimeWithNonExistentMovie() {
        Movie nonExistentMovie = new Movie("Non-existent Movie", "Action", 120, 8.0, 2022);
        nonExistentMovie.setId(999L);

        Showtime invalidShowtime = new Showtime(nonExistentMovie, "Theater1",
                Instant.parse("2023-03-01T10:00:00Z"),
                Instant.parse("2023-03-01T12:00:00Z"), 15.0);

        when(movieService.getMovieById(999L)).thenThrow(new MovieNotFoundException("Movie not found"));

        MovieNotFoundException exception = assertThrows(
                MovieNotFoundException.class,
                () -> showtimeService.createShowtime(invalidShowtime));

        assertTrue(exception.getMessage().contains("not found"));
        verify(showtimeRepository, never()).save(any(Showtime.class));
    }

    @Test
    public void testCreateShowtimeWithNullStartTime() {
        Showtime invalidShowtime = new Showtime(movie, "Theater1",
                null,
                Instant.parse("2023-03-01T12:00:00Z"), 15.0);

        InvalidTimeRangeException exception = assertThrows(
                InvalidTimeRangeException.class,
                () -> showtimeService.createShowtime(invalidShowtime));

        assertTrue(exception.getMessage().contains("Start time cannot be null"));
        verify(showtimeRepository, never()).save(any(Showtime.class));
    }

    @Test
    public void testCreateShowtimeWithNullEndTime() {
        Showtime invalidShowtime = new Showtime(movie, "Theater1",
                Instant.parse("2023-03-01T10:00:00Z"),
                null, 15.0);

        InvalidTimeRangeException exception = assertThrows(
                InvalidTimeRangeException.class,
                () -> showtimeService.createShowtime(invalidShowtime));

        assertTrue(exception.getMessage().contains("End time cannot be null"));
        verify(showtimeRepository, never()).save(any(Showtime.class));
    }

    @Test
    public void testCreateShowtimeWithInvalidTimeRange() {
        // End time is before start time
        Showtime invalidShowtime = new Showtime(movie, "Theater1",
                Instant.parse("2023-03-01T12:00:00Z"),
                Instant.parse("2023-03-01T10:00:00Z"), 15.0);

        when(movieService.getMovieById(movie.getId())).thenReturn(movie);

        InvalidTimeRangeException exception = assertThrows(
                InvalidTimeRangeException.class,
                () -> showtimeService.createShowtime(invalidShowtime));

        assertTrue(exception.getMessage().contains("End time must be after start time"));
        verify(showtimeRepository, never()).save(any(Showtime.class));
    }

    @Test
    public void testCreateShowtimeWithSameStartEndTime() {
        // Same start and end time
        Instant sameTime = Instant.parse("2023-03-01T10:00:00Z");
        Showtime invalidShowtime = new Showtime(movie, "Theater1",
                sameTime, sameTime, 15.0);

        when(movieService.getMovieById(movie.getId())).thenReturn(movie);

        InvalidTimeRangeException exception = assertThrows(
                InvalidTimeRangeException.class,
                () -> showtimeService.createShowtime(invalidShowtime));

        assertTrue(exception.getMessage().contains("End time must be after start time"));
        verify(showtimeRepository, never()).save(any(Showtime.class));
    }

    @Test
    public void testCreateShowtimeWithOverlappingShowtime() {
        Showtime newShowtime = new Showtime(movie, "Theater1",
                Instant.parse("2023-03-01T11:00:00Z"),
                Instant.parse("2023-03-01T13:00:00Z"), 15.0);

        List<Showtime> overlappingShowtimes = new ArrayList<>();
        overlappingShowtimes.add(showtime);

        when(movieService.getMovieById(movie.getId())).thenReturn(movie);
        when(showtimeRepository.findOverlappingShowtimes(
                eq("Theater1"),
                eq(Instant.parse("2023-03-01T11:00:00Z")),
                eq(Instant.parse("2023-03-01T13:00:00Z")),
                isNull()))
                .thenReturn(overlappingShowtimes);

        OverlappingShowtimeException exception = assertThrows(
                OverlappingShowtimeException.class,
                () -> showtimeService.createShowtime(newShowtime));

        assertTrue(exception.getMessage().contains("overlaps"));
        verify(showtimeRepository, never()).save(any(Showtime.class));
    }

    @Test
    public void testUpdateShowtimeNotFound() {
        when(showtimeRepository.findById(999L)).thenReturn(Optional.empty());

        Showtime updatedShowtime = new Showtime(movie, "Theater2",
                Instant.parse("2023-03-01T13:00:00Z"),
                Instant.parse("2023-03-01T15:00:00Z"), 20.0);

        ShowtimeNotFoundException exception = assertThrows(
                ShowtimeNotFoundException.class,
                () -> showtimeService.updateShowtime(999L, updatedShowtime));

        assertTrue(exception.getMessage().contains("not found"));
        verify(showtimeRepository, never()).save(any(Showtime.class));
    }

    @Test
    public void testUpdateShowtimeWithInvalidTimeRange() {
        Showtime updatedShowtime = new Showtime(movie, "Theater2",
                Instant.parse("2023-03-01T15:00:00Z"),
                Instant.parse("2023-03-01T13:00:00Z"), 20.0);

        when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.of(showtime));
        when(movieService.getMovieById(movie.getId())).thenReturn(movie);

        InvalidTimeRangeException exception = assertThrows(
                InvalidTimeRangeException.class,
                () -> showtimeService.updateShowtime(showtimeId, updatedShowtime));

        assertTrue(exception.getMessage().contains("End time must be after start time"));
        verify(showtimeRepository, never()).save(any(Showtime.class));
    }

    @Test
    public void testUpdateShowtimeWithOverlappingShowtime() {
        Showtime updatedShowtime = new Showtime(movie, "Theater2",
                Instant.parse("2023-03-01T13:00:00Z"),
                Instant.parse("2023-03-01T15:00:00Z"), 20.0);

        Showtime overlappingShowtime = new Showtime(movie, "Theater2",
                Instant.parse("2023-03-01T14:00:00Z"),
                Instant.parse("2023-03-01T16:00:00Z"), 18.0);
        overlappingShowtime.setId(2L);

        List<Showtime> overlappingShowtimes = new ArrayList<>();
        overlappingShowtimes.add(overlappingShowtime);

        when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.of(showtime));
        when(movieService.getMovieById(movie.getId())).thenReturn(movie);
        when(showtimeRepository.findOverlappingShowtimes(
                eq("Theater2"),
                eq(Instant.parse("2023-03-01T13:00:00Z")),
                eq(Instant.parse("2023-03-01T15:00:00Z")),
                eq(showtimeId)))
                .thenReturn(overlappingShowtimes);

        OverlappingShowtimeException exception = assertThrows(
                OverlappingShowtimeException.class,
                () -> showtimeService.updateShowtime(showtimeId, updatedShowtime));

        assertTrue(exception.getMessage().contains("overlaps"));
        verify(showtimeRepository, never()).save(any(Showtime.class));
    }

    @Test
    public void testDeleteShowtimeNotFound() {
        when(showtimeRepository.findById(999L)).thenReturn(Optional.empty());

        ShowtimeNotFoundException exception = assertThrows(
                ShowtimeNotFoundException.class,
                () -> showtimeService.deleteShowtime(999L));

        assertTrue(exception.getMessage().contains("not found"));
        verify(showtimeRepository, never()).delete(any(Showtime.class));
    }

    @Test
    public void testUpdateShowtimeNoChanges() {
        Showtime existingShowtime = new Showtime(movie, "Theater1",
                Instant.parse("2023-03-01T10:00:00Z"),
                Instant.parse("2023-03-01T12:00:00Z"), 15.0);
        existingShowtime.setId(showtimeId);

        // Same showtime details
        Showtime updatedShowtime = new Showtime(movie, "Theater1",
                Instant.parse("2023-03-01T10:00:00Z"),
                Instant.parse("2023-03-01T12:00:00Z"), 15.0);

        when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.of(existingShowtime));
        when(movieService.getMovieById(movie.getId())).thenReturn(movie);
        when(showtimeRepository.findOverlappingShowtimes(
                eq("Theater1"),
                eq(Instant.parse("2023-03-01T10:00:00Z")),
                eq(Instant.parse("2023-03-01T12:00:00Z")),
                eq(showtimeId)))
                .thenReturn(Collections.emptyList());
        when(showtimeRepository.save(existingShowtime)).thenReturn(existingShowtime);

        Showtime result = showtimeService.updateShowtime(showtimeId, updatedShowtime);

        assertNotNull(result);
        assertEquals("Theater1", result.getTheater());
        assertEquals(15.0, result.getPrice());
        verify(showtimeRepository, times(1)).save(existingShowtime);
    }

    @Test
    public void testUpdateShowtimePartialChanges() {
        Showtime existingShowtime = new Showtime(movie, "Theater1",
                Instant.parse("2023-03-01T10:00:00Z"),
                Instant.parse("2023-03-01T12:00:00Z"), 15.0);
        existingShowtime.setId(showtimeId);

        // Only theater and price changes
        Showtime updatedShowtime = new Showtime(movie, "Theater2",
                Instant.parse("2023-03-01T10:00:00Z"),
                Instant.parse("2023-03-01T12:00:00Z"), 20.0);

        when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.of(existingShowtime));
        when(movieService.getMovieById(movie.getId())).thenReturn(movie);
        when(showtimeRepository.findOverlappingShowtimes(
                eq("Theater2"),
                eq(Instant.parse("2023-03-01T10:00:00Z")),
                eq(Instant.parse("2023-03-01T12:00:00Z")),
                eq(showtimeId)))
                .thenReturn(Collections.emptyList());
        when(showtimeRepository.save(existingShowtime)).thenReturn(existingShowtime);

        Showtime result = showtimeService.updateShowtime(showtimeId, updatedShowtime);

        assertNotNull(result);
        assertEquals("Theater2", existingShowtime.getTheater());
        assertEquals(20.0, existingShowtime.getPrice());
        verify(showtimeRepository, times(1)).save(existingShowtime);
    }
}
