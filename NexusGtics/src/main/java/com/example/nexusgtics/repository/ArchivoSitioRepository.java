package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.Archivossitio;
import com.example.nexusgtics.entity.Sitio;
import com.example.nexusgtics.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArchivoSitioRepository extends JpaRepository<Archivossitio, Integer> {

    @Query(value ="SELECT nombreSitio, COUNT(*) AS cantidadArchivos FROM nexus.archivossitio GROUP BY nombreSitio", nativeQuery = true )
    List<Archivossitio> nombreSitios();

}
