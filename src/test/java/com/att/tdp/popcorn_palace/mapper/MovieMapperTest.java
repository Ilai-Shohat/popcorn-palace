package com.att.tdp.popcorn_palace.mapper;

import com.att.tdp.popcorn_palace.dto.MovieDTO;
import com.att.tdp.popcorn_palace.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Movie Mapper Tests")
public class MovieMapperTest {

    private MovieMapper movieMapper;
    private Movie movie;
    private MovieDTO movieDTO;

    @BeforeEach
    public void setUp() {
        // Get the mapper instance from MapStruct
        movieMapper = Mappers.getMapper(MovieMapper.class);

        // Create test data
        movie = new Movie("The Dark Knight", "Action", 152, 9.0, 2008);
        movie.setId(1L);

        movieDTO = new MovieDTO(
                1L,
                "The Dark Knight",
                "Action",
                152,
                9.0,
                2008);
    }

    @Test
    @DisplayName("Test converting Movie entity to MovieDTO successfully")
    public void testToDTOSuccess() {
        MovieDTO result = movieMapper.toDTO(movie);

        assertNotNull(result);
        assertEquals(movie.getId(), result.getId());
        assertEquals(movie.getTitle(), result.getTitle());
        assertEquals(movie.getGenre(), result.getGenre());
        assertEquals(movie.getDuration(), result.getDuration());
        assertEquals(movie.getRating(), result.getRating());
        assertEquals(movie.getReleaseYear(), result.getReleaseYear());
    }

    @Test
    @DisplayName("Test handling null Movie when converting to DTO")
    public void testToDTOWithNullMovie() {
        MovieDTO result = movieMapper.toDTO(null);
        assertNull(result);
    }

    @Test
    @DisplayName("Test converting MovieDTO to Movie entity successfully")
    public void testFromDTOSuccess() {
        Movie result = movieMapper.fromDTO(movieDTO);

        assertNotNull(result);
        assertEquals(movieDTO.getId(), result.getId());
        assertEquals(movieDTO.getTitle(), result.getTitle());
        assertEquals(movieDTO.getGenre(), result.getGenre());
        assertEquals(movieDTO.getDuration(), result.getDuration());
        assertEquals(movieDTO.getRating(), result.getRating());
        assertEquals(movieDTO.getReleaseYear(), result.getReleaseYear());
    }

    @Test
    @DisplayName("Test handling null DTO when converting to Movie")
    public void testFromDTOWithNullDTO() {
        Movie result = movieMapper.fromDTO(null);
        assertNull(result);
    }

    @Test
    @DisplayName("Test full round-trip conversion (Movie -> DTO -> Movie)")
    public void testRoundTripConversion() {
        // Movie -> DTO -> Movie
        MovieDTO dto = movieMapper.toDTO(movie);
        Movie convertedMovie = movieMapper.fromDTO(dto);

        assertEquals(movie.getId(), convertedMovie.getId());
        assertEquals(movie.getTitle(), convertedMovie.getTitle());
        assertEquals(movie.getGenre(), convertedMovie.getGenre());
        assertEquals(movie.getDuration(), convertedMovie.getDuration());
        assertEquals(movie.getRating(), convertedMovie.getRating());
        assertEquals(movie.getReleaseYear(), convertedMovie.getReleaseYear());
    }

    @Test
    @DisplayName("Test converting DTO without ID to Movie entity")
    public void testDTOWithoutId() {
        MovieDTO dtoWithoutId = new MovieDTO(
                null,
                "Inception",
                "Sci-Fi",
                148,
                8.8,
                2010);

        Movie result = movieMapper.fromDTO(dtoWithoutId);

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(dtoWithoutId.getTitle(), result.getTitle());
        assertEquals(dtoWithoutId.getGenre(), result.getGenre());
        assertEquals(dtoWithoutId.getDuration(), result.getDuration());
        assertEquals(dtoWithoutId.getRating(), result.getRating());
        assertEquals(dtoWithoutId.getReleaseYear(), result.getReleaseYear());
    }

    @Test
    @DisplayName("Test converting Movie without ID to DTO")
    public void testMovieWithoutId() {
        Movie movieWithoutId = new Movie(
                "Inception",
                "Sci-Fi",
                148,
                8.8,
                2010);

        MovieDTO result = movieMapper.toDTO(movieWithoutId);

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(movieWithoutId.getTitle(), result.getTitle());
        assertEquals(movieWithoutId.getGenre(), result.getGenre());
        assertEquals(movieWithoutId.getDuration(), result.getDuration());
        assertEquals(movieWithoutId.getRating(), result.getRating());
        assertEquals(movieWithoutId.getReleaseYear(), result.getReleaseYear());
    }

    @Test
    @DisplayName("Test handling empty string values in conversion")
    public void testWithEmptyValues() {
        // Empty strings should be preserved, not converted to null
        Movie movieWithEmptyValues = new Movie("", "", 0, 0.0, 0);
        movieWithEmptyValues.setId(2L);

        MovieDTO result = movieMapper.toDTO(movieWithEmptyValues);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("", result.getTitle());
        assertEquals("", result.getGenre());
        assertEquals(0, result.getDuration());
        assertEquals(0.0, result.getRating());
        assertEquals(0, result.getReleaseYear());
    }
}
