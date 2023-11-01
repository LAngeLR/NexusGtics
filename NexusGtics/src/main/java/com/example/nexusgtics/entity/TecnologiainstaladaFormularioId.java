package com.example.nexusgtics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class TecnologiainstaladaFormularioId implements Serializable {
    private static final long serialVersionUID = 8102180204087328867L;
    @NotNull
    @Column(name = "idTecnologiaInstalada", nullable = false)
    private Integer idTecnologiaInstalada;

    @NotNull
    @Column(name = "idFormularios", nullable = false)
    private Integer idFormularios;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TecnologiainstaladaFormularioId entity = (TecnologiainstaladaFormularioId) o;
        return Objects.equals(this.idFormularios, entity.idFormularios) &&
                Objects.equals(this.idTecnologiaInstalada, entity.idTecnologiaInstalada);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFormularios, idTecnologiaInstalada);
    }

}