package com.example.nexusgtics.repository;

import com.example.nexusgtics.dto.ListaTicketNuevoDto;
import com.example.nexusgtics.dto.ListaTicketsDto;
import com.example.nexusgtics.dto.TicketsCreadosCulminadosDto;
import com.example.nexusgtics.dto.TicketsDashSupDto;
import com.example.nexusgtics.entity.Ticket;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    // ------------------------------ SUPERVISOR --------------------------------------- //
    @Query(nativeQuery = true, value = "SELECT t.idTickets, t.usuarioSolicitante, CONCAT(u.nombre, ' ', u.apellido) AS nombreCompleto, t.fechaCreacion, t.fechaCierre, t.prioridad, t.estado FROM tickets t LEFT JOIN cuadrillas c ON t.idCuadrilla = c.idCuadrillas LEFT JOIN tecnicoscuadrillas tc ON c.idCuadrillas = tc.idCuadrilla AND tc.liderTecnico = 1 LEFT JOIN usuarios u ON tc.idTecnico = u.idUsuarios WHERE t.estado != ?1 AND t.idSupervisorEncargado = ?2")
    List<ListaTicketsDto> listaTicketsModificado(int valor1, int idSupervisor);

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

    @Query(nativeQuery = true, value = "SELECT * FROM tickets where (estado=7 or estado=6) and idCuadrilla = ?1")
    List<Ticket> listaTicketsCerradosPorCuadrilla (int id);

    @Query(nativeQuery = true, value = "SELECT * FROM tickets where idEmpresaAsignada = ?1 and estado = 1")
    List<Ticket> listaTicketsSinSupervisor (int idEmpresaAsignada);
    @Query(nativeQuery = true, value = "SELECT * FROM tickets where estado NOT IN (1) and idSupervisorEncargado= ?1 and idEmpresaAsignada = ?2")
    List<Ticket> listarMapaSupervisor(int supervisor, int empresa);

    @Query(nativeQuery = true, value = "SELECT * FROM tickets where estado NOT IN (1,2,3,4,5)")
    List<Ticket> cerrados();

    @Query(nativeQuery = true, value = "SELECT * FROM tickets where estado NOT IN (1,2,6,7)")
    List<Ticket> progreso();

    @Query(nativeQuery = true, value = "SELECT * FROM tickets where estado NOT IN (3,4,5,6,7)")
    List<Ticket> nuevos();

    @Query(value ="SELECT \n" +
            "    (SELECT COUNT(*) FROM tickets WHERE idSupervisorEncargado = ?1 AND reasignado=0) AS aceptados,\n" +
            "    (SELECT COUNT(*) FROM tickets WHERE idSupervisorEncargado = ?1 AND estado = ?2 AND reasignado=0) AS culminados,\n" +
            "    (SELECT COUNT(*) FROM tickets WHERE idSupervisorEncargado != ?1 AND reasignado=1) AS reasignados,\n" +
            "    (SELECT COUNT(*) FROM tickets WHERE idSupervisorEncargado != ?1 AND reasignado=1 AND estado = ?2) AS terminados;", nativeQuery = true )
    List<TicketsDashSupDto> infoDash(int id, int estado);

    @Query(nativeQuery = true, value = "SELECT t.idTickets, CONCAT(ucreador.nombre, ' ', ucreador.apellido) AS analistaCreador, t.usuarioSolicitante, t.fechaCreacion, t.prioridad\n" +
            "FROM tickets t\n" +
            "JOIN usuarios ucreador ON t.idUsuarioCreador = ucreador.idUsuarios\n" +
            "WHERE t.estado = 1 AND t.idSupervisorEncargado IS NULL AND t.idEmpresaAsignada = ?1\n" +
            "ORDER BY t.fechaCreacion ASC;")
    List<ListaTicketNuevoDto> listaTicketsNuevos (int idEmpresaAsignada);

    //------------------------ Tecnico --------------------//
    //------------------------ Tecnico --------------------//

