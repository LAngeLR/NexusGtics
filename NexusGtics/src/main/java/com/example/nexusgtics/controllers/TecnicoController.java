package com.example.nexusgtics.controllers;
import com.example.nexusgtics.entity.*;
import com.example.nexusgtics.repository.*;
import jakarta.servlet.http.HttpSession;
import org.hibernate.mapping.Formula;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.http.codec.FormHttpMessageReader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import javax.swing.*;
import java.io.IOException;
import java.text.Normalizer;
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
    final ArchivoRepository archivoRepository;
    private final FormularioRepository formularioRepository;

    public TecnicoController(TicketRepository ticketRepository,
                             UsuarioRepository usuarioRepository,
                             TipoticketRepository tipoticketRepository, SitioRepository sitioRepository, ComentarioRepository comentarioRepository,
                             CuadrillaRepository cuadrillaRepository, ArchivoRepository archivoRepository,
                             FormularioRepository formularioRepository,
                             EquipoRepository equipoRepository) {
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoticketRepository = tipoticketRepository;
        this.sitioRepository = sitioRepository;
        this.comentarioRepository = comentarioRepository;
        this.archivoRepository = archivoRepository;
        this.formularioRepository = formularioRepository;

    }

    @GetMapping(value = {"/", ""})
    public String paginaPrincipal(Model model) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession();
        System.out.println(session);
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        return "Tecnico/tecnico";
    }

    @GetMapping("/perfiltecnico")
    public String perfilTecnico(Model model, @RequestParam("id") int id) {
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);
        if (optUsuario.isPresent()) {
            Usuario usuario = optUsuario.get();
            model.addAttribute("usuario", usuario);
            return "Tecnico/perfiltecnico";
        } else {
            return "redirect:/Tecnico/perfiltecnico";
        }
    }

    //-----------------------------------------------------------------------

    @GetMapping("/comentarios")
    public String pagcomentarios(Model model, @RequestParam("id") int id,
                                 RedirectAttributes attr) {
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();
            model.addAttribute("ticket", ticket);
            model.addAttribute("listaTicket", ticketRepository.findAll());
            return "Tecnico/comentarios";
        } else {
            return "redirect:/ticket/verticket";
        }
    }

    //-----------------------------------------------------------------------
    @GetMapping("/dashboard")
    public String pagdashboard(Model model) {
        List<Ticket> lista = ticketRepository.findAll();
        model.addAttribute("ticketList", lista);
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        return "Tecnico/dashboard";
    }

    //YA ESTA CRUD LISTAR
    @GetMapping("/ticketasignado")
    public String pagtickasignado(Model model) {
        //'listar'
        List<Ticket> lista = ticketRepository.findAll();
        model.addAttribute("ticketList", lista);
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        return "Tecnico/ticket_asignado";
    }

    //-----------------------------------------------------------------------
    @GetMapping({"/verTicket", "/verticket"})
    public String pagdatostick(Model model, @RequestParam("id") int id,
                               RedirectAttributes attr) {
        // Optional <Ticket> optionalTicket = ticketRepository.findById(id);
        //Optional <Comentario> optionalComentario = comentarioRepository.findById(id);
        //Optional <Formulario> optionalFormulario = formularioRepository.findById(id);
        //if(optionalTicket.isPresent() && optionalComentario.isPresent() && optionalFormulario.isPresent()){
        //  Ticket ticket = optionalTicket.get();
        //Comentario comentario = optionalComentario.get();
        // Formulario formulario = optionalFormulario.get();
        // model.addAttribute("ticket", ticket);
        // model.addAttribute("comentario", comentario);
        // model.addAttribute("formulario", formulario);
        // return "Tecnico/datos_ticket";
        //  }else{
        //    return "Tecnico/ticket_asignado";
        //  }
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        try {
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/ticket/ticketasignado";
            }
            Optional<Ticket> optionalTicket1 = ticketRepository.findById(id);
            Optional<Formulario> optionalFormulario = formularioRepository.findById(id);
            if (optionalTicket1.isPresent() && optionalFormulario.isPresent()) {
                Ticket ticket = optionalTicket1.get();
                Formulario formulario = optionalFormulario.get();
                model.addAttribute("ticket", ticket);
                model.addAttribute("formulario", formulario);
                model.addAttribute("listaTicket", ticketRepository.findAll());
                return "Tecnico/datos_ticket";
            } else {
                return "redirect:/ticket/ticketasignado";
            }
        } catch (NumberFormatException e) {
            return "redirect:/tecnico/ticketasignado";
        }
    }

    //------------------- DATOS DE PROGRESO---------------------------------//
    @GetMapping({"/verTicketProgreso", "/verticketprogreso"})
    public String pagdatostickProgreso(Model model, @RequestParam("id") int id,
                               RedirectAttributes attr) {
        List<Ticket> listaT1 = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT1);
        try {
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/ticket/ticketasignado";
            }
            Optional<Ticket> optionalTicket1 = ticketRepository.findById(id);
            Optional<Formulario> optionalFormulario = formularioRepository.findById(id);
            if (optionalTicket1.isPresent() && optionalFormulario.isPresent()) {
                Ticket ticket = optionalTicket1.get();
                Formulario formulario = optionalFormulario.get();
                model.addAttribute("ticket", ticket);
                model.addAttribute("formulario", formulario);
                model.addAttribute("listaTicket", ticketRepository.findAll());
                return "Tecnico/datost_progreso";
            } else {
                return "redirect:/ticket/ticketasignado";
            }
        } catch (NumberFormatException e) {
            return "redirect:/tecnico/ticketasignado";
        }
    }

    //------------------- DATOS DE NUEVO---------------------------------//
    @GetMapping({"/verTicketNuevo", "/verticketnuevo"})
    public String pagdatostickNuevo(Model model, @RequestParam("id") int id,
                                       RedirectAttributes attr) {
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        try {
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/ticket/ticketasignado";
            }
            Optional<Ticket> optionalTicket1 = ticketRepository.findById(id);
            Optional<Formulario> optionalFormulario = formularioRepository.findById(id);
            if (optionalTicket1.isPresent() && optionalFormulario.isPresent()) {
                Ticket ticket = optionalTicket1.get();
                Formulario formulario = optionalFormulario.get();
                model.addAttribute("ticket", ticket);
                model.addAttribute("formulario", formulario);
                model.addAttribute("listaTicket", ticketRepository.findAll());
                return "Tecnico/datost_nuevo";
            } else {
                return "redirect:/ticket/ticketasignado";
            }
        } catch (NumberFormatException e) {
            return "redirect:/tecnico/ticketasignado";
        }
    }


    //------------------- DATOS DE CERRADO---------------------------------//
    @GetMapping({"/verTicketCerrado", "/verticketcerrado"})
    public String pagdatostickCerrado(Model model, @RequestParam("id") int id,
                                       RedirectAttributes attr) {
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        try {
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/ticket/ticketasignado";
            }
            Optional<Ticket> optionalTicket1 = ticketRepository.findById(id);
            Optional<Formulario> optionalFormulario = formularioRepository.findById(id);
            if (optionalTicket1.isPresent() && optionalFormulario.isPresent()) {
                Ticket ticket = optionalTicket1.get();
                Formulario formulario = optionalFormulario.get();
                model.addAttribute("ticket", ticket);
                model.addAttribute("formulario", formulario);
                model.addAttribute("listaTicket", ticketRepository.findAll());
                return "Tecnico/datost_cerrado";
            } else {
                return "redirect:/ticket/ticketasignado";
            }
        } catch (NumberFormatException e) {
            return "redirect:/tecnico/ticketasignado";
        }
    }

    @PostMapping("/guardarEstado")
    public String guardarEstado(Ticket ticket,
                                @RequestParam("estado") int estado,
                                RedirectAttributes attributes) {
        try {
            ticketRepository.guardarEstado(estado);
            return "redirect:/tecnico/ticketasignado";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //-----------------------------------------------------------------------
    @PostMapping("/actualizarEstado")
    public String actualizarEstado(@ModelAttribute("ticket") @Valid Ticket ticket, BindingResult bindingResult,
                                   Model model, RedirectAttributes attr) {

        if (!bindingResult.hasErrors()) {

            if (ticket.getEstado().equals(1)) {
                model.addAttribute("msg", "Error al ingresar Estado");
                model.addAttribute("listaEstado", ticketRepository.findAll());
                return "Tecnico/ticket_asignado";
            } else {
                if (ticket.getIdTickets() == 0) {
                    attr.addFlashAttribute("msg", "Producto creado exitosamente");
                } else {
                    attr.addFlashAttribute("msg", "Producto actualizado exitosamente");
                }
                ticketRepository.save(ticket);
                return "redirect:/ticket/verticket";
            }

        } else { //hay al menos 1 error
            model.addAttribute("listaEstado", ticketRepository.findAll());
            return "Tecnico/ticket_asignado";
        }
    }

    //-----------------------------------------------------------------------


    @GetMapping("/desplazamiento")
    public String pagdesplazamiento(Model model, @RequestParam("id") int id,
                                    RedirectAttributes attr) {
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket1", listaT);

        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();
            model.addAttribute("ticket", ticket);
            model.addAttribute("listaTicket", ticketRepository.findAll());
            return "Tecnico/desplazamiento";
        } else {
            return "redirect:/ticket/verticket";
        }

    }


    /*Formulario*/
    @GetMapping({"/formulario", "formulario"})
    public String pagformulario(Model model, @RequestParam("id") String idStr,
                                @ModelAttribute("formulario") @Valid Formulario formulario, BindingResult bindingResult) {
        /*List<Ticket> lista = ticketRepository.findAll();
        model.addAttribute("ticketList", lista);

        List<Usuario> list = usuarioRepository.findAll();
        Usuario usuario = new Usuario();
        model.addAttribute("usuarioList", list);
        model.addAttribute("usuario", usuario);*/

        /*Optional <Ticket> optionalTicket = ticketRepository.findById(id);
        Optional<Formulario> optionalFormulario = formularioRepository.findById(id);
        if(optionalTicket.isPresent() && optionalFormulario.isPresent()){
            Ticket ticket = optionalTicket.get();
            Formulario formulario = optionalFormulario.get();
            model.addAttribute("ticket", ticket);
            model.addAttribute("formulario",formulario);
        }

        model.addAttribute("listaUsuario", usuarioRepository.findAll());
        model.addAttribute("ticketList", ticketRepository.findAll());
        return "Tecnico/formulario";*/
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        try {
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !formularioRepository.existsById(id)) {
                return "redirect:/tecnico/verTicket";
            }
            Optional<Formulario> optionalFormulario = formularioRepository.findById(id);
            if (optionalFormulario.isPresent()) {
                formulario = optionalFormulario.get();
                model.addAttribute("formulario", formulario);
                model.addAttribute("idTick", formularioRepository.obtenerid(id));
                return "Tecnico/formulario";
            } else {
                return "redirect:/tecnico/verTicket";
            }
        } catch (NumberFormatException e) {
            return "redirect:/tecnico/verTicket";
        }
    }

    /* Guardar Datos*/
    @PostMapping("/saveFormulario")
    public String saveFormulario(@RequestParam("imagenSubida") MultipartFile file,
                                 @ModelAttribute("formulario") @Valid Formulario formulario, BindingResult bindingResult,
                                 Model model, RedirectAttributes attr) {
        if (!bindingResult.hasErrors()) {
            if (formulario.getArchivo() == null) {
                formulario.setArchivo(new Archivo());
            }
            if (file.isEmpty()) {
                model.addAttribute("msg", "Debe subir un archivo");
                return "redirect:/tecnico/formulario";
            }
            String fileName = file.getOriginalFilename();
            try {
                Archivo archivo = formulario.getArchivo();
                archivo.setNombre(fileName);
                archivo.setTipo(1);
                archivo.setArchivo(file.getBytes());
                archivo.setContentType(file.getContentType());
                archivoRepository.save(archivo);
                int idImagen = archivo.getIdArchivos();
                formulario.getArchivo().setIdArchivos(idImagen);
                formularioRepository.save(formulario);
                return "redirect:/tecnico/verticket";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return "Tecnico/formulario";
        }
    }

    //-----------------------------------------------------------------------
    @GetMapping("/mapa")
    public String pagmapa(Model model) {
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        List<Sitio> sitioList = sitioRepository.findAll();
        model.addAttribute("sitioList", sitioList);
        return "Tecnico/mapa";
    }

    @GetMapping("/formularioCerrado")
    public String formularioCerrado(Model model, @RequestParam("id") String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !formularioRepository.existsById(id)) {
                return "redirect:/tecnico/datostickets";
            }
            Optional<Formulario> formularioOptional = formularioRepository.findById(id);
            if (formularioOptional.isPresent()) {
                Formulario formulario = formularioOptional.get();
                model.addAttribute("formulario", formulario);
                return "Tecnico/formularioCerrado";
            } else {
            return "redirect:/tecnico/datostickets";
            }
        } catch (NumberFormatException e) {
            return "redirect:/tecnico/datosticket";
        }
    }

}
