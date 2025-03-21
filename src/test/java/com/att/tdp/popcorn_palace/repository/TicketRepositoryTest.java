package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.model.Ticket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TicketRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    public void testFindByShowtimeIdAndSeatNumber() {
        // Create a movie
        Movie movie = new Movie("Test Movie", "Action", 120, 8.0, 2022);
        entityManager.persist(movie);

        // Create a showtime
        Showtime showtime = new Showtime(movie, "Theater1",
                Instant.parse("2023-03-01T10:00:00Z"),
                Instant.parse("2023-03-01T12:00:00Z"), 15.0);
        entityManager.persist(showtime);

        // Create a ticket
        UUID userId = UUID.randomUUID();
        Ticket ticket = new Ticket(showtime, "A1", userId);
        ticket.setBookingId(UUID.randomUUID());
        entityManager.persist(ticket);

        // Create another ticket for same showtime but different seat
        Ticket anotherTicket = new Ticket(showtime, "B2", userId);
        anotherTicket.setBookingId(UUID.randomUUID());
        entityManager.persist(anotherTicket);

        entityManager.flush();

        // Test finding by showtime ID and seat number
        List<Ticket> foundTickets = ticketRepository.findByShowtimeIdAndSeatNumber(showtime.getId(), "A1");

        // Assertions
        assertFalse(foundTickets.isEmpty(), "Should find tickets");
        assertEquals(1, foundTickets.size(), "Should find exactly one ticket");
        assertEquals("A1", foundTickets.get(0).getSeatNumber(), "Seat number should match");
        assertEquals(userId, foundTickets.get(0).getUserId(), "User ID should match");

        // Test with non-existent seat
        List<Ticket> nonExistentSeat = ticketRepository.findByShowtimeIdAndSeatNumber(showtime.getId(), "Z9");
        assertTrue(nonExistentSeat.isEmpty(), "Should not find any tickets for non-existent seat");

        // Test with non-existent showtime
        List<Ticket> nonExistentShowtime = ticketRepository.findByShowtimeIdAndSeatNumber(999L, "A1");
        assertTrue(nonExistentShowtime.isEmpty(), "Should not find any tickets for non-existent showtime");
    }
}
