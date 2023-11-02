package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.*;
import com.example.nexusgtics.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
@RequestMapping(value = {"/analistaOYM"})
public class AnalistaOYMController {
    @Autowired
    private HttpSession session;
    final TicketRepository ticketRepository;
    final SitioRepository sitioRepository;
    final UsuarioRepository usuarioRepository;
    final EquipoRepository equipoRepository;
    final EmpresaRepository empresaRepository;
    private final ComentarioRepository comentarioRepository;
    final ArchivoRepository archivoRepository;
    final SitiosHasEquiposRepository sitiosHasEquiposRepository;
    final CargoRepository cargoRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private SitioCerradoRepository sitioCerradoRepository;

    public AnalistaOYMController(SitioRepository sitioRepository, TicketRepository ticketRepository, EquipoRepository equipoRepository, EmpresaRepository empresaRepository, EmpresaRepository empresaRepository1, UsuarioRepository usuarioRepository, ComentarioRepository comentarioRepository, ArchivoRepository archivoRepository, SitiosHasEquiposRepository sitiosHasEquiposRepository, CargoRepository cargoRepository, PasswordEncoder passwordEncoder, SitioCerradoRepository  sitioCerradoRepository){
        this.sitioRepository = sitioRepository;
        this.ticketRepository = ticketRepository;
        this.equipoRepository = equipoRepository;
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.comentarioRepository = comentarioRepository;
        this.archivoRepository = archivoRepository;
        this.sitiosHasEquiposRepository = sitiosHasEquiposRepository;
        this.cargoRepository = cargoRepository;
        this.passwordEncoder = passwordEncoder;
        this.sitioCerradoRepository = sitioCerradoRepository;
    }

    @GetMapping(value = {"/",""})
    public String paginaPrincipal(){
        return "AnalistaOYM/analistaOYM";
    }

    /* -------------------------- PERFIL -------------------------- */
    @GetMapping({"/perfil","perfilSuperadmin","perfilsuperadmin"})
    public String perfilOYM(Model model,
                                   @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession){
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        model.addAttribute("usuario", u);
        return "AnalistaOYM/perfilOYM";
    }

    /* PERFIL DEL SUPERADMINISTRADOR */
    @PostMapping("/savePerfil")
    public String savePerfil(@RequestParam("imagenSubida") MultipartFile file,
                             @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                             Model model,
                             RedirectAttributes attr, HttpSession httpSession){

        // ESTO SE AÑADIO DE BARD
        //session.setAttribute("usuario", usuario);

        if(usuario.getCargo() == null || usuario.getCargo().getIdCargos() == null || usuario.getCargo().getIdCargos() == -1){
            model.addAttribute("msgCargo", "Escoger un cargo");
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());

            if (usuario.getId() == null) {
                return "AnalistaOYM/analistaOYM";
            } else {
                return "AnalistaOYM/perfilEditar";
            }
        }
        if(usuario.getEmpresa() == null || usuario.getEmpresa().getIdEmpresas() == null || usuario.getEmpresa().getIdEmpresas() == -1){
            model.addAttribute("msgEmpresa", "Escoger una empresa");
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            if (usuario.getId() == null) {
                return "AnalistaOYM/analistaOYM";
            } else {
                return "AnalistaOYM/perfilEditar";
            }
        }

        if (file.getSize() > 0 && !file.getContentType().startsWith("image/")) {
            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
            if (usuario.getId() == null) {
                return "AnalistaOYM/analistaOYM";
            } else {
                return "AnalistaOYM/perfilEditar";
            }
        }

        int maxFileSize = 10485760;

