package com.att.tdp.popcorn_palace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.att.tdp.popcorn_palace.dto.TicketBookingRequest;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.model.Ticket;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import com.att.tdp.popcorn_palace.service.TicketService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
public class TicketBookingController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ShowtimeService showtimeService;

    @PostMapping
    public ResponseEntity<Map<String, String>> bookTicket(@Valid @RequestBody TicketBookingRequest request) {
        // Get showtime from service
        Optional<Showtime> showtimeOpt = showtimeService.getShowtimeById(request.getShowtimeId());
        if (!showtimeOpt.isPresent()) {
            throw new IllegalArgumentException("Showtime not found");
        }

        // Create ticket from request
        Ticket ticket = new Ticket();
        ticket.setShowtime(showtimeOpt.get());
        ticket.setSeatNumber(request.getSeatNumber());
        ticket.setUserId(request.getUserId());

        // Book the ticket
        Ticket bookedTicket = ticketService.bookTicket(ticket);

        // Create response according to README format
        Map<String, String> response = new HashMap<>();
        response.put("bookingId", bookedTicket.getBookingId().toString());

        return ResponseEntity.ok(response);
    }
}
