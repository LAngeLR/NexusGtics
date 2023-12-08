package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.*;
import com.example.nexusgtics.repository.EmpresaRepository;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
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
@RequestMapping("/analistaDespliegue")
public class AnalistaDespController {

    @Autowired
    private HttpSession session;
    final TicketRepository ticketRepository;
    final  ComentarioRepository comentarioRepository;
    final SitioRepository sitioRepository;
    final UsuarioRepository usuarioRepository;
    final EmpresaRepository empresaRepository;
    final EquipoRepository equipoRepository;
    final HistorialTicketRepository historialTicketRepository;

    final ArchivoRepository archivoRepository;
    final SitiosHasEquiposRepository sitiosHasEquiposRepository;
    final CargoRepository cargoRepository;

    private final PasswordEncoder passwordEncoder;


    public AnalistaDespController(TicketRepository ticketRepository, ComentarioRepository comentarioRepository, SitioRepository sitioRepository, UsuarioRepository usuarioRepository, EmpresaRepository empresaRepository, EquipoRepository equipoRepository, HistorialTicketRepository historialTicketRepository, ArchivoRepository archivoRepository, SitiosHasEquiposRepository sitiosHasEquiposRepository, CargoRepository cargoRepository, PasswordEncoder passwordEncoder){
        this.ticketRepository = ticketRepository;
        this.comentarioRepository = comentarioRepository;
        this.sitioRepository = sitioRepository;
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
        this.equipoRepository = equipoRepository;
        this.historialTicketRepository = historialTicketRepository;

        this.archivoRepository = archivoRepository;

        this.sitiosHasEquiposRepository = sitiosHasEquiposRepository;
        this.cargoRepository = cargoRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping(value = {"/", ""})
    public String paginaPrincipal(){
        return "AnalistaDespliegue/analistaDespliegue";
    }

    /* -------------------------- PERFIL -------------------------- */
    @GetMapping({"/perfil"})
    public String perfilOYM(Model model,
                            @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession){
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        model.addAttribute("usuario", u);
        return "AnalistaDespliegue/perfilDesp";
    }

    /* PERFIL DEL SUPERADMINISTRADOR */
    @PutMapping("/savePerfil")
    public String savePerfil(@RequestParam("imagenSubida") MultipartFile file,
                             @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                             Model model,
                             RedirectAttributes attr, HttpSession httpSession){

        // ESTO SE AÑADIO DE BARD
        //session.setAttribute("usuario", usuario);

//        if(usuario.getCargo() == null || usuario.getCargo().getIdCargos() == null || usuario.getCargo().getIdCargos() == -1){
//            model.addAttribute("msgCargo", "Escoger un cargo");
//            model.addAttribute("listaEmpresa", empresaRepository.findAll());
//            model.addAttribute("listaCargo", cargoRepository.findAll());
//
//            if (usuario.getId() == null) {
//                return "AnalistaDespliegue/analistaDespliegue";
//            } else {
//                return "AnalistaDespliegue/perfilEditar";
//            }
//        }
//        if(usuario.getEmpresa() == null || usuario.getEmpresa().getIdEmpresas() == null || usuario.getEmpresa().getIdEmpresas() == -1){
//            model.addAttribute("msgEmpresa", "Escoger una empresa");
//            model.addAttribute("listaEmpresa", empresaRepository.findAll());
//            model.addAttribute("listaCargo", cargoRepository.findAll());
//            if (usuario.getId() == null) {
//                return "AnalistaDespliegue/analistaDespliegue";
//            } else {
//                return "AnalistaDespliegue/perfilEditar";
//            }
//        }
//
//
//
//
//        if (file.getSize() > 0 && !file.getContentType().startsWith("image/") && !file.isEmpty()) {
//            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
//            if (usuario.getId() == null) {
//                return "AnalistaDespliegue/analistaDespliegue";
//            } else {
//                return "AnalistaDespliegue/perfilEditar";
//            }
//        }
//
//        String fileName1 = file.getOriginalFilename();
//
//        if (fileName1.contains("..") && !file.isEmpty()) {
//            model.addAttribute("msgImagen", "No se permiten '..' en el archivo ");
//            if (usuario.getId() == null) {
//                return "AnalistaDespliegue/analistaDespliegue";
//            } else {
//                return "AnalistaDespliegue/perfilEditar";
//            }
//        }
//
//        int maxFileSize = 10485760;
//
//        if (file.getSize() > maxFileSize && !file.isEmpty()) {
//            System.out.println(file.getSize());
//            model.addAttribute("msgImagen1", "El archivo subido excede el tamaño máximo permitido (10MB).");
//            if (usuario.getId() == null) {
//                return "AnalistaDespliegue/analistaDespliegue";
//            } else {
//                return "redirect:/analistaDespliegue/perfilEditar";
//            }
//        }
//
//        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
//            if (usuario.getArchivo() == null) {
//                usuario.setArchivo(new Archivo());
//            }
//
//            try{
//                if(!file.isEmpty()){
//                    // Obtenemos el nombre del archivo
//                    String fileName = file.getOriginalFilename();
//                    String extension = "";
//                    int i = fileName.lastIndexOf('.');
//                    if (i > 0) {
//                        extension = fileName.substring(i+1);
//                    }
//                    Archivo archivo = usuario.getArchivo();
//                    archivo.setNombre(fileName);
//                    archivo.setTipo(1);
//                    archivo.setArchivo(file.getBytes());
//                    archivo.setContentType(file.getContentType());
//                    Archivo archivo1 = archivoRepository.save(archivo);
//                    String nombreArchivo = "archivo-"+archivo1.getIdArchivos()+"."+extension;
//                    archivo1.setNombre(nombreArchivo);
//                    archivoRepository.save(archivo1);
//                    uploadObject(archivo1);
//
//                }
//
//                if (usuario.getId() == null) {
//                    attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha creado exitosamente");
//                } else {
//                    attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha actualizado exitosamente");
//                }
//                usuarioRepository.save(usuario);
//                //Usuario u = (Usuario) httpSession.getAttribute("usuario");
//                //HttpSession session = request.getSession(true);
//                //session.setAttribute("nombreUsuario", "nuevoNombre");
//                session.setAttribute("usuario", usuario);
//                return "redirect:/analistaDespliegue/perfil";
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//        } else { //hay al menos 1 error
//            model.addAttribute("listaEmpresa", empresaRepository.findAll());
//            model.addAttribute("listaCargo", cargoRepository.findAll());
//            if (usuario.getId() == null) {
//                return "AnalistaDespliegue/perfilDesp";
//            } else {
//                return "AnalistaDespliegue/perfilEditar";
//            }
//        }
        try {
            Integer idUsuario = usuario.getId();
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(idUsuario);

            if (optionalUsuario.isPresent()) {
                Usuario usuarioDB = optionalUsuario.get();

                if (file.getSize() > 0 && !file.getContentType().startsWith("image/") && !file.isEmpty()) {
                    model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
                    return "AnalistaDespliegue/perfilEditar";
                }
                String fileName1 = file.getOriginalFilename();
                if (fileName1.contains("..") && !file.isEmpty()) {
                    model.addAttribute("msgImagen", "No se permiten '..' en el archivo ");
                    return "AnalistaDespliegue/perfilEditar";
                }

                int maxFileSize = 10485760;
                if (file.getSize() > maxFileSize && !file.isEmpty()) {
                    System.out.println(file.getSize());
                    model.addAttribute("msgImagen1", "El archivo subido excede el tamaño máximo permitido (10MB).");
                    return "redirect:/admin/perfilEditar";
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
                        return "redirect:/analistaDespliegue/perfil";
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } else { //hay al menos 1 error
                    return "AnalistaDespliegue/perfilEditar";
                }
            }else {
                return "redirect:/analistaDespliegue/";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaDespliegue/";
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
                return "redirect:/AnalistaDespliegue/analistaDespliegue";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                usuario = optionalUsuario.get();    //modifiqué Usuario usuario para poder usar @ModelAttribute
                model.addAttribute("usuario", usuario);
                model.addAttribute("listaEmpresa", empresaRepository.findAll());
                model.addAttribute("listaCargo", cargoRepository.findAll());
                return "AnalistaDespliegue/perfilEditar";
            } else {
                return "redirect:/analistaDespliegue/perfil";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaDespliegue/analistaDespliegue";
        }

    }

    @GetMapping({"/perfilContra"})
    public String perfilContra(Model model,
                               @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession){
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        int id = u.getId();
        try{
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/analistaDespliegue/analistaDespliegue";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                model.addAttribute("idUsuario",id);
                return "AnalistaDespliegue/perfilContra";
            } else {
                return "redirect:/analistaDespliegue/perfil";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaDespliegue/analistaDespliegue";
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

            return "redirect:/analistaDespliegue/perfil";
        } else {
            redirectAttributes.addFlashAttribute("error","La contraseña actual no es correcta.");
            return "redirect:/analistaDespliegue/perfilContra";
        }
    }

    /* -------------------------- FIN PERFIL -------------------------- */

    /* -------------------------- SITIOS -------------------------- */


    @GetMapping("/listaSitio")
    public String listaSitio(Model model){
        List<Sitio>  listaSitio = sitioRepository.findAll();
        model.addAttribute("listaSitio",listaSitio);
        return "AnalistaDespliegue/despliegueListaSitio";
    }

    @GetMapping("/editarSitio")
    public String editarSitio(Model model, @RequestParam("id") String idStr,
                              @ModelAttribute("sitio") @Valid Sitio sitio, BindingResult bindingResult){
        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !sitioRepository.existsById(id)) {
                return "redirect:/analistaDespliegue/listaSitio";
            }
            Optional<Sitio> optSitio = sitioRepository.findById(id);
            if(optSitio.isPresent()){
                 sitio = optSitio.get();
                // Obtén la lista de equipos por sitio
                List<SitiosHasEquipo> listaEquipos = sitiosHasEquiposRepository.listaEquiposPorSitio(id);
                model.addAttribute("sitio", sitio);
                model.addAttribute("listaEquipos", listaEquipos);
                return "AnalistaDespliegue/despliegueEditarSitio";
            }else {
                return "redirect:/analistaDespliegue/listaSitio";
                }
        } catch (NumberFormatException e) {
            return "redirect:/analistaDespliegue/listaSitio";
        }

    }

