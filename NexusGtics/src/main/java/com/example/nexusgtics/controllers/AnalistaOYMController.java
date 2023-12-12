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
import java.time.*;
import java.util.*;

import static com.example.nexusgtics.controllers.GcsController.uploadObject;
import static com.example.nexusgtics.controllers.GcsController.uploadObjectArchivo;

@Controller
@RequestMapping(value = {"/analistaOYM"})
public class AnalistaOYMController {
    @Autowired
    private HttpSession session;
    final TicketRepository ticketRepository;
    final SitioRepository sitioRepository;
    final UsuarioRepository usuarioRepository;
    final EquipoRepository equipoRepository;
    final ArchivoSitioRepository archivoSitioRepository;
    final HistorialTicketRepository historialTicketRepository;
    final EmpresaRepository empresaRepository;
    private final ComentarioRepository comentarioRepository;
    final ArchivoRepository archivoRepository;
    final SitiosHasEquiposRepository sitiosHasEquiposRepository;
    final CargoRepository cargoRepository;
    final FormularioRepository formularioRepository;
    final TecnicosCuadrillaRepository tecnicosCuadrillaRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private SitioCerradoRepository sitioCerradoRepository;

    public AnalistaOYMController(SitioRepository sitioRepository, TicketRepository ticketRepository, EquipoRepository equipoRepository, ArchivoSitioRepository archivoSitioRepository, HistorialTicketRepository historialTicketRepository, EmpresaRepository empresaRepository, EmpresaRepository empresaRepository1, UsuarioRepository usuarioRepository, ComentarioRepository comentarioRepository, ArchivoRepository archivoRepository, SitiosHasEquiposRepository sitiosHasEquiposRepository, CargoRepository cargoRepository, FormularioRepository formularioRepository, TecnicosCuadrillaRepository tecnicosCuadrillaRepository, PasswordEncoder passwordEncoder, SitioCerradoRepository  sitioCerradoRepository){
        this.sitioRepository = sitioRepository;
        this.ticketRepository = ticketRepository;
        this.equipoRepository = equipoRepository;
        this.archivoSitioRepository = archivoSitioRepository;
        this.historialTicketRepository = historialTicketRepository;
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.comentarioRepository = comentarioRepository;
        this.archivoRepository = archivoRepository;
        this.sitiosHasEquiposRepository = sitiosHasEquiposRepository;
        this.cargoRepository = cargoRepository;
        this.formularioRepository = formularioRepository;
        this.tecnicosCuadrillaRepository = tecnicosCuadrillaRepository;
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


                if(file.getSize()>1){
                    if (file.getOriginalFilename().length() > 40) {
                        model.addAttribute("msgCadena", "El nombre del archivo no debe sobrepasar los 45 caracteres");
                        return "AnalistaOYM/perfilEditar";
                    }
                    String extensionValida = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
                    if (!extensionValida.equals("png") && !extensionValida.equals("jpg") && !extensionValida.equals("jpeg")) {
                        model.addAttribute("msgExtension", "Solo se permiten archivos con extensión png, jpg y jpeg");
                        return "AnalistaOYM/perfilEditar";
                    }
                }


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
        List<Sitio>  listaSitio = sitioRepository.listaDeSitios();
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
        if(file.getSize()>1){
            if (file.getOriginalFilename().length() > 40) {
                model.addAttribute("msgCadena", "El nombre del archivo no debe sobrepasar los 45 caracteres");
                return "AnalistaOYM/oymEditarSitio";
            }
            String extensionValida = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
            if (!extensionValida.equals("png") && !extensionValida.equals("jpg") && !extensionValida.equals("jpeg")) {
                model.addAttribute("msgExtension", "Solo se permiten archivos con extensión png, jpg y jpeg");
                return "AnalistaOYM/oymEditarSitio";
            }
        }
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
            // Agrega un mensaje de éxito como flash attribute
            redirectAttributes.addFlashAttribute("mensaje", "Equipo agregado con éxito.");
        } else {
            // Agrega un mensaje de error como flash attribute
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
        List<Equipo> leq = equipoRepository.listaEquiposHabilitados();
        int idSitios =   0;

        for (Equipo equipo : leq) {
            // Verificar si ya existe un registro con la combinación idSitios y idEquipos
            List<SitiosHasEquipo> existentes = sitiosHasEquiposRepository.listaEquiposPorSitioYEquipo(idSitios, equipo.getIdEquipos());

            if (existentes.isEmpty()) {
                // No hay registros existentes, podemos agregar el equipo
                sitiosHasEquiposRepository.agregarEquipo(0, equipo.getIdEquipos());
                System.out.println(equipo.getIdEquipos());
            } else {
                // Ya existe un registro, puedes manejarlo de alguna manera si es necesario
                // Por ejemplo, puedes registrar un mensaje de registro, omitirlo, etc.
                System.out.println("Ya existe un registro para idSitios=" + idSitios + " e idEquipos=" + equipo.getIdEquipos());
            }
        }
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
        List<Sitio> sitioList = sitioRepository.listaDeSitios();
        model.addAttribute("sitioList", sitioList);
        return "AnalistaOYM/oymMapaSitios";
    }
    @GetMapping("/mapaTickets")
    public String mapaTickets(Model model){

        List<Ticket> listaT= ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        List<Sitio> sitioList = sitioRepository.listaDeSitios();
        model.addAttribute("sitioList", sitioList);

        return "AnalistaOYM/oymMapaTickets";
    }

