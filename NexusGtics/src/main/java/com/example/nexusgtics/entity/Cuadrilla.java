package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cuadrillas")
public class Cuadrilla {
    @Id
    @Column(name = "idCuadrillas", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idSupervisor", nullable = false)
    private Usuario idSupervisor;

    @ManyToOne
    @JoinColumn(name = "idTecnico", nullable = false)
    private Usuario idTecnico;

    @Column(name = "ocupado", nullable = false)
    private Boolean ocupado = false;

}