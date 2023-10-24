package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTickets", nullable = false)
    private Integer idTickets;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "estado", nullable = false)
    private Integer estado;

    @Column(name = "fechaCreacion", nullable = false)
    private LocalDate fechaCreacion;

    @Column(name = "fechaCierre")
    private LocalDate fechaCierre;

    @Column(name = "usuarioSolicitante", length = 70)
    private String usuarioSolicitante;

    @ManyToOne
    @JoinColumn(name = "idSupervisorEncargado")
    private Usuario idSupervisorEncargado;

    @ManyToOne
    @JoinColumn(name = "idUsuarioCreador")
    private Usuario idUsuarioCreador;

    @ManyToOne
    @JoinColumn(name = "idEmpresaAsignada", nullable = false)
    private Empresa idEmpresaAsignada;

    @ManyToOne
    @JoinColumn(name = "idCuadrilla")
    private Cuadrilla idCuadrilla;

    @ManyToOne
    @JoinColumn(name = "idSitios", nullable = false)
    private Sitio idSitios;

    @ManyToOne
    @JoinColumn(name = "idsitioCerrado", nullable = false)
    private Sitio idsitioCerrado;

    @ManyToOne
    @JoinColumn(name = "idTipoTicket", nullable = false)
    private Tipoticket idTipoTicket;

    @Column(name = "prioridad", length = 45)
    private String prioridad;
}