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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

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
    public String editarTicket(Model model, @RequestParam("id") int id){
        Optional<Ticket> optTicket = ticketRepository.findById(id);
        if(optTicket.isPresent()){
            Ticket ticket = optTicket.get();
            model.addAttribute("ticket",ticket);
            return "AnalistaOYM/oymEditarTicket";
        }else{
            return "redirect:/analistaOYM/ticket";
        }


    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "AnalistaOYM/oymDashboard";
    }

    @GetMapping("/verTicket")
    public String verticket(Model model, @RequestParam("id") int id){
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if(optionalTicket.isPresent()){
            Ticket ticket = optionalTicket.get();
            model.addAttribute("ticket", ticket);
            return "AnalistaOYM/oymVistaTicket";
        }else{
            return "redirect:/analistaOYM/ticket";
        }
    }


    @GetMapping("/comentarios")
    public String comentarios(){
        return "AnalistaOYM/oymComentarios";
    }






}
