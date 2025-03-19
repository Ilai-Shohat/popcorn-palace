package com.att.tdp.popcorn_palace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.att.tdp.popcorn_palace.model.Ticket;
import com.att.tdp.popcorn_palace.service.TicketService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
public class TicketBookingController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<Map<String, String>> bookTicket(@RequestBody Ticket ticket) {
        Ticket bookedTicket = ticketService.bookTicket(ticket);

        // Create response according to README format
        Map<String, String> response = new HashMap<>();
        response.put("bookingId", bookedTicket.getBookingId().toString());

        return ResponseEntity.ok(response);
    }
}
