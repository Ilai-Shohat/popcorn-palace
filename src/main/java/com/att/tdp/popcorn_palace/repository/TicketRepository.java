package com.att.tdp.popcorn_palace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.att.tdp.popcorn_palace.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}