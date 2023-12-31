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
@Table(name = "tipoequipo")
public class Tipoequipo {
    @Id
    @Column(name = "idTipoEquipo", nullable = false)
    private Integer idTipoEquipo;

    @Column(name = "nombreTipo", nullable = false, length = 45)
    private String nombreTipo;

}