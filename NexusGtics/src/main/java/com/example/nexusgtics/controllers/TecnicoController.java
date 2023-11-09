package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.*;
import com.example.nexusgtics.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tecnico")
public class TecnicoController {
    @Autowired
    private HttpSession session;

    final TicketRepository ticketRepository;
    final UsuarioRepository usuarioRepository;
    final TipoticketRepository tipoticketRepository;

    final SitioRepository sitioRepository;
    final SitioCerradoRepository sitioCerradoRepository;
    final ComentarioRepository comentarioRepository;
    final ArchivoRepository archivoRepository;

    final EmpresaRepository empresaRepository;

    final CargoRepository cargoRepository;
    private final FormularioRepository formularioRepository;
    private final TecInstaladaRepository tecInstaladaRepository;
    private final TecnologiainstaladaFormularioRepository tecnologiainstaladaRepository;
    private final PasswordEncoder passwordEncoder;


    public TecnicoController(TicketRepository ticketRepository,
                             UsuarioRepository usuarioRepository,
                             TipoticketRepository tipoticketRepository, SitioRepository sitioRepository, ComentarioRepository comentarioRepository,
                             CuadrillaRepository cuadrillaRepository, ArchivoRepository archivoRepository,
                             FormularioRepository formularioRepository,
                             EquipoRepository equipoRepository, SitioCerradoRepository sitioCerradoRepository,TecInstaladaRepository tecInstaladaRepository ,
                             EmpresaRepository empresaRepository, CargoRepository cargoRepository, TecnologiainstaladaFormularioRepository tecnologiainstaladaRepository, PasswordEncoder passwordEncoder) {
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoticketRepository = tipoticketRepository;
        this.sitioRepository = sitioRepository;
        this.comentarioRepository = comentarioRepository;
        this.archivoRepository = archivoRepository;
        this.formularioRepository = formularioRepository;
        this.sitioCerradoRepository = sitioCerradoRepository;
        this.empresaRepository = empresaRepository;
        this.cargoRepository = cargoRepository;
        this.tecnologiainstaladaRepository = tecnologiainstaladaRepository;
        this.tecInstaladaRepository = tecInstaladaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


    @GetMapping({"/", "","tecnico"})
    public String paginaPrincipal(Model model, HttpSession httpSession) {
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        model.addAttribute("currentPage", "Inicio");
        Usuario user = (Usuario) httpSession.getAttribute("usuario");
        //Usuario user2 = (Usuario) session.getAttribute("usuario");
        System.out.println("User: "+ user.getNombre());
        //System.out.println("User2: "+ user2.getNombre());
        return "Tecnico/tecnico";
    }

    public String mandarFragmento(Model model){
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        return "Fragmentos/headerDos";
    }


    /* -------------------------- PERFIL -------------------------- */
    @GetMapping({"/perfil"})
    public String perfilTecnico(Model model,
                                   @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession){
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        model.addAttribute("usuario", u);
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        return "Tecnico/perfilTecnico";
    }

    /* PERFIL DEL Tecnico */
    @PostMapping("/savePerfil")
    public String savePerfil(@RequestParam("imagenSubida") MultipartFile file,
                             @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                             Model model,
                             RedirectAttributes attr, HttpSession httpSession){
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        // ESTO SE AÑADIO DE BARD
        //session.setAttribute("usuario", usuario);

        if(usuario.getCargo() == null || usuario.getCargo().getIdCargos() == null || usuario.getCargo().getIdCargos() == -1){
            model.addAttribute("msgCargo", "Escoger un cargo");
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());

            if (usuario.getId() == null) {
                return "Tecnico/tecnico";
            } else {
                return "Tecnico/perfilEditar";
            }
        }
        if(usuario.getEmpresa() == null || usuario.getEmpresa().getIdEmpresas() == null || usuario.getEmpresa().getIdEmpresas() == -1){
            model.addAttribute("msgEmpresa", "Escoger una empresa");
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            if (usuario.getId() == null) {
                return "Tecnico/tecnico";
            } else {
                return "Tecnico/perfilEditar";
            }
        }

        if (file.getSize() > 0 && !file.getContentType().startsWith("image/")) {
            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
            if (usuario.getId() == null) {
                return "Tecnico/tecnico";
            } else {
                return "Tecnico/perfilEditar";
            }
        }

        int maxFileSize = 10485760;

        if (file.getSize() > maxFileSize) {
            System.out.println(file.getSize());
            model.addAttribute("msgImagen1", "El archivo subido excede el tamaño máximo permitido (10MB).");
            if (usuario.getId() == null) {
                return "Tecnico/tecnico";
            } else {
                return "redirect:/Tecnico/perfilEditar";
            }
        }

        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
            if (usuario.getArchivo() == null) {
                usuario.setArchivo(new Archivo());
            }
            String fileName = file.getOriginalFilename();
            try{
                //validación de nombre, apellido y correo
                Archivo archivo = usuario.getArchivo();
                archivo.setNombre(fileName);
                archivo.setTipo(1);
                archivo.setArchivo(file.getBytes());
                archivo.setContentType(file.getContentType());
                archivoRepository.save(archivo);
                int idImagen = archivo.getIdArchivos();
                usuario.getArchivo().setIdArchivos(idImagen);
                if (usuario.getId() == null) {
                    attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha creado exitosamente");
                } else {
                    attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha actualizado exitosamente");
                }
                usuarioRepository.save(usuario);
                //Usuario u = (Usuario) httpSession.getAttribute("usuario");
                //HttpSession session = request.getSession(true);
                //session.setAttribute("nombreUsuario", "nuevoNombre");
                session.setAttribute("usuario", usuario);
                return "redirect:/Tecnico/perfil";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else { //hay al menos 1 error
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            if (usuario.getId() == null) {
                return "Tecnico/tecnico";
            } else {
                return "Tecnico/perfilEditar";
            }
        }
    }

    @GetMapping({"/perfilEditar"})
    public String perfilEditar(Model model,
                               @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession){
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);

        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        int id = u.getId();
        try{
            //int id = Integer.parseInt(idStr);
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/tecnico/perfil";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                usuario = optionalUsuario.get();    //modifiqué Usuario usuario para poder usar @ModelAttribute
                model.addAttribute("usuario", usuario);
                model.addAttribute("listaEmpresa", empresaRepository.findAll());
                model.addAttribute("listaCargo", cargoRepository.findAll());
                return "Tecnico/perfilEditar";
            } else {
                return "redirect:/tecnico/perfil";
            }
        } catch (NumberFormatException e) {
            return "redirect:/tecnico/perfil";
        }

    }

    @GetMapping({"/perfilContra"})
    public String perfilContra(Model model,
                               @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession){
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);

        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        int id = u.getId();
        try{
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/tecnico/perfil";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                model.addAttribute("idUsuario",id);
                return "Tecnico/perfilContra";
            } else {
                return "redirect:/tecnico/perfil";
            }
        } catch (NumberFormatException e) {
            return "redirect:/tecnico/tecnico";
        }
    }

    @PostMapping({"/actualizarContra"})
    public String actualizarContra(Model model, @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession,
                                   @RequestParam("password") String contrasenia,
                                   @RequestParam("newpassword") String contraseniaNueva, @RequestParam("renewpassword") String contraseniaConfirm,
                                   RedirectAttributes redirectAttributes){
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);

        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        int id = u.getId();

        String contraseniaAlmacenada = usuarioRepository.obtenerContraseña(id);

        if (passwordEncoder.matches(contrasenia, contraseniaAlmacenada)) {
            String contraseniaNuevaEncriptada = passwordEncoder.encode(contraseniaNueva);
            usuarioRepository.actualizarContraA(contraseniaNuevaEncriptada, id);
            redirectAttributes.addFlashAttribute("msg1", "La contraseña se ha actualizado exitosamente");

            return "redirect:/tecnico/perfil";
        } else {
            redirectAttributes.addFlashAttribute("error","La contraseña actual no es correcta.");
            return "redirect:/tecnico/perfilContra";
        }
    }

    /* -------------------------- FIN PERFIL -------------------------- */


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
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);

        List<Ticket> lista = ticketRepository.listarEstado();
        model.addAttribute("ticketList", lista);
        //List<Ticket> listaT = ticketRepository.listarEstado();
        //model.addAttribute("listaTicket", listaT);
        return "Tecnico/dashboard";
    }

    //YA ESTA CRUD LISTAR
    @GetMapping("/ticketasignado")
    public String Tickets(Model model, HttpSession httpSession){
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        Integer idTecnico = u.getId();
        List<Ticket> listaTickets = ticketRepository.listaTickets( 1, idTecnico);
        model.addAttribute("listaTickets",listaTickets);

        return "Tecnico/ticket_asignado";
    }

    //-----------------------------------------------------------------------
    @GetMapping({"/verTicket", "/verticket"})
    public String pagdatostick(Model model, @RequestParam("id") int id,
                               RedirectAttributes attr) {
        List<Ticket> listaT = ticketRepository.listarEstado();
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
                model.addAttribute("listaTicket", ticketRepository.listarEstado());
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
        List<Ticket> listaT1 = ticketRepository.listarEstado();
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
                model.addAttribute("listaTicket", ticketRepository.listarEstado());
                return "Tecnico/datost_progreso";
            } else {
                return "redirect:/ticket/ticketasignado";
            }
        } catch (NumberFormatException e) {
            return "redirect:/tecnico/ticketasignado";
        }
    }

    //------------------- DATOS NUEVO ---------------------------------//
    @GetMapping({"/verTicketNuevo", "/verticketnuevo"})
    public String pagdatostickNuevo(Model model, @RequestParam("id") int id,
                                    RedirectAttributes attr) {
        List<Ticket> listaT = ticketRepository.listarEstado();
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
                model.addAttribute("listaTicket", ticketRepository.listarEstado());
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
        List<Ticket> listaT = ticketRepository.listarEstado();
        model.addAttribute("listaTicket", listaT);
        try {
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/ticket/ticketasignado";
            }
            Optional<Ticket> optionalTicket1 = ticketRepository.findById(id);
            Optional<Formulario> optionalFormulario = formularioRepository.findById(id);
            Optional<SitioCerrado> optionalSitioCerrado = sitioCerradoRepository.findById(id);
            if (optionalTicket1.isPresent() && optionalFormulario.isPresent() && optionalSitioCerrado.isPresent()) {
                Ticket ticket = optionalTicket1.get();
                Formulario formulario = optionalFormulario.get();
                SitioCerrado sitioCerrado = optionalSitioCerrado.get();
                model.addAttribute("ticket", ticket);
                model.addAttribute("formulario", formulario);
                model.addAttribute("sitioCerrado", sitioCerrado);
                model.addAttribute("listaTicket", ticketRepository.listarEstado());
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

    //Desplazamiento en progreso para cerrado
    @GetMapping("/desplazamientoProgreso")
    public String desplazamientoProgreso(Model model, @RequestParam("id") int id,
                                         RedirectAttributes attr) {
        List<Ticket> lista = ticketRepository.listarEstado();
        model.addAttribute("ticketList", lista);
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();
            model.addAttribute("ticket", ticket);
            model.addAttribute("listaTicket", ticketRepository.findAll());
            return "Tecnico/desplazamientoProgreso";
        } else {
            return "redirect:/ticket/verTicketProgreso";
        }

    }

    //Desplazamiento cerrado, sin datos por modificar - Listar
    @GetMapping("/desplazamientoCerrado")
    public String desplazamientoCerrado(Model model, @RequestParam("id") int id,
                                        RedirectAttributes attr) {
        //'listar'
        List<Ticket> lista = ticketRepository.listarEstado();
        model.addAttribute("ticketList", lista);
        List<Sitio> sitio = sitioRepository.findAll();
        model.addAttribute("sitioListC", sitio);
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        Optional<SitioCerrado> optionalSitioCerrado = sitioCerradoRepository.findById(id);
        Optional<Sitio> optionalSitio = sitioRepository.findById(id);
        if (optionalTicket.isPresent() && optionalSitioCerrado.isPresent() && optionalSitio.isPresent()) {
            Ticket ticket = optionalTicket.get();
            SitioCerrado sitioCerrado = optionalSitioCerrado.get();
            Sitio sitio1 = optionalSitio.get();
            model.addAttribute("sitio",sitio1);
            model.addAttribute("sitioCerrado", sitioCerrado);
            model.addAttribute("ticket", ticket);
            model.addAttribute("listaTicket", ticketRepository.findAll());
            return "Tecnico/desplazamientoCerrado";
        } else {
            return "redirect:/ticket/verTicketCerrado";
        }
    }


    /*Formulario*/
    @GetMapping({"/formulario","formulario"})
    public String pagformulario(Model model, @RequestParam("id") String idStr,
                                @ModelAttribute("formulario") @Valid Formulario formulario, BindingResult bindingResult) {
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

    //------FORMULARIO1 ----
    @GetMapping({"/formulario1", "formulario1"})
    public String pagformulario1(Model model, @RequestParam("id") String idStr,
                                @ModelAttribute("formulario1") @Valid Formulario formulario1, BindingResult bindingResult) {
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        try{
            int id = Integer.parseInt(idStr);
            if(id <= 0|| !formularioRepository.existsById(id)){
                return "redirect:/tecnico/formulario";
            }
            Optional<Formulario> optionalFormulario = formularioRepository.findById(id);
            if(optionalFormulario.isPresent()){
                formulario1 = optionalFormulario.get();
                model.addAttribute("formulario", formulario1);
                return "Tecnico/formulario1";
            }else{
                return "redirect:/tecnico";
            }
        }catch (NumberFormatException e){
            return "redirect:/tecnico/formulario";
        }

    }

    /* Guardar Datos*/
    @PostMapping("/saveFormulario1")
    public String saveFormulario1(@RequestParam("imagenSubida") MultipartFile file,@RequestParam("imagenSubida2") MultipartFile file2,
                                 @ModelAttribute("formulario1") @Valid Formulario formulario1, BindingResult bindingResult,
                                 Model model, RedirectAttributes attr) {
        if(formulario1.getHrelevantes() == null){
            model.addAttribute("msgFormulario","Por favor, complete el recuadro de texto");
            model.addAttribute("form1",formularioRepository.findAll());
            if(formulario1.getIdFormularios() == null){
                return "Tecnico/datost_nuevo";
            }else{
                return "Tecnico/formulario1";
            }
        }
        if(!file.isEmpty()){
            try {

                Archivo archivo = new Archivo();
                archivo.setNombre(file.getOriginalFilename());
                archivo.setTipo(1);
                archivo.setArchivo(file.getBytes());
                archivo.setContentType(file.getContentType());
                archivoRepository.save(archivo);
                formulario1.setArchivo(archivo);
            } catch (IOException e) {
                System.out.println("Error al procesar el archivo");
                throw new RuntimeException(e);
            }
        }
        if(!file2.isEmpty()){
            try {
                Archivo archivo = new Archivo();
                archivo.setNombre(file.getOriginalFilename());
                archivo.setTipo(1);
                archivo.setArchivo(file.getBytes());
                archivo.setContentType(file.getContentType());
                archivoRepository.save(archivo);
                formulario1.setArchivo(archivo);
            } catch (IOException e) {
                System.out.println("Error al procesar el archivo");
                throw new RuntimeException(e);
            }
        }
        if(!bindingResult.hasErrors()){
            if (formulario1.getArchivo() == null) {
                formulario1.setArchivo(new Archivo());
            }
            try {
                if (formulario1.getIdFormularios() == null) {
                    attr.addFlashAttribute("msg", "Datos de formulario 1 - llegada a sitio guardados exitosamente");
                } else {
                    attr.addFlashAttribute("msg", "Datos de formulario 1 - llegada a sitio guardados exitosamente");
                }
                formularioRepository.save(formulario1);
                return "redirect:/tecnico/formulario";
            } catch (Exception e) {
                System.out.println("Error al guardar los datos del formulario");
                throw new RuntimeException(e);
            }
        } else{
            model.addAttribute("formulario", formularioRepository.findAll());
            if (formulario1.getIdFormularios() == null) {
                return "Tecnico/datost_nuevo";
            } else {
                return "Tecnico/formulario1";
            }
        }
    }


    //------FORMULARIO2 ----
    @GetMapping({"/formulario2", "formulario2"})
    public String pagformulario2(Model model, @RequestParam("id") String idStr,
                                @ModelAttribute("formulario") @Valid Formulario formulario2, BindingResult bindingResult) {
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        try{
            int id = Integer.parseInt(idStr);
            if(id <= 0|| !formularioRepository.existsById(id)){
                return "redirect:/tecnico/formulario";
            }
            Optional<Formulario> optionalFormulario=formularioRepository.findById(id);
            Optional<SitioCerrado> sitioCerradoOptional = sitioCerradoRepository.findById(id);
            if(optionalFormulario.isPresent()){
                formulario2 = optionalFormulario.get();
                SitioCerrado sitioCerrado = sitioCerradoOptional.get();
                model.addAttribute("sitioCerrado", sitioCerrado);
                model.addAttribute("formulario", formulario2);
                return "Tecnico/formulario2";
            }else{
                return "redirect:/tecnico";
            }
        }catch (NumberFormatException e){
            return "redirect:/tecnico/formulario";
        }

    }

    /* Guardar Datos*/
    @PostMapping("/saveFormulario2")
    public String saveFormulario2(@RequestParam("imagenSubida") MultipartFile img,@RequestParam("files") List<MultipartFile> files,
                                 @ModelAttribute("formulario2") @Valid Formulario formulario2, BindingResult bindingResult,
                                 Model model, RedirectAttributes attr) {
        List<Archivo> archivos = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    Archivo archivo = new Archivo();
                    archivo.setNombre(file.getOriginalFilename());
                    archivo.setTipo(1);
                    archivo.setArchivo(file.getBytes());
                    archivo.setContentType(file.getContentType());
                    archivoRepository.save(archivo);
                    archivos.add(archivo);
                } catch (IOException e) {
                    System.out.println("Error al procesar los archivos");
                    throw new RuntimeException(e);
                }
            }
        }
        if(formulario2.getConexion() == null || formulario2.getMovilidad()==null || formulario2.getNomredantario() == null){
            model.addAttribute("msgFormulario2","Por favor, complete lo pedido por el formulario");
            model.addAttribute("form2",formularioRepository.findAll());
            if(formulario2.getIdFormularios() == null){
                return "Tecnico/datost_nuevo";
            }else{
                return "Tecnico/formulario2";
            }
        }
        if(formulario2.getArea() == null || formulario2.getDni()==null || formulario2.getObservaciones()==null){
            model.addAttribute("msgFormulario","Por favor, complete lo pedido por el formulario");
            model.addAttribute("form2",formularioRepository.findAll());
            if(formulario2.getIdFormularios() == null){
                return "Tecnico/datost_nuevo";
            }else{
                return "Tecnico/formulario2";
            }
        }

        if(!img.isEmpty()){
            try {
                Archivo archivo = new Archivo();
                archivo.setNombre(img.getOriginalFilename());
                archivo.setTipo(1);
                archivo.setArchivo(img.getBytes());
                archivo.setContentType(img.getContentType());
                archivoRepository.save(archivo);
                formulario2.setArchivo(archivo);
            } catch (IOException e) {
                System.out.println("Error al procesar el archivo");
                throw new RuntimeException(e);
            }
        }
        if(!bindingResult.hasErrors()){
            if (formulario2.getArchivo() == null) {
                formulario2.setArchivo(new Archivo());
            }
            try {
                if (formulario2.getIdFormularios() == null) {
                    attr.addFlashAttribute("msg", "Datos de formulario 2 - validación de sitio guardados exitosamente");
                } else {
                    attr.addFlashAttribute("msg", "Datos de formulario 2 - validación de sitio guardados exitosamente");
                }
                formularioRepository.save(formulario2);
                return "redirect:/tecnico/formulario";
            } catch (Exception e) {
                System.out.println("Error al guardar los datos del formulario");
                throw new RuntimeException(e);
            }
        } else{
            model.addAttribute("formulario", formularioRepository.findAll());
            if (formulario2.getIdFormularios() == null) {
                return "Tecnico/datost_nuevo";
            } else {
                return "Tecnico/formulario2";
            }
        }
    }


    //------FORMULARIO3 ----

    @GetMapping({"/formulario3", "formulario3"})
    public String pagformulario3(Model model, @RequestParam("id") String idStr,
                                @ModelAttribute("formulario") @Valid Formulario formulario3, BindingResult bindingResult) {
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        try{
            int id = Integer.parseInt(idStr);
            if(id <= 0|| !formularioRepository.existsById(id)){
                return "redirect:/tecnico/formulario";
            }
            Optional<Formulario> optionalFormulario=formularioRepository.findById(id);
            if(optionalFormulario.isPresent()){
                formulario3 = optionalFormulario.get();
                List<TecnologiainstaladaFormulario> list = tecnologiainstaladaRepository.agruparTecnologia(formulario3.getIdFormularios());
                List<Tecnologiainstalada> listInst = tecInstaladaRepository.findAll();
                model.addAttribute("formulario", formulario3);
                model.addAttribute("tecFormList", list);
                model.addAttribute("tecInsList", listInst);
                return "Tecnico/formulario3";
            }else{
                return "redirect:/tecnico";
            }
        }catch (NumberFormatException e){
            return "redirect:/tecnico/formulario";
        }
    }

    /* Guardar Datos*/
    @PostMapping("/saveFormulario3")
    public String saveFormulario3(@RequestParam("files") List<MultipartFile> files,
                                 @ModelAttribute("formulario") @Valid Formulario formulario, BindingResult bindingResult,
                                 Model model, RedirectAttributes attr) {
        List<Archivo> archivos = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    Archivo archivo = new Archivo();
                    archivo.setNombre(file.getOriginalFilename());
                    archivo.setTipo(1);
                    archivo.setArchivo(file.getBytes());
                    archivo.setContentType(file.getContentType());
                    archivoRepository.save(archivo);
                    archivos.add(archivo);
                } catch (IOException e) {
                    System.out.println("Error al procesar los archivos");
                    throw new RuntimeException(e);
                }
            }
        }
        if(formulario.getConstruccion() == null || formulario.getInstalacion()==null || formulario.getDespliegue() == null){
            model.addAttribute("msgFormulario3","Por favor, complete lo pedido por el formulario");
            model.addAttribute("form3",formularioRepository.findAll());
            if(formulario.getIdFormularios() == null){
                return "Tecnico/datost_nuevo";
            }else{
                return "Tecnico/formulario2";
            }
        }
        if( formulario.getDescripcion()==null){
            model.addAttribute("msgFormulario","Por favor, complete lo pedido por el formulario");
            model.addAttribute("form3",formularioRepository.findAll());
            if(formulario.getIdFormularios() == null){
                return "Tecnico/datost_nuevo";
            }else{
                return "Tecnico/formulario3";
            }
        }

        if(!bindingResult.hasErrors()){
            if (formulario.getArchivo() == null) {
                formulario.setArchivo(new Archivo());
            }
            try {
                if (formulario.getIdFormularios() == null) {
                    attr.addFlashAttribute("msg", "Datos de formulario 3 - Trabajo realizado guardados exitosamente");
                } else {
                    attr.addFlashAttribute("msg", "Datos de formulario 3 - Trabajo realizado guardados exitosamente");
                }
                formularioRepository.save(formulario);
                return "redirect:/tecnico/formulario";
            } catch (Exception e) {
                System.out.println("Error al guardar los datos del formulario");
                throw new RuntimeException(e);
            }
        } else{
            model.addAttribute("formulario", formularioRepository.findAll());
            if (formulario.getIdFormularios() == null) {
                return "Tecnico/datost_nuevo";
            } else {
                return "Tecnico/formulario3";
            }
        }
    }

    //-----------------------------------------------------------------------
    @GetMapping("/mapa")
    public String pagmapa(Model model) {
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        List<Ticket> listaMp = ticketRepository.listarmapa();
        model.addAttribute("listaMp",listaMp);
        List<SitioCerrado> sitioCerradoList = sitioCerradoRepository.findAll();
        model.addAttribute("sitioCerradoList", sitioCerradoList);
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
            Optional<SitioCerrado> sitioCerradoOptional = sitioCerradoRepository.findById(id);
            if (formularioOptional.isPresent() && sitioCerradoOptional.isPresent()) {
                Formulario formulario = formularioOptional.get();
                SitioCerrado sitioCerrado = sitioCerradoOptional.get();
                model.addAttribute("formulario", formulario);
                model.addAttribute("sitioCerrado", sitioCerrado);
                List<Ticket> listaT = ticketRepository.listarEstado();
                model.addAttribute("listaTicket", listaT);
                return "Tecnico/formularioCerrado";
            } else {
            return "redirect:/tecnico/datostickets";
            }
        } catch (NumberFormatException e) {
            return "redirect:/tecnico/datosticket";
        }
    }

}
