package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cargos")
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCargos", nullable = false)
    private Integer idCargos;

    @Column(name = "nombreCargo", nullable = false, length = 45)
    private String nombreCargo;

}