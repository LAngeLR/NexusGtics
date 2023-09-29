package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotBlank(message = "El campo no debe estar vacío")
    @Pattern(regexp = "^[A-Za-zñáéíóúÁÉÍÓÚ ]+$", message = "El nombre solo debe contener letras")
    @Size(max = 45)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 45)
    @NotBlank(message = "El campo no debe estar vacío")
    @Pattern(regexp = "^[A-Za-zñáéíóúÁÉÍÓÚ ]+$", message = "El apellido debe contener solo letras")
    @Size(max = 45)
    private String apellido;

    @Column(name = "correo", nullable = false, length = 45)
    @NotBlank(message = "El campo no debe estar vacío")
    @Email(message = "ingrese un correo electronico")
    @Size(max = 45)
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
    @Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe ser un número de 8 dígitos")
    @Size(min = 8, max = 8, message = "El DNI debe tener exactamente 8 dígitos")
    private String dni;

    @Column(name = "descripcion", length = 130)
    @Size(max = 130, message = "La descripción no puede tener más de 130 caracteres")
    private String descripcion;

}