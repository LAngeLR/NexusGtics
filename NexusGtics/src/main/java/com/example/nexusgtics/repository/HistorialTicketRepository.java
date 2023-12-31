package com.example.nexusgtics.repository;

import com.example.nexusgtics.dto.ActividadDto;
import com.example.nexusgtics.dto.DashboardGraficoDto;
import com.example.nexusgtics.dto.HistorialTicketDto;
import com.example.nexusgtics.entity.Comentario;
import com.example.nexusgtics.entity.HistorialTicket;
import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface HistorialTicketRepository extends JpaRepository<HistorialTicket, Integer> {

    // ------------------------------ SUPERVISOR --------------------------------------- //
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into historialtickets (estado, fechaCambioEstado, fechaCambio, horaCambio, idTickets, idUsuarios, descripcion) values (?1, ?2, ?3, ?4, ?5, ?6, ?7)")
    void crearHistorial(int estado, Date fechaCambioEstado, LocalDate fechaCambio, LocalTime horaCambio, int idTicket, int idUser, String des);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into historialtickets (estado, fechaCambio, horaCambio, idTickets, idUsuarios, descripcion) values (?1, ?2, ?3, ?4, ?5,?6)")
    void crearHistorial1(int estado, LocalDate fechaCambio, LocalTime horaCambio, int idTicket, int idUser, String des);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into historialtickets (estado,fechaCambioEstado, fechaCambio, horaCambio, idTickets, idUsuarios, descripcion, idUsuariosReasignados) values (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)")
    void crearHistorialReasignado(int estado, Date fechaCambioEstado, LocalDate fechaCambio, LocalTime horaCambio, int idTicket, int idUser, String des, int idReasignado);

    @Query(nativeQuery = true, value = "SELECT \n" +
            "    TIMEDIFF(\n" +
            "        COALESCE(\n" +
            "            (SELECT MAX(fechaCambioEstado) FROM historialtickets WHERE idTickets = ht.idTickets AND estado = 5)\n" +
            "        ),\n" +
            "        (SELECT MAX(fechaCambioEstado) FROM historialtickets WHERE idTickets = ht.idTickets AND estado = 2 AND descripcion = \"Pasando a Tecnico\")\n" +
            "    ) AS transcurrido\n" +
            "FROM historialtickets ht\n" +
            "WHERE idTickets = ?1 AND (estado = 2 OR estado = 5)\n" +
            "ORDER BY transcurrido DESC\n" +
            "LIMIT 1;\n")
    Time tiempoTranscurrido(int idTickets);

    @Query(nativeQuery = true, value = "SELECT\n" +
            "    ht.descripcion,\n" +
            "    ht.idUsuariosReasignados AS idReasignado,\n" +
            "    ht.idUsuarios,\n" +
            "    CONCAT(u.nombre, ' ', u.apellido) AS Reasignado,\n" +
            "    CONCAT(a.nombre, ' ', a.apellido) AS usuario,\n" +
            "    t.estado,\n" +
            "    CONCAT(f.nombre, ' ', f.apellido) AS Creador,\n" +
            "    CONCAT(o.nombre, ' ', o.apellido) AS Supervisor,\n" +
            "    c.idCuadrillas,\n" +
            "    TIMEDIFF(NOW(), ht.fechaCambioEstado) AS transcurso\n" +
            "FROM\n" +
            "    historialtickets ht\n" +
            "    LEFT JOIN usuarios u ON ht.idUsuariosReasignados = u.idUsuarios\n" +
            "    LEFT JOIN usuarios a ON ht.idUsuarios = a.idUsuarios\n" +
            "    LEFT JOIN tickets t ON ht.idTickets = t.idTickets\n" +
            "    LEFT JOIN usuarios f ON t.idUsuarioCreador = f.idUsuarios\n" +
            "    LEFT JOIN usuarios o ON t.idSupervisorEncargado = o.idUsuarios\n" +
            "    LEFT JOIN cuadrillas c ON t.idCuadrilla = c.idCuadrillas\n" +
            "WHERE\n" +
            "    (ht.idUsuarios = ?1 OR ht.idUsuariosReasignados = ?1)\n" +
            "    AND ht.fechaCambioEstado >= DATE_SUB(NOW(), INTERVAL 2 DAY)\n" +
            "    AND TIMEDIFF(NOW(), ht.fechaCambioEstado) <= '24:00:00'\n" +
            "ORDER BY\n" +
            "    ht.fechaCambioEstado DESC\n" +
            "LIMIT 6;\n")
    List<ActividadDto> actividadReciente(int idSession);

    /*
    @Query(nativeQuery = true, value = "SELECT\n" +
            "    t.idCuadrilla,\n" +
            "    COUNT(*) AS cantidad,\n" +
            "    MONTH(ht.fechaCambioEstado) AS month\n" +
            "FROM\n" +
            "    historialtickets ht\n" +
            "JOIN\n" +
            "    tickets t ON ht.idTickets = t.idTickets\n" +
            "WHERE\n" +
            "    ht.estado = 2\n" +
            "    AND t.idCuadrilla IS NOT NULL\n" +
            "GROUP BY\n" +
            "    t.idCuadrilla,\n" +
            "    month")
    List<DashboardGraficoDto> grafico();
    */

    @Query(nativeQuery = true, value = "SELECT ht.estado,  ht.idTickets,  ht.descripcion,  ht.fechaCambio,  ht.horaCambio, CONCAT( u.nombre, ' ' ,u.apellido) AS nombre, c.nombreCargo AS cargo\n" +
            "FROM historialtickets ht\n" +
            "JOIN usuarios u ON ht.idUsuarios = u.idUsuarios\n" +
            "JOIN cargos c ON u.idCargos = c.idCargos\n" +
            "WHERE ht.idTickets = ?1\n" +
            "ORDER BY ht.fechaCambio DESC, ht.horaCambio DESC;")
    List<HistorialTicketDto> listarHistorial(int id);

    @Query(nativeQuery = true, value = "SELECT * FROM historialtickets WHERE idTickets = ?1 ORDER BY fechaCambio DESC, horaCambio DESC;\n")
    List<HistorialTicket> listarHistorial1(int id);
}
