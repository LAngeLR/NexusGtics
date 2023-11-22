package com.example.nexusgtics.dto;

import com.example.nexusgtics.entity.HistorialTicket;

import java.sql.Time;

public interface ActividadDto {

    String getDescripcion();
    Integer getIdReasignado();
    Integer getIdUsuarios();
    String getReasignado();
    String getUsuario();
    Integer getEstado();
    String getCreador();
    String getSupervisor();
    Integer getIdCuadrillas();
    Time getTranscurso();
}
