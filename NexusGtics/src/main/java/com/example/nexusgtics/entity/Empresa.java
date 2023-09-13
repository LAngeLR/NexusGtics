package com.example.nexusgtics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "empresas")
public class Empresa {
    @Id
    @Column(name = "idEmpresas", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "ruc", nullable = false)
    private Integer ruc;

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