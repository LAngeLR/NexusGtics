package com.example.nexusgtics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tecnologiainstalada")
public class Tecnologiainstalada {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTecnologiaInstalada", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 20)
    private String nombre;
    public boolean checkTecInstalada(List<TecnologiainstaladaFormulario> formularioList){
        for(TecnologiainstaladaFormulario tecInsForm : formularioList){
            if(tecInsForm.getIdTecnologiaInstalada().getId() == id ){
                return true;
            }
        }
        return false;
    }
}