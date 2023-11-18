package com.example.nexusgtics.repository;

import com.example.nexusgtics.dto.DashboardGraficoDto;
import com.example.nexusgtics.entity.HistorialTicket;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface HistorialTicketRepository extends JpaRepository<HistorialTicket, Integer> {

    // ------------------------------ SUPERVISOR --------------------------------------- //
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into historialtickets (estado, fechaCambioEstado, idTickets, idUsuarios, descripcion) values (?1, ?2, ?3, ?4, ?5)")
    void crearHistorial(int estado, Date fechaCambio, int idTicket, int idUser, String des);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into historialtickets (estado, fechaCambioEstado, idTickets, idUsuarios, descripcion, idUsuariosReasignados) values (?1, ?2, ?3, ?4, ?5, ?6)")
    void crearHistorialReasignado(int estado, Date fechaCambio, int idTicket, int idUser, String des, int idReasignado);

    @Query(nativeQuery = true, value = "SELECT * FROM historialtickets WHERE (idUsuarios = ?1 OR idUsuariosReasignados = ?1) AND fechaCambioEstado >= DATE_SUB(NOW(), INTERVAL 2 DAY) ORDER BY fechaCambioEstado DESC LIMIT 6;")
    List<HistorialTicket> actividadReciente(int idSession);

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
}
