package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ShowtimeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Test
    public void testFindOverlappingShowtimes() {
        // Create a movie
        Movie movie = new Movie("Test Movie", "Action", 120, 8.0, 2022);
        entityManager.persist(movie);

        // Create an existing showtime
        Showtime existingShowtime = new Showtime(movie, "Theater1",
                Instant.parse("2023-03-01T10:00:00Z"),
                Instant.parse("2023-03-01T12:00:00Z"), 15.0);
        entityManager.persist(existingShowtime);
        entityManager.flush();

        // Test case 1: No overlap - completely before
        List<Showtime> result1 = showtimeRepository.findOverlappingShowtimes(
                "Theater1",
                Instant.parse("2023-03-01T08:00:00Z"),
                Instant.parse("2023-03-01T09:30:00Z"),
                null);
        assertTrue(result1.isEmpty(), "Should not find any overlapping showtimes");

        // Test case 2: No overlap - completely after
        List<Showtime> result2 = showtimeRepository.findOverlappingShowtimes(
                "Theater1",
                Instant.parse("2023-03-01T12:30:00Z"),
                Instant.parse("2023-03-01T14:00:00Z"),
                null);
        assertTrue(result2.isEmpty(), "Should not find any overlapping showtimes");

        // Test case 3: Overlap - start time within existing showtime
        List<Showtime> result3 = showtimeRepository.findOverlappingShowtimes(
                "Theater1",
                Instant.parse("2023-03-01T11:00:00Z"),
                Instant.parse("2023-03-01T13:00:00Z"),
                null);
        assertFalse(result3.isEmpty(), "Should find an overlapping showtime");
        assertEquals(existingShowtime.getId(), result3.get(0).getId());

        // Test case 4: Overlap - end time within existing showtime
        List<Showtime> result4 = showtimeRepository.findOverlappingShowtimes(
                "Theater1",
                Instant.parse("2023-03-01T09:00:00Z"),
                Instant.parse("2023-03-01T11:00:00Z"),
                null);
        assertFalse(result4.isEmpty(), "Should find an overlapping showtime");
        assertEquals(existingShowtime.getId(), result4.get(0).getId());

        // Test case 5: Overlap - completely encompasses existing showtime
        List<Showtime> result5 = showtimeRepository.findOverlappingShowtimes(
                "Theater1",
                Instant.parse("2023-03-01T09:00:00Z"),
                Instant.parse("2023-03-01T13:00:00Z"),
                null);
        assertFalse(result5.isEmpty(), "Should find an overlapping showtime");
        assertEquals(existingShowtime.getId(), result5.get(0).getId());

        // Test case 6: Overlap - completely within existing showtime
        List<Showtime> result6 = showtimeRepository.findOverlappingShowtimes(
                "Theater1",
                Instant.parse("2023-03-01T10:30:00Z"),
                Instant.parse("2023-03-01T11:30:00Z"),
                null);
        assertFalse(result6.isEmpty(), "Should find an overlapping showtime");
        assertEquals(existingShowtime.getId(), result6.get(0).getId());

        // Test case 7: Different theater - no overlap
        List<Showtime> result7 = showtimeRepository.findOverlappingShowtimes(
                "Theater2",
                Instant.parse("2023-03-01T10:00:00Z"),
                Instant.parse("2023-03-01T12:00:00Z"),
                null);
        assertTrue(result7.isEmpty(), "Should not find any overlapping showtimes in different theater");

        // Test case 8: Same showtime ID - should exclude self
        List<Showtime> result8 = showtimeRepository.findOverlappingShowtimes(
                "Theater1",
                Instant.parse("2023-03-01T10:00:00Z"),
                Instant.parse("2023-03-01T12:00:00Z"),
                existingShowtime.getId());
        assertTrue(result8.isEmpty(), "Should not find itself as overlapping");
    }
}
