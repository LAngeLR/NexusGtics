package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "campos_dinamicos")
public class CampoDinamico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String valor;

    @ManyToOne
    @JoinColumn(name = "sitio_id")
    private Sitio sitio;

    // Getters y setters
}
