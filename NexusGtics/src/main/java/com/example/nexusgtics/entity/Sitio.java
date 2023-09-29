package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "sitios")
public class Sitio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSitios", nullable = false)
    private Integer idSitios;

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
    @DecimalMin(value = "-999.9999999", message = "La latitud debe ser mayor o igual a -999.9999999")
    @DecimalMax(value = "999.9999999", message = "La latitud debe ser menor o igual a 999.9999999")
    private BigDecimal latitud;


    @Column(name = "longitud", nullable = false, precision = 10, scale = 7)
    @NotNull(message = "El campo no debe estar vacío")
    @DecimalMin(value = "-999.9999999", message = "La longitud debe ser mayor o igual a -999.9999999")
    @DecimalMax(value = "999.9999999", message = "La longitud debe ser menor o igual a 999.9999999")
    private BigDecimal longitud;

    @Column(name = "habilitado", nullable = false)
    private Boolean habilitado = false;

    @Column(name = "tipoZona", nullable = false)
    private String tipoZona;

    @ManyToOne
    @JoinColumn(name = "idArchivos")
    private Archivo archivo;
    /*
    @OneToMany(mappedBy = "sitio", cascade = CascadeType.ALL)
    private List<CampoDinamico> camposDinamicos = new ArrayList<>();
*/
}