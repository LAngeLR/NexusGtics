package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dinamicasitiovalor")
public class Dinamicasitiovalor {
    @EmbeddedId
    private DinamicasitiovalorId id;

    @MapsId("idSitios")
    @ManyToOne
    @JoinColumn(name = "idSitios", nullable = false)
    private Sitio idSitios;

    @MapsId("idAtributoSitio")
    @ManyToOne
    @JoinColumn(name = "idAtributoSitio", nullable = false)
    private Dinamicasitioatributo idAtributoSitio;

    @Column(name = "valorNuevoCampo", length = 45)
    private String valorNuevoCampo;

}