package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.Equipo;
import com.example.nexusgtics.entity.Sitio;
import com.example.nexusgtics.entity.Ticket;
import com.example.nexusgtics.repository.EquipoRepository;
import com.example.nexusgtics.repository.SitioRepository;
import com.example.nexusgtics.repository.TicketRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/analistaOYM")
public class AnalistaOYMController {
    final TicketRepository ticketRepository;
    final SitioRepository sitioRepository;

    final EquipoRepository equipoRepository;


    public AnalistaOYMController(SitioRepository sitioRepository, TicketRepository ticketRepository, EquipoRepository equipoRepository ){
        this.sitioRepository = sitioRepository;
        this.ticketRepository = ticketRepository;
        this.equipoRepository = equipoRepository;
    }

    @GetMapping("/")
    public String paginaPrincipal(){
        return "AnalistaOYM/analistaOYM";
    }

    @GetMapping("/listaSitio")
    public String listaSitio(Model model){
        List<Sitio> listaSitio = sitioRepository.findAll();

        model.addAttribute("listaSitio",listaSitio);

        return "AnalistaOYM/oymListaSitio";
    }



    @GetMapping("/editarSitio")
    public String editarSitio(){
        return "AnalistaOYM/oymEditarSitio";
    }
    @GetMapping("/listaEquipo")
    public String listaEquipo(Model model){
        List<Equipo> listaEquipo = equipoRepository.findAll();

        model.addAttribute("listaEquipo",listaEquipo);

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
    public String listaTicket(Model model){
        //'listar'
        List<Ticket> listaTicket = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaTicket);
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
