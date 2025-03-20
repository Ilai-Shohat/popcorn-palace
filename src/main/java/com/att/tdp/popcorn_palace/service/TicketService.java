package com.att.tdp.popcorn_palace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.att.tdp.popcorn_palace.dto.TicketBookingRequest;
import com.att.tdp.popcorn_palace.exception.BookingException;
import com.att.tdp.popcorn_palace.exception.ShowtimeNotFoundException;
import com.att.tdp.popcorn_palace.model.Showtime;
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
        // Validate the showtime exists - this will throw ShowtimeNotFoundException if
        // not found

        if (!showtimeService.showtimeExists(ticket.getShowtime().getId())) {
            throw new ShowtimeNotFoundException("Showtime with ID " + ticket.getShowtime().getId() + " not found");
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
     * Books a ticket based on the provided request
     * 
     * @param request the ticket booking request
     * @return the booked ticket
     * @throws ShowtimeNotFoundException if the showtime doesn't exist
     * @throws BookingException          if there's a booking conflict
     */
    public Ticket createTicketFromRequest(TicketBookingRequest request) {
        // Get showtime from service - this will throw ShowtimeNotFoundException if not
        // found
        Showtime showtime = showtimeService.getShowtimeById(request.getShowtimeId());

        // Create ticket from request
        Ticket ticket = new Ticket();
        ticket.setShowtime(showtime);
        ticket.setSeatNumber(request.getSeatNumber());
        ticket.setUserId(request.getUserId());

        // Book the ticket using existing method
        return bookTicket(ticket);
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
