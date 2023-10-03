package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "archivos")
public class Archivo  implements Serializable {
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

    @Column(name = "contentType")
    private String contentType;

}