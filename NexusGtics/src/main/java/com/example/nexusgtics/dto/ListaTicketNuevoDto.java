package com.example.nexusgtics.dto;

import java.util.Date;

public interface ListaTicketNuevoDto {
    String getIdTickets();
    String getAnalistaCreador();
    String getUsuarioSolicitante();

    Date getFechaCreacion();

    String getPrioridad();
}
