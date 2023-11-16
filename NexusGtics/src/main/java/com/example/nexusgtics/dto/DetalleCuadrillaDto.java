package com.example.nexusgtics.dto;

import java.util.Date;

public interface DetalleCuadrillaDto {

    Integer getIdCuadrilla();
    String getLider();
    String getSupervisor();
    byte[] getFoto();
    int getTrabajos();
    Date getUltimo();
    int getYear();
    String getTipo();
    Date getFechaCreacion();

}
