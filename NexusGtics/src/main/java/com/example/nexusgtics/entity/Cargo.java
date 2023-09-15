package com.example.nexusgtics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cargos")
public class Cargo {
    @Id
    @Column(name = "idCargos", nullable = false)
    private Integer idCargos;

    @Column(name = "nombreCargo", nullable = false, length = 45)
    private String nombreCargo;

}