        if (file.getSize() > maxFileSize) {
            System.out.println(file.getSize());
            model.addAttribute("msgImagen1", "El archivo subido excede el tamaño máximo permitido (10MB).");
            if (usuario.getId() == null) {
                return "AnalistaOYM/analistaOYM";
            } else {
                return "redirect:/analistaOYM/perfilEditar";
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
                return "redirect:/analistaOYM/perfil";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else { //hay al menos 1 error
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            if (usuario.getId() == null) {
                return "AnalistaOYM/analistaOYM";
            } else {
                return "AnalistaOYM/perfilEditar";
            }
        }
    }

    @GetMapping({"/perfilEditar"})
    public String perfilEditar(Model model,
                               @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession){

        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        int id = u.getId();
        try{
            //int id = Integer.parseInt(idStr);
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/analistaOYM/analistaOYM";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                usuario = optionalUsuario.get();    //modifiqué Usuario usuario para poder usar @ModelAttribute
                model.addAttribute("usuario", usuario);
                model.addAttribute("listaEmpresa", empresaRepository.findAll());
                model.addAttribute("listaCargo", cargoRepository.findAll());
                return "AnalistaOYM/perfilEditar";
            } else {
                return "redirect:/analistaOYM/perfil";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaOYM/analistaOYM";
        }

    }

    @GetMapping({"/perfilContra"})
    public String perfilContra(Model model,
                               @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession){
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        int id = u.getId();
        try{
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/analistaOYM/analistaOYM";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                model.addAttribute("idUsuario",id);
                return "AnalistaOYM/perfilContra";
            } else {
                return "redirect:/analistaOYM/perfil";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaOYM/analistaOYM";
        }
    }

    @PostMapping({"/actualizarContra"})
    public String actualizarContra(Model model, @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession,
                                   @RequestParam("password") String contrasenia,
                                   @RequestParam("newpassword") String contraseniaNueva, @RequestParam("renewpassword") String contraseniaConfirm,
                                   RedirectAttributes redirectAttributes){
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        int id = u.getId();

        String contraseniaAlmacenada = usuarioRepository.obtenerContraseña(id);

        if (passwordEncoder.matches(contrasenia, contraseniaAlmacenada)) {
            String contraseniaNuevaEncriptada = passwordEncoder.encode(contraseniaNueva);
            usuarioRepository.actualizarContraA(contraseniaNuevaEncriptada, id);
            redirectAttributes.addFlashAttribute("msg1", "La contraseña se ha actualizado exitosamente");

            return "redirect:/analistaOYM/perfil";
        } else {
            redirectAttributes.addFlashAttribute("error","La contraseña actual no es correcta.");
            return "redirect:/analistaOYM/perfilContra";
        }
    }

    /* -------------------------- FIN PERFIL -------------------------- */


    @GetMapping("/listaSitio")
    public String listaSitio(Model model){
        List<Sitio> listaSitio = sitioRepository.findAll();
        model.addAttribute("listaSitio",listaSitio);
        return "AnalistaOYM/oymListaSitio";
    }



    @GetMapping("/editarSitio")
    public String editarSitio(Model model, @RequestParam("id") String idStr,
                              @ModelAttribute("sitio") @Valid Sitio sitio, BindingResult bindingResult){
        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !sitioRepository.existsById(id)) {
                return "redirect:/analistaOYM/listaSitio";
            }
            Optional<Sitio> optSitio = sitioRepository.findById(id);
            if(optSitio.isPresent()){
                sitio = optSitio.get();

                // Obtén la lista de equipos por sitio
                List<SitiosHasEquipo> listaEquipos = sitiosHasEquiposRepository.listaEquiposPorSitio(id);
                model.addAttribute("sitio", sitio);
                model.addAttribute("listaEquipos", listaEquipos);
                return "AnalistaOYM/oymEditarSitio";
            }else {
                return "redirect:/analistaOYM/listaSitio";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaOYM/listaSitio";
        }

    }

