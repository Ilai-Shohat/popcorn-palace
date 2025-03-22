package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.TicketBookingRequest;
import com.att.tdp.popcorn_palace.exception.BookingException;
import com.att.tdp.popcorn_palace.exception.ShowtimeNotFoundException;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.model.Ticket;
import com.att.tdp.popcorn_palace.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ShowtimeService showtimeService;

    @InjectMocks
    private TicketService ticketService;

    private Showtime showtime;
    private UUID userId;
    private final Long showtimeId = 1L;
    private final String seatNumber = "A1";

    @BeforeEach
    public void setUp() {
        // Create a simple showtime
        showtime = new Showtime();
        showtime.setId(showtimeId);
        showtime.setTheater("Theater1");
        showtime.setStartTime(Instant.parse("2023-03-01T10:00:00Z"));
        showtime.setEndTime(Instant.parse("2023-03-01T12:00:00Z"));
        showtime.setPrice(15.0);

        // Simple user ID
        userId = UUID.randomUUID();
    }

    @Test
    public void testBookTicketBasic() {
        // Create a basic ticket
        Ticket ticket = new Ticket(showtime, seatNumber, userId);

        // Setup mocks
        when(showtimeService.showtimeExists(showtimeId)).thenReturn(true);
        when(ticketRepository.findByShowtimeIdAndSeatNumber(showtimeId, seatNumber))
                .thenReturn(Collections.emptyList());
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // Test booking the ticket
        Ticket result = ticketService.bookTicket(ticket);

        // Basic assertions
        assertNotNull(result);
        assertNotNull(result.getBookingId());

        // Verify repository interactions
        verify(showtimeService).showtimeExists(showtimeId);
        verify(ticketRepository).findByShowtimeIdAndSeatNumber(showtimeId, seatNumber);
        verify(ticketRepository).save(ticket);
    }

    @Test
    public void testBookTicketShowtimeDoesNotExist() {
        // Create a ticket with a showtime that doesn't exist
        Ticket ticket = new Ticket(showtime, seatNumber, userId);

        // Setup mocks to indicate showtime doesn't exist
        when(showtimeService.showtimeExists(showtimeId)).thenReturn(false);

        // Test that booking throws the right exception
        ShowtimeNotFoundException exception = assertThrows(
                ShowtimeNotFoundException.class,
                () -> ticketService.bookTicket(ticket));

        // Basic assertion on exception message
        assertTrue(exception.getMessage().contains("not found"));

        // Verify repository was never called for saving
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    public void testBookTicketSeatAlreadyBooked() {
        // Create a ticket
        Ticket ticket = new Ticket(showtime, seatNumber, userId);

        // Create an existing ticket for the same seat
        Ticket existingTicket = new Ticket(showtime, seatNumber, UUID.randomUUID());
        existingTicket.setBookingId(UUID.randomUUID());

        // Setup mocks
        when(showtimeService.showtimeExists(showtimeId)).thenReturn(true);
        when(ticketRepository.findByShowtimeIdAndSeatNumber(showtimeId, seatNumber))
                .thenReturn(List.of(existingTicket));

        // Test that booking throws the right exception
        BookingException exception = assertThrows(
                BookingException.class,
                () -> ticketService.bookTicket(ticket));

        // Basic assertion on exception message
        assertTrue(exception.getMessage().contains("already booked"));

        // Verify repository was never called for saving
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    public void testCreateTicketFromRequestBasic() {
        // Create a basic request
        TicketBookingRequest request = new TicketBookingRequest();
        request.setShowtimeId(showtimeId);
        request.setSeatNumber(seatNumber);
        request.setUserId(userId);

        // Setup mocks
        when(showtimeService.getShowtimeById(showtimeId)).thenReturn(showtime);
        when(showtimeService.showtimeExists(showtimeId)).thenReturn(true);
        when(ticketRepository.findByShowtimeIdAndSeatNumber(showtimeId, seatNumber))
                .thenReturn(Collections.emptyList());
        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(i -> i.getArgument(0));

        // Test creating the ticket
        Ticket result = ticketService.createTicketFromRequest(request);

        // Basic assertions
        assertNotNull(result);
        assertEquals(seatNumber, result.getSeatNumber());
        assertEquals(userId, result.getUserId());
        assertNotNull(result.getBookingId());

        // Verify service and repository interactions
        verify(showtimeService).getShowtimeById(showtimeId);
        verify(ticketRepository).save(any(Ticket.class));
    }
}
