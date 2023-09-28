package com.example.nexusgtics.controllers;
import com.example.nexusgtics.entity.*;
import com.example.nexusgtics.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import javax.swing.*;
import java.io.IOException;
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
                             FormularioRepository formularioRepository){
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoticketRepository = tipoticketRepository;
        this.sitioRepository = sitioRepository;
        this.comentarioRepository = comentarioRepository;
        this.archivoRepository = archivoRepository;
        this.formularioRepository = formularioRepository;
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
    public String pagdashboard(Model model){
        List<Ticket> lista = ticketRepository.findAll();
        model.addAttribute("ticketList", lista);
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
        Optional <Formulario> optionalFormulario = formularioRepository.findById(id);
        if(optionalTicket.isPresent() && optionalComentario.isPresent() && optionalFormulario.isPresent()){
            Ticket ticket = optionalTicket.get();
            Comentario comentario = optionalComentario.get();
            Formulario formulario = optionalFormulario.get();
            model.addAttribute("ticket", ticket);
            model.addAttribute("comentario", comentario);
            model.addAttribute("formulario", formulario);
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


    /*Formulario*/
    @GetMapping({"/formulario", "formulario"})
    public String pagformulario(Model model, @RequestParam("id") String idStr,
                                @ModelAttribute("formulario") @Valid Formulario formulario, BindingResult bindingResult){
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

        try {
            int id = Integer.parseInt(idStr);
            if(id <=0 || !formularioRepository.existsById(id)){
                return "redirect:/tecnico/verTicket";
            }
            Optional<Formulario> optionalFormulario = formularioRepository.findById(id);
            if(optionalFormulario.isPresent()){
                formulario = optionalFormulario.get();
                model.addAttribute("formulario", formulario);
                model.addAttribute("idTick", formularioRepository.obtenerid(id));
                return "Tecnico/formulario";
            }else{
                return "redirect:/tecnico/verTicket";
            }
        }
        catch (NumberFormatException e){
            return "redirect:/tecnico/verTicket";
        }
    }

    /* Guardar Datos*/
    @PostMapping("/saveFormulario")
    public String saveFormulario(@RequestParam("imagenSubida") MultipartFile file,
                                 Formulario formulario, Model model, RedirectAttributes attr){

        if(formulario.getArchivo()==null){
            formulario.setArchivo(new Archivo());
        }
        if(file.isEmpty()){
            model.addAttribute("msg", "Debe subir un archivo");
            return "redirect:/tecnico/formulario";
        }
        String fileName = file.getOriginalFilename();
        try{
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
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    //-----------------------------------------------------------------------
    @GetMapping("/mapa")
    public String pagmapa(Model model){
        model.addAttribute("sitio", sitioRepository.findAll());
        return "Tecnico/mapa";
    }



}
