package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "cargos")
public class Cargo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCargos", nullable = false)
    private Integer idCargos;

    @Column(name = "nombreCargo", nullable = false, length = 45)
    private String nombreCargo;

}