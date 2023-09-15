package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "comentarios")
public class Comentario {
    @Id
    @Column(name = "idComentarios", nullable = false)
    private Integer idComentarios;

    @Column(name = "comentario", length = 250)
    private String comentario;

    @Column(name = "fechaPublicacion", nullable = false)
    private Instant fechaPublicacion;

    @ManyToOne
    @JoinColumn(name = "idTickets", nullable = false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "idComentarista", nullable = false)
    private Usuario comentarista;

}