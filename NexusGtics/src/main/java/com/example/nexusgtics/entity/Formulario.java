package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "formularios")
public class Formulario {
    @Id
    @Column(name = "idFormularios", nullable = false)
    private Integer idFormularios;

    @Column(name = "fechaLlenado", nullable = false)
    private Instant fechaLlenado;

    @Column(name = "descripcion", nullable = false, length = 45)
    private String descripcion;

    @Column(name = "confirmacion", nullable = false)
    private Boolean confirmacion = false;

    @ManyToOne
    @JoinColumn(name = "idTecnico", nullable = false)
    private Usuario idTecnico;

    @ManyToOne
    @JoinColumn(name = "idImagenesForm", nullable = false)
    private Archivo idImagenesForm;

    @ManyToOne
    @JoinColumn(name = "idTickets", nullable = false)
    private Ticket idTickets;

    @ManyToOne
    @JoinColumn(name = "idTipoTicket", nullable = false)
    private Tipoticket idTipoTicket;

}