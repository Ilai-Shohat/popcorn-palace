package com.att.tdp.popcorn_palace.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.att.tdp.popcorn_palace.dto.MovieDTO;
import com.att.tdp.popcorn_palace.exception.MovieAlreadyExistsException;
import com.att.tdp.popcorn_palace.exception.MovieNotFoundException;
import com.att.tdp.popcorn_palace.mapper.MovieMapper;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(MovieController.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MovieService movieService;

    @MockitoBean
    private MovieMapper movieMapper;

    private MovieDTO validMovieDTO;
    private Movie validMovie;

    @BeforeEach
    public void setup() {
        validMovieDTO = new MovieDTO(1L, "Test Movie", "Action", 120, 8.5, 2022);
        validMovie = new Movie("Test Movie", "Action", 120, 8.5, 2022);
        validMovie.setId(1L);

        // Default mapping behavior
        when(movieMapper.toDTO(any(Movie.class))).thenAnswer(invocation -> {
            Movie movie = invocation.getArgument(0);

            return new MovieDTO(movie.getId(), movie.getTitle(), movie.getGenre(), movie.getDuration(),
                    movie.getRating(), movie.getReleaseYear());
        });

        when(movieMapper.fromDTO(any(MovieDTO.class))).thenAnswer(invocation -> {
            MovieDTO dto = invocation.getArgument(0);
            Movie movie = new Movie(dto.getTitle(), dto.getGenre(), dto.getDuration(), dto.getRating(),
                    dto.getReleaseYear());
            movie.setId(dto.getId());
            return movie;
        });
    }

    @Nested
    @DisplayName("GET /movies/all - Get All Movies Tests")
    class GetAllMoviesTests {

        @Test
        @DisplayName("Should return empty list when no movies exist")
        public void testGetAllMoviesEmptyList() throws Exception {
            // Given
            when(movieService.getAllMovies()).thenReturn(new ArrayList<>());

            // When/Then
            mockMvc.perform(get("/movies/all"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[]"));
        }

        @Test
        @DisplayName("Should handle service exception during getAllMovies")
        public void testGetAllMoviesServiceException() throws Exception {
            // Given
            when(movieService.getAllMovies()).thenThrow(new RuntimeException("Database connection failed"));

            // When/Then
            mockMvc.perform(get("/movies/all"))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    @DisplayName("POST /movies - Create Movie Tests")
    class CreateMovieTests {

        @Test
        @DisplayName("Should return 400 when creating movie with null title")
        public void testCreateMovieWithNullTitle() throws Exception {
            // Given
            MovieDTO invalidDTO = new MovieDTO(null, null, "Action", 120, 8.5, 2022);

            // When/Then
            mockMvc.perform(post("/movies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Validation Error"));
        }

        @Test
        @DisplayName("Should return 400 when creating movie with invalid rating")
        public void testCreateMovieWithInvalidRating() throws Exception {
            // Given
            MovieDTO invalidDTO = new MovieDTO(null, "Test Movie", "Action", 120, 11.5, 2022);

            // When/Then
            mockMvc.perform(post("/movies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Validation Error"));
        }

        @Test
        @DisplayName("Should return 400 when creating movie with negative duration")
        public void testCreateMovieWithNegativeDuration() throws Exception {
            // Given
            MovieDTO invalidDTO = new MovieDTO(null, "Test Movie", "Action", -10, 8.5, 2022);

            // When/Then
            mockMvc.perform(post("/movies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Validation Error"));
        }

        @Test
        @DisplayName("Should return 400 when creating movie with future release year")
        public void testCreateMovieWithFutureReleaseYear() throws Exception {
            // Given
            MovieDTO invalidDTO = new MovieDTO(null, "Test Movie", "Action", 120, 8.5, 2030);

            // When/Then
            mockMvc.perform(post("/movies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Validation Error"));
        }

        @Test
        @DisplayName("Should return 409 when creating movie with existing title")
        public void testCreateMovieWithExistingTitle() throws Exception {
            // Given
            when(movieService.createMovie(any(Movie.class)))
                    .thenThrow(new MovieAlreadyExistsException(
                            "Movie with title 'Test Movie' already exists"));

            // When/Then
            mockMvc.perform(post("/movies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validMovieDTO)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message")
                            .value("Movie with title 'Test Movie' already exists"));
        }

        @Test
        @DisplayName("Should handle internal server error during creation")
        public void testCreateMovieInternalServerError() throws Exception {
            // Given
            when(movieService.createMovie(any(Movie.class)))
                    .thenThrow(new RuntimeException("Database error"));

            // When/Then
            mockMvc.perform(post("/movies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validMovieDTO)))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    @DisplayName("POST /movies/update/{movieTitle} - Update Movie Tests")
    class UpdateMovieTests {

        @Test
        @DisplayName("Should return 404 when updating non-existent movie")
        public void testUpdateMovieNotFound() throws Exception {
            // Given
            doThrow(new MovieNotFoundException("Movie with title 'Non-Existent Movie' not found"))
                    .when(movieService).updateMovie(eq("Non-Existent Movie"), any(Movie.class));

            // When/Then
            mockMvc.perform(post("/movies/update/Non-Existent Movie")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validMovieDTO)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message")
                            .value("Movie with title 'Non-Existent Movie' not found"));
        }

        @Test
        @DisplayName("Should return 409 when updating to an existing title")
        public void testUpdateMovieWithExistingTitle() throws Exception {
            // Given
            doThrow(new MovieAlreadyExistsException(
                    "Cannot update: Movie with title 'Existing Title' already exists"))
                    .when(movieService).updateMovie(anyString(), any(Movie.class));

            // When/Then
            mockMvc.perform(post("/movies/update/Original Title")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validMovieDTO)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message")
                            .value("Cannot update: Movie with title 'Existing Title' already exists"));
        }

        @Test
        @DisplayName("Should return 400 when updating with invalid data")
        public void testUpdateMovieWithInvalidData() throws Exception {
            // Given
            MovieDTO invalidDTO = new MovieDTO(1L, "Test Movie", "Action", -10, 8.5, 2022);

            // When/Then
            mockMvc.perform(post("/movies/update/Test Movie")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Validation Error"));
        }

        @Test
        @DisplayName("Should handle concurrent update conflict")
        public void testUpdateMovieConcurrentModification() throws Exception {
            // Given
            doThrow(new RuntimeException("Concurrent modification error"))
                    .when(movieService).updateMovie(anyString(), any(Movie.class));

            // When/Then
            mockMvc.perform(post("/movies/update/Test Movie")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validMovieDTO)))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    @DisplayName("DELETE /movies/{movieTitle} - Delete Movie Tests")
    class DeleteMovieTests {

        @Test
        @DisplayName("Should return 404 when deleting non-existent movie")
        public void testDeleteMovieNotFound() throws Exception {
            // Given
            doThrow(new MovieNotFoundException("Movie with title 'Non-Existent Movie' not found"))
                    .when(movieService).deleteMovie("Non-Existent Movie");

            // When/Then
            mockMvc.perform(delete("/movies/Non-Existent Movie"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message")
                            .value("Movie with title 'Non-Existent Movie' not found"));
        }

        // @Test
        // @DisplayName("Should handle foreign key constraint violation when deleting")
        // public void testDeleteMovieWithActiveShowtimes() throws Exception {
        // // Given
        // doThrow(new IllegalArgumentException("Cannot delete movie that has active
        // showtimes"))
        // .when(movieService).deleteMovie("Test Movie");

        // // When/Then
        // mockMvc.perform(delete("/movies/Test Movie"))
        // .andExpect(status().isBadRequest())
        // .andExpect(jsonPath("$.message")
        // .value("Cannot delete movie that has active showtimes"));
        // }

        @Test
        @DisplayName("Should handle database exception during deletion")
        public void testDeleteMovieDatabaseError() throws Exception {
            // Given
            doThrow(new RuntimeException("Database connection error"))
                    .when(movieService).deleteMovie("Test Movie");

            // When/Then
            mockMvc.perform(delete("/movies/Test Movie"))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        // @Test
        // @DisplayName("Should handle movie titles with special characters")
        // public void testMovieWithSpecialCharactersInTitle() throws Exception {
        // // Given
        // String complexTitle = "Star Wars: Episode IV - A New Hope (1977) [Director's
        // Cut]";
        // MovieDTO specialDTO = new MovieDTO(1L, complexTitle, "Sci-Fi", 121, 8.7,
        // 1977);
        // Movie specialMovie = new Movie(complexTitle, "Sci-Fi", 121, 8.7, 1977);
        // specialMovie.setId(1L);

        // when(movieService.createMovie(any(Movie.class))).thenReturn(specialMovie);
        // when(movieMapper.toDTO(specialMovie)).thenReturn(specialDTO);

        // // When/Then
        // mockMvc.perform(post("/movies")
        // .contentType(MediaType.APPLICATION_JSON)
        // .content(objectMapper.writeValueAsString(specialDTO)))
        // .andExpect(status().isOk())
        // .andExpect(jsonPath("$.title").value(complexTitle));
        // }

        // @Test
        // @DisplayName("Should handle extremely long movie titles")
        // public void testMovieWithExtremelyLongTitle() throws Exception {
        // // Given
        // StringBuilder longTitleBuilder = new StringBuilder();
        // for (int i = 0; i < 50; i++) {
        // longTitleBuilder.append("Very Long Movie Title Part ").append(i).append(" ");
        // }
        // String longTitle = longTitleBuilder.toString().trim();

        // MovieDTO longTitleDTO = new MovieDTO(1L, longTitle, "Drama", 180, 7.5, 2020);

        // // When/Then
        // mockMvc.perform(post("/movies")
        // .contentType(MediaType.APPLICATION_JSON)
        // .content(objectMapper.writeValueAsString(longTitleDTO)))
        // .andExpect(status().isOk());
        // }

        @Test
        @DisplayName("Should handle boundary values for rating")
        public void testMovieWithBoundaryRatings() throws Exception {
            // Test with minimum valid rating (0)
            MovieDTO minRatingDTO = new MovieDTO(1L, "Minimum Rating Movie", "Horror", 90, 0.0, 2020);
            mockMvc.perform(post("/movies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(minRatingDTO)))
                    .andExpect(status().isOk());

            // Test with maximum valid rating (10)
            MovieDTO maxRatingDTO = new MovieDTO(1L, "Maximum Rating Movie", "Comedy", 90, 10.0, 2020);
            mockMvc.perform(post("/movies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(maxRatingDTO)))
                    .andExpect(status().isOk());

            // Test with just beyond maximum rating (10.1)
            MovieDTO invalidMaxRatingDTO = new MovieDTO(1L, "Invalid Max Rating Movie", "Comedy", 90, 10.1,
                    2020);
            mockMvc.perform(post("/movies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidMaxRatingDTO)))
                    .andExpect(status().isBadRequest());

            // Test with just below minimum rating (-0.1)
            MovieDTO invalidMinRatingDTO = new MovieDTO(1L, "Invalid Min Rating Movie", "Comedy", 90, -0.1,
                    2020);
            mockMvc.perform(post("/movies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidMinRatingDTO)))
                    .andExpect(status().isBadRequest());
        }
    }
}
