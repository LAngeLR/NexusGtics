package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.Ticket;
import com.example.nexusgtics.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    // ------------------------------ SUPERVISOR --------------------------------------- //
    @Query(nativeQuery = true, value = "SELECT * FROM tickets WHERE estado != ?1")
    List<Ticket> listaTicketsModificado(int valor1);


}
