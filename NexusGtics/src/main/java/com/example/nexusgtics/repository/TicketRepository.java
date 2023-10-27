package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.Formulario;
import com.example.nexusgtics.entity.Ticket;
import com.example.nexusgtics.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    // ------------------------------ SUPERVISOR --------------------------------------- //
    @Query(nativeQuery = true, value = "SELECT * FROM tickets WHERE estado != ?1")
    List<Ticket> listaTicketsModificado(int valor1);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE tickets SET idCuadrilla = ?2 WHERE idTickets = ?1")
    void actualizarCuadrilla(int ticketId, int cuadrillaId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE tickets SET idSupervisorEncargado = ?2 WHERE idTickets = ?1")
    void actualizarSupervisor(int ticketId, int supervisorId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE tickets SET estado = ?2 WHERE idTickets = ?1")
    void actualizarEstado(int ticketId, int estado);

    @Query(nativeQuery = true, value = "SELECT estado FROM tickets where idTickets=?1")
    int obtenerEstado(int idTicket);

    @Query(nativeQuery = true, value = "SELECT * FROM tickets where estado=7 and idCuadrilla = ?1")
    List<Ticket> listaTicketsCerradosPorCuadrilla (int id);

    @Query(nativeQuery = true, value = "SELECT * FROM tickets where idEmpresaAsignada = ?1 and estado=2")
    List<Ticket> listaDash (int idEmpresaAsignada);

    //------------------------ Tecnico --------------------//

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into ticket (estado) values (?1)")
    void guardarEstado(int estado);

    @Query(value ="select * from nexus.tickets where estado not IN (1,2,7);", nativeQuery = true )
    List<Ticket> listarEstado();

}
