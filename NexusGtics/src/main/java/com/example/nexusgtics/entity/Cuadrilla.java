package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "cuadrillas")
public class Cuadrilla implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCuadrillas", nullable = false)
    private Integer idCuadrillas;

    @ManyToOne
    @JoinColumn(name = "idSupervisor", nullable = false)
    private Usuario supervisor;  //cuidado

    @Column(name = "ocupado", nullable = false)
    private Boolean ocupado = false;

    @Column(name = "fechaCreacion", nullable = false)
    private Instant fechaCreacion;

}