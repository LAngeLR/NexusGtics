package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @Column(name = "idTickets", nullable = false)
    private Integer idTickets;

    @Column(name = "descripcion", nullable = false, length = 45)
    private String descripcion;

    @Column(name = "estado", nullable = false)
    private Integer estado;

    @Column(name = "fechaCreacion", nullable = false)
    private Instant fechaCreacion;

    @Column(name = "fechaCierre")
    private Instant fechaCierre;

    @Column(name = "usuarioSolicitante", length = 70)
    private String usuarioSolicitante;

    @ManyToOne
    @JoinColumn(name = "idSupervisorEncargado")
    private Usuario idSupervisorEncargado;

    @ManyToOne
    @JoinColumn(name = "idUsuarioCreador", nullable = false)
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
    @JoinColumn(name = "idTipoTicket", nullable = false)
    private Tipoticket idTipoTicket;

    @Column(name = "prioridad", length = 45)
    private String prioridad;

}