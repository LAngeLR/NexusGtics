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

    @Query(nativeQuery = true, value = "SELECT COUNT(tc.idTecnico) AS cantidad_Tecnicos\n" +
            "            FROM tecnicoscuadrillas tc\n" +
            "            WHERE tc.idCuadrilla = ?1")
    int numeroTecnicosPorCuadrilla(int idCuadrilla);

    @Query(nativeQuery = true, value = "SELECT COUNT(DISTINCT idCuadrillas) as cuadrilla_count FROM cuadrillas")
    int cantidadCuadrillas();

    @Query(nativeQuery = true, value = "SELECT \n" +
            "    c.idCuadrillas AS idCuadrilla,\n" +
            "    CONCAT(u.nombre, ' ', u.apellido) AS lider,\n" +
            "    COUNT(DISTINCT CASE WHEN (t.estado IN (6, 7, 8) OR t.estado IS NULL) THEN t.idTickets END) AS trabajos,\n" +
            "    MAX(t.fechaCierre) AS ultimo,\n" +
            "    IFNULL(ROUND(DATEDIFF(CURDATE(), u.fechaRegistro) / 365), 0) AS year\n" +
            "FROM tecnicoscuadrillas tc \n" +
            "JOIN usuarios u ON tc.idTecnico = u.idUsuarios \n" +
            "LEFT JOIN cuadrillas c ON tc.idCuadrilla = c.idCuadrillas\n" +
            "LEFT JOIN tickets t ON c.idCuadrillas = t.idCuadrilla \n" +
            "WHERE tc.liderTecnico = 1 AND u.idEmpresas = ?1\n" +
            "GROUP BY c.idCuadrillas, u.idUsuarios;\n")
    List<ListaCuadrillaCompletaDto> cuadrillaCompleta(int idEmpresa);

    @Query(nativeQuery = true, value = "SELECT \n" +
            "    idCuadrilla,\n" +
            "    CONCAT(leader.nombre, ' ', leader.apellido) AS lider,\n" +
            "    cantidad,\n" +
            "    fecha,\n" +
            "    year\n" +
            "FROM (\n" +
            "    SELECT \n" +
            "        c.idCuadrillas AS idCuadrilla,\n" +
            "        MAX(CASE WHEN tc.liderTecnico = 1 THEN u.nombre END) AS nombreLider,\n" +
            "        MAX(CASE WHEN tc.liderTecnico = 1 THEN u.apellido END) AS apellidoLider,\n" +
            "        COUNT(DISTINCT u.idUsuarios) AS cantidad,\n" +
            "        c.fechaCreacion as fecha,\n" +
            "        IFNULL(ROUND(DATEDIFF(CURDATE(), MIN(u.fechaRegistro)) / 365), 0) AS year\n" +
            "    FROM \n" +
            "        cuadrillas c\n" +
            "    JOIN \n" +
            "        tecnicoscuadrillas tc ON c.idCuadrillas = tc.idCuadrilla\n" +
            "    LEFT JOIN \n" +
            "        usuarios u ON tc.idTecnico = u.idUsuarios\n" +
            "    LEFT JOIN \n" +
            "        tickets t ON c.idCuadrillas = t.idCuadrilla\n" +
            "    WHERE \n" +
            "        (t.estado IN (6, 7) OR t.estado IS NULL) \n" +
            "    GROUP BY \n" +
            "        c.idCuadrillas\n" +
            "    HAVING \n" +
            "        COUNT(DISTINCT u.idUsuarios) < 5\n" +
            ") AS subquery\n" +
            "JOIN usuarios leader ON leader.nombre = subquery.nombreLider AND leader.apellido = subquery.apellidoLider\n" +
            "WHERE leader.idEmpresas = ?1\n" +
            "GROUP BY idCuadrilla, cantidad, fecha, year;\n")
    List<ListaCuadrillaImcompletaDto> cuadrillaImCompleta(int idEmpresa);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS nuevos\n" +
            "FROM (\n" +
            "    SELECT c.idCuadrillas\n" +
            "    FROM cuadrillas c\n" +
            "    JOIN tecnicoscuadrillas ct ON c.idCuadrillas = ct.idCuadrilla\n" +
            "    WHERE MONTH(c.fechaCreacion) = MONTH(CURRENT_DATE())\n" +
            "      AND YEAR(c.fechaCreacion) = YEAR(CURRENT_DATE())\n" +
            "    GROUP BY c.idCuadrillas\n" +
            "    HAVING COUNT(ct.idAsignacion) = 5\n" +
            ") AS CuadrillasCon5Tecnicos;")
    int obtenerCuadrillasUltimoMes();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS mespasado\n" +
            "FROM (\n" +
            "    SELECT c.idCuadrillas\n" +
            "    FROM cuadrillas c\n" +
            "    JOIN tecnicoscuadrillas ct ON c.idCuadrillas = ct.idCuadrilla\n" +
            "    WHERE MONTH(c.fechaCreacion) = MONTH(CURRENT_DATE() - INTERVAL 1 MONTH)\n" +
            "      AND YEAR(c.fechaCreacion) = YEAR(CURRENT_DATE() - INTERVAL 1 MONTH)\n" +
            "    GROUP BY c.idCuadrillas\n" +
            "    HAVING COUNT(ct.idAsignacion) = 5\n" +
            ") AS CuadrillasCon5Tecnicos;")
    int cuadrillaMesPasado();

}
