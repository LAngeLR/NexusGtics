package com.example.nexusgtics.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "sitiocerrado")
public class SitioCerrado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsitioCerrado", nullable = false)
    private Integer idsitioCerrado;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "provincia", nullable = false, length = 45)
    @Size(max = 45)
    @Pattern(regexp = "^[A-Za-zñáéíóúÁÉÍÓÚ ]+$", message = "La Provincia debe contener solo letras")
    @NotBlank(message = "El campo no debe estar vacío")
    private String provincia;

    @Column(name = "distrito", nullable = false, length = 45)
    @Size(max = 45)
    @Pattern(regexp = "^[A-Za-zñáéíóúÁÉÍÓÚ ]+$", message = "El distrito debe contener solo letras")
    @NotBlank(message = "El campo no debe estar vacío")
    private String distrito;

    @Column(name = "ubigeo", nullable = false)
    @NotNull(message = "El campo no debe estar vacío")
    @Digits(integer = 6,fraction = 0)
    @Positive
    private Integer ubigeo;

    @Column(name = "departamento", nullable = false, length = 45)
    @Size(max = 45)
    @Pattern(regexp = "^[A-Za-zñáéíóúÁÉÍÓÚ ]+$", message = "El departamento debe contener solo letras")
    @NotBlank(message = "El campo no debe estar vacío")
    private String departamento;

    @Column(name = "latitud", nullable = false, precision = 10, scale = 7)
    @NotNull(message = "El campo no debe estar vacío")
    @DecimalMin(value = "-90.0000000", message = "La latitud debe ser mayor o igual a -90.0000000")
    @DecimalMax(value = "90.0000000", message = "La latitud debe ser menor o igual a 90.0000000")
    private BigDecimal latitud;


    @Column(name = "longitud", nullable = false, precision = 10, scale = 7)
    @NotNull(message = "El campo no debe estar vacío")
    @DecimalMin(value = "-180.0000000", message = "La longitud debe ser mayor o igual a -180.0000000")
    @DecimalMax(value = "180.0000000", message = "La longitud debe ser menor o igual a 180.0000000")
    private BigDecimal longitud;

    @Column(name = "habilitado", nullable = false)
    private Boolean habilitado = false;

    @Column(name = "tipoZona", nullable = false)
    private String tipoZona;

    @Column(name = "nombre", length = 45)
    @Size(max = 45)
    @NotBlank(message = "El campo no debe estar vacío")
    private String nombre;
}
