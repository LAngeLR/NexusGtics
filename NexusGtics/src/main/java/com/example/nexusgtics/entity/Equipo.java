package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "equipos")
public class Equipo {
    @Id
    @Column(name = "idEquipos", nullable = false)
    private Integer idEquipos;

    @Column(name = "marca", nullable = false, length = 45)
    private String marca;

    @Column(name = "descripcion", nullable = false, length = 45)
    private String descripcion;

    @Column(name = "modelo", nullable = false, length = 45)
    private String modelo;

    @Column(name = "paginaModelo", length = 130)
    private String paginaModelo;

    @ManyToOne
    @JoinColumn(name = "idSitios", nullable = false)
    private Sitio sitio;

    @ManyToOne
    @JoinColumn(name = "idImagenes", nullable = false)
    private Archivo archivo;

    @ManyToOne
    @JoinColumn(name = "idTipoEquipo", nullable = false)
    private Tipoequipo tipoequipo;

}