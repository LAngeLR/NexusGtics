package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.Equipo;
import com.example.nexusgtics.entity.Sitio;
import com.example.nexusgtics.entity.Ticket;
import com.example.nexusgtics.repository.EquipoRepository;
import com.example.nexusgtics.repository.SitioRepository;
import com.example.nexusgtics.repository.TicketRepository;
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
    final TicketRepository ticketRepository;
    final SitioRepository sitioRepository;

    final EquipoRepository equipoRepository;


    public AnalistaDespController(TicketRepository ticketRepository, SitioRepository sitioRepository,EquipoRepository equipoRepository ){
        this.ticketRepository = ticketRepository;
        this.sitioRepository = sitioRepository;
        this.equipoRepository = equipoRepository;

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
            return "redirect:/analistaDespliegue/listaSitio";
        }

    }

    @PostMapping("/guardarSitio")
    public String guardarSitio(Sitio sitio){
        sitioRepository.save(sitio);
        return "redirect:/analistaDespliegue/listaSitio";
    }


    @GetMapping("/listaEquipo")
    public String listaEquipo(Model model ){
        List<Equipo> listaEquipo = equipoRepository.findAll();

        model.addAttribute("listaEquipo",listaEquipo);
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
    public String listaTicket(Model model){
        //'listar'
        List<Ticket> lista = ticketRepository.findAll();
        model.addAttribute("listaTicket", lista);
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
