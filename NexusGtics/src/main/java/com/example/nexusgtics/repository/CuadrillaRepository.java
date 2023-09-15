package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.Cuadrilla;
import com.example.nexusgtics.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CuadrillaRepository extends JpaRepository<Cuadrilla, Integer> {
    @Query(nativeQuery = true, value = "SELECT COUNT(DISTINCT t.idTickets) AS cantidad_tickets\n" +
            "FROM tickets t \n" +
            "WHERE t.idCuadrilla = ?1 AND (t.estado = 6 OR t.estado = 7)\n" +
            "GROUP BY t.idTickets;")
    int contarTrabajosFinalizados(int idCuadrilla);
}
