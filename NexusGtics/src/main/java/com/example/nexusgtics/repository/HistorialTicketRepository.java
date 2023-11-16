package com.example.nexusgtics.repository;

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

}