//    @Query(nativeQuery = true, value = "SELECT * FROM tickets WHERE estado NOT IN (1,2,7) and idSupervisorEncargado = 5 AND idEmpresaAsignada=3 and idCuadrilla=1")
//    List<Ticket> listaTickets(int valor1, int idTecnico);

    @Query(nativeQuery = true, value = "SELECT * FROM tickets WHERE estado NOT IN (1,2,7) and idSupervisorEncargado = 5 AND idEmpresaAsignada=3 and idCuadrilla=1")
    List<Ticket> listaTicketsAsignado();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS total_tickets FROM tickets")
    int cantidadTickets();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS total_tickets FROM tickets WHERE estado=3")
    int cantidadTicketsnuevos();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS total_empresa FROM empresas")
    int cantidadEmpresas();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS total_cuadrilla FROM tickets where idCuadrilla=2")
    int cantidadCuadrilla();

    @Query(nativeQuery = true, value = "SELECT * FROM nexus.tickets where estado not IN (1,2,7)")
    List<Ticket> listarmapa();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into ticket (estado) values (?1)")
    void guardarEstado(int estado);

    @Query(value ="select * from nexus.tickets where estado not IN (1,2,7)", nativeQuery = true )
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

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM usuarios WHERE idEmpresas != 1 AND MONTH(fechaRegistro) = MONTH(NOW());")
    int numeroClientesActual();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM usuarios WHERE idEmpresas != 1 AND MONTH(fechaRegistro) = MONTH(DATE_SUB(NOW(), INTERVAL 1 MONTH));")
    Integer numeroClientesAnterior();

    @Query(nativeQuery = true, value = "SELECT\n" +
            "    ((SELECT COUNT(*) FROM usuarios WHERE idEmpresas != 1 AND MONTH(fechaRegistro) = MONTH(NOW())) -\n" +
            "    (SELECT COUNT(*) FROM usuarios WHERE idEmpresas != 1 AND MONTH(fechaRegistro) = MONTH(DATE_SUB(NOW(), INTERVAL 1 MONTH)))) /\n" +
            "    (SELECT COUNT(*) FROM usuarios WHERE idEmpresas != 1 AND MONTH(fechaRegistro) = MONTH(DATE_SUB(NOW(), INTERVAL 1 MONTH))) * 100 as diferencia_porcentaje;")
    Integer diferenciaRegistros();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) as total_tickets FROM tickets WHERE MONTH(fechaCreacion) = MONTH(NOW());\n")
    Integer cantTicketsCreados();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) as total_tickets_estado_8\n" +
            "FROM tickets\n" +
            "WHERE estado = 8 AND MONTH(fechaCreacion) = MONTH(NOW());\n")
    Integer cantTicketsFinalizados();

    @Query(nativeQuery = true, value = "SELECT count(*) FROM tickets WHERE MONTH(fechaCreacion) = MONTH(CURRENT_DATE())\n")
    Integer CantporMes();

    @Query(nativeQuery = true, value = "SELECT count(*) FROM tickets WHERE MONTH(fechaCreacion) = MONTH(CURRENT_DATE()) - 1")
    Integer CantporMesAnterior();

    @Query(nativeQuery = true, value = "SELECT count(*) FROM tickets WHERE MONTH(fechaCreacion) = MONTH(CURRENT_DATE()) - 2")
    Integer CantHaceDosMeses();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS total_tickets\n" +
            "FROM nexus.tickets\n" +
            "WHERE idEmpresaAsignada = ?1\n" +
            "AND YEAR(fechaCreacion) = YEAR(CURRENT_DATE() - INTERVAL ?2 MONTH)\n" +
            "AND MONTH(fechaCreacion) = MONTH(CURRENT_DATE() - INTERVAL ?2 MONTH);\n")
    Integer TicketXMes(int idEmpresa,int mes);
}
