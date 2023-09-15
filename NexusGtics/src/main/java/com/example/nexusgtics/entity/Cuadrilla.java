package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "cuadrillas")
public class Cuadrilla {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCuadrillas", nullable = false)
    private Integer idCuadrillas;

    @ManyToOne
    @JoinColumn(name = "idSupervisor", nullable = false)
    private Usuario supervisor;  //cuidado

    @ManyToOne
    @JoinColumn(name = "idTecnico", nullable = false)
    private Usuario tecnico;    //cuidado

    @Column(name = "ocupado", nullable = false)
    private Boolean ocupado = false;

    @Column(name = "fechaCreacion", nullable = false)
    private Instant fechaCreacion;

}