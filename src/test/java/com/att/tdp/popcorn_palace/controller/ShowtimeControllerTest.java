package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO;
import com.att.tdp.popcorn_palace.exception.InvalidTimeRangeException;
import com.att.tdp.popcorn_palace.exception.MovieNotFoundException;
import com.att.tdp.popcorn_palace.exception.OverlappingShowtimeException;
import com.att.tdp.popcorn_palace.exception.ShowtimeNotFoundException;
import com.att.tdp.popcorn_palace.mapper.ShowtimeMapper;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShowtimeController.class)
public class ShowtimeControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private ShowtimeService showtimeService;

        @MockitoBean
        private ShowtimeMapper showtimeMapper;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        public void testGetShowtimeById() throws Exception {
                Long showtimeId = 1L;

                // Create movie with ID
                Movie movie = new Movie("Movie1", "Action", 120, 8.5, 2021);
                movie.setId(1L);

                // Create showtime with ID
                Showtime showtime = new Showtime(movie, "Theater1",
                                Instant.parse("2023-03-01T10:00:00Z"),
                                Instant.parse("2023-03-01T12:00:00Z"), 15.0);
                showtime.setId(showtimeId);

                // Create DTO directly with all fields
                ShowtimeDTO dto = new ShowtimeDTO(
                                showtimeId,
                                movie.getId(),
                                15.0,
                                "Theater1",
                                Instant.parse("2023-03-01T10:00:00Z"),
                                Instant.parse("2023-03-01T12:00:00Z"));

                Mockito.when(showtimeService.getShowtimeById(showtimeId)).thenReturn(showtime);
                Mockito.when(showtimeMapper.toDTO(showtime)).thenReturn(dto);

                mockMvc.perform(get("/showtimes/{showtimeId}", showtimeId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.theater").value("Theater1"));
        }

        @Test
        @DisplayName("Test GET showtime with non-existent ID returns 404")
        public void testGetShowtimeByIdNotFound() throws Exception {
                Long nonExistentId = 999L;

                Mockito.when(showtimeService.getShowtimeById(nonExistentId))
                                .thenThrow(new ShowtimeNotFoundException(
                                                "Showtime with ID " + nonExistentId + " not found"));

                mockMvc.perform(get("/showtimes/{showtimeId}", nonExistentId))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error").value("Not Found"))
                                .andExpect(jsonPath("$.message")
                                                .value("Showtime with ID " + nonExistentId + " not found"));
        }

        @Test
        public void testCreateShowtime() throws Exception {
                // Create movie with ID
                Movie movie = new Movie("Movie1", "Action", 120, 8.5, 2021);
                movie.setId(1L); // Set movie ID

                // Create showtime
                Showtime showtime = new Showtime(movie, "Theater1",
                                Instant.parse("2023-03-01T10:00:00Z"), Instant.parse("2023-03-01T12:00:00Z"), 15.0);

                // Create created showtime with ID
                Showtime createdShowtime = new Showtime(movie, "Theater1",
                                Instant.parse("2023-03-01T10:00:00Z"), Instant.parse("2023-03-01T12:00:00Z"), 15.0);
                createdShowtime.setId(1L); // Set showtime ID

                // Create DTOs
                ShowtimeDTO showtimeDTO = new ShowtimeDTO(null, 1L, 15.0, "Theater1",
                                Instant.parse("2023-03-01T10:00:00Z"), Instant.parse("2023-03-01T12:00:00Z"));

                ShowtimeDTO createdDTO = new ShowtimeDTO(1L, 1L, 15.0, "Theater1",
                                Instant.parse("2023-03-01T10:00:00Z"), Instant.parse("2023-03-01T12:00:00Z"));

                // Set up mocks
                Mockito.when(showtimeMapper.fromDTO(any(ShowtimeDTO.class))).thenReturn(showtime);
                Mockito.when(showtimeService.createShowtime(any(Showtime.class))).thenReturn(createdShowtime);
                Mockito.when(showtimeMapper.toDTO(createdShowtime)).thenReturn(createdDTO);

                mockMvc.perform(post("/showtimes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(showtimeDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L));
        }

        @Test
        @DisplayName("Test CREATE showtime with invalid data returns 400")
        public void testCreateShowtimeInvalidData() throws Exception {
                // Create DTO with invalid data (negative price)
                ShowtimeDTO invalidDTO = new ShowtimeDTO(
                                null, 1L, -15.0, "Theater1",
                                Instant.parse("2023-03-01T10:00:00Z"),
                                Instant.parse("2023-03-01T12:00:00Z"));

                mockMvc.perform(post("/showtimes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("Validation Error"));
        }

        @Test
        @DisplayName("Test CREATE showtime with overlapping times returns 409")
        public void testCreateShowtimeWithOverlappingTimes() throws Exception {
                // Create movie with ID
                Movie movie = new Movie("Movie1", "Action", 120, 8.5, 2021);
                movie.setId(1L);

                // Create showtime with overlapping times
                Showtime showtime = new Showtime(movie, "Theater1",
                                Instant.parse("2023-03-01T10:00:00Z"),
                                Instant.parse("2023-03-01T12:00:00Z"), 15.0);

                // Create DTO
                ShowtimeDTO showtimeDTO = new ShowtimeDTO(
                                null, 1L, 15.0, "Theater1",
                                Instant.parse("2023-03-01T10:00:00Z"),
                                Instant.parse("2023-03-01T12:00:00Z"));

                // Mock the service to throw OverlappingShowtimeException
                Mockito.when(showtimeMapper.fromDTO(any(ShowtimeDTO.class))).thenReturn(showtime);
                Mockito.when(showtimeService.createShowtime(any(Showtime.class)))
                                .thenThrow(new OverlappingShowtimeException(
                                                "Showtime overlaps with existing showtime in theater Theater1 from 2023-03-01T09:30:00Z to 2023-03-01T11:30:00Z"));

                mockMvc.perform(post("/showtimes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(showtimeDTO)))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.error").value("Conflict"));
        }

        @Test
        @DisplayName("Test CREATE showtime with invalid time range returns 400")
        public void testCreateShowtimeWithInvalidTimeRange() throws Exception {
                // Create movie with ID
                Movie movie = new Movie("Movie1", "Action", 120, 8.5, 2021);
                movie.setId(1L);

                // Create showtime with end time before start time
                Showtime showtime = new Showtime(movie, "Theater1",
                                Instant.parse("2023-03-01T12:00:00Z"),
                                Instant.parse("2023-03-01T10:00:00Z"), 15.0);

                // Create DTO with end time before start time
                ShowtimeDTO showtimeDTO = new ShowtimeDTO(
                                null, 1L, 15.0, "Theater1",
                                Instant.parse("2023-03-01T12:00:00Z"),
                                Instant.parse("2023-03-01T10:00:00Z"));

                // Mock the service to throw InvalidTimeRangeException
                Mockito.when(showtimeMapper.fromDTO(any(ShowtimeDTO.class))).thenReturn(showtime);
                Mockito.when(showtimeService.createShowtime(any(Showtime.class)))
                                .thenThrow(new InvalidTimeRangeException("End time must be after start time"));

                mockMvc.perform(post("/showtimes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(showtimeDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("Bad Request"))
                                .andExpect(jsonPath("$.message").value("End time must be after start time"));
        }

        @Test
        @DisplayName("Test CREATE showtime with non-existent movie returns 404")
        public void testCreateShowtimeWithNonExistentMovie() throws Exception {
                // Create DTO with non-existent movie ID
                ShowtimeDTO showtimeDTO = new ShowtimeDTO(
                                null, 999L, 15.0, "Theater1",
                                Instant.parse("2023-03-01T10:00:00Z"),
                                Instant.parse("2023-03-01T12:00:00Z"));

                // Mock the mapper to throw MovieNotFoundException
                Mockito.when(showtimeMapper.fromDTO(any(ShowtimeDTO.class)))
                                .thenThrow(new MovieNotFoundException("Movie with ID 999 not found"));

                mockMvc.perform(post("/showtimes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(showtimeDTO)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error").value("Not Found"))
                                .andExpect(jsonPath("$.message").value("Movie with ID 999 not found"));
        }

        @Test
        public void testUpdateShowtime() throws Exception {
                Long showtimeId = 1L;

                // Create movie with ID
                Movie movie = new Movie("Movie1", "Action", 120, 8.5, 2021);
                movie.setId(1L); // Set movie ID

                // Create showtime with ID
                Showtime showtime = new Showtime(movie, "Theater2",
                                Instant.parse("2023-03-01T13:00:00Z"), Instant.parse("2023-03-01T15:00:00Z"), 20.0);
                showtime.setId(showtimeId);

                // Create updated showtime with ID
                Showtime updatedShowtime = new Showtime(movie, "Theater2",
                                Instant.parse("2023-03-01T13:00:00Z"), Instant.parse("2023-03-01T15:00:00Z"), 20.0);
                updatedShowtime.setId(showtimeId);

                // Create DTOs directly
                ShowtimeDTO showtimeDTO = new ShowtimeDTO(showtimeId, 1L, 20.0, "Theater2",
                                Instant.parse("2023-03-01T13:00:00Z"), Instant.parse("2023-03-01T15:00:00Z"));

                ShowtimeDTO updatedDTO = new ShowtimeDTO(showtimeId, 1L, 20.0, "Theater2",
                                Instant.parse("2023-03-01T13:00:00Z"), Instant.parse("2023-03-01T15:00:00Z"));

                // Set up mocks
                Mockito.when(showtimeMapper.fromDTO(any(ShowtimeDTO.class))).thenReturn(showtime);
                Mockito.when(showtimeService.updateShowtime(eq(showtimeId), any(Showtime.class)))
                                .thenReturn(updatedShowtime);
                Mockito.when(showtimeMapper.toDTO(updatedShowtime)).thenReturn(updatedDTO);

                mockMvc.perform(post("/showtimes/update/{showtimeId}", showtimeId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(showtimeDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.theater").value("Theater2"));
        }

        @Test
        @DisplayName("Test UPDATE non-existent showtime returns 404")
        public void testUpdateShowtimeNotFound() throws Exception {
                Long nonExistentId = 999L;

                // Create valid DTO
                ShowtimeDTO showtimeDTO = new ShowtimeDTO(
                                nonExistentId, 1L, 15.0, "Theater1",
                                Instant.parse("2023-03-01T10:00:00Z"),
                                Instant.parse("2023-03-01T12:00:00Z"));

                // Create showtime entity from DTO
                Movie movie = new Movie("Movie1", "Action", 120, 8.5, 2021);
                movie.setId(1L);
                Showtime showtime = new Showtime(movie, "Theater1",
                                Instant.parse("2023-03-01T10:00:00Z"),
                                Instant.parse("2023-03-01T12:00:00Z"), 15.0);
                showtime.setId(nonExistentId);

                // Mock the service to throw ShowtimeNotFoundException
                Mockito.when(showtimeMapper.fromDTO(any(ShowtimeDTO.class))).thenReturn(showtime);
                Mockito.when(showtimeService.updateShowtime(eq(nonExistentId), any(Showtime.class)))
                                .thenThrow(new ShowtimeNotFoundException(
                                                "Showtime with ID " + nonExistentId + " not found"));

                mockMvc.perform(post("/showtimes/update/{showtimeId}", nonExistentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(showtimeDTO)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error").value("Not Found"))
                                .andExpect(jsonPath("$.message")
                                                .value("Showtime with ID " + nonExistentId + " not found"));
        }

        @Test
        @DisplayName("Test UPDATE showtime causing overlap returns 409")
        public void testUpdateShowtimeWithOverlappingTimes() throws Exception {
                Long showtimeId = 1L;

                // Create valid DTO that would cause overlap
                ShowtimeDTO showtimeDTO = new ShowtimeDTO(
                                showtimeId, 1L, 15.0, "Theater1",
                                Instant.parse("2023-03-01T14:00:00Z"),
                                Instant.parse("2023-03-01T16:00:00Z"));

                // Create showtime entity from DTO
                Movie movie = new Movie("Movie1", "Action", 120, 8.5, 2021);
                movie.setId(1L);
                Showtime showtime = new Showtime(movie, "Theater1",
                                Instant.parse("2023-03-01T14:00:00Z"),
                                Instant.parse("2023-03-01T16:00:00Z"), 15.0);
                showtime.setId(showtimeId);

                // Mock the service to throw OverlappingShowtimeException
                Mockito.when(showtimeMapper.fromDTO(any(ShowtimeDTO.class))).thenReturn(showtime);
                Mockito.when(showtimeService.updateShowtime(eq(showtimeId), any(Showtime.class)))
                                .thenThrow(new OverlappingShowtimeException(
                                                "Showtime overlaps with existing showtime in theater Theater1 from 2023-03-01T15:30:00Z to 2023-03-01T17:30:00Z"));

                mockMvc.perform(post("/showtimes/update/{showtimeId}", showtimeId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(showtimeDTO)))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.error").value("Conflict"));
        }

        @Test
        public void testDeleteShowtime() throws Exception {
                Long showtimeId = 1L;
                Mockito.doNothing().when(showtimeService).deleteShowtime(showtimeId);

                mockMvc.perform(delete("/showtimes/{showtimeId}", showtimeId))
                                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Test DELETE non-existent showtime returns 404")
        public void testDeleteShowtimeNotFound() throws Exception {
                Long nonExistentId = 999L;

                // Mock the service to throw ShowtimeNotFoundException
                doThrow(new ShowtimeNotFoundException("Showtime with ID " + nonExistentId + " not found"))
                                .when(showtimeService).deleteShowtime(nonExistentId);

                mockMvc.perform(delete("/showtimes/{showtimeId}", nonExistentId))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error").value("Not Found"))
                                .andExpect(jsonPath("$.message")
                                                .value("Showtime with ID " + nonExistentId + " not found"));
        }

        @Test
        @DisplayName("Test CREATE showtime with identical start and end times returns 400")
        public void testCreateShowtimeWithSameStartEndTime() throws Exception {
                // Create movie with ID
                Movie movie = new Movie("Movie1", "Action", 120, 8.5, 2021);
                movie.setId(1L);

                // Create instant for both start and end time
                Instant sameTime = Instant.parse("2023-03-01T10:00:00Z");

                // Create showtime with same start and end time
                Showtime showtime = new Showtime(movie, "Theater1", sameTime, sameTime, 15.0);

                // Create DTO with same start and end time
                ShowtimeDTO showtimeDTO = new ShowtimeDTO(null, 1L, 15.0, "Theater1", sameTime, sameTime);

                // Mock the service to throw InvalidTimeRangeException
                Mockito.when(showtimeMapper.fromDTO(any(ShowtimeDTO.class))).thenReturn(showtime);
                Mockito.when(showtimeService.createShowtime(any(Showtime.class)))
                                .thenThrow(new InvalidTimeRangeException("End time must be after start time"));

                mockMvc.perform(post("/showtimes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(showtimeDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("Bad Request"))
                                .andExpect(jsonPath("$.message").value("End time must be after start time"));
        }

        @Test
        @DisplayName("Test handling malformed JSON request")
        public void testMalformedJsonRequest() throws Exception {
                String malformedJson = "{\"movieId\": 1, \"price\": 15.0, malformed}";

                mockMvc.perform(post("/showtimes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(malformedJson))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Test CREATE showtime with missing required fields returns 400")
        public void testCreateShowtimeMissingRequiredFields() throws Exception {
                // Create DTO with missing required field (theater)
                ShowtimeDTO invalidDTO = new ShowtimeDTO();
                invalidDTO.setMovieId(1L);
                invalidDTO.setPrice(15.0);
                // Theater is intentionally missing
                invalidDTO.setStartTime(Instant.parse("2023-03-01T10:00:00Z"));
                invalidDTO.setEndTime(Instant.parse("2023-03-01T12:00:00Z"));

                mockMvc.perform(post("/showtimes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("Validation Error"))
                                .andExpect(jsonPath("$.errors[0]").value("theater: Theater is required"));
        }
}
