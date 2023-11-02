package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tecnologiainstalada_formularios")
public class TecnologiainstaladaFormulario {
    @EmbeddedId
    private TecnologiainstaladaFormularioId id;

    @MapsId("idTecnologiaInstalada")
    @ManyToOne
    @JoinColumn(name = "idTecnologiaInstalada", nullable = false)
    private Tecnologiainstalada idTecnologiaInstalada;

    @MapsId("idFormularios")
    @ManyToOne
    @JoinColumn(name = "idFormularios", nullable = false)
    private Formulario idFormularios;

}