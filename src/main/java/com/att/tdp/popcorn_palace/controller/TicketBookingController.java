package com.att.tdp.popcorn_palace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.att.tdp.popcorn_palace.model.Ticket;
import com.att.tdp.popcorn_palace.service.TicketService;

@RestController
@RequestMapping("/bookings")
public class TicketBookingController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public String bookTicket(@RequestBody Ticket ticket) {
        ticketService.bookTicket(ticket);
        return "Ticket booked successfully";
    }

}
