package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "tickets")
public class Ticket implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTickets", nullable = false)
    private Integer idTickets;

    @Column(name = "descripcion", nullable = false, length = 45)
    private String descripcion;

    @Column(name = "estado", nullable = false)
    private Integer estado;

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

    @Column(name = "idsitioCerrado")
    private Integer idsitioCerrado;

    @ManyToOne
    @JoinColumn(name = "idTipoTicket", nullable = false)
    private Tipoticket idTipoTicket;

    @Column(name = "prioridad", length = 45)
    private String prioridad;

    @Column(name = "reasignado")
    private Integer reasignado;

    @Column(name = "fechaCreacion", nullable = false)
    private LocalDate fechaCreacion;

    @NotNull
    @Column(name = "horaCreacion", nullable = false)
    private LocalTime horaCreacion;

    @Column(name = "fechaCierre")
    private LocalDate fechaCierre;

    @Column(name = "horaCierre")
    private LocalTime horaCierre;

}