package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "comentarios")
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idComentarios", nullable = false)
    private Integer idComentarios;

    @Column(name = "comentario", length = 250)
    private String comentario;

    @Column(name = "fechaPublicacion")
    private Instant fechaPublicacion;

    @ManyToOne
    @JoinColumn(name = "idTickets", nullable = false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "idComentarista", nullable = false)
    private Usuario comentarista;

    @Column(name = "fechaCreacion")
    private LocalDate fechaCreacion;

    @Column(name = "horaCreacion")
    private LocalTime horaCreacion;

}