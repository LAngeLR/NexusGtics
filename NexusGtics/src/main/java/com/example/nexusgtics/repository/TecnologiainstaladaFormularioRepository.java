package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.TecnologiainstaladaFormulario;
import com.example.nexusgtics.entity.TecnologiainstaladaFormularioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TecnologiainstaladaFormularioRepository extends JpaRepository<TecnologiainstaladaFormulario, TecnologiainstaladaFormularioId> {
    @Query(value ="select * from tecnologiainstalada_formularios where  idFormularios = ?1", nativeQuery = true )
    List<TecnologiainstaladaFormulario> agruparTecnologia(int idFormularios);



}
