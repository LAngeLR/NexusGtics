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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/analistaDespliegue")
public class AnalistaDespController {

    @Autowired
    private HttpSession session;
    final TicketRepository ticketRepository;
    final SitioRepository sitioRepository;
    final UsuarioRepository usuarioRepository;
    final EmpresaRepository empresaRepository;
    final EquipoRepository equipoRepository;
    final ArchivoRepository archivoRepository;
    final SitiosHasEquiposRepository sitiosHasEquiposRepository;
    final CargoRepository cargoRepository;

    private final PasswordEncoder passwordEncoder;


    public AnalistaDespController(TicketRepository ticketRepository, SitioRepository sitioRepository, UsuarioRepository usuarioRepository, EmpresaRepository empresaRepository, EquipoRepository equipoRepository, ArchivoRepository archivoRepository, SitiosHasEquiposRepository sitiosHasEquiposRepository, CargoRepository cargoRepository, PasswordEncoder passwordEncoder){
        this.ticketRepository = ticketRepository;
        this.sitioRepository = sitioRepository;
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
        this.equipoRepository = equipoRepository;

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
                return "AnalistaDespliegue/analistaDespliegue";
            } else {
                return "AnalistaDespliegue/perfilEditar";
            }
        }
        if(usuario.getEmpresa() == null || usuario.getEmpresa().getIdEmpresas() == null || usuario.getEmpresa().getIdEmpresas() == -1){
            model.addAttribute("msgEmpresa", "Escoger una empresa");
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            if (usuario.getId() == null) {
                return "AnalistaDespliegue/analistaDespliegue";
            } else {
                return "AnalistaDespliegue/perfilEditar";
            }
        }

        if (file.getSize() > 0 && !file.getContentType().startsWith("image/")) {
            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
            if (usuario.getId() == null) {
                return "AnalistaDespliegue/analistaDespliegue";
            } else {
                return "AnalistaDespliegue/perfilEditar";
            }
        }

        int maxFileSize = 10485760;

        if (file.getSize() > maxFileSize) {
            System.out.println(file.getSize());
            model.addAttribute("msgImagen1", "El archivo subido excede el tamaño máximo permitido (10MB).");
            if (usuario.getId() == null) {
                return "AnalistaDespliegue/analistaDespliegue";
            } else {
                return "redirect:/analistaDespliegue/perfilEditar";
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
                return "redirect:/analistaDespliegue/perfil";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else { //hay al menos 1 error
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            if (usuario.getId() == null) {
                return "AnalistaDespliegue/analistaDespliegue";
            } else {
                return "AnalistaDespliegue/perfilEditar";
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

        // Verificar si se cargó un nuevo archivo
        if (!file.isEmpty()) {
            try {
                // Procesar el archivo
                Archivo archivo = new Archivo();
                archivo.setNombre(file.getOriginalFilename());
                archivo.setTipo(1);
                archivo.setArchivo(file.getBytes());
                archivo.setContentType(file.getContentType());

                BigDecimal longitud1 = sitio.getLongitud();
                BigDecimal latitud1 = sitio.getLatitud();
                sitio.setLongitud(longitud1.setScale(7, RoundingMode.DOWN));
                sitio.setLatitud(latitud1.setScale(7, RoundingMode.DOWN));
                archivoRepository.save(archivo);

                // Asignar el nuevo archivo al equipo
                sitio.setArchivo(archivo);
            } catch (IOException e) {
                System.out.println("Error al procesar el archivo");
                throw new RuntimeException(e);
            }
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
                if (sitio.getIdSitios() == null) {
                    attr.addFlashAttribute("msg1", "El sitio '" + sitio.getNombre() + "' ha sido creado exitosamente");
                } else {
                    attr.addFlashAttribute("msg1", "El sitio '" + sitio.getNombre() + "' ha sido actualizado exitosamente");
                }
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
    public String agregarEquipo(@RequestParam("idSitios") int idSitios, @RequestParam("idEquipos") int idEquipos) {
        Optional<Equipo> optionalEquipo = equipoRepository.findById(idEquipos);

        if (optionalEquipo.isPresent()) {
            sitiosHasEquiposRepository.agregarEquipo(idSitios, idEquipos);
            return "redirect:/analistaDespliegue/listaEquiposNoPerteneciente?idSitios=" + idSitios;
        } else {
            return "redirect:/analistaDespliegue/listaSitio";
        }
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
    public String mapaTickets(){
        return "AnalistaDespliegue/despliegueMapaTickets";
    }

    @GetMapping("/ticket")
    public String listaTicket(Model model){
        //'listar'
        List<Ticket> listaTicket = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaTicket);
        return "AnalistaDespliegue/despliegueListaTickets";
    }



    @GetMapping("/crearTicket")
    public String crearTicket(Model model) {
        model.addAttribute("listaEmpresa", empresaRepository.findAll());
        model.addAttribute("listaSitios", sitioRepository.findAll());

        return "AnalistaDespliegue/despliegueCrearTicket";
    }

    @PostMapping("/saveTicket")
    public String saveTicket( Ticket ticket,
                              RedirectAttributes attr){

        ticketRepository.save(ticket);
        return "redirect:/analistaDespliegue/ticket";
    }


    @GetMapping("/editarTicket")
    public String editarTicket(Model model, @RequestParam("id") int id){

        Optional<Ticket> optTicket = ticketRepository.findById(id);
        if(optTicket.isPresent()){
            Ticket ticket = optTicket.get();
            model.addAttribute("ticket", ticket);
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaSitios", sitioRepository.findAll());
            return "AnalistaDespliegue/despliegueEditarTicket";

        }else{
            return "redirect:/analistaDespliegue/ticket";
        }

    }


    @GetMapping("/verTicket")
    public String verticket(Model model, @RequestParam("id") int id){
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if(optionalTicket.isPresent()){
            Ticket ticket = optionalTicket.get();
            model.addAttribute("ticket", ticket);
            return "AnalistaDespliegue/despliegueVistaTicket";
        }else{
            return "redirect:/analistaDespliegue/ticket";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "AnalistaDespliegue/despliegueDashboard";
    }
    @GetMapping("/comentarios")
    public String comentarios(){
        return "AnalistaDespliegue/despliegueComentarios";
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


