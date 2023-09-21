package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.Equipo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EquipoRepository extends JpaRepository<Equipo, Integer> {


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into equipos (marca, modelo, descripcion, paginaModelo, idSitios, idTipoEquipo, habilitado) values (?1, ?2, ?3, ?4, ?5, ?6, true)")
    void guardarEquipo(String marca, String modelo, String descripcion, String paginaModelo, int idSitios, int idTipoEquipo);
}
