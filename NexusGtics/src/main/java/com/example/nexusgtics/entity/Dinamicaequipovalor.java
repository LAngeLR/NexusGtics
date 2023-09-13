package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dinamicaequipovalor")
public class Dinamicaequipovalor {
    @EmbeddedId
    private DinamicaequipovalorId id;

    @MapsId("idAtributoEquipo")
    @ManyToOne
    @JoinColumn(name = "idAtributoEquipo", nullable = false)
    private Dinamicaequipoatributo idAtributoEquipo;

    @MapsId("idEquipos")
    @ManyToOne
    @JoinColumn(name = "idEquipos", nullable = false)
    private Equipo idEquipos;

    @Column(name = "valorNuevoCampo", length = 45)
    private String valorNuevoCampo;

}