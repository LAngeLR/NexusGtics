package com.example.nexusgtics.repository;
import com.example.nexusgtics.entity.Sitio;
import com.example.nexusgtics.entity.SitioCerrado;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface SitioCerradoRepository extends JpaRepository<SitioCerrado, Integer>{

    @Query(value ="select * from nexus.sitios where habilitado = 1", nativeQuery = true )
    List<Sitio> listaDeSitiosCerrados();


}
