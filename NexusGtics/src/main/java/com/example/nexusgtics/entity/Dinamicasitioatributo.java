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
@Table(name = "dinamicasitioatributo")
public class Dinamicasitioatributo {
    @Id
    @Column(name = "idDinamicaSitioAtributo", nullable = false)
    private Integer idDinamicaSitioAtributo;

    @Column(name = "nuevoCampo", nullable = false, length = 45)
    private String nuevoCampo;

    @Column(name = "tipoAtributo", nullable = false)
    private Integer tipoAtributo;

}