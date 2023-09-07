package com.example.nexusgtics.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/analistaOYM")
public class AnalistaOYMController {

    @GetMapping("/")
    public String paginaPrincipal(){
        return "AnalistaOYM/analistaOYM";
    }

    @GetMapping("/listaSitio")
    public String listaSitio(){
        return "AnalistaOYM/oymListaSitio";
    }

    @GetMapping("/editarSitio")
    public String editarSitio(){
        return "AnalistaOYM/oymEditarSitio";
    }
    @GetMapping("/listaEquipo")
    public String listaEquipo(){
        return "AnalistaOYM/oymListaEquipos";
    }
    @GetMapping("/verEquipo")
    public String verEquipo(){
        return "AnalistaOYM/oymVerEquipos";
    }
    @GetMapping("/perfil")
    public String perfilAD(){
        return "AnalistaOYM/perfilAnalistaOYM";
    }
    @GetMapping("/mapaSitios")
    public String mapaSitios(){
        return "AnalistaOYM/oymMapaSitios";
    }
    @GetMapping("/mapaTickets")
    public String mapaTickets(){
        return "AnalistaOYM/oymMapaTickets";
    }

    @GetMapping("/ticket")
    public String listaTicket(){
        return "AnalistaOYM/oymListaTickets";
    }

    @GetMapping("/crearTicket")
    public String crearTicket(){
        return "AnalistaOYM/oymCrearTicket";
    }

    @GetMapping("/editarTicket")
    public String editarTicket(){
        return "AnalistaOYM/oymEditarTicket";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "AnalistaOYM/oymDashboard";
    }

    @GetMapping("/verTicket")
    public String verticket(){
        return "AnalistaOYM/oymVistaTicket";
    }


    @GetMapping("/comentarios")
    public String comentarios(){
        return "AnalistaOYM/oymComentarios";
    }






}
