package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "empresas")
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEmpresas", nullable = false)
    private Integer idEmpresas;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "ruc", nullable = false)
    private Long ruc;

    @Column(name = "telefono", nullable = false)
    private Integer telefono;

    @Column(name = "direccion", nullable = false, length = 120)
    private String direccion;

    @Column(name = "correo", nullable = false, length = 45)
    private String correo;

    @Column(name = "fechaAfiliacion")
    private LocalDate fechaAfiliacion;

    @Column(name = "representante", nullable = false, length = 75)
    private String representante;

}