    @GetMapping("/verSitio")
    public String verSitio(Model model, @RequestParam("id") String idStr,@ModelAttribute("sitio") @Valid Sitio sitio, BindingResult bindingResult){
        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !sitioRepository.existsById(id)) {
                return "redirect:/analistaDespliegue/listaSitio";
            }
            Optional<Sitio> optSitio = sitioRepository.findById(id);
            if(optSitio.isPresent()){
                sitio = optSitio.get();
                List<SitiosHasEquipo> listaEquipos = sitiosHasEquiposRepository.listaEquiposPorSitio(id);
                model.addAttribute("sitio", sitio);
                model.addAttribute("listaEquipos", listaEquipos);
                return "AnalistaDespliegue/despliegueVistaSitio";
            }else {
                return "redirect:/analistaDespliegue/listaSitio";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaDespliegue/listaSitio";
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

                return "AnalistaDespliegue/despliegueEditarSitio";

        }
        if (sitio.getTipoZona() == null || sitio.getTipoZona().equals("-1")) {
            model.addAttribute("msgZona", "Escoger un tipo de zona");

                return "AnalistaDespliegue/despliegueEditarSitio";
        }

        if (file.getSize() > 0 && !file.getContentType().startsWith("image/")) {
            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
                return "AnalistaDespliegue/despliegueEditarSitio";
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
                return "redirect:/analistaDespliegue/listaSitio";
            } catch (Exception e) {
                System.out.println("Error al guardar el equipo");
                throw new RuntimeException(e);
            }
        } else { //hay al menos 1 error
            System.out.println("se mando en sitio, Binding");

                return "AnalistaDespliegue/despliegueEditarSitio";
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

                return "AnalistaDespliegue/despliegueEditarSitio";
        }
        if (sitio.getTipoZona() == null || sitio.getTipoZona().equals("-1")) {
            model.addAttribute("msgZona", "Escoger un tipo de zona");
            return "AnalistaDespliegue/despliegueEditarSitio";
        }
        if (file.getSize() > 0 && !file.getContentType().startsWith("image/")) {
            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
            return "AnalistaDespliegue/despliegueEditarSitio";
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
                return "redirect:/analistaDespliegue/listaSitio";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else { //hay al menos 1 error
            return "AnalistaDespliegue/despliegueEditarSitio";
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

        return "redirect:/analistaDespliegue/listaEquiposNoPerteneciente?idSitios=" + idSitios;
    }


    @GetMapping("/listaEquiposNoPerteneciente")
        public String listaEquipoNoP(Model model, @RequestParam("idSitios") int idSitios){
        List<SitiosHasEquipo> listaEquipo = sitiosHasEquiposRepository.listaEquiposNoSitio(idSitios);
        model.addAttribute("listaEquipo",listaEquipo);
        model.addAttribute("idSitios", idSitios); // Agregar el valor de "id" al modelo
        return "AnalistaDespliegue/despliegueListaEquiposNoP";
    }
    @GetMapping("/listaEquiposPerteneciente")
    public String listaEquipoP(Model model, @RequestParam("id") int id){
        List<SitiosHasEquipo> listaEquipos = sitiosHasEquiposRepository.listaEquiposPorSitio(id);
        model.addAttribute("listaEquipo",listaEquipos);
        model.addAttribute("idSitios", id); // Agregar el valor de "id" al modelo
        return "AnalistaDespliegue/despliegueListaEquiposP";
    }
    @GetMapping("/verEquipo")
    public String verEquipo(Model model, @RequestParam("idEquipos") String idStr, @RequestParam("idSitios") int idSitios){
        model.addAttribute("idSitios", idSitios);
        try{
            int idEquipos = Integer.parseInt(idStr);
            if (idEquipos <= 0 || !equipoRepository.existsById(idEquipos)) {
                return "redirect:/analistaDespliegue/listaSitio";
            }
        Optional<Equipo> optionalEquipo = equipoRepository.findById(idEquipos);
        if(optionalEquipo.isPresent()){
            Equipo equipo = optionalEquipo.get();
            model.addAttribute("equipo", equipo);
            return "AnalistaDespliegue/despliegueVerEquipos";
        }else {
            return "redirect:/analistaDespliegue/listaSitio";
        }
        } catch (NumberFormatException e) {
            return "redirect:/analistaDespliegue/listaSitio";
        }
    }
    @GetMapping({"/soloVerEquipo", "/soloverequipo"})
    public String soloVerEquipo(Model model, @RequestParam("idEquipos") String idStr, @RequestParam("idSitios") int idSitios){
        model.addAttribute("idSitios", idSitios);
        try {
            int idEquipos = Integer.parseInt(idStr);
            if (idEquipos <= 0 || !equipoRepository.existsById(idEquipos)) {
                return "redirect:/analistaDespliegue/listaEquipo";
            }
            Optional<Equipo> optionalEquipo = equipoRepository.findById(idEquipos);
            if (optionalEquipo.isPresent()) {
                Equipo equipo = optionalEquipo.get();
                model.addAttribute("equipo", equipo);
                return "AnalistaDespliegue/despliegueSoloVerEquipos";
            } else {
                return "redirect:/analistaDespliegue/listaEquipo";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaDespliegue/listaEquipo";
        }
    }

    @GetMapping("/mapaSitios")
    public String mapaSitios(Model model){
        List<Sitio> sitioList = sitioRepository.findAll();
        model.addAttribute("sitioList", sitioList);

        return "AnalistaDespliegue/despliegueMapaSitios";
    }
    @GetMapping("/mapaTickets")
    public String mapaTickets(Model model){

        List<Ticket> listaT= ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        List<Sitio> sitioList = sitioRepository.findAll();
        model.addAttribute("sitioList", sitioList);

        return "AnalistaDespliegue/despliegueMapaTickets";
    }

    @GetMapping("/ticket")
    public String listaTicket(Model model, HttpSession httpSession){

        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        Integer idAnalista = u.getId();
        List<Ticket> listaTickets = ticketRepository.listaTicketsModificados(idAnalista);

        model.addAttribute("listaTicket",listaTickets);
        return "AnalistaDespliegue/despliegueListaTickets";
    }



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

        return "AnalistaDespliegue/despliegueCrearTicket";
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
            return "AnalistaDespliegue/despliegueCrearTicket";
        }

        if(ticket.getIdEmpresaAsignada() == null || ticket.getIdEmpresaAsignada().getIdEmpresas() == null || ticket.getIdEmpresaAsignada().getIdEmpresas() == -1){
            model.addAttribute("msgEmpresa", "Escoger una empresa");
            model.addAttribute("listaEmpresa", empresaRepository.noNexus());
            model.addAttribute("listaSitios", sitioRepository.findAll());
            return "AnalistaDespliegue/despliegueCrearTicket";
        }

        if(ticket.getIdSitios() == null || ticket.getIdSitios().getIdSitios() == null || ticket.getIdSitios().getIdSitios() == -1){
            model.addAttribute("msgSitio", "Escoger una sitio");
            model.addAttribute("listaEmpresa", empresaRepository.noNexus());
            model.addAttribute("listaSitios", sitioRepository.findAll());
            return "AnalistaDespliegue/despliegueCrearTicket";
        }

        if(ticket.getPrioridad() == null || ticket.getPrioridad().equals("-1")){
            model.addAttribute("msgPrioridad", "Seleccionar prioridad");
            model.addAttribute("listaEmpresa", empresaRepository.noNexus());
            model.addAttribute("listaSitios", sitioRepository.findAll());
            return "AnalistaDespliegue/despliegueCrearTicket";

        }

        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal

            Random random = new Random();
            int numeroRandom = random.nextInt(7) + 1;

            ticket.setIdUsuarioCreador(u);
            ticket.setIdsitioCerrado(numeroRandom);
            ticket.setReasignado(0);
            ticketRepository.save(ticket);
            attr.addFlashAttribute("msg1", "El ticket ha sido creado exitosamente por el usuario: " + ticket.getUsuarioSolicitante());

            return "redirect:/analistaDespliegue/ticket";
        } else { //hay al menos 1 error
            System.out.println("error binding");
            model.addAttribute("listaEmpresa", empresaRepository.noNexus());
            model.addAttribute("listaSitios", sitioRepository.findAll());
            return "AnalistaDespliegue/despliegueCrearTicket";
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
            return "AnalistaDespliegue/despliegueEditarTicket";

        }else{
            return "redirect:/analistaDespliegue/ticket";
        }

    }


    @GetMapping("/verTicket")
    public String verticket(Model model, @RequestParam("id") String idStr){

        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/analistaDespliegue/ticket";
            }
            Optional<Ticket> ticketBuscado = ticketRepository.findById(id);
            if (ticketBuscado.isPresent()) {
                Ticket ticket = ticketBuscado.get();
                model.addAttribute("ticket", ticket);
                return "AnalistaDespliegue/despliegueVistaTicket";
            } else {
                return "redirect:/analistaDespliegue/ticket";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaDespliegue/ticket";
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
            estadoUtilizar = 8;
            Date fechaCambioEstado = new Date();
            historialTicketRepository.crearHistorial(7,fechaCambioEstado,id,idAnalista,"Aprobación y finalización del ticket.");
            ticketRepository.actualizarEstado(id,estadoUtilizar);
            redirectAttributes.addFlashAttribute("yum","El ticket ha sido finalizado correctamente");
            System.out.println("711");
            return "redirect:/analistaDespliegue/ticket";
        } else{
            System.out.println("714");
            return "redirect:/analistaDespliegue/verTicket?id=" + id;
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
            for (int j = 0; j <= 6; j++) {
                String attributeName = String.format("TicketXMes%d%d", i, j);
                model.addAttribute(attributeName, ticketRepository.TicketXMes(i, j));
            }
        }

        return "AnalistaDespliegue/despliegueDashboard";
    }
    @GetMapping("/comentarios")
    public String comentarioTickets(Model model, @RequestParam("id") String idStr){

        try {
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/analistaDespliegue/ticket";
            }
            Optional<Ticket> ticketOptional = ticketRepository.findById(id);
            List<Comentario> listaComentarios = comentarioRepository.listarComentarios(id);
            if (ticketOptional.isPresent()) {
                Ticket ticket = ticketOptional.get();
                model.addAttribute("ticket", ticket);
                model.addAttribute("listaComentarios", listaComentarios);
                return "AnalistaDespliegue/despliegueComentarios";
            } else {
                return "redirect:/analistaDespliegue/ticket";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaDespliegue/ticket";
        }

    }

    @PostMapping("/escribirComentario")
    public String escribirComentarios(@RequestParam("id") int id,@RequestParam("idTicket") String idTicketStr, @RequestParam("comentario") String comentario, RedirectAttributes redirectAttributes){

        try{
            int idTicket = Integer.parseInt(idTicketStr);
            if (idTicket <= 0 || !ticketRepository.existsById(idTicket)) {
                return "redirect:/analistaDespliegue/ticket";
            }
            Optional<Ticket> optionalTicket = ticketRepository.findById(idTicket);
            if (optionalTicket.isPresent()) {
                Date fechaCreacion = new Date();
                comentarioRepository.ingresarComentario(id,idTicket,comentario,fechaCreacion);
                redirectAttributes.addFlashAttribute("error","Comentario Añadido");

                return "redirect:/analistaDespliegue/comentarios?id="+idTicketStr;
            } else {
                return "redirect:/analistaDespliegue/ticket";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaDespliegue/ticket";
        }
    }

    /*PARA VISUALIZAR FOTOS*/
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


