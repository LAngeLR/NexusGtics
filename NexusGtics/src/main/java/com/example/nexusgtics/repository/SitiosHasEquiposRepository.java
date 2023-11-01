package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.SitiosHasEquipo;
import com.example.nexusgtics.entity.SitiosHasEquipoId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SitiosHasEquiposRepository extends JpaRepository<SitiosHasEquipo, SitiosHasEquipoId> {

    @Query(value ="select * from sitios_has_equipos where  idSitios = ?1", nativeQuery = true )
    List<SitiosHasEquipo> listaEquiposPorSitio(int idSitios);

    @Query(value ="select * from sitios_has_equipos where  idSitios != ?1", nativeQuery = true )
    List<SitiosHasEquipo> listaEquiposNoSitio(int idSitios);

        @Transactional
        @Modifying
        @org.springframework.transaction.annotation.Transactional
        @Query(value = "INSERT INTO sitios_has_equipos (idSitios, idEquipos) VALUES (?1, ?2)", nativeQuery = true)
        void agregarEquipo(int idSitios, int idEquipos);
}
