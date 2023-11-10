package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "empresas")
public class Empresa implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEmpresas", nullable = false)
    private Integer idEmpresas;

    @Column(name = "nombre", nullable = false, length = 45)
    @NotBlank(message = "El campo no debe estar vacío")
    @Size(max = 45)
    private String nombre;

    @Column(name = "ruc", nullable = false)
    @NotBlank(message = "El campo no debe estar vacío")
    @Pattern(regexp = "^[0-9]{11}$", message = "El RUC debe ser un número de 11 dígitos")
    private String ruc;

    @Column(name = "telefono", nullable = false)
    @NotBlank(message = "El campo no debe estar vacío")
    @Pattern(regexp = "^[0-9]{7,10}$", message = "El teléfono debe ser un número de entre 7 a 10 dígitos")
    private String telefono;

    @Column(name = "direccion", nullable = false, length = 120)
    @NotBlank(message = "El campo no debe estar vacío")
    @Size(max = 120)
    private String direccion;

    @Column(name = "correo", nullable = false, length = 45)
    @NotBlank(message = "El campo no debe estar vacío")
    @Email(message = "ingrese un correo electronico")
    @Size(max = 45)
    private String correo;

    @Column(name = "fechaAfiliacion")
    private LocalDate fechaAfiliacion;

    @Column(name = "representante", nullable = false, length = 75)
    @NotBlank(message = "El campo no debe estar vacío")
    @Pattern(regexp = "^[A-Za-zñáéíóúÁÉÍÓÚ ]+$", message = "El nombre del representante debe contener solo letras")
    @Size(max = 75)
    private String representante;

}