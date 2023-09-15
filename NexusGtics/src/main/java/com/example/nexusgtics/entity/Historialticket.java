package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "historialtickets")
public class Historialticket {
    @Id
    @Column(name = "idhistorialTickets", nullable = false)
    private Integer idhistorialTickets;

    @Column(name = "estado", nullable = false)
    private Integer estado;

    @Column(name = "fechaCambioEstado", nullable = false)
    private Instant fechaCambioEstado;

    @ManyToOne
    @JoinColumn(name = "idTickets", nullable = false)
    private Ticket idTickets;

    @ManyToOne
    @JoinColumn(name = "idUsuarios", nullable = false)
    private Usuario idUsuarios;

    @Column(name = "descripcion", length = 250)
    private String descripcion;

}