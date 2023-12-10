package com.example.nexusgtics.controllers;

import com.example.nexusgtics.dto.DashboardGraficoDto;
import com.example.nexusgtics.dto.DetalleCuadrillaDto;
import com.example.nexusgtics.dto.ListaCuadrillaCompletaDto;
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
import java.time.*;
import java.util.*;

import static com.example.nexusgtics.controllers.GcsController.uploadObject;

@Controller
@RequestMapping("/supervisor")
public class SupervisorController {
    @Autowired
    private HttpSession session;
    private final CuadrillaRepository cuadrillaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TicketRepository ticketRepository;
    private final SitioRepository sitioRepository;
    private final FormularioRepository formularioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ArchivoRepository archivoRepository;
    private final SitioCerradoRepository sitioCerradoRepository;
    final CargoRepository cargoRepository;
    final EmpresaRepository empresaRepository;
    private final ComentarioRepository comentarioRepository;
    private final EquipoRepository equipoRepository;
    private final HistorialTicketRepository historialTicketRepository;
    @Autowired
    private TecnicosCuadrillaRepository tecnicosCuadrillaRepository;


    public SupervisorController(CuadrillaRepository cuadrillaRepository,
                                UsuarioRepository usuarioRepository,
                                TicketRepository ticketRepository,
                                SitioRepository sitioRepository,
                                FormularioRepository formularioRepository,
                                PasswordEncoder passwordEncoder,
                                EmpresaRepository empresaRepository,
                                CargoRepository cargoRepository,
                                ArchivoRepository archivoRepository,
                                SitioCerradoRepository sitioCerradoRepository,
                                ComentarioRepository comentarioRepository,
                                EquipoRepository equipoRepository,
                                HistorialTicketRepository historialTicketRepository) {
        this.cuadrillaRepository = cuadrillaRepository;
        this.usuarioRepository = usuarioRepository;
        this.ticketRepository = ticketRepository;
        this.sitioRepository = sitioRepository;
        this.formularioRepository = formularioRepository;
        this.passwordEncoder = passwordEncoder;
        this.cargoRepository = cargoRepository;
        this.empresaRepository = empresaRepository;
        this.archivoRepository = archivoRepository;
        this.sitioCerradoRepository = sitioCerradoRepository;
        this.comentarioRepository = comentarioRepository;
        this.equipoRepository = equipoRepository;
        this.historialTicketRepository = historialTicketRepository;
    }



    /* -------------------------- PERFIL -------------------------- */
    @GetMapping({"/perfil"})
    public String perfilSupervisor(Model model,
                            @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession){
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        model.addAttribute("usuario", u);
        return "Supervisor/perfilSupervisor";
    }

