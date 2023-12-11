package com.example.nexusgtics.dto;
import org.springframework.cglib.core.Local;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
public interface HistorialTicketDto {

    int getEstado();
    Integer getIdTickets();

    String getDescripcion();
    Date getFechaCambio();
    Time getHoraCambio();
    String getNombre();
    String getCargo();

}
