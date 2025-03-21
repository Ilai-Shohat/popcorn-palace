package com.att.tdp.popcorn_palace.mapper;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO;
import com.att.tdp.popcorn_palace.exception.MovieNotFoundException;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShowtimeMapperTest {

    @Mock
    private MovieService movieService;

    @InjectMocks
    private ShowtimeMapper showtimeMapper;

    private Movie movie;
    private Showtime showtime;
    private ShowtimeDTO showtimeDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test data
        movie = new Movie("Test Movie", "Action", 120, 8.0, 2022);
        movie.setId(1L);

        showtime = new Showtime(movie, "Theater1",
                Instant.parse("2023-03-01T10:00:00Z"),
                Instant.parse("2023-03-01T12:00:00Z"), 15.0);
        showtime.setId(1L);

        showtimeDTO = new ShowtimeDTO(
                1L,
                1L,
                15.0,
                "Theater1",
                Instant.parse("2023-03-01T10:00:00Z"),
                Instant.parse("2023-03-01T12:00:00Z"));
    }

    @Test
    public void testToDTOSuccess() {
        ShowtimeDTO result = showtimeMapper.toDTO(showtime);

        assertNotNull(result);
        assertEquals(showtime.getId(), result.getId());
        assertEquals(showtime.getMovie().getId(), result.getMovieId());
        assertEquals(showtime.getTheater(), result.getTheater());
        assertEquals(showtime.getPrice(), result.getPrice());
        assertEquals(showtime.getStartTime(), result.getStartTime());
        assertEquals(showtime.getEndTime(), result.getEndTime());
    }

    @Test
    public void testToDTOWithNullShowtime() {
        ShowtimeDTO result = showtimeMapper.toDTO(null);
        assertNull(result);
    }

    @Test
    public void testFromDTOSuccess() {
        when(movieService.getMovieById(1L)).thenReturn(movie);

        Showtime result = showtimeMapper.fromDTO(showtimeDTO);

        assertNotNull(result);
        assertEquals(movie, result.getMovie());
        assertEquals(showtimeDTO.getTheater(), result.getTheater());
        assertEquals(showtimeDTO.getPrice(), result.getPrice());
        assertEquals(showtimeDTO.getStartTime(), result.getStartTime());
        assertEquals(showtimeDTO.getEndTime(), result.getEndTime());

        verify(movieService).getMovieById(1L);
    }

    @Test
    public void testFromDTOWithNullDTO() {
        Showtime result = showtimeMapper.fromDTO(null);
        assertNull(result);
    }

    @Test
    public void testFromDTOWithNonExistentMovie() {
        when(movieService.getMovieById(999L)).thenReturn(null);

        showtimeDTO.setMovieId(999L);

        MovieNotFoundException exception = assertThrows(
                MovieNotFoundException.class,
                () -> showtimeMapper.fromDTO(showtimeDTO));

        assertTrue(exception.getMessage().contains("not found"));
        verify(movieService).getMovieById(999L);
    }
}
