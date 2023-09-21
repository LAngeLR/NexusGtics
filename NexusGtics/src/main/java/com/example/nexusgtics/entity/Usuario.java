package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuarios", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 45)
    private String apellido;

    @Column(name = "correo", nullable = false, length = 45)
    private String correo;

    @Column(name = "contrasenia", length = 45)
    private String contrasenia;

    @Column(name = "habilitado")
    private Boolean habilitado = false;

    @Column(name = "fechaRegistro")
    private LocalDate fechaRegistro;

    @Column(name = "tecnicoConCuadrilla")
    private Boolean tecnicoConCuadrilla;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idCargos", nullable = false)
    private Cargo cargo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idEmpresas", nullable = false)
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "idImagenPerfil")
    private Archivo archivo;

    @ManyToOne
    @JoinColumn(name = "IdCuadrilla")
    private Cuadrilla cuadrilla;

    @Column(name = "dni")
    private Integer dni;

    @Column(name = "descripcion", length = 130)
    private String descripcion;

}