package com.example.nexusgtics.repository;

import com.example.nexusgtics.dto.ListaCuadrillaCompletaDto;
import com.example.nexusgtics.dto.ListaCuadrillaImcompletaDto;
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
    Integer contarTrabajosFinalizados(int idCuadrilla);

    @Query(nativeQuery = true, value = "SELECT COUNT(u.idUsuarios) AS cantidad_Tecnicos\n" +
            "FROM usuarios u \n" +
            "WHERE u.idCuadrilla = ?1 AND u.idCargos = 6;")
    int numeroTecnicosPorCuadrilla(int idCuadrilla);

    @Query(nativeQuery = true, value = "SELECT COUNT(DISTINCT idCuadrillas) as cuadrilla_count FROM cuadrillas")
    int cantidadCuadrillas();

    @Query(nativeQuery = true, value = "SELECT \n" +
            "    c.idCuadrillas AS idCuadrilla,\n" +
            "    CONCAT(u.nombre, ' ', u.apellido) AS lider,\n" +
            "    COUNT(DISTINCT t.idTickets) AS trabajos,\n" +
            "    MAX(t.fechaCierre) AS ultimo,\n" +
            "    IFNULL(ROUND(DATEDIFF(CURDATE(), u.fechaRegistro) / 365), 0) AS year\n" +
            "FROM \n" +
            "    cuadrillas c\n" +
            "JOIN \n" +
            "    usuarios u ON c.idTecnico = u.idUsuarios\n" +
            "LEFT JOIN \n" +
            "    usuarios u_tecnicos ON c.idCuadrillas = u_tecnicos.IdCuadrilla\n" +
            "LEFT JOIN \n" +
            "    tickets t ON c.idCuadrillas = t.idCuadrilla\n" +
            "WHERE \n" +
            "    (t.estado IN (6, 7) OR t.estado IS NULL)\n" +
            "GROUP BY \n" +
            "    c.idCuadrillas, u.idUsuarios\n" +
            "HAVING \n" +
            "    COUNT(DISTINCT u_tecnicos.idUsuarios) = 5;\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n")
    List<ListaCuadrillaCompletaDto> cuadrillaCompleta();

    @Query(nativeQuery = true, value = "SELECT \n" +
            "    c.idCuadrillas AS idCuadrilla,\n" +
            "    CONCAT(u.nombre, ' ', u.apellido) AS lider,\n" +
            "    COUNT(DISTINCT u_tecnicos.idUsuarios) AS cantidad,\n" +
            "    c.fechaCreacion as fecha,\n" +
            "    IFNULL(ROUND(DATEDIFF(CURDATE(), u.fechaRegistro) / 365), 0) AS year\n" +
            "FROM \n" +
            "    cuadrillas c\n" +
            "JOIN \n" +
            "    usuarios u ON c.idTecnico = u.idUsuarios\n" +
            "LEFT JOIN \n" +
            "    usuarios u_tecnicos ON c.idCuadrillas = u_tecnicos.IdCuadrilla\n" +
            "LEFT JOIN \n" +
            "    tickets t ON c.idCuadrillas = t.idCuadrilla\n" +
            "WHERE \n" +
            "    (t.estado IN (6, 7) OR t.estado IS NULL)\n" +
            "GROUP BY \n" +
            "    c.idCuadrillas, u.idUsuarios\n" +
            "HAVING \n" +
            "    COUNT(DISTINCT u_tecnicos.idUsuarios) < 5;\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n")
    List<ListaCuadrillaImcompletaDto> cuadrillaImCompleta();
}
