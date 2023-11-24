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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.nexusgtics.controllers.GcsController.uploadObject;

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
    @GetMapping({"/perfil"})
    public String perfilOYM(Model model,
                                   @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession){
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        model.addAttribute("usuario", u);
        return "AnalistaOYM/perfilOYM";
    }

    /* PERFIL DEL SUPERADMINISTRADOR */
    @PutMapping("/savePerfil")
    public String savePerfil(@RequestParam("imagenSubida") MultipartFile file,
                             @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                             Model model,
                             RedirectAttributes attr, HttpSession httpSession){

        // ESTO SE AÑADIO DE BARD
        //session.setAttribute("usuario", usuario);

        try {
            Integer idUsuario = usuario.getId();
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(idUsuario);

            if (optionalUsuario.isPresent()) {
                Usuario usuarioDB = optionalUsuario.get();

                if (file.getSize() > 0 && !file.getContentType().startsWith("image/") && !file.isEmpty()) {
                    model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
                    return "AnalistaOYM/perfilEditar";
                }
                String fileName1 = file.getOriginalFilename();
                if (fileName1.contains("..") && !file.isEmpty()) {
                    model.addAttribute("msgImagen", "No se permiten '..' en el archivo ");
                    return "AnalistaOYM/perfilEditar";
                }

                int maxFileSize = 10485760;
                if (file.getSize() > maxFileSize && !file.isEmpty()) {
                    System.out.println(file.getSize());
                    model.addAttribute("msgImagen1", "El archivo subido excede el tamaño máximo permitido (10MB).");
                    return "redirect:/analistaOYM/perfilEditar";
                }

                if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
                    if (usuario.getArchivo() == null) {
                        usuario.setArchivo(new Archivo());
                    }

                    try {
                        if (file.getSize() > 1) {
                            // Obtenemos el nombre del archivo
                            String fileName = file.getOriginalFilename();
                            String extension = "";
                            int i = fileName.lastIndexOf('.');
                            if (i > 0) {
                                extension = fileName.substring(i + 1);
                            }
                            Archivo archivo = usuario.getArchivo();
                            archivo.setNombre(fileName);
                            archivo.setTipo(1);
                            archivo.setArchivo(file.getBytes());
                            archivo.setContentType(file.getContentType());
                            Archivo archivo1 = archivoRepository.save(archivo);
                            String nombreArchivo = "archivo-" + archivo1.getIdArchivos() + "." + extension;
                            archivo1.setNombre(nombreArchivo);
                            archivoRepository.save(archivo1);
                            uploadObject(archivo1);
                            archivo1.setArchivo(null);
                            usuarioDB.setArchivo(archivo1);
                        }
                        if (usuario.getId() == null) {
                            attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha creado exitosamente");
                        } else {
                            attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha actualizado exitosamente");
                        }

                        usuarioDB.setNombre(usuario.getNombre());
                        usuarioDB.setApellido(usuario.getApellido());
                        usuarioDB.setCorreo(usuario.getCorreo());
                        usuarioDB.setDescripcion(usuario.getDescripcion());
                        usuarioRepository.save(usuarioDB);
                        session.setAttribute("usuario", usuarioDB);
                        return "redirect:/analistaOYM/perfil";
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } else { //hay al menos 1 error
                    return "AnalistaOYM/perfilEditar";
                }
            }else {
                return "redirect:/analistaOYM/";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaOYM/listaUsuario";
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
        List<Sitio>  listaSitio = sitioRepository.findAll();
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
    public String verSitio(Model model, @RequestParam("id") String idStr,@ModelAttribute("sitio") @Valid Sitio sitio, BindingResult bindingResult){
        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !sitioRepository.existsById(id)) {
                return "redirect:/analistaOYM/listaSitio";
            }
            Optional<Sitio> optSitio = sitioRepository.findById(id);
            if(optSitio.isPresent()){
                sitio = optSitio.get();
                List<SitiosHasEquipo> listaEquipos = sitiosHasEquiposRepository.listaEquiposPorSitio(id);
                model.addAttribute("sitio", sitio);
                model.addAttribute("listaEquipos", listaEquipos);
                return "AnalistaOYM/oymVistaSitio";
            }else {
                return "redirect:/analistaOYM/listaSitio";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaOYM/listaSitio";
        }
    }

    @PostMapping("/actualizarSitio")
    public String actualizarSitio(@RequestParam("imagenSubida") MultipartFile file,
                                  @ModelAttribute("sitio") @Valid Sitio sitio,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes attr) {

        if (sitio.getTipo() == null || sitio.getTipo().equals("-1")) {
            model.addAttribute("msgTipo", "Escoger un tipo de Sitio");

            return "AnalistaOYM/oymEditarSitio";

        }
        if (sitio.getTipoZona() == null || sitio.getTipoZona().equals("-1")) {
            model.addAttribute("msgZona", "Escoger un tipo de zona");

            return "AnalistaOYM/oymEditarSitio";
        }

        if (file.getSize() > 0 && !file.getContentType().startsWith("image/")) {
            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");

            return "AnalistaOYM/oymEditarSitio";
        }

        if (!bindingResult.hasErrors()) {
            // Si no hay errores, se realiza el flujo normal
            if (sitio.getArchivo() == null) {
                sitio.setArchivo(new Archivo());
            }

            try {


                if(file.getSize()>1){
                    // Obtenemos el nombre del archivo
                    String fileName = file.getOriginalFilename();
                    String extension = "";
                    int i = fileName.lastIndexOf('.');
                    if (i > 0) {
                        extension = fileName.substring(i+1);
                    }
                    Archivo archivo = sitio.getArchivo();
                    archivo.setNombre(fileName);
                    archivo.setTipo(1);
                    archivo.setArchivo(file.getBytes());
                    archivo.setContentType(file.getContentType());
                    Archivo archivo1 = archivoRepository.save(archivo);
                    String nombreArchivo = "archivo-"+archivo1.getIdArchivos()+"."+extension;
                    archivo1.setNombre(nombreArchivo);
                    archivoRepository.save(archivo1);
                    uploadObject(archivo1);
                    archivo1.setArchivo(null);
                }

                if (sitio.getIdSitios() == null) {
                    attr.addFlashAttribute("msg1", "El sitio '" + sitio.getNombre() + "' ha sido creado exitosamente");
                } else {
                    attr.addFlashAttribute("msg1", "El sitio '" + sitio.getNombre() + "' ha sido actualizado exitosamente");
                }
                BigDecimal longitud1 = sitio.getLongitud();
                BigDecimal latitud1 = sitio.getLatitud();
                sitio.setLongitud(longitud1.setScale(7, RoundingMode.DOWN));
                sitio.setLatitud(latitud1.setScale(7, RoundingMode.DOWN));
                sitioRepository.save(sitio);
                return "redirect:/analistaOYM/listaSitio";
            } catch (Exception e) {
                System.out.println("Error al guardar el equipo");
                throw new RuntimeException(e);
            }
        } else { //hay al menos 1 error
            System.out.println("se mando en sitio, Binding");

            return "AnalistaOYM/oymEditarSitio";
        }
    }


    @PostMapping("/saveSitio")
    public String saveSitio(@RequestParam("imagenSubida") MultipartFile file,
                            @ModelAttribute("sitio") @Valid Sitio sitio,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes attr) {

        String tipoSeleccionado = sitio.getTipo();
        String tipoZonaSeleccionado = sitio.getTipoZona();
        model.addAttribute("tipoSeleccionado", tipoSeleccionado);
        model.addAttribute("tipoZonaSeleccionado", tipoZonaSeleccionado);

        if (sitio.getTipo() == null || sitio.getTipo().equals("-1")) {
            model.addAttribute("msgTipo", "Escoger un tipo de Sitio");

            return "AnalistaOYM/oymEditarSitio";
        }
        if (sitio.getTipoZona() == null || sitio.getTipoZona().equals("-1")) {
            model.addAttribute("msgZona", "Escoger un tipo de zona");
            return "AnalistaOYM/oymEditarSitio";
        }
        if (file.getSize() > 0 && !file.getContentType().startsWith("image/")) {
            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
            return "AnalistaOYM/oymEditarSitio";
        }

        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
            if (sitio.getArchivo() == null) {
                sitio.setArchivo(new Archivo());
            }
            String fileName = file.getOriginalFilename();
            try {
                Archivo archivo = sitio.getArchivo();
                archivo.setNombre(fileName);
                archivo.setTipo(1);
                archivo.setArchivo(file.getBytes());
                archivo.setContentType(file.getContentType());
                archivoRepository.save(archivo);
                int idImagen = archivo.getIdArchivos();
                sitio.getArchivo().setIdArchivos(idImagen);

                if (sitio.getIdSitios() == null) {
                    attr.addFlashAttribute("msg1", "El sitio '" + sitio.getNombre() + "' ha sido creado exitosamente");
                } else {
                    attr.addFlashAttribute("msg1", "El sitio '" + sitio.getNombre() + "' ha sido actualizado exitosamente");
                }
                //truncar latitud y long
                BigDecimal longitud1 = sitio.getLongitud();
                BigDecimal latitud1 = sitio.getLatitud();
                sitio.setLongitud(longitud1.setScale(7, RoundingMode.DOWN));
                sitio.setLatitud(latitud1.setScale(7, RoundingMode.DOWN));

                sitioRepository.save(sitio);
                return "redirect:/analistaOYM/listaSitio";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else { //hay al menos 1 error
            return "AnalistaOYM/oymEditarSitio";
        }
    }

    @PostMapping("/agregarEquipo")
    public String agregarEquipo(@RequestParam("idSitios") int idSitios, @RequestParam("idEquipos") int idEquipos, RedirectAttributes redirectAttributes) {
        Optional<Equipo> optionalEquipo = equipoRepository.findById(idEquipos);

        if (optionalEquipo.isPresent()) {
            sitiosHasEquiposRepository.agregarEquipo(idSitios, idEquipos);
            redirectAttributes.addFlashAttribute("mensaje", "Equipo agregado con éxito.");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Error: Equipo no encontrado.");
        }

        return "redirect:/analistaOYM/listaEquiposNoPerteneciente?idSitios=" + idSitios;
    }

    @GetMapping("/listaEquiposNoPerteneciente")
    public String listaEquipoNoP(Model model, @RequestParam("idSitios") int idSitios){
        List<SitiosHasEquipo> listaEquipo = sitiosHasEquiposRepository.listaEquiposNoSitio(idSitios);
        model.addAttribute("listaEquipo",listaEquipo);
        model.addAttribute("idSitios", idSitios); // Agregar el valor de "id" al modelo
        return "AnalistaOYM/oymListaEquiposNoP";
    }
    @GetMapping("/listaEquiposPerteneciente")
    public String listaEquipoP(Model model, @RequestParam("id") int id){
        List<SitiosHasEquipo> listaEquipos = sitiosHasEquiposRepository.listaEquiposPorSitio(id);
        model.addAttribute("listaEquipo",listaEquipos);
        model.addAttribute("idSitios", id); // Agregar el valor de "id" al modelo
        return "AnalistaOYM/oymListaEquiposP";
    }

    @GetMapping("/verEquipo")
    public String verEquipo(Model model, @RequestParam("idEquipos") String idStr, @RequestParam("idSitios") int idSitios){
        model.addAttribute("idSitios", idSitios);
        try{
            int idEquipos = Integer.parseInt(idStr);
            if (idEquipos <= 0 || !equipoRepository.existsById(idEquipos)) {
                return "redirect:/analistaOYM/listaSitio";
            }
            Optional<Equipo> optionalEquipo = equipoRepository.findById(idEquipos);
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

    @GetMapping({"/soloVerEquipo", "/soloverequipo"})
    public String soloVerEquipo(Model model, @RequestParam("idEquipos") String idStr, @RequestParam("idSitios") int idSitios){
        model.addAttribute("idSitios", idSitios);
        try {
            int idEquipos = Integer.parseInt(idStr);
            if (idEquipos <= 0 || !equipoRepository.existsById(idEquipos)) {
                return "redirect:/analistaOYM/listaEquipo";
            }
            Optional<Equipo> optionalEquipo = equipoRepository.findById(idEquipos);
            if (optionalEquipo.isPresent()) {
                Equipo equipo = optionalEquipo.get();
                model.addAttribute("equipo", equipo);
                return "AnalistaOYM/oymSoloVerEquipos";
            } else {
                return "redirect:/analistaOYM/listaEquipo";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaOYM/listaEquipo";
        }
    }

    @GetMapping("/mapaSitios")
    public String mapaSitios(Model model){
        List<Sitio> sitioList = sitioRepository.findAll();
        model.addAttribute("sitioList", sitioList);
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
    public String crearTicket(Model model,
                              @ModelAttribute("ticket") Ticket ticket, Ticket ticket2) {
        model.addAttribute("listaEmpresa", empresaRepository.noNexus());
        model.addAttribute("listaSitios", sitioRepository.findAll());

        Empresa empresaSeleccionada = ticket.getIdEmpresaAsignada();
        if (empresaSeleccionada == null) {
            empresaSeleccionada = new Empresa();
            empresaSeleccionada.setIdEmpresas(-1);
        }
        model.addAttribute("empresaSeleccionada", empresaSeleccionada);

        Sitio sitioSeleccionada = ticket.getIdSitios();
        if (sitioSeleccionada == null) {
            sitioSeleccionada = new Sitio();
            sitioSeleccionada.setIdSitios(-1);
        }
        model.addAttribute("sitioSeleccionada", sitioSeleccionada);

        //para mandar las pripridades como una lista y no como 1 a 1 en HTML
        List<String> listaPrioridad = new ArrayList<>();
        listaPrioridad.add("Baja prioridad");
        listaPrioridad.add("Hacer");
        listaPrioridad.add("No crítico");
        listaPrioridad.add("Urgente");
        model.addAttribute("listaPrioridad", listaPrioridad);

        if(ticket2 == null) {
            ticket2 = new Ticket();
            ticket2.setPrioridad("");
        }
        model.addAttribute("ticket2",ticket2);

        return "AnalistaOYM/oymCrearTicket";
    }

    /*CREAR NUEVO TICKET*/
    @PostMapping("/saveTicket")
    public String saveTicket(RedirectAttributes attr,
                              @ModelAttribute("ticket") @Valid Ticket ticket,
                              BindingResult bindingResult,
                             Model model, HttpSession httpSession, Ticket ticket2){

        Usuario u = (Usuario) httpSession.getAttribute("usuario");

        ticket.setEstado(1);
        //ticket.setIdTipoTicket(1);
        ZoneId zonaHoraria = ZoneId.of("GMT-5");
        LocalDate fechaActual = LocalDate.now(zonaHoraria); // Obtener la fecha actual en la zona horaria GMT-5
        ticket.setFechaCreacion(fechaActual);

        Empresa empresaSeleccionada = ticket.getIdEmpresaAsignada();
        model.addAttribute("empresaSeleccionada", empresaSeleccionada);

        Sitio sitioSeleccionada = ticket.getIdSitios();
        model.addAttribute("sitioSeleccionada", sitioSeleccionada);

        ticket2.setPrioridad(ticket.getPrioridad());
        model.addAttribute("ticket2", ticket2);



        if (ticket.getFechaCierre() == null) {
            //bindingResult.rejectValue("fechaCierre", "error.ticket", "La fecha de cierre es obligatoria.");
            model.addAttribute("fechaCierre", "La fecha de cierre es obligatoria");
            model.addAttribute("listaEmpresa", empresaRepository.noNexus());
            model.addAttribute("listaSitios", sitioRepository.findAll());
            return "AnalistaOYM/oymCrearTicket";
        }

        if(ticket.getIdEmpresaAsignada() == null || ticket.getIdEmpresaAsignada().getIdEmpresas() == null || ticket.getIdEmpresaAsignada().getIdEmpresas() == -1){
            model.addAttribute("msgEmpresa", "Escoger una empresa");
            model.addAttribute("listaEmpresa", empresaRepository.noNexus());
            model.addAttribute("listaSitios", sitioRepository.findAll());
            return "AnalistaOYM/oymCrearTicket";
        }

        if(ticket.getIdSitios() == null || ticket.getIdSitios().getIdSitios() == null || ticket.getIdSitios().getIdSitios() == -1){
            model.addAttribute("msgSitio", "Escoger una sitio");
            model.addAttribute("listaEmpresa", empresaRepository.noNexus());
            model.addAttribute("listaSitios", sitioRepository.findAll());
            return "AnalistaOYM/oymCrearTicket";
        }

        if(ticket.getPrioridad() == null || ticket.getPrioridad().equals("-1")){
            model.addAttribute("msgPrioridad", "Seleccionar prioridad");
            model.addAttribute("listaEmpresa", empresaRepository.noNexus());
            model.addAttribute("listaSitios", sitioRepository.findAll());
            return "AnalistaOYM/oymCrearTicket";

        }

        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal

            Random random = new Random();
            int numeroRandom = random.nextInt(7) + 1;

            ticket.setIdUsuarioCreador(u);
            ticket.setIdsitioCerrado(numeroRandom);
            ticket.setReasignado(0);
            ticketRepository.save(ticket);
            attr.addFlashAttribute("msg1", "El ticket ha sido creado exitosamente por el usuario: " + ticket.getUsuarioSolicitante());

            return "redirect:/analistaOYM/ticket";
        } else { //hay al menos 1 error
            System.out.println("error binding");
            model.addAttribute("listaEmpresa", empresaRepository.noNexus());
            model.addAttribute("listaSitios", sitioRepository.findAll());
            return "AnalistaOYM/oymCrearTicket";
        }

    }

    @GetMapping("/editarTicket")
    public String editarTicket(Model model, @RequestParam("id") int id){

        Optional<Ticket> optTicket = ticketRepository.findById(id);
        if(optTicket.isPresent()){
            Ticket ticket = optTicket.get();


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fechaCierreFormateada = ticket.getFechaCierre().format(formatter);

            model.addAttribute("fechaCierreFormateada", fechaCierreFormateada);
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
        model.addAttribute("numeroClientesActual", ticketRepository.numeroClientesActual());
        model.addAttribute("numeroClientesAnterior", ticketRepository.numeroClientesAnterior());
        model.addAttribute("diferenciaRegistros", ticketRepository.diferenciaRegistros());
        model.addAttribute("numeroEmpresasAfiliadasMes", empresaRepository.numeroEmpresasAfiliadasMes());
        model.addAttribute("EmpresasAfiliadasDiferencia", empresaRepository.EmpresasAfiliadasDiferencia());
        model.addAttribute("cantTicketsCreados", ticketRepository.cantTicketsCreados());
        model.addAttribute("cantTicketsFinalizados", ticketRepository.cantTicketsFinalizados());
        model.addAttribute("CantporMes", ticketRepository.CantporMes());
        model.addAttribute("CantporMesAnterior", ticketRepository.CantporMesAnterior());
        model.addAttribute("CantHaceDosMeses", ticketRepository.CantHaceDosMeses());
        model.addAttribute("TicketXMes11", ticketRepository.TicketXMes(1,1));
        model.addAttribute("TicketXMes12", ticketRepository.TicketXMes(1,2));
        model.addAttribute("TicketXMes13", ticketRepository.TicketXMes(1,3));
        model.addAttribute("TicketXMes14", ticketRepository.TicketXMes(1,4));
        model.addAttribute("TicketXMes15", ticketRepository.TicketXMes(1,5));
        model.addAttribute("TicketXMes16", ticketRepository.TicketXMes(1,6));
        model.addAttribute("TicketXMes17", ticketRepository.TicketXMes(1,7));
        model.addAttribute("TicketXMes21", ticketRepository.TicketXMes(2,1));
        model.addAttribute("TicketXMes22", ticketRepository.TicketXMes(2,2));
        model.addAttribute("TicketXMes23", ticketRepository.TicketXMes(2,3));
        model.addAttribute("TicketXMes24", ticketRepository.TicketXMes(2,4));
        model.addAttribute("TicketXMes25", ticketRepository.TicketXMes(2,5));
        model.addAttribute("TicketXMes26", ticketRepository.TicketXMes(2,6));
        model.addAttribute("TicketXMes27", ticketRepository.TicketXMes(2,7));
        model.addAttribute("TicketXMes31", ticketRepository.TicketXMes(3,1));
        model.addAttribute("TicketXMes32", ticketRepository.TicketXMes(3,2));
        model.addAttribute("TicketXMes33", ticketRepository.TicketXMes(3,3));
        model.addAttribute("TicketXMes34", ticketRepository.TicketXMes(3,4));
        model.addAttribute("TicketXMes35", ticketRepository.TicketXMes(3,5));
        model.addAttribute("TicketXMes36", ticketRepository.TicketXMes(3,6));
        model.addAttribute("TicketXMes37", ticketRepository.TicketXMes(3,7));
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