    /* PERFIL DEL SUPERVISOR */
    @PutMapping("/savePerfil")
    public String savePerfil(@RequestParam("imagenSubida") MultipartFile file,
                             @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                             Model model,
                             RedirectAttributes attr, HttpSession httpSession) {

        // ESTO SE AÑADIO DE BARD
        //session.setAttribute("usuario", usuario);

//        if (usuario.getCargo() == null || usuario.getCargo().getIdCargos() == null || usuario.getCargo().getIdCargos() == -1) {
//            model.addAttribute("msgCargo", "Escoger un cargo");
//            model.addAttribute("listaEmpresa", empresaRepository.findAll());
//            model.addAttribute("listaCargo", cargoRepository.findAll());
//
//            if (usuario.getId() == null) {
//                return "Supervisor/perfilSupervisor";
//            } else {
//                return "Supervisor/perfilEditar";
//            }
//        }
//        if (usuario.getEmpresa() == null || usuario.getEmpresa().getIdEmpresas() == null || usuario.getEmpresa().getIdEmpresas() == -1) {
//            model.addAttribute("msgEmpresa", "Escoger una empresa");
//            model.addAttribute("listaEmpresa", empresaRepository.findAll());
//            model.addAttribute("listaCargo", cargoRepository.findAll());
//            if (usuario.getId() == null) {
//                return "Supervisor/perfilSupervisor";
//            } else {
//                return "Supervisor/perfilEditar";
//            }
//        }
//
//        if (file.getSize() > 0 && !file.getContentType().startsWith("image/") && !file.isEmpty()) {
//            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
//            if (usuario.getId() == null) {
//                return "Supervisor/perfilSupervisor";
//            } else {
//                return "Supervisor/perfilEditar";
//            }
//        }
//
//        String fileName1 = file.getOriginalFilename();
//
//        if (fileName1.contains("..") && !file.isEmpty()) {
//            model.addAttribute("msgImagen", "No se permiten '..' en el archivo ");
//            if (usuario.getId() == null) {
//                return "Supervisor/perfilSupervisor";
//            } else {
//                return "Supervisor/perfilEditar";
//            }
//        }
//
//
//        int maxFileSize = 10485760;
//
//        if (file.getSize() > maxFileSize && !file.isEmpty()) {
//            System.out.println(file.getSize());
//            model.addAttribute("msgImagen1", "El archivo subido excede el tamaño máximo permitido (10MB).");
//            if (usuario.getId() == null) {
//                return "Supervisor/perfilSupervisor";
//            } else {
//                return "redirect:/supervisor/perfilEditar";
//            }
//        }
//
//        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
//            if (usuario.getArchivo() == null) {
//                usuario.setArchivo(new Archivo());
//            }
//            String fileName = file.getOriginalFilename();
//            try{
//                //validación de nombre, apellido y correo
//                Archivo archivo = usuario.getArchivo();
//                archivo.setNombre(fileName);
//                archivo.setTipo(1);
//                archivo.setArchivo(file.getBytes());
//                archivo.setContentType(file.getContentType());
//                archivoRepository.save(archivo);
//                int idImagen = archivo.getIdArchivos();
//                usuario.getArchivo().setIdArchivos(idImagen);
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
//                return "redirect:/supervisor/perfil";
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//        } else { //hay al menos 1 error
//            model.addAttribute("listaEmpresa", empresaRepository.findAll());
//            model.addAttribute("listaCargo", cargoRepository.findAll());
//            if (usuario.getId() == null) {
//                return "Supervisor/perfilSupervisor";
//            } else {
//                return "Supervisor/perfilEditar";
//            }
//        }
        try {
            Integer idUsuario = usuario.getId();
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(idUsuario);

            if (optionalUsuario.isPresent()) {
                Usuario usuarioDB = optionalUsuario.get();

                if (file.getSize() > 0 && !file.getContentType().startsWith("image/") && !file.isEmpty()) {
                    model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
                    return "Supervisor/perfilEditar";
                }
                String fileName1 = file.getOriginalFilename();
                if (fileName1.contains("..") && !file.isEmpty()) {
                    model.addAttribute("msgImagen", "No se permiten '..' en el archivo ");
                    return "Supervisor/perfilEditar";
                }

                int maxFileSize = 10485760;
                if (file.getSize() > maxFileSize && !file.isEmpty()) {
                    System.out.println(file.getSize());
                    model.addAttribute("msgImagen1", "El archivo subido excede el tamaño máximo permitido (10MB).");
                    return "redirect:/supervisor/perfilEditar";
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
                        return "redirect:/supervisor/perfil";
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } else { //hay al menos 1 error
                    return "Supervisor/perfilEditar";
                }
            }else {
                return "redirect:/supervisor/";
            }
        } catch (NumberFormatException e) {
            return "redirect:/supervisor/listaUsuario";
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
                return "redirect:/supervisor/perfil";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                usuario = optionalUsuario.get();    //modifiqué Usuario usuario para poder usar @ModelAttribute
                model.addAttribute("usuario", usuario);
                model.addAttribute("listaEmpresa", empresaRepository.findAll());
                model.addAttribute("listaCargo", cargoRepository.findAll());
                return "Supervisor/perfilEditar";
            } else {
                return "redirect:/supervisor/perfil";
            }
        } catch (NumberFormatException e) {
            return "redirect:/supervisor/perfil";
        }

    }

    @GetMapping({"/perfilContra"})
    public String perfilContra(Model model,
                               @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession){
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        int id = u.getId();
        try{
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/supervisor/supervisor";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                model.addAttribute("idUsuario",id);
                return "Supervisor/perfilContra";
            } else {
                return "redirect:/supervisor/perfil";
            }
        } catch (NumberFormatException e) {
            return "redirect:/supervisor/supervisor";
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

            return "redirect:/supervisor/perfil";
        } else {
            redirectAttributes.addFlashAttribute("error","La contraseña actual no es correcta.");
            return "redirect:/supervisor/perfilContra";
        }
    }

    /* -------------------------- FIN PERFIL -------------------------- */


    @GetMapping( {"/","","supervisor"})
    public String paginaPrincipal(Model model){
        model.addAttribute("currentPage", "Inicio");
        return "Supervisor/menuSupervisor";
    }

    @GetMapping("/listaTickets")
    public String Tickets(Model model, HttpSession httpSession){

        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        Integer idSupervisor = u.getId();

        model.addAttribute("listaTickets",ticketRepository.listaTicketsModificado( 1, idSupervisor));

        return "Supervisor/listaTickets";
    }

    //lista de tickets nuevos asignados a la empresa
    @GetMapping("/listaTicketsNuevos")
    public String TicketsNuevos(Model model, HttpSession httpSession){

        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        Integer idEmpresa = u.getEmpresa().getIdEmpresas();
        System.out.println(idEmpresa);
        model.addAttribute("listaTicketsNuevos",ticketRepository.listaTicketsNuevos(idEmpresa));

        return "Supervisor/listaTicketsNuevos";
    }

    @GetMapping("/listaCuadrillas")
    public String Cuadrillas(Model model, HttpSession httpSession){

        Usuario u = (Usuario) httpSession.getAttribute("usuario");

        model.addAttribute("listaCuadrillaCompleta", cuadrillaRepository.cuadrillaCompleta(u.getEmpresa().getIdEmpresas()));
        model.addAttribute("listaCuadrillaImcompleta", cuadrillaRepository.cuadrillaImCompleta(u.getEmpresa().getIdEmpresas()));
        return "Supervisor/listaCuadrillas";
    }


    @GetMapping("/ticketNuevo")
    public String nuevoTicket(Model model, @RequestParam("id") String idStr, HttpSession httpSession){

        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        Integer idSupervisor = u.getId();

        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/supervisor/listaTickets";
            }
            Optional<Ticket> ticketBuscado = ticketRepository.findById(id);
            List<Usuario> listaSupervisor = usuarioRepository.listaDeSupervisores(5,idSupervisor, u.getEmpresa().getIdEmpresas());
            List<ListaCuadrillaCompletaDto> listaCuadrillas = cuadrillaRepository.cuadrillaCompleta(u.getEmpresa().getIdEmpresas());
            if (ticketBuscado.isPresent()) {
                Ticket ticket = ticketBuscado.get();
                model.addAttribute("ticket", ticket);
                model.addAttribute("listaSupervisores",listaSupervisor);
                model.addAttribute("listaCuadrillas",listaCuadrillas);

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

                return "Supervisor/ticketNuevo";
            } else {
                return "redirect:/supervisor/listaTickets";
            }
        } catch (NumberFormatException e) {
            return "redirect:/supervisor/listaTickets";
        }
    }

    //vista para asignarse el ticket
    @GetMapping("/ticketAsignar")
    public String asignarTicket(Model model, @RequestParam("id") String idStr, HttpSession httpSession){

        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        Integer idSupervisor = u.getId();

        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/supervisor/listaTickets";
            }
            Optional<Ticket> ticketBuscado = ticketRepository.findById(id);
            if (ticketBuscado.isPresent()) {
                Ticket ticket = ticketBuscado.get();
                model.addAttribute("ticket", ticket);
                return "Supervisor/ticketAsignar";
            } else {
                return "redirect:/supervisor/listaTickets";
            }
        } catch (NumberFormatException e) {
            return "redirect:/supervisor/listaTickets";
        }
    }

    @GetMapping("/ticketProceso")
    public String procesoTicket(Model model, @RequestParam("id") String idStr){

        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/supervisor/listaTickets";
            }
            Optional<Ticket> ticketBuscado = ticketRepository.findById(id);
            if (ticketBuscado.isPresent()) {
                Ticket ticket = ticketBuscado.get();
                Integer idTecnicoLider = tecnicosCuadrillaRepository.obtenerIdLider(ticket.getIdCuadrilla().getIdCuadrillas());
                Optional<Usuario> tecnicoLider = usuarioRepository.findById(idTecnicoLider);
                Usuario user = tecnicoLider.get();
                model.addAttribute("tickets", ticket);
                model.addAttribute("tecnicoLider",user);


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
                return "Supervisor/ticketProceso";
            } else {
                return "redirect:/supervisor/listaTickets";
            }
        } catch (NumberFormatException e) {
            return "redirect:/supervisor/listaTickets";
        }
    }

    @GetMapping("/ticketCerrado")
    public String cerradoTicket(Model model, @RequestParam("id") String idStr){

        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/supervisor/listaTickets";
            }
            Optional<Ticket> ticketBuscado = ticketRepository.findById(id);
            if (ticketBuscado.isPresent()) {
                Ticket ticket = ticketBuscado.get();
                Integer idTecnicoLider = tecnicosCuadrillaRepository.obtenerIdLider(ticket.getIdCuadrilla().getIdCuadrillas());
                Optional<Usuario> tecnicoLider = usuarioRepository.findById(idTecnicoLider);
                Usuario user = tecnicoLider.get();
                model.addAttribute("tickets", ticket);
                model.addAttribute("comentario",comentarioRepository.obtenerUltimoComentario(ticket.getIdTickets()));
                model.addAttribute("tecnicoLider",user);
                model.addAttribute("tiempoTranscurrido",historialTicketRepository.tiempoTranscurrido(id));

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

                return "Supervisor/ticketCerrado";
            } else {
                return "redirect:/supervisor/listaTickets";
            }
        } catch (NumberFormatException e) {
            return "redirect:/supervisor/listaTickets";
        }
    }

    @PostMapping("/actualizarSupervisor")
    public String actualizarSupervisor(Ticket ticket , RedirectAttributes redirectAttributes, @RequestParam("condicion") int condicion,HttpSession httpSession){
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        Integer idSupervisor = u.getId();
        Date fechaCambioEstado = new Date();

        ticketRepository.actualizarSupervisor(ticket.getIdTickets(),ticket.getIdSupervisorEncargado().getId(), condicion);

        if(ticket.getIdSupervisorEncargado().getId() != null &&
                ticket.getIdSupervisorEncargado().getId().intValue() != idSupervisor.intValue()){
            historialTicketRepository.crearHistorialReasignado(1,fechaCambioEstado,ticket.getIdTickets(),idSupervisor,"Supervisor Asignado",ticket.getIdSupervisorEncargado().getId());
            ticketRepository.actualizarEstado(ticket.getIdTickets(),2);
            redirectAttributes.addFlashAttribute("mensaje","Supervisor " + ticket.getIdSupervisorEncargado().getNombre()+" asignado");
            System.out.println("Mensaje Flash: " + redirectAttributes.getFlashAttributes());
            return "redirect:/supervisor/dashboard";
        }
        else{
            redirectAttributes.addAttribute("id",ticket.getIdTickets());
            historialTicketRepository.crearHistorial(1,fechaCambioEstado,ticket.getIdTickets(),idSupervisor,"Supervisor Asignado");
            ticketRepository.actualizarEstado(ticket.getIdTickets(),2);
            redirectAttributes.addFlashAttribute("mensaje", "Supervisor " + ticket.getIdSupervisorEncargado().getNombre() + " asignado");
            return "redirect:/supervisor/ticketNuevo";
        }
    }

    @PostMapping("/actualizarCuadrilla")
    public String actualizarCuadrilla(Ticket ticket, RedirectAttributes redirectAttributes, HttpSession httpSession){
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        Integer idSupervisor = u.getId();
        ticketRepository.actualizarCuadrilla(ticket.getIdTickets(),ticket.getIdCuadrilla().getIdCuadrillas());
        Date fechaCambioEstado = new Date();
        historialTicketRepository.crearHistorial(2,fechaCambioEstado,ticket.getIdTickets(),idSupervisor,"Pasando a Tecnico");
        ticketRepository.actualizarEstado(ticket.getIdTickets(),3);

        redirectAttributes.addFlashAttribute("abc","Cuadrilla " + ticket.getIdCuadrilla().getIdCuadrillas()+ " asignada");
        return "redirect:/supervisor/listaTickets";
    }

    @PostMapping("/actualizarEstado")
    public String actualizarEstado(@RequestParam("idTickets") int id, @RequestParam("cambioEstado") String cambioEstado, RedirectAttributes redirectAttributes, HttpSession httpSession) {

        int estadoUtilizar;
        redirectAttributes.addAttribute("id",id);
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        Integer idSupervisor = u.getId();

        if (cambioEstado.equals("Cerrado")) {
            estadoUtilizar = 7;
            Date fechaCambioEstado = new Date();
            historialTicketRepository.crearHistorial(6,fechaCambioEstado,id,idSupervisor,"Pasando a Analista");
            ticketRepository.actualizarEstado(id,estadoUtilizar);
            redirectAttributes.addFlashAttribute("yum","El ticket ha sido cerrado correctamente");
            return "redirect:/supervisor/listaTickets";
        } else{
            return "redirect:/supervisor/ticketCerrado";
        }
    }


    @GetMapping("/comentarios")
    public String comentarioTicket(Model model, @RequestParam("id") String idStr){

        try {
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/supervisor/listaTickets";
            }
            Optional<Ticket> ticketOptional = ticketRepository.findById(id);
            List<Comentario> listaComentarios = comentarioRepository.listarComentarios(id);
            if (ticketOptional.isPresent()) {
                Ticket ticket = ticketOptional.get();
                model.addAttribute("ticket", ticket);
                model.addAttribute("listaComentarios", listaComentarios);
                return "Supervisor/comentariosTickets";
            } else {
                return "redirect:/supervisor/listaTickets";
            }
        } catch (NumberFormatException e) {
            return "redirect:/supervisor/listaTickets";
        }

    }

    @GetMapping("/formulario")
    public String formulario(Model model, @RequestParam("id") String idStr){
        try {
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/supervisor/ticketCerrado?id="+idStr;
            }
            /*Analizar Sitio cerrado y su funcion para mostrar form*/
            Optional<Formulario> formularioOptional = formularioRepository.findById(id);
            /*Optional<SitioCerrado> sitioCerradoOptional = sitioCerradoRepository.findById(id);*/
            if (formularioOptional.isPresent() /*&& sitioCerradoOptional.isPresent()*/) {
                Formulario formulario = formularioOptional.get();
                /*SitioCerrado sitioCerrado = sitioCerradoOptional.get();*/
                model.addAttribute("formulario", formulario);
                /*model.addAttribute("sitioCerrado", sitioCerrado);*/
                return "Supervisor/formulario";
            } else {
                return "redirect:/supervisor/ticketCerrado?id="+idStr;
            }
        } catch (NumberFormatException e) {
            return "redirect:/supervisor/ticketCerrado?id="+idStr;
        }
    }

    @GetMapping("/mapaTickets")
    public String mapaTickets(Model model, HttpSession httpSession){

        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        Integer idSupervisor = u.getId();

        List<Sitio> listaSitios = sitioRepository.findAll();

        List<Ticket> listaTickets1 = ticketRepository.cerrados();
        List<Ticket> listaTickets2 = ticketRepository.progreso();
        List<Ticket> listaTickets3 = ticketRepository.nuevos();


        //List<Ticket> listaTickets = ticketRepository.listarMapaSupervisor(idSupervisor,u.getEmpresa().getIdEmpresas());

        model.addAttribute("ticketsCerrados", listaTickets1);
        model.addAttribute("ticketsProgreso", listaTickets2);
        model.addAttribute("ticketsNuevos", listaTickets3);

        model.addAttribute("sitios", listaSitios);

        return "Supervisor/mapaTickets";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession httpSession) {
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        Integer idEmpresa = u.getEmpresa().getIdEmpresas();
        Integer idSup = u.getId();
        List<Ticket> listaTickets = ticketRepository.listaTicketsSinSupervisor(idEmpresa);

        model.addAttribute("listaTickets", listaTickets);
        model.addAttribute("cantidadEquipos", equipoRepository.obtenerEquiposMarca());
        model.addAttribute("culminados", ticketRepository.infoDash(idSup,7));
        model.addAttribute("actividad",historialTicketRepository.actividadReciente(idSup));

        //Valores cuadrillas
        model.addAttribute("ultimoMesCuadrilla",cuadrillaRepository.obtenerCuadrillasUltimoMes());
        model.addAttribute("cMesPasado",cuadrillaRepository.cuadrillaMesPasado());

        //valores tecnicos
        model.addAttribute("tecnicosMes",usuarioRepository.tecnicosEsteMes(6,u.getEmpresa().getIdEmpresas()));
        model.addAttribute("tecnicosPasados",usuarioRepository.tecnicosMesPasado(6,u.getEmpresa().getIdEmpresas()));

        /*
        List<DashboardGraficoDto> graficoData = historialTicketRepository.grafico();

        model.addAttribute("grafico",historialTicketRepository.grafico());

        for (DashboardGraficoDto entry : graficoData) {
            System.out.println("ID Cuadrilla: " + entry.getIdCuadrilla() +
                    ", Cantidad: " + entry.getCantidad() +
                    ", Mes: " + entry.getMonth());
        }

        model.addAttribute("grafico", graficoData);

        No funciona
        */

        return "Supervisor/dashboardSupervisor";
    }


    @GetMapping("/crearCuadrilla")
    public String crearCuadrilla(Model model,  @RequestParam(name = "id", required = false, defaultValue = "-1") int id, HttpSession httpSession){

        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        Integer idSupervisor = u.getId();

        if(id == -1){
            model.addAttribute("valor", id);
            int numero= cuadrillaRepository.cantidadCuadrillas();
            model.addAttribute("cantidad",numero+1);
        }
        else{
            model.addAttribute("valor", cuadrillaRepository.numeroTecnicosPorCuadrilla(id));
            model.addAttribute("a", id);
        }
        model.addAttribute("listaTecnicos",usuarioRepository.listaDeSupervisores(6, idSupervisor, u.getEmpresa().getIdEmpresas()));
        return "Supervisor/crearCuadrilla";
    }

    @PostMapping("/guardarCuadrilla")
    public String guardarCuadrilla(Cuadrilla cuadrilla, RedirectAttributes redirectAttributes, @RequestParam("tecnico") int tecnico) {

        Instant fechaCreacion = Instant.now();
        cuadrilla.setFechaCreacion(fechaCreacion);
        cuadrillaRepository.save(cuadrilla);
        tecnicosCuadrillaRepository.insertarTecnico(cuadrilla.getIdCuadrillas(),tecnico,1);
        redirectAttributes.addFlashAttribute("msg","Cuadrilla " + cuadrilla.getIdCuadrillas() + " creada Correctamente");
        usuarioRepository.cambiarTecnico(tecnico);

        return "redirect:/supervisor/crearCuadrilla?id=" + cuadrilla.getIdCuadrillas();
    }

    @PostMapping("/guardarTecnicos")

    public String guardarTecnicos(
            @RequestParam(name = "tecnicosSeleccionados", required = false) List<Integer> tecnicosSeleccionados, @RequestParam("a") int valor){
        if (tecnicosSeleccionados != null && !tecnicosSeleccionados.isEmpty()) {
            for (Integer userId : tecnicosSeleccionados) {
                Usuario usuario = usuarioRepository.findById(userId).orElse(null);

                if (usuario != null) {
                    tecnicosCuadrillaRepository.insertarTecnico(valor,usuario.getId(),0);
                    usuarioRepository.cambiarTecnico(usuario.getId());
                }
            }
        }
        return "redirect:/supervisor/listaCuadrillas";
    }

    @GetMapping("/detallesCuadrilla")
    public String detallesCuadrilla(Model model, @RequestParam("id") String idStr){

        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !cuadrillaRepository.existsById(id)) {
                return "redirect:/supervisor/listaCuadrillas";
            }
            Optional<Cuadrilla> optShipper = cuadrillaRepository.findById(id);

            List<Usuario> integrantesCuadrilla = usuarioRepository.listaDeTecnicosPorCuadrilla(id);
            List<Ticket> listaTicketsCerradosPorCuadrilla = ticketRepository.listaTicketsCerradosPorCuadrilla(id);

            Integer idTecnicoLider = tecnicosCuadrillaRepository.obtenerIdLider(id);
            Optional<Usuario> tecnicoLider = usuarioRepository.findById(idTecnicoLider);
            Usuario user = tecnicoLider.get();

            Optional<DetalleCuadrillaDto> detalleOptional = tecnicosCuadrillaRepository.detalleCuadrilla(id);



            if (optShipper.isPresent()) {
                DetalleCuadrillaDto detalle = detalleOptional.get();
                model.addAttribute("detalle", detalle);
                model.addAttribute("integrantes", integrantesCuadrilla);
                model.addAttribute("listaTicketsCerradosPorCuadrilla", listaTicketsCerradosPorCuadrilla);
                model.addAttribute("tecnicoLider",user);

                return "Supervisor/detallesCuadrilla";
            } else {
                return "redirect:/supervisor/listaCuadrillas";
            }
        } catch (NumberFormatException e) {
            return "redirect:/supervisor/listaCuadrillas";
        }
    }

    @PostMapping("/saveUsuario")
    public String saveUsuario(@RequestParam("imagenSubida") MultipartFile file,
                              @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                              Model model,
                              RedirectAttributes attr){

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

                attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha actualizado exitosamente");

                usuarioRepository.save(usuario);
                return "redirect:/supervisor/perfil";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else { //hay al menos 1 error
            return "redirect:/supervisor/perfilEditar?id="+usuario.getId();
        }
    }


    @PostMapping("/escribirComentario")
    public String escribirComentario(@RequestParam("id") int id,@RequestParam("idTicket") String idTicketStr, @RequestParam("comentario") String comentario, RedirectAttributes redirectAttributes, HttpSession httpSession){
        Usuario u = (Usuario) httpSession.getAttribute("usuario");


        try{
            int idTicket = Integer.parseInt(idTicketStr);
            if (idTicket <= 0 || !ticketRepository.existsById(idTicket)) {
                return "redirect:/supervisor/listaTickets";
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

                return "redirect:/supervisor/comentarios?id="+idTicketStr;
            } else {
                return "redirect:/supervisor/listaTickets";
            }
        } catch (NumberFormatException e) {
            return "redirect:/supervisor/listaTickets";
        }
    }
}