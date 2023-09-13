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
public class DinamicasitiovalorId implements Serializable {
    @Column(name = "idSitios", nullable = false)
    private Integer idSitios;

    @Column(name = "idAtributoSitio", nullable = false)
    private Integer idAtributoSitio;

}