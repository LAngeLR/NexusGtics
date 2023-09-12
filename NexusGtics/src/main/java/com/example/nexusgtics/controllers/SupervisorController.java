package com.example.nexusgtics.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/supervisor")
public class SupervisorController {

    @GetMapping("/")
    public String paginaPrincipal(){
        return "Supervisor/menuSupervisor";
    }

    @GetMapping("/listaTickets")
    public String Tickets(){
        return "Supervisor/listaTickets";
    }

    @GetMapping("/listaCuadrillas")
    public String Cuadrillas(){
        return "Supervisor/listaCuadrillas";
    }

    @GetMapping("/ticketNuevo")
    public String nuevoTicket(){
        return "Supervisor/ticketNuevo";
    }

    @GetMapping("/ticketProceso")
    public String procesoTicket(){
        return "Supervisor/ticketProceso";
    }

    @GetMapping("/ticketCerrado")
    public String cerradoTicket(){
        return "Supervisor/ticketCerrado";
    }

    @GetMapping("/comentarios")
    public String comentarioTicket(){
        return "Supervisor/comentariosTickets";
    }

    @GetMapping("/perfil")
    public String perfilSupervisor(){
        return "Supervisor/perfilSupervisor";
    }

    @GetMapping("/mapaTickets")
    public String mapaTickets(){
        return "Supervisor/mapaTickets";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "Supervisor/dashboardSupervisor";
    }

    @GetMapping("/crearCuadrilla")
    public String crearCuadrilla(){
        return "Supervisor/crearCuadrilla";
    }
}
