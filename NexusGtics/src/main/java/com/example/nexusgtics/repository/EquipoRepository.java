package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.Equipo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EquipoRepository extends JpaRepository<Equipo, Integer> {


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into equipos (marca, modelo, descripcion, paginaModelo, idSitios, idTipoEquipo, idImagenes, habilitado) values (?1, ?2, ?3, ?4, ?5, ?6, ?7, true)")
    void guardarEquipo(String marca, String modelo, String descripcion, String paginaModelo, int idSitios, int idTipoEquipo, int idImagenes);



    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update equipos set marca = ?1, modelo = ?2, descripcion = ?3, paginaModelo = ?4, idSitios = ?5, idTipoEquipo = ?6, idImagenes = ?7, habilitado = true where idEquipos = ?8")
    void actualizarEquipo(String marca, String modelo, String descripcion, String paginaModelo, int idSitios, int idTipoEquipo, int idImagenes, int idEquipos);

}
