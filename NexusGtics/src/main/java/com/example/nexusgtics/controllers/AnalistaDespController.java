package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.Sitio;
import com.example.nexusgtics.repository.SitioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/analistaDespliegue")
public class AnalistaDespController {

    final SitioRepository sitioRepository;

    public AnalistaDespController(SitioRepository sitioRepository ){
        this.sitioRepository = sitioRepository;
    }


    @GetMapping("/")
    public String paginaPrincipal(){
        return "AnalistaDespliegue/analistaDespliegue";
    }

    @GetMapping("/listaSitio")
    public String listaSitio(Model model){

        List<Sitio>  listaSitio = sitioRepository.findAll();

        model.addAttribute("listaSitio",listaSitio);

        return "AnalistaDespliegue/despliegueListaSitio";
    }

    @GetMapping("/editarSitio")
    public String editarSitio(Model model, @RequestParam("id") int id){
        Optional<Sitio> optSitio = sitioRepository.findById(id);
        if(optSitio.isPresent()){
            Sitio sitio = optSitio.get();
            model.addAttribute("sitio", sitio);
            return "AnalistaDespliegue/despliegueEditarSitio";
        }else {
            return "redirect: /AnalistaDespliegue/despliegueListaSitio";
        }

    }

    @PostMapping("/guardarSitio")
    public String guardarSitio(Sitio sitio){
        sitioRepository.save(sitio);
        return "redirect:/AnalistaDespliegue/despliegueListaSitio";
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

    @GetMapping("/ticket")
    public String listaTicket(){
        return "AnalistaDespliegue/despliegueListaTickets";
    }

    @GetMapping("/crearTicket")
    public String crearTicket(){
        return "AnalistaDespliegue/despliegueCrearTicket";
    }

    @GetMapping("/editarTicket")
    public String editarTicket(){
        return "AnalistaDespliegue/despliegueEditarTicket";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "AnalistaDespliegue/despliegueDashboard";
    }

    @GetMapping("/verTicket")
    public String verticket(){
        return "AnalistaDespliegue/despliegueVistaTicket";
    }


    @GetMapping("/comentarios")
    public String comentarios(){
        return "AnalistaDespliegue/despliegueComentarios";
    }
}
