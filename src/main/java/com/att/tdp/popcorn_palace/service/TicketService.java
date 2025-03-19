package com.att.tdp.popcorn_palace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.att.tdp.popcorn_palace.exception.BookingException;
import com.att.tdp.popcorn_palace.model.Ticket;
import com.att.tdp.popcorn_palace.repository.TicketRepository;

import java.util.List;
import java.util.UUID;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ShowtimeService showtimeService;

    public Ticket bookTicket(Ticket ticket) {
        // Validate the showtime exists
        if (!showtimeService.getShowtimeById(ticket.getShowtime().getId()).isPresent()) {
            throw new IllegalArgumentException("Showtime not found");
        }

        // Check for double booking
        validateNoDoubleBooking(ticket);

        // Generate booking ID if not present
        if (ticket.getBookingId() == null) {
            ticket.setBookingId(UUID.randomUUID());
        }

        return ticketRepository.save(ticket);
    }

    /**
     * Validates that there is no double booking for the same seat and showtime
     * 
     * @param ticket The ticket to be booked
     * @throws BookingException if the seat is already booked for the showtime
     */
    private void validateNoDoubleBooking(Ticket ticket) {
        List<Ticket> existingTickets = ticketRepository.findByShowtimeIdAndSeatNumber(
                ticket.getShowtime().getId(),
                ticket.getSeatNumber());

        if (!existingTickets.isEmpty()) {
            throw new BookingException(
                    String.format("Seat %s is already booked for this showtime",
                            ticket.getSeatNumber()));
        }
    }
}
