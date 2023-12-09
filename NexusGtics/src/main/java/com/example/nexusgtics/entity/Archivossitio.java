package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "archivossitio")
public class Archivossitio implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idArchivosSitio", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "tipo", nullable = false)
    private Integer tipo;

    @Column(name = "archivo")
    private byte[] archivo;

    @Size(max = 45)
    @Column(name = "nombreArchivo", length = 45)
    private String nombreArchivo;

    @Column(name = "contentType", length = 255)
    private String contentType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idSitioSitio", nullable = false)
    private Sitio idSitioSitio;

    @Column(name = "nombreSitio", length = 45)
    private String nombreSitio;

    @Column(name = "nombreFront", length = 100)
    private String nombreFront;

}