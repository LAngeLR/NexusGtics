package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "formularios")
public class Formulario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idFormularios", nullable = false)
    private Integer idFormularios;

    @Column(name = "fechaLlenado", nullable = false)
    private Instant fechaLlenado;

    @Column(name = "descripcion", nullable = false, length = 45)
    private String descripcion;

    @Column(name = "confirmacion", nullable = false)
    private Boolean confirmacion = false;

    @ManyToOne
    @JoinColumn(name = "idTecnico", nullable = false)
    private Usuario tecnico;    //cuidado, no se llama usuario sino tecnico

    @ManyToOne
    @JoinColumn(name = "idImagenesForm", nullable = false)
    private Archivo archivo;

    @ManyToOne
    @JoinColumn(name = "idTickets", nullable = false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "idTipoTicket", nullable = false)
    private Tipoticket tipoticket;

    @Size(max = 45)
    @NotNull
    @Column(name = "hrelevantes", nullable = false, length = 45)
    private String hrelevantes;

    @NotNull
    @Column(name = "conexion", nullable = false)
    private Boolean conexion;

    @NotNull
    @Column(name = "movilidad", nullable = false)
    private Boolean movilidad;

    @Size(max = 45)
    @NotNull
    @Column(name = "nomredantario", nullable = false, length = 45)
    private String nomredantario;

    @NotNull
    @Column(name = "dni", nullable = false)
    private Integer dni;

    @NotNull
    @Column(name = "area", nullable = false)
    private Float area;

    @Size(max = 45)
    @NotNull
    @Column(name = "observaciones", nullable = false, length = 45)
    private String observaciones;

    @NotNull
    @Column(name = "construccion", nullable = false)
    private Boolean construccion;

    @NotNull
    @Column(name = "instalacion", nullable = false)
    private Boolean instalacion;

    @NotNull
    @Column(name = "despliegue", nullable = false)
    private Boolean despliegue;

    @Size(max = 45)
    @NotNull
    @Column(name = "trabarealizados", nullable = false, length = 45)
    private String trabarealizados;

    @NotNull
    @Column(name = "equipoencendido", nullable = false)
    private Boolean equipoencendido;

    @NotNull
    @Column(name = "equipoconectado", nullable = false)
    private Boolean equipoconectado;

    @Size(max = 45)
    @NotNull
    @Column(name = "situacion", nullable = false, length = 45)
    private String situacion;

    @Size(max = 45)
    @NotNull
    @Column(name = "acciones", nullable = false, length = 45)
    private String acciones;

    @NotNull
    @Column(name = "bateriasestado", nullable = false)
    private Boolean bateriasestado;

    @Size(max = 45)
    @NotNull
    @Column(name = "averia", nullable = false, length = 45)
    private String averia;

    /* Analizar estas dos

    @Size(max = 45)
    @NotNull
    @Column(name = "tipoTransporte", nullable = false, length = 45)
   private String tipoTransporte;*/
//
    /*@ManyToMany
    @JoinTable(name = "tecnologiainstalada_formularios",
           joinColumns = @JoinColumn(name = "idFormularios"),
            inverseJoinColumns = @JoinColumn(name = "idTecnologiaInstalada"))
    private Set<Tecnologiainstalada> tecnologiainstaladas = new LinkedHashSet<>();*/

}