    @GetMapping("/verSitio")
    public String verSitio(Model model, @RequestParam("id") String idStr){
        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !sitioRepository.existsById(id)) {
                return "redirect:/analistaOYM/listaSitio";
            }
            Optional<Sitio> optSitio = sitioRepository.findById(id);
            if(optSitio.isPresent()){
                Sitio sitio = optSitio.get();
                model.addAttribute("sitio", sitio);
                return "AnalistaOYM/oymVistaSitio";
            }else {
                return "redirect:/analistaOYM/listaSitio";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaOYM/listaSitio";
        }

    }

    @PostMapping("/guardarSitio")
    public String guardarSitio(@RequestParam("imagenSubida") MultipartFile file,
                               Sitio sitio,
                               Model model,
                               RedirectAttributes attr){
        if (sitio.getArchivo() == null) {
            sitio.setArchivo(new Archivo());
        }
        String fileName = file.getOriginalFilename();
        try{
            Archivo archivo = sitio.getArchivo();
            archivo.setNombre(fileName);
            archivo.setTipo(1);
            archivo.setArchivo(file.getBytes());
            archivo.setContentType(file.getContentType());
            archivoRepository.save(archivo);
            int idImagen = archivo.getIdArchivos();
            sitio.getArchivo().setIdArchivos(idImagen);
            sitioRepository.save(sitio);
            return "redirect:/analistaOYM/listaSitio";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/saveSitio")
    public String saveSitio(@RequestParam("imagenSubida") MultipartFile file,
                            @ModelAttribute("sitio") @Valid Sitio sitio,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes attr){

        if (sitio.getTipo() == null || sitio.getTipo().equals("-1")) {
            model.addAttribute("msgTipo", "Escoger un tipo de Sitio");

            if (sitio.getIdSitios() == null) {
                System.out.println("se mando en sitio, Tipo");
                return "AnalistaOYM/oymListaSitio";
            } else {
                return "AnalistaOYM/oymListaSitio";
            }
        }
        if (sitio.getTipoZona() == null || sitio.getTipoZona().equals("-1")) {
            model.addAttribute("msgZona", "Escoger un tipo de zona");
            if (sitio.getIdSitios() == null) {
                System.out.println("se mando en sitio, TipoZona");
                return "AnalistaOYM/oymListaSitio";
            } else {
                return "AnalistaOYM/oymListaSitio";
            }
        }
        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
            if (sitio.getArchivo() == null) {
                sitio.setArchivo(new Archivo());
            }
            String fileName = file.getOriginalFilename();
            try{
                Archivo archivo = sitio.getArchivo();
                archivo.setNombre(fileName);
                archivo.setTipo(1);
                archivo.setArchivo(file.getBytes());
                archivo.setContentType(file.getContentType());
                archivoRepository.save(archivo);
                int idImagen = archivo.getIdArchivos();
                sitio.getArchivo().setIdArchivos(idImagen);
                sitioRepository.save(sitio);
                attr.addFlashAttribute("msg1", "El sitio en '" + sitio.getDistrito() + "' ha sido editado exitosamente");

                return "redirect:/analistaOYM/listaSitio";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else { //hay al menos 1 error
            System.out.println("se mando en sitio, Binding");
            if (sitio.getIdSitios() == null) {
                System.out.println("se mando en sitio, TipoZona");
                return "AnalistaOYM/oymEditarSitio";
            } else {
                return "redirect:/analistaOYM/listaSitio";
            }
        }
    }

    @PostMapping("/agregarEquipo")
    public String agregarEquipo(@RequestParam("idSitios") int idSitios, @RequestParam("idEquipos") int idEquipos) {
        Optional<Equipo> optionalEquipo = equipoRepository.findById(idEquipos);

        if (optionalEquipo.isPresent()) {
            sitiosHasEquiposRepository.agregarEquipo(idSitios, idEquipos);

            return "redirect:/analistaOYM/listaSitio";
        } else {
            return "redirect:/analistaOYM/listaSitio";
        }
    }

    @GetMapping("/listaEquipo")
    public String listaEquipo(Model model, @RequestParam("id") int id){
        List<SitiosHasEquipo> listaEquipo = sitiosHasEquiposRepository.listaEquiposNoSitio(id);
        model.addAttribute("listaEquipo",listaEquipo);
        return "AnalistaOYM/oymListaEquipos";
    }

    @GetMapping("/verEquipo")
    public String verEquipo(Model model, @RequestParam("idEquipos") String idStr){
        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !equipoRepository.existsById(id)) {
                return "redirect:/analistaOYM/listaSitio";
            }
            Optional<Equipo> optionalEquipo = equipoRepository.findById(id);
            if(optionalEquipo.isPresent()){
                Equipo equipo = optionalEquipo.get();
                model.addAttribute("equipo", equipo);
                return "AnalistaOYM/oymVerEquipos";
            }else {
                return "redirect:/analistaOYM/listaSitio";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaOYM/listaSitio";
        }
    }

    @GetMapping("/mapaSitios")
    public String mapaSitios(){
        return "AnalistaOYM/oymMapaSitios";
    }

    @GetMapping("/mapaTickets")
    public String mapaTickets(Model model){

        List<Ticket> listaT= ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        List<Sitio> sitioList = sitioRepository.findAll();
        model.addAttribute("sitioList", sitioList);

        return "AnalistaOYM/oymMapaTickets";
    }

    @GetMapping("/ticket")
    public String listaTicket(Model model, HttpSession httpSession){

        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        Integer idAnalista = u.getId();
        List<Ticket> listaTickets = ticketRepository.listaTicketsModificados(idAnalista);

        model.addAttribute("listaTicket",listaTickets);
        return "AnalistaOYM/oymListaTickets";
    }

    /* DIRECCIONAR A FORMULARIO*/
    @GetMapping("/crearTicket")
    public String crearTicket(Model model) {
        model.addAttribute("listaEmpresa", empresaRepository.noNexus());
        model.addAttribute("listaSitios", sitioRepository.findAll());

        return "AnalistaOYM/oymCrearTicket";
    }

    /*CREAR NUEVO TICKET*/
    @PostMapping("/saveTicket")
    public String saveTicket(RedirectAttributes attr,
                              @ModelAttribute("ticket") @Valid Ticket ticket,
                              BindingResult bindingResult,
                             Model model, HttpSession httpSession){

        Usuario u = (Usuario) httpSession.getAttribute("usuario");

        if(ticket.getIdEmpresaAsignada() == null || ticket.getIdEmpresaAsignada().equals("-1")){
            model.addAttribute("msgEmpresa", "Escoger una empresa");
            if(ticket.getIdTickets()==null){
                return "AnalistaOYM/oymCrearTicket";

            }else{
                return "AnalistaOYM/oymEditarTicket";            }
        }

        if(ticket.getIdSitios() == null || ticket.getIdSitios().equals("-1")){
            model.addAttribute("msgSitio", "Escoger una sitio");
            if(ticket.getIdTickets()==null){
                return "AnalistaOYM/oymCrearTicket";

            }else{
                return "AnalistaOYM/oymEditarTicket";            }
        }

        if(ticket.getPrioridad() == null || ticket.getPrioridad().equals("-1")){
            model.addAttribute("msgPrioridad", "Seleccionar prioridad");
            if(ticket.getIdTickets()==null){
                return "AnalistaOYM/oymCrearTicket";

            }else{
                return "AnalistaOYM/oymEditarTicket";            }
        }

        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal

            Random random = new Random();
            int numeroRandom = random.nextInt(7) + 1;

            LocalDate fechaCreacion = LocalDate.now();
            ticket.setFechaCreacion(fechaCreacion);
            ticket.setIdUsuarioCreador(u);
            ticket.setIdsitioCerrado(numeroRandom);
            ticket.setReasignado(0);

            ticketRepository.save(ticket);
            attr.addFlashAttribute("msg1", "El ticket ha sido creado exitosamente");

            return "redirect:/analistaOYM/ticket";
        } else { //hay al menos 1 error
            if(ticket.getIdTickets()==null){
                return "AnalistaOYM/oymCrearTicket";

            }else {
                return "AnalistaOYM/oymEditarTicket";
            }
        }

    }

    @GetMapping("/editarTicket")
    public String editarTicket(Model model, @RequestParam("id") int id){

        Optional<Ticket> optTicket = ticketRepository.findById(id);
        if(optTicket.isPresent()){
            Ticket ticket = optTicket.get();
            model.addAttribute("ticket", ticket);
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaSitios", sitioRepository.findAll());
            return "AnalistaOYM/oymEditarTicket";

        }else{
            return "redirect:/analistaOYM/ticket";
        }

    }


    @GetMapping("/verTicket")
    public String verticket(Model model, @RequestParam("id") String idStr){

        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/analistaOYM/ticket";
            }
            Optional<Ticket> ticketBuscado = ticketRepository.findById(id);
            if (ticketBuscado.isPresent()) {
                Ticket ticket = ticketBuscado.get();
                model.addAttribute("ticket", ticket);
                return "AnalistaOYM/oymVistaTicket";
            } else {
                return "redirect:/analistaOYM/ticket";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaOYM/ticket";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession httpSession){
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        Integer idAnalista = u.getId();

        model.addAttribute("ticketsRecienCreados", ticketRepository.recienCreados(idAnalista));
        model.addAttribute("cantidadEquipos", equipoRepository.obtenerEquiposMarca());
        model.addAttribute("culminados", ticketRepository.creadosCulminados(idAnalista,7));

        return "AnalistaOYM/oymDashboard";
    }



    @GetMapping("/comentarios")
    public String comentarioTickets(Model model, @RequestParam("id") String idStr){

        try {
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/analistaOYM/ticket";
            }
            Optional<Ticket> ticketOptional = ticketRepository.findById(id);
            List<Comentario> listaComentarios = comentarioRepository.listarComentarios(id);
            if (ticketOptional.isPresent()) {
                Ticket ticket = ticketOptional.get();
                model.addAttribute("ticket", ticket);
                model.addAttribute("listaComentarios", listaComentarios);
                return "AnalistaOYM/oymComentarios";
            } else {
                return "redirect:/analistaOYM/ticket";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaOYM/ticket";
        }

    }

    @PostMapping("/escribirComentario")
    public String escribirComentarios(@RequestParam("id") int id,@RequestParam("idTicket") String idTicketStr, @RequestParam("comentario") String comentario, RedirectAttributes redirectAttributes){

        try{
            int idTicket = Integer.parseInt(idTicketStr);
            if (idTicket <= 0 || !ticketRepository.existsById(idTicket)) {
                return "redirect:/analistaOYM/ticket";
            }
            Optional<Ticket> optionalTicket = ticketRepository.findById(idTicket);
            if (optionalTicket.isPresent()) {
                Date fechaCreacion = new Date();
                comentarioRepository.ingresarComentario(id,idTicket,comentario,fechaCreacion);
                redirectAttributes.addFlashAttribute("error","Comentario Añadido");

                return "redirect:/analistaOYM/comentarios?id="+idTicketStr;
            } else {
                return "redirect:/analistaOYM/ticket";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaOYM/ticket";
        }
    }


    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable("id") int id){
        Optional<Archivo> opt = archivoRepository.findById(id);

        if(opt.isPresent()){
            Archivo u = opt.get();

            byte[] imagenComoBytes = u.getArchivo();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType(u.getContentType()));


            return new ResponseEntity<>(
                    imagenComoBytes,
                    httpHeaders,
                    HttpStatus.OK);
        } else {
            return  null;
        }
    }






}
