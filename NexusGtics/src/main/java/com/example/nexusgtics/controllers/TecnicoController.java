package com.example.nexusgtics.controllers;
import com.example.nexusgtics.entity.Formulario;
import com.example.nexusgtics.entity.Ticket;
import com.example.nexusgtics.entity.Tipoticket;
import com.example.nexusgtics.entity.Usuario;
import com.example.nexusgtics.repository.TicketRepository;
import com.example.nexusgtics.repository.TipoticketRepository;
import com.example.nexusgtics.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/tecnico")
public class TecnicoController {
    final TicketRepository ticketRepository;
    final UsuarioRepository usuarioRepository;
    private final TipoticketRepository tipoticketRepository;

    public TecnicoController(TicketRepository ticketRepository,
                             UsuarioRepository usuarioRepository,
                             TipoticketRepository tipoticketRepository){
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoticketRepository = tipoticketRepository;
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


    @GetMapping("/comentarios")
    public String pagcomentarios(){
        return "Tecnico/comentarios";
    }

    @GetMapping("/dashboard")
    public String pagdashboard(){
        return "Tecnico/dashboard";
    }

    @GetMapping("/datosticket")
    public String pagdatostick(Model model){
        //Ticket ticket = new Ticket();
        Tipoticket tipoticket = new Tipoticket();
        //model.addAttribute("ticket",ticket);
        model.addAttribute("tipoticket",tipoticket);

        //'listar'
        List<Ticket> lista = ticketRepository.findAll();
        model.addAttribute("ticket", lista);
       List<Tipoticket> list = tipoticketRepository.findAll();
      model.addAttribute("tipoticket1", list);
            return "Tecnico/datos_ticket";

    }

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

    @PostMapping("/save")
    public String guardarFormulario(Ticket ticket, RedirectAttributes attr){
        ticketRepository.save(ticket);
        return "redirect:/Tecnico/datos_ticket";
    }


    @GetMapping("/mapa")
    public String pagmapa(){
        return "Tecnico/mapa";
    }

    //YA ESTA CRUD LISTAR
    @GetMapping("/ticketasignado")
    public String pagtickasignado(Model model){
        //'listar'
        List<Ticket> lista = ticketRepository.findAll();
        model.addAttribute("ticketList", lista);
        return "Tecnico/ticket_asignado";
    }

}
