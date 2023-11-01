package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.HistorialTicket;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface HistorialTicketRepository extends JpaRepository<HistorialTicket, Integer> {

    // ------------------------------ SUPERVISOR --------------------------------------- //
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into historialtickets (estado, fechaCambioEstado, idTickets, idUsuarios, descripcion) values (?1, ?2, ?3, ?4, ?5)")
    void crearHistorial(int estado, Date fechaCambio, int idTicket, int idUser, String des);

}