    @GetMapping("/ticket")
    public String listaTicket(Model model, HttpSession httpSession){

        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        Integer idAnalista = u.getId();
        List<Ticket> listaTickets = ticketRepository.listaTicketsModificados(idAnalista,1);
        model.addAttribute("listaTicket",listaTickets);
        return "AnalistaOYM/oymListaTickets";
    }

    /* DIRECCIONAR A FORMULARIO*/
    @GetMapping("/crearTicket")
    public String crearTicket(Model model,
                              @ModelAttribute("ticket") Ticket ticket, Ticket ticket2) {
        model.addAttribute("listaEmpresa", empresaRepository.noNexus());
        model.addAttribute("listaSitios", sitioRepository.listaDeSitios());

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
    public String saveTicket(@ModelAttribute("ticket") @Valid Ticket ticket,
                              BindingResult bindingResult,
                             Model model, HttpSession httpSession, Ticket ticket2,
                             RedirectAttributes attr){

        Usuario u = (Usuario) httpSession.getAttribute("usuario");

        ticket.setEstado(1);
        //ticket.setIdTipoTicket(1);
        ZoneId zonaHoraria = ZoneId.of("GMT-5");
        LocalDate fechaActual = LocalDate.now(zonaHoraria); // Obtener la fecha actual en la zona horaria GMT-5
        ticket.setFechaCreacion(fechaActual);
        LocalTime horaActual = LocalTime.now(zonaHoraria);
        ticket.setHoraCreacion(horaActual);

        Empresa empresaSeleccionada = ticket.getIdEmpresaAsignada();
        model.addAttribute("empresaSeleccionada", empresaSeleccionada);

        Sitio sitioSeleccionada = ticket.getIdSitios();
        model.addAttribute("sitioSeleccionada", sitioSeleccionada);

        ticket2.setPrioridad(ticket.getPrioridad());
        model.addAttribute("ticket2", ticket2);

        System.out.println(ticket.getUsuarioSolicitante());
        System.out.println(ticket.getDescripcion());
        ticket.setDescripcion(ticket2.getDescripcion());
        ticket.setUsuarioSolicitante(ticket2.getUsuarioSolicitante());

        if(ticket.getIdEmpresaAsignada() == null || ticket.getIdEmpresaAsignada().getIdEmpresas() == null || ticket.getIdEmpresaAsignada().getIdEmpresas() == -1){
            model.addAttribute("msgEmpresa", "Escoger una empresa");
            model.addAttribute("listaEmpresa", empresaRepository.noNexus());
            model.addAttribute("listaSitios", sitioRepository.listaDeSitios());
            return "AnalistaOYM/oymCrearTicket";
        }
        if(ticket.getIdSitios() == null || ticket.getIdSitios().getIdSitios() == null || ticket.getIdSitios().getIdSitios() == -1){
            model.addAttribute("msgSitio", "Escoger una sitio");
            model.addAttribute("listaEmpresa", empresaRepository.noNexus());
            model.addAttribute("listaSitios", sitioRepository.listaDeSitios());
            return "AnalistaOYM/oymCrearTicket";
        }
        if(ticket.getUsuarioSolicitante().isEmpty() || ticket.getUsuarioSolicitante().equals(" ")){
            model.addAttribute("msgPrioridad", "Debe seleccionar un usuario solicitante");
            model.addAttribute("listaEmpresa", empresaRepository.noNexus());
            model.addAttribute("listaSitios", sitioRepository.listaDeSitios());
            return "AnalistaOYM/oymCrearTicket";
        }
        if (!ticket.getUsuarioSolicitante().matches("^[a-zA-Z ]+$")) {
            model.addAttribute("msgPrioridad", "El usuario solicitante solo puede contener letras y espacios en blanco");
            model.addAttribute("listaEmpresa", empresaRepository.noNexus());
            model.addAttribute("listaSitios", sitioRepository.listaDeSitios());
            return "AnalistaOYM/oymCrearTicket";
        }
        if(ticket.getPrioridad() == null || ticket.getPrioridad().equals("-1")){
            model.addAttribute("msgPrioridad", "Seleccionar prioridad");
            model.addAttribute("listaEmpresa", empresaRepository.noNexus());
            model.addAttribute("listaSitios", sitioRepository.listaDeSitios());
            return "AnalistaOYM/oymCrearTicket";
        }
        if(ticket.getDescripcion().isEmpty() || ticket.getDescripcion().equals(" ")){
            model.addAttribute("msgPrioridad", "Debe seleccionar una descripción");
            model.addAttribute("listaEmpresa", empresaRepository.noNexus());
            model.addAttribute("listaSitios", sitioRepository.listaDeSitios());
            return "AnalistaOYM/oymCrearTicket";
        }

        Random random = new Random();
        int numeroRandom = random.nextInt(7) + 1;

        ticket.setIdUsuarioCreador(u);
        Optional<SitioCerrado> optionalSitioCerrado = sitioCerradoRepository.findById(numeroRandom);
        if (optionalSitioCerrado.isPresent()) {
            SitioCerrado sitioCerrado = optionalSitioCerrado.get();
            ticket.setIdsitioCerrado(sitioCerrado);
        }

        ticket.setReasignado(0);
        ticketRepository.save(ticket);
        attr.addFlashAttribute("msg1", "El ticket ha sido creado exitosamente por el usuario: " + ticket.getUsuarioSolicitante());

        //guardar también en historialTicket
        historialTicketRepository.crearHistorial1(1,fechaActual,horaActual,ticket.getIdTickets(),u.getId(),"Ticket creado");
        return "redirect:/analistaOYM/ticket";
//        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
//
//            Random random = new Random();
//            int numeroRandom = random.nextInt(7) + 1;
//
//            ticket.setIdUsuarioCreador(u);
//            ticket.setIdsitioCerrado(numeroRandom);
//            ticket.setReasignado(0);
//            ticketRepository.save(ticket);
//            attr.addFlashAttribute("msg1", "El ticket ha sido creado exitosamente por el usuario: " + ticket.getUsuarioSolicitante());
//
//            return "redirect:/analistaOYM/ticket";
//        } else { //hay al menos 1 error
//            System.out.println("error binding");
//            model.addAttribute("listaEmpresa", empresaRepository.noNexus());
//            model.addAttribute("listaSitios", sitioRepository.findAll());
//            return "AnalistaOYM/oymCrearTicket";
//        }

    }


    @PostMapping("/updateTicket")
    public String updateTicket(@ModelAttribute("ticket")  @Valid Ticket ticket,
                              BindingResult bindingResult, Model model, RedirectAttributes attr){
        try {

            Integer idTicket = ticket.getIdTickets();
            Optional<Ticket> optTicket = ticketRepository.findById(idTicket);

            if (optTicket.isPresent()){

                Ticket ticketBD = optTicket.get();

                if(ticketBD.getIdEmpresaAsignada() == null || ticket.getIdEmpresaAsignada().equals("-1")){
                    model.addAttribute("msgEmpresa", "Selecciona una Empresa");
                    model.addAttribute("listaEmpresa", empresaRepository.noNexus());
                    model.addAttribute("listaSitios", sitioRepository.listaDeSitios());
                    return "AnalistaOYM/oymEditarTicket";
                }
                if(ticketBD.getIdSitios() == null || ticket.getIdSitios().equals("-1")){
                    model.addAttribute("msgSitio", "Selecciona un Sitio de Despliegue");
                    model.addAttribute("listaEmpresa", empresaRepository.noNexus());
                    model.addAttribute("listaSitios", sitioRepository.listaDeSitios());
                    return "AnalistaOYM/oymEditarTicket";
                }
//                if(ticketBD.getDescripcion() == null){
//                    model.addAttribute("msgDescrip", "La descripcion debe estar llena");
//                    return "AnalistaOYM/oymEditarTicket";
//                }
//                if(ticketBD.getUsuarioSolicitante() == null){
//                    model.addAttribute("msgUser", "Por favor indica el Usuario Solicitante");
//                    return "AnalistaOYM/oymEditarTicket";
//                }
                if(ticketBD.getPrioridad() == null){
                    model.addAttribute("msgPrio", "Selecciona la prioridad del Ticket");
                    model.addAttribute("listaEmpresa", empresaRepository.noNexus());
                    model.addAttribute("listaSitios", sitioRepository.listaDeSitios());
                    return "AnalistaOYM/oymEditarTicket";
                }
                if (!bindingResult.hasErrors()) {
                    if (ticket.getIdSitios() != null) {
                        attr.addFlashAttribute("msg2", "El ticket ha sido actualizado exitosamente");
                    }
                    ticketBD.setIdEmpresaAsignada(ticket.getIdEmpresaAsignada());
                    ticketBD.setIdSitios(ticket.getIdSitios());
                    ticketBD.setDescripcion(ticket.getDescripcion());
                    ticketBD.setUsuarioSolicitante(ticket.getUsuarioSolicitante());
                    ticketBD.setPrioridad(ticket.getPrioridad());

                    ticketRepository.save(ticketBD);
                    return "redirect:/analistaOYM/ticket";
                }
                else {
                    model.addAttribute("listaEmpresa", empresaRepository.noNexus());
                    model.addAttribute("listaSitios", sitioRepository.listaDeSitios());
                    System.out.println("se mando en ticket, Binding");
                    return "AnalistaOYM/oymEditarTicket";
                }
            } else {

                return "redirect:/analistaOYM";
            }
        } catch (NumberFormatException e){
            return "redirect:/analistaOYM/ticket";
        }
    }
    @GetMapping("/editarTicket")
    public String editarTicket(Model model, @RequestParam("id") int id){

        Optional<Ticket> optTicket = ticketRepository.findById(id);
        if(optTicket.isPresent()){
            Ticket ticket = optTicket.get();
            model.addAttribute("ticket", ticket);
            model.addAttribute("listaEmpresa", empresaRepository.noNexus());
            model.addAttribute("listaSitios", sitioRepository.listaDeSitios());
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

                //---mandar tiempo transcurrido---
                ZoneId zonaHoraria = ZoneId.of("GMT-5");
                LocalDate fechaActual = LocalDate.now(zonaHoraria);
                LocalTime horaActual = LocalTime.now(zonaHoraria);
                LocalDate fechaVariable = ticket.getFechaCreacion();
                LocalTime horaVariable = ticket.getHoraCreacion();
                Period diferencia = fechaVariable.until(fechaActual);
                int difDia = diferencia.getDays(), hor, min;
                float difTiempo= Duration.between(horaVariable,horaActual ).getSeconds(); //hv-ha
                if(difDia==0){
                    if(difTiempo>=0){
                        hor = (int)(difTiempo/3600);
                        min = (int)((difTiempo/3600-hor)*60);
                    }else{
                        hor = (int)(-difTiempo/3600);
                        min = (int)((-difTiempo/3600-hor)*60);
                    }
                }else {
                    if(difTiempo>=0){
                        hor = (int)(difTiempo/3600);
                        min = (int)((difTiempo/3600-hor)*60);
                    }else{
                        hor = (int)((24*3600+difTiempo)/3600);
                        min = (int)(((24*3600+difTiempo)/3600 -hor)*60);
                        difDia--;
                    }
                }
                model.addAttribute("dias",difDia);
                model.addAttribute("horas",hor);
                model.addAttribute("minutos",min);
                //--------------------------------

                return "AnalistaOYM/oymVistaTicket";
            } else {
                return "redirect:/analistaOYM/oymListaTickets";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaOYM/ticket";
        }
    }

    //método para pasar el ticket en estado 7 a estado 8
    @PostMapping("/actualizarEstado")
    public String actualizarEstado(@RequestParam("idTickets") int id, @RequestParam("cambioEstado") String cambioEstado, RedirectAttributes redirectAttributes, HttpSession httpSession) {

        int estadoUtilizar;
        redirectAttributes.addAttribute("id",id);
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        Integer idAnalista = u.getId();
        System.out.println(cambioEstado);
        if (cambioEstado.equals("Finalizado")) {

            Optional<Ticket> ticketBuscado = ticketRepository.findById(id);
            if (ticketBuscado.isPresent()) {
                Ticket ticket = ticketBuscado.get();
                //poner fechaCierre hora actual
                ZoneId zonaHoraria = ZoneId.of("GMT-5");
                LocalDate fechaActual = LocalDate.now(zonaHoraria); // Obtener la fecha actual en la zona horaria GMT-5
                LocalTime horaActual = LocalTime.now(zonaHoraria);
                ticketRepository.guardarCierre(fechaActual,horaActual,id);
            }

            estadoUtilizar = 8;
            ZoneId zonaHoraria = ZoneId.of("GMT-5");
            LocalDate fechaCambio = LocalDate.now(zonaHoraria); // Obtener la fecha actual en la zona horaria GMT-5
            LocalTime horaCambio = LocalTime.now(zonaHoraria);
            historialTicketRepository.crearHistorial1(7,fechaCambio,horaCambio,id,idAnalista,"Aprobación y finalización del ticket.");
            ticketRepository.actualizarEstado(id,estadoUtilizar);
            redirectAttributes.addFlashAttribute("yum","El ticket ha sido finalizado correctamente");
            System.out.println("711");
            return "redirect:/analistaOYM/ticket";
        } else{
            System.out.println("714");
            return "redirect:/analistaOYM/verTicket?id=" + id;
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
        for (int i : new int[]{4, 2, 3}) {
            for (int j = 0; j <= 7; j++) {
                String attributeName = String.format("TicketXMes%d%d", i, j);
                model.addAttribute(attributeName, ticketRepository.TicketXMesMantenimiento(i, j));
            }
        }
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
    public String escribirComentarios(@RequestParam("id") int id,@RequestParam("idTicket") String idTicketStr, @RequestParam("comentario") String comentario, RedirectAttributes redirectAttributes, HttpSession httpSession){
        Usuario u = (Usuario) httpSession.getAttribute("usuario");

        try{
            int idTicket = Integer.parseInt(idTicketStr);
            if (idTicket <= 0 || !ticketRepository.existsById(idTicket)) {
                return "redirect:/analistaOYM/ticket";
            }
            Optional<Ticket> optionalTicket = ticketRepository.findById(idTicket);
            if (optionalTicket.isPresent()) {
                Ticket ticket = optionalTicket.get();
                ZoneId zonaHoraria = ZoneId.of("GMT-5");
                LocalDate fechaActual = LocalDate.now(zonaHoraria); // Obtener la fecha actual en la zona horaria GMT-5
                LocalTime horaActual = LocalTime.now(zonaHoraria);
                comentarioRepository.ingresarComentario1(id,idTicket,comentario,fechaActual,horaActual);
                redirectAttributes.addFlashAttribute("error","Comentario Añadido");

                //guardar también en historialTicket
                historialTicketRepository.crearHistorial1(ticket.getEstado(),fechaActual,horaActual,ticket.getIdTickets(),u.getId(),"Comentario agregado");

                return "redirect:/analistaOYM/comentarios?id="+idTicketStr;
            } else {
                return "redirect:/analistaOYM/ticket";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaOYM/ticket";
        }
    }

    @GetMapping("/historial")
    public String historialTicket(Model model, @RequestParam("id") String idStr){

        try {
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/analistaOYM/listaTickets";
            }
            Optional<Ticket> ticketOptional = ticketRepository.findById(id);
            List<HistorialTicket> listaHistorial= historialTicketRepository.listarHistorial1(id);

            if (ticketOptional.isPresent()) {
                Ticket ticket = ticketOptional.get();
                model.addAttribute("ticket", ticket);
                model.addAttribute("listaHistorial", listaHistorial);
                return "AnalistaOYM/oymHistorialTicket";
            } else {
                return "redirect:/analistaOYM/listaTickets";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaOYM/listaTickets";
        }

    }

    @GetMapping("/formulario")
    public String formulario(Model model, @RequestParam("id") String idStr,
                             @ModelAttribute("formulario") @Valid Formulario formulario, BindingResult bindingResult, HttpSession httpSession) {
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);

        try {
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !formularioRepository.existsById(id)) {
                System.out.println("error 1");
                return "redirect:/analistaOYM/ticket";
            }
            Optional<Formulario> optionalFormulario = formularioRepository.findById(id);
            Optional<Sitio> sitioOptional = sitioRepository.findById(id);
            if (optionalFormulario.isPresent() && sitioOptional.isPresent()) {
                formulario = optionalFormulario.get();
                Sitio sitio = sitioOptional.get();
                Usuario u = (Usuario) httpSession.getAttribute("usuario");
                Integer idCuadrilla = tecnicosCuadrillaRepository.obtenerCuadrillaId(u.getId());
                model.addAttribute("cuadrilla",idCuadrilla);
                model.addAttribute("formulario", formulario);
                model.addAttribute("sitio", sitio);
                model.addAttribute("idTick", formularioRepository.obtenerid(id));
                return "AnalistaOYM/oymFormulario";
            } else {
                System.out.println("error 2");
                return "redirect:/analistaOYM/ticket";
            }
        } catch (NumberFormatException e) {
            System.out.println("error 3");
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


    /* SUBIDA DE ARCHIVOS */
    @GetMapping("/archivosSitios")
    public String archivosSitios(Model model, @ModelAttribute("sitio") Sitio sitio){
        List<Sitio>  listaSitio = sitioRepository.listaDeSitios();
        model.addAttribute("listaSitio",listaSitio);
        model.addAttribute("listaSitios", sitioRepository.listaDeSitios());
//        model.addAttribute("nombreSitios", archivoSitioRepository.nombreSitios());
        System.out.println();
        model.addAttribute("listaArchivos", archivoSitioRepository.findAll());
        return "AnalistaOYM/oymArchivosSitios";
    }

    /* SUBIR ARCHIVOS*/
    @PostMapping("/subirArchivo")
    public String subirArchivo(@RequestParam("imagenSubida") MultipartFile file,
                               @RequestParam("idSitios") int idSitios,
                               Model model,
                               RedirectAttributes attr) {
        System.out.println(idSitios);
        System.out.println(file.getContentType());
        System.out.println(file.getSize());
        System.out.println(file.getName());

        System.out.println("Maximo tamaño permitido - 1048576 bytes");

        if(file.getSize()>1){
            /*VALIDACIÓN PARA QUE EL NOMBRE DEL ARHCIVO SEA MENOR A 40*/
            if (file.getOriginalFilename().length() > 40) {
                model.addAttribute("msgCadena", "El nombre del archivo no debe sobrepasar los 45 caracteres");
//                return "redirect:/analistaDespliegue/archivosSitios";
                model.addAttribute("listaSitios", sitioRepository.listaDeSitios());
                model.addAttribute("listaArchivos", archivoSitioRepository.findAll());
                return "AnalistaOYM/oymArchivosSitios";
            }

            /*VALIDACION DE EXTENSIÓN*/
            String extensionValida = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
            if (!extensionValida.equals("pdf") && !extensionValida.equals("docx") && !extensionValida.equals("xlsx")) {
                model.addAttribute("msgExtension", "Solo se permiten archivos con extensión pdf, docx y xlsx");
//                return "redirect:/analistaDespliegue/archivosSitios";
                model.addAttribute("listaSitios", sitioRepository.listaDeSitios());
                model.addAttribute("listaArchivos", archivoSitioRepository.findAll());
                return "AnalistaOYM/oymArchivosSitios";

            }

        }

//        int maxFileSize = 20000000;
//        int maxFileSize = 1048576;
//
//        if (file.getSize() > maxFileSize) {
//            System.out.println(file.getSize());
//            model.addAttribute("msgImagen1", "El archivo subido excede el tamaño máximo permitido (100MB).");
//            return "redirect:/analistaDespliegue/archivosSitios";
//        }

        try {
            /*OBTENGO EL SITIO DE MANERA OPCIONAL*/
            Optional<Sitio> optionalSitio = sitioRepository.findById(idSitios);
            Integer idSitioNombre = idSitios;

            if (optionalSitio.isPresent()) {
                /*pregunto si el sitio existe en al BD*/
                Sitio sitioBD = optionalSitio.get();

                if(file.getSize()>1){
                    // Obtenemos el nombre del archivo
                    String fileName = file.getOriginalFilename();
                    String nombreFront = fileName.substring(0, fileName.lastIndexOf('.'));
                    String extension = "";
                    int i = fileName.lastIndexOf('.');
                    if (i > 0) {
                        extension = fileName.substring(i+1);
                    }
                    Archivossitio archivossitio = new Archivossitio();
                    archivossitio.setNombreArchivo(fileName);
                    archivossitio.setTipo(2);
                    archivossitio.setArchivo(file.getBytes());
                    archivossitio.setContentType(file.getContentType());
                    archivossitio.setIdSitioSitio(optionalSitio.get());
                    archivossitio.setNombreSitio(optionalSitio.get().getNombre());
                    archivossitio.setNombreFront(nombreFront);
                    Archivossitio archivossitio1 = archivoSitioRepository.save(archivossitio);
                    String nombreArchivo = "archivo-"+archivossitio1.getIdSitioSitio().getIdSitios()+"-"+archivossitio1.getId()+"."+extension;
                    archivossitio1.setNombreArchivo(nombreArchivo);
                    System.out.println("El nombre del archivo essss: ");
                    System.out.println(archivossitio1.getNombreArchivo());
                    archivoSitioRepository.save(archivossitio1);
                    uploadObjectArchivo(archivossitio1);
                    archivossitio1.setArchivo(null);
                    attr.addFlashAttribute("archivoSubido", "El archivo '"+ archivossitio1.getNombreFront() +"' ha sido subido exitosamente al sitio '" + archivossitio1.getNombreSitio()+"'");

                }

            } else {
                return "redirect:/analistaOYM";
            }
            return "redirect:/analistaOYM/archivosSitios";

        } catch (IOException e) {
            model.addAttribute("msgImagen1", "Error al procesar el archivo: " + e.getMessage());
            return "redirect:/analistaOYM/archivosSitios";
        }
    }


    @GetMapping("/borrarArchivo")
    public String borrarArchivo(@RequestParam("id") int id,
                                RedirectAttributes attr) {

        Optional<Archivossitio> optional = archivoSitioRepository.findById(id);

        if (optional.isPresent()) {
            Archivossitio archivossitio = optional.get();
            archivoSitioRepository.deleteById(id);
            attr.addFlashAttribute("archivoEliminado", "El archivo '"+ archivossitio.getNombreFront() +"' ha sido eliminado exitosamente del sitio '" + archivossitio.getNombreSitio()+"'");

        }

        return "redirect:/analistaOYM/archivosSitios";
    }



}
