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

    // Books a ticket based on the provided request
    public Ticket createTicketFromRequest(TicketBookingRequest request) {
        // Get showtime from service - this will throw ShowtimeNotFoundException if not
        // found
        Showtime showtime = showtimeService.getShowtimeById(request.getShowtimeId());
        Ticket ticket = new Ticket();

        ticket.setShowtime(showtime);
        ticket.setSeatNumber(request.getSeatNumber());
        ticket.setUserId(request.getUserId());

        return bookTicket(ticket);
    }

    public Ticket bookTicket(Ticket ticket) {
        if (!showtimeService.showtimeExists(ticket.getShowtime().getId())) {
            throw new ShowtimeNotFoundException("Showtime with ID " + ticket.getShowtime().getId() + " not found");
        }

        validateNoDoubleBooking(ticket);
        if (ticket.getBookingId() == null) {
            ticket.setBookingId(UUID.randomUUID());
        }

        return ticketRepository.save(ticket);
    }

    // Validates that there is no double booking for the same seat and showtime
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
