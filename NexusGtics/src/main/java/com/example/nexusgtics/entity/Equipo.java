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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idSitios", nullable = false)
    private Sitio sitio;

    @ManyToOne
    @JoinColumn(name = "idImagenes")
    private Archivo archivo;

    @ManyToOne
    @JoinColumn(name = "idTipoEquipo", nullable = false, referencedColumnName = "idTipoEquipo")
    private Tipoequipo tipoequipo;

    @Column(name = "habilitado")
    private Boolean habilitado;

}