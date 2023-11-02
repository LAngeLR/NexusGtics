package com.example.nexusgtics.repository;

import com.example.nexusgtics.dto.TicketsCreadosCulminadosDto;
import com.example.nexusgtics.dto.TicketsDashSupDto;
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
    @Query(nativeQuery = true, value = "SELECT * FROM tickets WHERE estado != ?1 and idSupervisorEncargado = ?2")
    List<Ticket> listaTicketsModificado(int valor1, int idSupervisor);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE tickets SET idCuadrilla = ?2 WHERE idTickets = ?1")
    void actualizarCuadrilla(int ticketId, int cuadrillaId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE tickets SET idSupervisorEncargado = ?2 , reasignado = ?3 WHERE idTickets = ?1 ")
    void actualizarSupervisor(int ticketId, int supervisorId, int condicion);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE tickets SET estado = ?2 WHERE idTickets = ?1")
    void actualizarEstado(int ticketId, int estado);

    @Query(nativeQuery = true, value = "SELECT estado FROM tickets where idTickets=?1")
    int obtenerEstado(int idTicket);

    @Query(nativeQuery = true, value = "SELECT * FROM tickets where estado=7 and idCuadrilla = ?1")
    List<Ticket> listaTicketsCerradosPorCuadrilla (int id);

    @Query(nativeQuery = true, value = "SELECT * FROM tickets where idEmpresaAsignada = ?1 and (estado=1 or estado=2) and idSupervisorEncargado IS NULL")
    List<Ticket> listaTicketsSinSupervisor (int idEmpresaAsignada);

    @Query(value ="SELECT \n" +
            "    (SELECT COUNT(*) FROM tickets WHERE idSupervisorEncargado = ?1 AND reasignado=0) AS aceptados,\n" +
            "    (SELECT COUNT(*) FROM tickets WHERE idSupervisorEncargado = ?1 AND estado = ?2 AND reasignado=0) AS culminados,\n" +
            "    (SELECT COUNT(*) FROM tickets WHERE idSupervisorEncargado != ?1 AND reasignado=1) AS reasignados,\n" +
            "    (SELECT COUNT(*) FROM tickets WHERE idSupervisorEncargado != ?1 AND reasignado=1 AND estado = ?2) AS terminados;", nativeQuery = true )
    List<TicketsDashSupDto> infoDash(int id, int estado);

    //------------------------ Tecnico --------------------//

    @Query(nativeQuery = true, value = "SELECT * FROM tickets WHERE idCuadrilla=1 AND idEmpresaAsignada=2")
    List<Ticket> listaTicketModificado(int valor1, int idCuadrilla);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into ticket (estado) values (?1)")
    void guardarEstado(int estado);

    @Query(value ="select * from nexus.tickets where estado not IN (1,2,7);", nativeQuery = true )
    List<Ticket> listarEstado();

    //------------------------ Analista m --------------------//

    @Query(value ="select * from tickets where estado=2 and idUsuarioCreador=?1 and idSupervisorEncargado IS NULL;", nativeQuery = true )
    List<Ticket> recienCreados(int id);

    @Query(value ="SELECT \n" +
            "\t(SELECT COUNT(*) FROM tickets WHERE idUsuarioCreador = ?1) AS creados,\n" +
            "    (SELECT COUNT(*) FROM tickets WHERE idUsuarioCreador = ?1 AND estado = ?2) AS culminados;", nativeQuery = true )
    List<TicketsCreadosCulminadosDto> creadosCulminados(int id, int estado);

    @Query(nativeQuery = true, value = "SELECT * FROM tickets WHERE idUsuarioCreador = ?1 and idTipoTicket=1")
    List<Ticket> listaTicketsModificados(int idAnalista);
}
