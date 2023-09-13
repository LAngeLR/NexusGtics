package com.example.nexusgtics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tipoticket")
public class Tipoticket {
    @Id
    @Column(name = "idTipoTicket", nullable = false)
    private Integer id;

    @Column(name = "nombreTipoTicket", nullable = false, length = 45)
    private String nombreTipoTicket;

}