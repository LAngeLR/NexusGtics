package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
}
