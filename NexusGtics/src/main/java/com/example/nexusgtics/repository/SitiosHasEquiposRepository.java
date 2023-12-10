package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.SitiosHasEquipo;
import com.example.nexusgtics.entity.SitiosHasEquipoId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SitiosHasEquiposRepository extends JpaRepository<SitiosHasEquipo, SitiosHasEquipoId> {

    @Query(value ="select * from sitios_has_equipos where  idSitios = ?1", nativeQuery = true )
    List<SitiosHasEquipo> listaEquiposPorSitio(int idSitios);

    @Query(value ="SELECT MIN(idSitios) AS idSitios, idEquipos\n" +
            "FROM sitios_has_equipos\n" +
            "WHERE idEquipos NOT IN (SELECT idEquipos FROM sitios_has_equipos WHERE idSitios = ?1)\n" +
            "GROUP BY idEquipos;", nativeQuery = true )
    List<SitiosHasEquipo> listaEquiposNoSitio(int idSitios);

        @Transactional
        @Modifying
        @org.springframework.transaction.annotation.Transactional
        @Query(value = "INSERT INTO sitios_has_equipos (idSitios, idEquipos) VALUES (?1, ?2)", nativeQuery = true)
        void agregarEquipo(int idSitios, int idEquipos);

    // En la interfaz SitiosHasEquiposRepository
    @Query(value = "SELECT * FROM sitios_has_equipos WHERE idSitios = ?1 AND idEquipos = ?2", nativeQuery = true)
    List<SitiosHasEquipo> listaEquiposPorSitioYEquipo(int idSitios, int idEquipos);


}
