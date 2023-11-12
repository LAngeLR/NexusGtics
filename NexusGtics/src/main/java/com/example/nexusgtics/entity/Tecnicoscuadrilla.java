package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "tecnicoscuadrillas")
public class Tecnicoscuadrilla implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAsignacion", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idTecnico", nullable = false)
    private Usuario idTecnico;

    @ManyToOne
    @JoinColumn(name = "idCuadrilla", nullable = false)
    private Cuadrilla idCuadrilla;

    @Column(name = "liderTecnico", nullable = false)
    private Integer liderTecnico;

}