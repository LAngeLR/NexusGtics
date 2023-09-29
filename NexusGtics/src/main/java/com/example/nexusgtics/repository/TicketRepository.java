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

    //------------------------ Tecnico --------------------//

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into ticket (estado) values (?1)")
    void guardarEstado(int estado);

    //obtener descripcion de formulario

    //@Query(nativeQuery = true, value = "SELECT descripcion FROM formularios where idTickets=?1")
    //int obtenerDescripcion(int idTicket);
    //List<Formulario> obtenerDescripcion(int idTicket);

    //@Query(nativeQuery = true, value = "SELECT estado FROM tickets WHERE  estado = 1 OR estado=2")
    //List<Ticket> listaEstado(int valor1);

    //@Query(nativeQuery = true, value = "SELECT COUNT(estado) FROM tickets WHERE  estado = 1 OR estado=2;")
    //int contarEstado(int idTicket);



}
