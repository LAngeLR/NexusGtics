package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.TecnologiainstaladaFormulario;
import com.example.nexusgtics.entity.TecnologiainstaladaFormularioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

public interface TecnologiainstaladaRepository extends JpaRepository<TecnologiainstaladaFormulario, TecnologiainstaladaFormularioId> {
    @Query(value ="select * from sitios_has_equipos where  idSitios != ?1", nativeQuery = true )
    List<TecnologiainstaladaFormulario> agruparTecnologia(int idTecnologiaInstalada);



}
