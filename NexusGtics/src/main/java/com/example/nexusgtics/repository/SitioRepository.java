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

}
