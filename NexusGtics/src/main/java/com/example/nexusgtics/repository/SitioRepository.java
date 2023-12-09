package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.Sitio;
import com.example.nexusgtics.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface SitioRepository extends JpaRepository<Sitio, Integer> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into sitios (departamento, provincia, distrito, ubigeo, latitud, longitud, tipo, tipoZona, idArchivos, habilitado) values (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, true)")
    void guardarSitio(String departamento, String provincia, String distrito, Integer ubigeo, BigDecimal latitud, BigDecimal longitud, String tipo, String tipoZona, int idArchivos);

    @Query(value ="select * from nexus.sitios where habilitado = 1", nativeQuery = true )
    List<Sitio> listaDeSitios();

    @Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query(value ="update nexus.sitios set habilitado = false where idSitios = ?1", nativeQuery = true )
    void eliminarEmpresa(int id);

    @Modifying
    @Transactional
    @Query(value ="ALTER TABLE nexus.sitios ADD COLUMN ?1 ?2", nativeQuery = true )
    void dinamicoDouble(String campo, String variable);

    @Modifying
    @Transactional
    @Query(value ="ALTER TABLE nexus.sitios ADD COLUMN ?1 ?2", nativeQuery = true )
    void dinamicoInt(String campo, String variable);

    @Modifying
    @Transactional
    @Query(value ="ALTER TABLE nexus.sitios ADD COLUMN ?1 ?2", nativeQuery = true )
    void dinamicoVarchar(String campo, String variable);

}
