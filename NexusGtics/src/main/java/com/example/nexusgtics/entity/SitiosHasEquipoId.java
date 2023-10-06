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
public class SitiosHasEquipoId implements Serializable {
    @NotNull
    @Column(name = "idSitios", nullable = false)
    private Integer idSitios;

    @NotNull
    @Column(name = "idEquipos", nullable = false)
    private Integer idEquipos;
}