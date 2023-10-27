package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "`sitios_has_equipos`")
public class SitiosHasEquipo {
    @EmbeddedId
    private SitiosHasEquipoId id;

    @MapsId("idSitios")
    @ManyToOne
    @JoinColumn(name = "idSitios", nullable = false)
    private Sitio idSitios;

    @MapsId("idEquipos")
    @ManyToOne
    @JoinColumn(name = "idEquipos", nullable = false)
    private Equipo idEquipos;

}