package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.Ticket;
import com.example.nexusgtics.entity.Usuario;
import com.example.nexusgtics.repository.TicketRepository;
import com.example.nexusgtics.repository.UsuarioRepository;
import org.springframework.ui.Model;
import com.example.nexusgtics.entity.Cuadrilla;
import com.example.nexusgtics.repository.CuadrillaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping("/supervisor")
public class SupervisorController {

    private final CuadrillaRepository cuadrillaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TicketRepository ticketRepository;

    public SupervisorController(CuadrillaRepository cuadrillaRepository,
                                UsuarioRepository usuarioRepository,
                                TicketRepository ticketRepository) {
        this.cuadrillaRepository = cuadrillaRepository;
        this.usuarioRepository = usuarioRepository;
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/")
    public String paginaPrincipal(){
        return "Supervisor/menuSupervisor";
    }

    @GetMapping("/listaTickets")
    public String Tickets(Model model){

        List<Ticket> listaTickets = ticketRepository.findAll();
        List<String> categorias = new ArrayList<>();
        categorias.add("Urgente");
        categorias.add("Hacer");
        categorias.add("No critico");
        categorias.add("Baja Prioridad");

        Random random = new Random();
        String categoriaAleatoria = categorias.get(random.nextInt(categorias.size()));

        model.addAttribute("categoriaAleatoria", categoriaAleatoria);

        model.addAttribute("listaTickets",listaTickets);

        return "Supervisor/listaTickets";
    }

    @GetMapping("/listaCuadrillas")
    public String Cuadrillas(Model model){

        List<Cuadrilla> listaCuadrilla = cuadrillaRepository.findAll();
        Map<Integer, Integer> trabajosFinalizadosPorCuadrilla = new HashMap<>();

        for (Cuadrilla cuadrilla : listaCuadrilla) {
            int trabajosFinalizados = cuadrillaRepository.contarTrabajosFinalizados(cuadrilla.getIdCuadrillas()); // Reemplaza "getId()" con el método adecuado para obtener el ID de la cuadrilla
            trabajosFinalizadosPorCuadrilla.put(cuadrilla.getIdCuadrillas(), trabajosFinalizados);
        }

        model.addAttribute("listaCuadrilla",listaCuadrilla);

        model.addAttribute("trabajosFinalizadosPorCuadrilla", trabajosFinalizadosPorCuadrilla);

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

    @GetMapping("/detallesCuadrilla")
    public String detallesCuadrilla(Model model, @RequestParam("id") int id){

        Optional<Cuadrilla> optShipper = cuadrillaRepository.findById(id);
        List<Usuario> integrantesCuadrilla = usuarioRepository.listaDeTecnicosPorCuadrilla(id);
        List<Cuadrilla> listaCuadrilla = cuadrillaRepository.findAll();

        Map<Integer, Integer> trabajosFinalizadosPorCuadrilla = new HashMap<>();

        for (Cuadrilla cuadrilla : listaCuadrilla) {
            int trabajosFinalizados = cuadrillaRepository.contarTrabajosFinalizados(cuadrilla.getIdCuadrillas()); // Reemplaza "getId()" con el método adecuado para obtener el ID de la cuadrilla
            trabajosFinalizadosPorCuadrilla.put(cuadrilla.getIdCuadrillas(), trabajosFinalizados);
        }

        if (optShipper.isPresent()) {
            Cuadrilla cuadrilla = optShipper.get();
            model.addAttribute("cuadrilla", cuadrilla);
            model.addAttribute("integrantes", integrantesCuadrilla);
            model.addAttribute("trabajosFinalizadosPorCuadrilla", trabajosFinalizadosPorCuadrilla);
            return "supervisor/detallesCuadrilla";
        } else {
            return "redirect:/supervisor/listCuadrillas";
        }
    }
}
