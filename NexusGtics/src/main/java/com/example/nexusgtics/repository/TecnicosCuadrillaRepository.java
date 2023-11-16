package com.example.nexusgtics.repository;

import com.example.nexusgtics.dto.DetalleCuadrillaDto;
import com.example.nexusgtics.entity.Tecnicoscuadrilla;
import com.example.nexusgtics.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TecnicosCuadrillaRepository extends JpaRepository<Tecnicoscuadrilla, Integer> {

    @Query(value ="select idTecnico from tecnicoscuadrillas where idCuadrilla=?1 and liderTecnico=1", nativeQuery = true )
    Integer obtenerIdLider(int idCuadrilla);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into tecnicoscuadrillas (idTecnico, idCuadrilla, liderTecnico) values (?2, ?1, ?3)")
    void insertarTecnico(int idCuadrilla, int idTecnico, int es);

    @Query(nativeQuery = true, value = "SELECT \n" +
            "    c.idCuadrillas AS idCuadrilla,\n" +
            "    CONCAT(u.nombre, ' ', u.apellido) AS lider,\n" +
            "    CONCAT(supervisor.nombre, ' ', supervisor.apellido) AS supervisor,\n" +
            "    archivos.archivo AS foto,  -- Nuevo campo agregado\n" +
            "    COUNT(DISTINCT t.idTickets) AS trabajos,\n" +
            "    MAX(t.fechaCierre) AS ultimo,\n" +
            "    IFNULL(ROUND(DATEDIFF(CURDATE(), u.fechaRegistro) / 365), 0) AS year,\n" +
            "    (SELECT tt.nombreTipoTicket\n" +
            "     FROM historialtickets ht\n" +
            "     LEFT JOIN tickets t2 ON ht.idTickets = t2.idTickets\n" +
            "     LEFT JOIN tipoticket tt ON t2.idTipoTicket = tt.idTipoTicket\n" +
            "     WHERE t2.idCuadrilla = c.idCuadrillas AND ht.estado = 7\n" +
            "     ORDER BY ht.fechaCambioEstado DESC\n" +
            "     LIMIT 1) AS tipo,\n" +
            "     c.fechaCreacion\n" +
            "FROM tecnicoscuadrillas tc \n" +
            "JOIN usuarios u ON tc.idTecnico = u.idUsuarios \n" +
            "LEFT JOIN cuadrillas c ON tc.idCuadrilla = c.idCuadrillas\n" +
            "LEFT JOIN tickets t ON c.idCuadrillas = t.idCuadrilla \n" +
            "LEFT JOIN usuarios supervisor ON c.idSupervisor = supervisor.idUsuarios\n" +
            "LEFT JOIN archivos ON supervisor.idImagenPerfil = archivos.idArchivos  -- Nuevo LEFT JOIN\n" +
            "WHERE (t.estado IN (2, 3, 4, 5, 6, 7, 8) OR t.estado IS NULL) AND tc.liderTecnico = 1 AND c.idCuadrillas = ?1\n" +
            "GROUP BY c.idCuadrillas, u.idUsuarios\n")
    Optional<DetalleCuadrillaDto> detalleCuadrilla(int idCuadrilla);
}
