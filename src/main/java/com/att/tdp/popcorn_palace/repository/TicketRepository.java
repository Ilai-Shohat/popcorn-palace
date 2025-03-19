package com.att.tdp.popcorn_palace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.att.tdp.popcorn_palace.model.Ticket;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // Find tickets by showtime ID and seat number to check for double bookings
    List<Ticket> findByShowtimeIdAndSeatNumber(Long showtimeId, String seatNumber);
}