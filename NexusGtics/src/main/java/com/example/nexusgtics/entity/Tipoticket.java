package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tipoticket")
public class Tipoticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTipoTicket", nullable = false)
    private Integer idTipoTicket;

    @Column(name = "nombreTipoTicket", nullable = false, length = 45)
    private String nombreTipoTicket;

}