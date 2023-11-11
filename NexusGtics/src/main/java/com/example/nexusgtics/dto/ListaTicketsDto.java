package com.example.nexusgtics.dto;

import java.util.Date;

public interface ListaTicketsDto {
    String getIdTickets();
    String getUsuarioSolicitante();
    String getNombreCompleto();
    Date getFechaCreacion();
    Date getFechaCierre();
    String getPrioridad();
    int getEstado();
}
