package com.example.nexusgtics.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/analistaDespliegue")
public class AnalistaDespController {

    @GetMapping("/")
    public String paginaPrincipal(){
        return "AnalistaDespliegue/analistaDespliegue";
    }

    @GetMapping("/listaSitio")
    public String listaSitio(){
        return "AnalistaDespliegue/despliegueListaSitio";
    }

    @GetMapping("/editarSitio")
    public String editarSitio(){
        return "AnalistaDespliegue/despliegueEditarSitio";
    }
    @GetMapping("/listaEquipo")
    public String listaEquipo(){
        return "AnalistaDespliegue/despliegueListaEquipos";
    }
    @GetMapping("/verEquipo")
    public String verEquipo(){
        return "AnalistaDespliegue/despliegueVerEquipos";
    }
    @GetMapping("/perfil")
    public String perfilAD(){
        return "AnalistaDespliegue/perfilAnalistaDespliegue";
    }
    @GetMapping("/mapaSitios")
    public String mapaSitios(){
        return "AnalistaDespliegue/despliegueMapaSitios";
    }
    @GetMapping("/mapaTickets")
    public String mapaTickets(){
        return "AnalistaDespliegue/despliegueMapaTickets";
    }
}
