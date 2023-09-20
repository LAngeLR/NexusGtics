package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "archivos")
public class Archivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idArchivos", nullable = false)
    private Integer idArchivos;

    @Column(name = "tipo", nullable = false)
    private Integer tipo;

    @Column(name = "archivo")
    private byte[] archivo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "contentType", nullable = false)
    private String contentType;

}