package com.example.nexusgtics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dinamicaequipoatributo")
public class Dinamicaequipoatributo {
    @Id
    @Column(name = "idDinamicaEquipoAtributo", nullable = false)
    private Integer idDinamicaEquipoAtributo;

    @Column(name = "nuevoCampo", nullable = false, length = 45)
    private String nuevoCampo;

    @Column(name = "tipoDato", nullable = false)
    private Integer tipoDato;

}