package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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
    @NotBlank(message = "El campo no debe estar vacío")
    @Size(max = 45)
    private String descripcion;

    @Column(name = "estado", nullable = false)
    private Integer estado;

    @Column(name = "fechaCreacion", nullable = false)
    private LocalDate fechaCreacion;

    @Column(name = "fechaCierre")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @FutureOrPresent(message="La fecha de cierre debe ser en el presente o en el futuro")
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

    @Column(name = "idsitioCerrado", nullable = false)
    private Integer idsitioCerrado;

    @ManyToOne
    @JoinColumn(name = "idTipoTicket", nullable = false)
    private Tipoticket idTipoTicket;

    @Column(name = "prioridad", length = 45)
    private String prioridad;

    @Column(name = "reasignado")
    private Integer reasignado;
}