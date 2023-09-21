package com.example.nexusgtics.controllers;
import com.example.nexusgtics.entity.Comentario;
import com.example.nexusgtics.entity.Cuadrilla;
import com.example.nexusgtics.entity.Ticket;
import com.example.nexusgtics.entity.Usuario;
import com.example.nexusgtics.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/tecnico")
public class TecnicoController {
    final TicketRepository ticketRepository;
    final UsuarioRepository usuarioRepository;
    final TipoticketRepository tipoticketRepository;

    final SitioRepository sitioRepository;
    final ComentarioRepository comentarioRepository;
    private final CuadrillaRepository cuadrillaRepository;

    public TecnicoController(TicketRepository ticketRepository,
                             UsuarioRepository usuarioRepository,
                             TipoticketRepository tipoticketRepository, SitioRepository sitioRepository, ComentarioRepository comentarioRepository,
                             CuadrillaRepository cuadrillaRepository){
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoticketRepository = tipoticketRepository;
        this.sitioRepository = sitioRepository;
        this.comentarioRepository = comentarioRepository;
        this.cuadrillaRepository = cuadrillaRepository;
    }
    @GetMapping("/")
    public String paginaPrincipal(){
        return "Tecnico/tecnico";
    }
    @GetMapping("/perfiltecnico")
    public String perfilTecnico(Model model, @RequestParam("id") int id){

        Optional<Usuario> optUsuario = usuarioRepository.findById(id);
        if(optUsuario.isPresent()){
            Usuario usuario = optUsuario.get();
            model.addAttribute("usuario",usuario);
            return "Tecnico/perfiltecnico";
        }else {
            return "redirect:/Tecnico/perfiltecnico";
        }
    }

    //-----------------------------------------------------------------------

    @GetMapping("/comentarios")
    public String pagcomentarios(){
        return "Tecnico/comentarios";
    }

    @GetMapping("/dashboard")
    public String pagdashboard(){
        return "Tecnico/dashboard";
    }

    //YA ESTA CRUD LISTAR
    @GetMapping("/ticketasignado")
    public String pagtickasignado(Model model){
        //'listar'
        List<Ticket> lista = ticketRepository.findAll();
        model.addAttribute("ticketList", lista);
        return "Tecnico/ticket_asignado";
    }

    @GetMapping({"/verTicket","/verticket"})
    public String pagdatostick(Model model, @RequestParam("id") int id){
        Optional <Ticket> optionalTicket = ticketRepository.findById(id);
        Optional <Comentario> optionalComentario = comentarioRepository.findById(id);
        if(optionalTicket.isPresent() && optionalComentario.isPresent()){
            Ticket ticket = optionalTicket.get();
            Comentario comentario = optionalComentario.get();
            model.addAttribute("ticket", ticket);
            model.addAttribute("comentario", comentario);
            return "Tecnico/datos_ticket";
        }else{
            return "Tecnico/ticket_asignado";
        }

    }

    //-----------------------------------------------------------------------



    @GetMapping("/desplazamiento")
    public String pagdesplazamiento(){
        return "Tecnico/desplazamiento";
    }

    @GetMapping("/formulario")
    public String pagformulario(Model model){
        List<Ticket> lista = ticketRepository.findAll();
        model.addAttribute("ticketList", lista);

        List<Usuario> list = usuarioRepository.findAll();
        Usuario usuario = new Usuario();
        model.addAttribute("usuarioList", list);
        model.addAttribute("usuario", usuario);
        return "Tecnico/formulario";
    }


    @GetMapping("/mapa")
    public String pagmapa(){
        return "Tecnico/mapa";
    }



}
