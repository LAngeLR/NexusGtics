package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "sitios")
public class Sitio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSitios", nullable = false)
    private Integer idSitios;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "provincia", nullable = false, length = 45)
    private String provincia;

    @Column(name = "distrito", nullable = false, length = 45)
    private String distrito;

    @Column(name = "ubigeo", nullable = false)
    private Integer ubigeo;

    @Column(name = "departamento", nullable = false, length = 45)
    private String departamento;

    @Column(name = "latitud", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitud;

    @Column(name = "longitud", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitud;

    @Column(name = "habilitado", nullable = false)
    private Boolean habilitado = false;

    @Column(name = "tipoZona", nullable = false)
    private String tipoZona;

    @ManyToOne
    @JoinColumn(name = "idArchivos")
    private Archivo archivo;

}