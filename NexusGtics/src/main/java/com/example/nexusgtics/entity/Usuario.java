package com.example.nexusgtics.entity;

import com.example.nexusgtics.entity.Archivo;
import com.example.nexusgtics.entity.Cargo;
import com.example.nexusgtics.entity.Empresa;
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
    @Column(name = "idUsuarios", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 45)
    private String apellido;

    @Column(name = "correo", nullable = false, length = 45)
    private String correo;

    @Column(name = "contrasenia", nullable = false, length = 45)
    private String contrasenia;

    @Column(name = "habilitado", nullable = false)
    private Boolean habilitado = false;

    @Column(name = "fechaRegistro")
    private LocalDate fechaRegistro;

    @Column(name = "tecnicoConCuadrilla")
    private Boolean tecnicoConCuadrilla;

    @ManyToOne
    @JoinColumn(name = "idCargos", nullable = false)
    private Cargo idCargos;

    @ManyToOne
    @JoinColumn(name = "idEmpresas", nullable = false)
    private Empresa idEmpresas;

    @ManyToOne
    @JoinColumn(name = "idImagenPerfil", nullable = false)
    private Archivo idImagenPerfil;

}