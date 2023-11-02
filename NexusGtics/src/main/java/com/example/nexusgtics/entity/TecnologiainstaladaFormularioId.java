package com.example.nexusgtics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class TecnologiainstaladaFormularioId implements Serializable {
    @NotNull
    @Column(name = "idTecnologiaInstalada", nullable = false)
    private Integer idTecnologiaInstalada;

    @NotNull
    @Column(name = "idFormularios", nullable = false)
    private Integer idFormularios;

}