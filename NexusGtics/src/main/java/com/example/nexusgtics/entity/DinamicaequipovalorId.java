package com.example.nexusgtics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class DinamicaequipovalorId implements Serializable {
    @Column(name = "idAtributoEquipo", nullable = false)
    private Integer idAtributoEquipo;

    @Column(name = "idEquipos", nullable = false)
    private Integer idEquipos;


}