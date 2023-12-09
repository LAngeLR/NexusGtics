package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.*;
import com.example.nexusgtics.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.nexusgtics.controllers.GcsController.downloadObject;
import static com.example.nexusgtics.controllers.GcsController.uploadObject;
import static java.lang.Integer.valueOf;

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
    final HistorialTicketRepository historialTicketRepository;

    final EmpresaRepository empresaRepository;

    final CargoRepository cargoRepository;
    private final FormularioRepository formularioRepository;
    private final TecInstaladaRepository tecInstaladaRepository;
    private final TecnologiainstaladaFormularioRepository tecnologiainstaladaRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private TecnicosCuadrillaRepository tecnicosCuadrillaRepository;
    private EquipoRepository equipoRepository;


    public TecnicoController(TicketRepository ticketRepository,
                             UsuarioRepository usuarioRepository,
                             TipoticketRepository tipoticketRepository, SitioRepository sitioRepository, ComentarioRepository comentarioRepository,
                             CuadrillaRepository cuadrillaRepository, ArchivoRepository archivoRepository,
                             FormularioRepository formularioRepository,
                             EquipoRepository equipoRepository, SitioCerradoRepository sitioCerradoRepository, TecInstaladaRepository tecInstaladaRepository ,
                             EmpresaRepository empresaRepository, CargoRepository cargoRepository, TecnologiainstaladaFormularioRepository tecnologiainstaladaRepository, PasswordEncoder passwordEncoder, HistorialTicketRepository historialTicketRepository) {
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
        this.historialTicketRepository=historialTicketRepository;
    }

    //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


    @GetMapping({"/", "","tecnico"})
    public String paginaPrincipal(Model model, HttpSession httpSession) {
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        model.addAttribute("currentPage", "Inicio");
        //Usuario user = (Usuario) httpSession.getAttribute("usuario");
        //Usuario user2 = (Usuario) session.getAttribute("usuario");
        //System.out.println("User: "+ user.getNombre());
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
    @PutMapping("/savePerfil")
    public String savePerfil(@RequestParam("imagenSubida") MultipartFile file,
                             @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                             Model model,
                             RedirectAttributes attr, HttpSession httpSession){
//        List<Ticket> listaT = ticketRepository.findAll();
//        model.addAttribute("listaTicket", listaT);
//        // ESTO SE AÑADIO DE BARD
//        //session.setAttribute("usuario", usuario);
//
//        if(usuario.getCargo() == null || usuario.getCargo().getIdCargos() == null || usuario.getCargo().getIdCargos() == -1){
//            model.addAttribute("msgCargo", "Escoger un cargo");
//            model.addAttribute("listaEmpresa", empresaRepository.findAll());
//            model.addAttribute("listaCargo", cargoRepository.findAll());
//
//            if (usuario.getId() == null) {
//                return "Tecnico/tecnico";
//            } else {
//                return "Tecnico/perfilEditar";
//            }
//        }
//        if(usuario.getEmpresa() == null || usuario.getEmpresa().getIdEmpresas() == null || usuario.getEmpresa().getIdEmpresas() == -1){
//            model.addAttribute("msgEmpresa", "Escoger una empresa");
//            model.addAttribute("listaEmpresa", empresaRepository.findAll());
//            model.addAttribute("listaCargo", cargoRepository.findAll());
//            if (usuario.getId() == null) {
//                return "Tecnico/tecnico";
//            } else {
//                return "Tecnico/perfilEditar";
//            }
//        }
//
//        if (file.getSize() > 0 && !file.getContentType().startsWith("image/") && !file.isEmpty()) {
//            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
//            if (usuario.getId() == null) {
//                return "Tecnico/perfilTecnico";
//            } else {
//                return "Tecnico/perfilEditar";
//            }
//        }
//
//        String fileName1 = file.getOriginalFilename();
//        /*Validación para evitar 2 puntos*/
//        if (fileName1.contains("..") && !file.isEmpty()) {
//            model.addAttribute("msgImagen", "No se permiten '..' en el archivo ");
//            if (usuario.getId() == null) {
//                return "Tecnico/perfilTecnico";
//            } else {
//                return "Tecnico/perfilEditar";
//            }
//        }
//
//        /*Validación para archivos grande (NO FUNCIONA :C)*/
//        int maxFileSize = 10485760;
//        if (file.getSize() > maxFileSize && !file.isEmpty()) {
//            System.out.println(file.getSize());
//            model.addAttribute("msgImagen", "El archivo subido excede el tamaño máximo permitido (10MB).");
//            if (usuario.getId() == null) {
//                return "Tecnico/perfilTecnico";
//            } else {
//                return "redirect:/tecnico/perfilEditar";
//            }
//        }
//
//
//        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
//            if (usuario.getArchivo() == null) {
//                usuario.setArchivo(new Archivo());
//            }
//
//            try{
//                /*Si file contiene algo --> Guardarlo*/
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
//                }
//                if (usuario.getId() == null) {
//                    attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha creado exitosamente");
//                } else {
//                    attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha actualizado exitosamente");
//                }
//                usuarioRepository.save(usuario);
//                return "redirect:/tecnico/ticketasignado";
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//        } else { //hay al menos 1 error
//            model.addAttribute("listaEmpresa", empresaRepository.findAll());
//            model.addAttribute("listaCargo", cargoRepository.findAll());
//            if (usuario.getId() == null) {
//                return "Tecnico/ticket_asignado";
//            } else {
//                return "Tecnico/perfilEditar";
//            }
//        }
        try {
            Integer idUsuario = usuario.getId();
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(idUsuario);

            if (optionalUsuario.isPresent()) {
                Usuario usuarioDB = optionalUsuario.get();

                if (file.getSize() > 0 && !file.getContentType().startsWith("image/") && !file.isEmpty()) {
                    model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
                    return "Tecnico/perfilEditar";
                }
                String fileName1 = file.getOriginalFilename();
                if (fileName1.contains("..") && !file.isEmpty()) {
                    model.addAttribute("msgImagen", "No se permiten '..' en el archivo ");
                    return "Tecnico/perfilEditar";
                }

                int maxFileSize = 10485760;
                if (file.getSize() > maxFileSize && !file.isEmpty()) {
                    System.out.println(file.getSize());
                    model.addAttribute("msgImagen1", "El archivo subido excede el tamaño máximo permitido (10MB).");
                    return "redirect:/tecnico/perfilEditar";
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
                        return "redirect:/tecnico/perfil";
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } else { //hay al menos 1 error
                    return "Tecnico/perfilEditar";
                }
            }else {
                return "redirect:/tecnico/";
            }
        } catch (NumberFormatException e) {
            return "redirect:/Tecnico/listaUsuario";
        }
    }

    @GetMapping({"/perfilEditar"})
    public String perfilEditar(Model model,
                               @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession){
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        model.addAttribute("usuario", u);
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
    public String pagcomentarios(Model model, @RequestParam("id") String idStr) {

        try {
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/tecnico/verticket";
            }
            Optional<Ticket> ticketOptional = ticketRepository.findById(id);
            Optional<Formulario> optionalFormulario = formularioRepository.findById(id);
            List<Comentario> listaComentarios = comentarioRepository.listarComentarios(id);
            if (ticketOptional.isPresent() && optionalFormulario.isPresent()) {
                Ticket ticket = ticketOptional.get();
                Formulario formulario = optionalFormulario.get();
                model.addAttribute("formulario", formulario);
                model.addAttribute("ticket", ticket);
                model.addAttribute("listComentarios", listaComentarios);
                return "Tecnico/comentarios";
            } else {
                return "redirect:/tecnico/verticket";
            }
        } catch (NumberFormatException e) {
            return "redirect:/tecnico/verticket";
        }

    }

    @PostMapping("/subircomentarios")
    public String subirComentario(@RequestParam("id") int id,@RequestParam("idTicket") String idTicketStr,
                                  @RequestParam("comentario") String comentario, RedirectAttributes redirectAttributes){
        try{
            int idTicket = Integer.parseInt(idTicketStr);
            if(idTicket <= 0 || !ticketRepository.existsById(idTicket)){
                return "redirect:/tecnico/ticketasignado";
            }
            Optional<Ticket> optionalTicket = ticketRepository.findById(idTicket);
            if(optionalTicket.isPresent()){
                ZoneId zonaHoraria = ZoneId.of("GMT-5");
                LocalDate fechaActual = LocalDate.now(zonaHoraria); // Obtener la fecha actual en la zona horaria GMT-5
                LocalTime horaActual = LocalTime.now(zonaHoraria);
                comentarioRepository.ingresarComentario1(id,idTicket,comentario,fechaActual,horaActual);
                redirectAttributes.addFlashAttribute("error","Comentario Añadido");
                return "redirect:/tecnico/comentarios?id="+idTicketStr;
            }else{
                return "redirect:/tecnico/ticketasignado";
            }
        }catch (NumberFormatException e){
            return "redirect:/tecnico/ticketasignado";
        }
    }

    //-----------------------------------------------------------------------
    @GetMapping("/dashboard")
    public String pagdashboard(Model model,@ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,HttpSession httpSession) {

        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        model.addAttribute("usuario", u);

        //Ticket u = (Ticket) httpSession.getAttribute("ticket");
        //Integer idTickets = u.getIdTickets();
        int ticketsnuevos = ticketRepository.cantidadTicketsnuevos();
        model.addAttribute("totalTicketsNuevos", ticketsnuevos);

        int totalEmpresas = ticketRepository.cantidadEmpresas();
        model.addAttribute("totalempresas", totalEmpresas);

        int totalCuadrillas = ticketRepository.cantidadCuadrilla();
        model.addAttribute("totalCuadrillas", totalCuadrillas);

        List<Ticket> listaT1 = ticketRepository.listaTicketsAsignado();
        model.addAttribute("listaTicket1", listaT1);

        return "Tecnico/dashboard";

    }


    //YA ESTA CRUD LISTAR
    @GetMapping("/ticketasignado")
    public String Tickets(Model model,
                          RedirectAttributes attr,HttpSession httpSession){
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);

                List<Ticket> ticketAsignados = ticketRepository.listaTicketsAsignado();
                model.addAttribute("ticketAsignados",ticketAsignados);

                    return "Tecnico/ticket_asignado";

    }

    //-----------------PROBANDO HISTORIAL

    @GetMapping("/historialticket")
    public String historialTicket(Model model,
                          RedirectAttributes attr,HttpSession httpSession){
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);

        List<Ticket> ticketAsignados = ticketRepository.listaTicketsAsignado();
        model.addAttribute("ticketAsignados",ticketAsignados);

        return "Tecnico/historial_ticket";

    }

    //--------------------------------------------------
    @GetMapping("/historialticket2")
    public String historialTicket2(Model model,
                                  RedirectAttributes attr,HttpSession httpSession){
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);

        List<Ticket> ticketAsignados = ticketRepository.listaTicketsAsignado();
        model.addAttribute("ticketAsignados",ticketAsignados);

        return "Tecnico/historial_ticket2";

    }

    //-----------------------------------------------------------------------
    @GetMapping({"/verTicket", "/verticket"})
    public String pagdatostick(Model model, @RequestParam("id") String idStr,
                               RedirectAttributes attr, HttpSession httpSession) {
        /* Lo que se mando al Header */

        List<Ticket> listaT = ticketRepository.listarEstado();
        model.addAttribute("listaTicket", listaT);

        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        Integer idTecnico = u.getId();

        try {
            int id = Integer.parseInt(idStr);
            if (idTecnico <= 0 || !ticketRepository.existsById(idTecnico)) {
                return "redirect:/tecnico/ticketasignado";
            }
            Optional<Ticket> optionalTicket1 = ticketRepository.findById(idTecnico);
            Optional<Formulario> optionalFormulario = formularioRepository.findById(idTecnico);
            if (optionalTicket1.isPresent() && optionalFormulario.isPresent()) {
                Ticket ticket = optionalTicket1.get();
                Formulario formulario = optionalFormulario.get();
                List<Ticket> ticketAsignados = ticketRepository.listaTicketsAsignado();

                boolean encontrado = false;
                for (Ticket ticket1 : ticketAsignados) {
                    System.out.println(ticket1.getIdTickets());
                    if (ticket1.getIdTickets() == id) {
                        encontrado = true;
                        break;
                    }
                }
                if (encontrado) {
                    //Usuario usuario2 = optionalUsuario.get();
                    model.addAttribute("ticket", ticket);
                    model.addAttribute("formulario", formulario);
                    model.addAttribute("listaTicket", ticketRepository.listarEstado());

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

                    return "Tecnico/datos_ticket";
                } else {
                    return "redirect:/tecnico/ticketasignado";
                }

            } else {
                return "redirect:/tecnico/ticketasignado";
            }
        } catch (NumberFormatException e) {
            return "redirect:/tecnico/ticketasignado";
        }
    }

    //------------------- DATOS DE PROGRESO---------------------------------//
    @GetMapping({"/verTicketProgreso", "/verticketprogreso"})
    public String pagdatostickProgreso(Model model, @RequestParam("id") String idStr,
                                       RedirectAttributes attr) {
        List<Ticket> listaT1 = ticketRepository.listarEstado();
        model.addAttribute("listaTicket", listaT1);
        try {
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/tecnico/ticketasignado";
            }
            Optional<Ticket> optionalTicket1 = ticketRepository.findById(id);
            Optional<Formulario> optionalFormulario = formularioRepository.findById(id);
            if (optionalTicket1.isPresent() && optionalFormulario.isPresent()) {
                Ticket ticket = optionalTicket1.get();
                Formulario formulario = optionalFormulario.get();
                model.addAttribute("ticket", ticket);
                model.addAttribute("formulario", formulario);
                model.addAttribute("listaTicket", ticketRepository.listarEstado());

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

                return "Tecnico/datost_progreso";
            } else {
                return "redirect:/tecnico/ticketasignado";
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
                return "redirect:/tecnico/ticketasignado";
            }
            Optional<Ticket> optionalTicket1 = ticketRepository.findById(id);
            Optional<Formulario> optionalFormulario = formularioRepository.findById(id);
            if (optionalTicket1.isPresent() && optionalFormulario.isPresent()) {
                Ticket ticket = optionalTicket1.get();
                Formulario formulario = optionalFormulario.get();
                model.addAttribute("ticket", ticket);
                model.addAttribute("formulario", formulario);
                model.addAttribute("listaTicket", ticketRepository.listarEstado());

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

                return "Tecnico/datost_nuevo";
            } else {
                return "redirect:/tecnico/ticketasignado";
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
            System.out.println("Optional Ticket 1: " + optionalTicket1.isPresent());

            Optional<Formulario> optionalFormulario1 = formularioRepository.findById(id);
            System.out.println("Optional Formulario 1: " + optionalFormulario1.isPresent());

            /* Obtenemos el objeto ticket para obtener el idSitioCerrado */
            Ticket ticketPrevio = optionalTicket1.get();
            Integer idSitioCerradoTicket = ticketPrevio.getIdsitioCerrado();

            Optional<SitioCerrado> optionalSitioCerrado = sitioCerradoRepository.findById(idSitioCerradoTicket);
            System.out.println("Optional Sitio 1: " + optionalSitioCerrado.isPresent());

            if (optionalTicket1.isPresent() && optionalFormulario1.isPresent() && optionalSitioCerrado.isPresent()) {
                Ticket ticket = optionalTicket1.get();
                Formulario formulario = optionalFormulario1.get();
                SitioCerrado sitioCerrado = optionalSitioCerrado.get();
                model.addAttribute("ticket", ticket);
                model.addAttribute("formulario", formulario);
                model.addAttribute("sitioCerrado", sitioCerrado);
                model.addAttribute("listaTicket", ticketRepository.listarEstado());

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

                return "Tecnico/datost_cerrado";
            } else {
                return "redirect:/tecnico/ticketasignado";
            }
        } catch (NumberFormatException e) {
            return "redirect:/tecnico/ticketasignado";
        }
    }

    @PostMapping("/guardarEstado")
    public String guardarEstado(Ticket ticket,
                                @RequestParam("cambioEstado") int estado,
                                RedirectAttributes redirectAttributes, HttpSession httpSession) {
        
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        Integer idTecnico = u.getId();
        try {
            if (estado==6) {
                Date fechaCambioEstado = new Date();
                ticketRepository.guardarEstado(ticket.getIdTickets(),estado);
                historialTicketRepository.crearHistorial(5, fechaCambioEstado, ticket.getIdTickets(), idTecnico, "Pasando a Supervisor");
                redirectAttributes.addFlashAttribute("yum", "El ticket ha sido cerrado correctamente");
                return "redirect:/tecnico/ticketasignado";
            } else {
                return "redirect:/tecnico/verTicketProgreso?id=" + ticket.getIdTickets();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    //-----------------------------------------------------------------------
    /*
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
    */

    //-----------------------------------------------------------------------


    @GetMapping("/desplazamiento")
    public String pagdesplazamiento(Model model, @RequestParam("id") int id,
                                    RedirectAttributes attr) {
        List<Ticket> lista = ticketRepository.listarEstado();
        model.addAttribute("ticketList", lista);
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();
            model.addAttribute("ticket", ticket);
            model.addAttribute("listaTicket", ticketRepository.findAll());
            return "Tecnico/desplazamiento";
        } else {
            return "redirect:/ticket/verTicket";
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
        Optional<Sitio> optionalSitio = sitioRepository.findById(id);
        if (optionalTicket.isPresent() && optionalSitio.isPresent()) {
            Ticket ticket = optionalTicket.get();
            Sitio sitio1 = optionalSitio.get();
            model.addAttribute("sitio",sitio1);
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
                                @ModelAttribute("formulario1") @Valid Formulario formulario1, BindingResult bindingResult,
                                 HttpSession httpSession) {

        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        List<Formulario> formularioList = formularioRepository.findAll();
        model.addAttribute("formularioList",formularioList);
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        int id = u.getId();
        try{
            //int id = Integer.parseInt(idStr);
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/tecnico/formulario";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            Optional<Formulario> optionalFormulario = formularioRepository.findById(id);
            if (optionalFormulario.isPresent()) {
                formulario1 = optionalFormulario.get();
                model.addAttribute("formulario1", formulario1);
                model.addAttribute("listaFormulario", formularioRepository.findAll());
                return "Tecnico/formulario1";
            } else {
                return "redirect:/tecnico/formulario";
            }
        } catch (NumberFormatException e) {
            return "redirect:/tecnico/formulario";
        }

    }

    /* De Formulario */
    @PostMapping("/saveFormulario1")
    public String saveFormulario1(@RequestParam("imagenSubida") MultipartFile file,@RequestParam("imagenSubida2") MultipartFile file2,
                                  @ModelAttribute("formulario1") @Valid Formulario formulario1, BindingResult bindingResult,
                                  Model model, RedirectAttributes attr, HttpSession httpSession) {

        List<Ticket> ticketList = ticketRepository.findAll();
        model.addAttribute("listaTicket", ticketList);

        if (file.getSize() > 0 && !file.getContentType().startsWith("image/") && !file.isEmpty()) {
            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
            if (formulario1.getIdFormularios() == null) {
                return "Tecnico/formulario";
            } else {
                return "Tecnico/formulario1";
            }
        }
        if (file2.getSize() > 0 && !file2.getContentType().startsWith("image/") && !file2.isEmpty()) {
            model.addAttribute("msgImagen1", "El archivo subido no es una imagen válida");
            if (formulario1.getIdFormularios() == null) {
                return "Tecnico/formulario";
            } else {
                return "Tecnico/formulario1";
            }
        }

        String fileName1 = file.getOriginalFilename();
        if (fileName1.contains("..") && !file.isEmpty()) {
            model.addAttribute("msgImagen", "No se permiten '..' en el archivo ");
            if (formulario1.getIdFormularios() == null) {
                return "Tecnico/formulario";
            } else {
                return "Tecnico/formulario1";
            }
        }
        String fileName2 = file2.getOriginalFilename();
        if (fileName2.contains("..") && !file2.isEmpty()) {
            model.addAttribute("msgImagen1", "No se permiten '..' en el archivo ");
            if (formulario1.getIdFormularios() == null) {
                return "Tecnico/formulario";
            } else {
                return "Tecnico/formulario1";
            }
        }

        int maxFileSize = 10485760;
        if (file.getSize() > maxFileSize && !file.isEmpty()) {
            System.out.println(file.getSize());
            model.addAttribute("msgImagen", "El archivo subido excede el tamaño máximo permitido (10MB).");
            if (formulario1.getIdFormularios() == null) {
                return "Tecnico/formulario";
            } else {
                return "redirect:/tecnico/formulario1";
            }
        }
        if (file2.getSize() > maxFileSize && !file2.isEmpty()) {
            System.out.println(file2.getSize());
            model.addAttribute("msgImagen1", "El archivo subido excede el tamaño máximo permitido (10MB).");
            if (formulario1.getIdFormularios() == null) {
                return "Tecnico/formulario";
            } else {
                return "redirect:/tecnico/formulario1";
            }
        }


        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
            if (formulario1.getArchivo() == null) {
                formulario1.setArchivo(new Archivo());
            }

            try{
                /*Si file contiene algo --> Guardarlo*/
                if(!file.isEmpty() && !file2.isEmpty()){
                    // Obtenemos el nombre del archivo
                    String fileName = file.getOriginalFilename();
                    String extension1 = "";
                    int i = fileName.lastIndexOf('.');
                    if (i > 0) {
                        extension1 = fileName.substring(i+1);
                    }

                    Archivo archivo = formulario1.getArchivo();
                    archivo.setNombre(fileName);
                    archivo.setTipo(1);
                    archivo.setArchivo(file.getBytes());
                    archivo.setContentType(file.getContentType());
                    Archivo archivo1 = archivoRepository.save(archivo);
                    Archivo archivo2 = archivoRepository.save(archivo);
                    String nombreArchivo = "archivo-"+archivo1.getIdArchivos()+"."+extension1;
                    archivo1.setNombre(nombreArchivo);
                    archivoRepository.save(archivo1);
                    archivoRepository.save(archivo2);
                    uploadObject(archivo1);
                    uploadObject(archivo2);
                }
                if (formulario1.getIdFormularios() == null) {
                    attr.addFlashAttribute("msg", "El formulario 1 se ha guardado exitosamente");
                } else {
                    attr.addFlashAttribute("msg", "El formulario 1 se ha guardado exitosamente");
                }
                formularioRepository.save(formulario1);
                return "redirect:/tecnico/formulario";

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            if (formulario1.getIdFormularios() == null) {
                return "Tecnico/formulario";
            } else {
                return "Tecnico/formulario1";
            }
        }
    }


    //------FORMULARIO2 ----
    @GetMapping({"/formulario2", "formulario2"})
    public String pagformulario2(Model model, @RequestParam("id") String idStr,
                                @ModelAttribute("formulario2") @Valid Formulario formulario2, BindingResult bindingResult
                                ,HttpSession httpSession
    ) {

        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        List<Formulario> formularioList = formularioRepository.findAll();
        model.addAttribute("formularioList",formularioList);
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        int id = u.getId();
        try{
            //int id = Integer.parseInt(idStr);
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/tecnico/formulario";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            Optional<Formulario> optionalFormulario = formularioRepository.findById(id);
            Optional<Sitio> sitioOptional = sitioRepository.findById(id);
            if (optionalFormulario.isPresent() && sitioOptional.isPresent()) {
                formulario2 = optionalFormulario.get();
                Sitio sitio = sitioOptional.get();
                model.addAttribute("sitio", sitio);
                model.addAttribute("formulario2", formulario2);
                model.addAttribute("listaFormulario", formularioRepository.findAll());
                return "Tecnico/formulario2";
            } else {
                return "redirect:/tecnico/formulario";
            }
        } catch (NumberFormatException e) {
            return "redirect:/tecnico/formulario";
        }

    }

    /* Guardar Datos*/
    @PostMapping("/saveFormulario2")
    public String saveFormulario2(@RequestParam("imagenSubida") MultipartFile file,
                                 @ModelAttribute("formulario2") @Valid Formulario formulario2, BindingResult bindingResult,
                                 Model model, RedirectAttributes attr, HttpSession httpSession) {

        List<Ticket> ticketList = ticketRepository.findAll();
        model.addAttribute("listaTicket", ticketList);

        if (file.getSize() > 0 && !file.getContentType().startsWith("image/") && !file.isEmpty()) {
            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
            if (formulario2.getIdFormularios() == null) {
                return "Tecnico/formulario";
            } else {
                return "Tecnico/formulario2";
            }
        }

        String fileName1 = file.getOriginalFilename();
        if (fileName1.contains("..") && !file.isEmpty()) {
            model.addAttribute("msgImagen", "No se permiten '..' en el archivo ");
            if (formulario2.getIdFormularios() == null) {
                return "Tecnico/formulario";
            } else {
                return "Tecnico/formulario2";
            }
        }

        int maxFileSize = 10485760;
        if (file.getSize() > maxFileSize && !file.isEmpty()) {
            System.out.println(file.getSize());
            model.addAttribute("msgImagen", "El archivo subido excede el tamaño máximo permitido (10MB).");
            if (formulario2.getIdFormularios() == null) {
                return "Tecnico/formulario";
            } else {
                return "redirect:/tecnico/formulario2";
            }
        }
        if (file.getSize() > maxFileSize && !file.isEmpty()) {
            System.out.println(file.getSize());
            model.addAttribute("msgImagen1", "El archivo subido excede el tamaño máximo permitido (10MB).");
            if (formulario2.getIdFormularios() == null) {
                return "Tecnico/formulario";
            } else {
                return "redirect:/tecnico/formulario2";
            }
        }

        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
            if (formulario2.getArchivo() == null) {
                formulario2.setArchivo(new Archivo());
            }

            try{
                /*Si file contiene algo --> Guardarlo*/
                if(!file.isEmpty()){
                    // Obtenemos el nombre del archivo
                    String fileName = file.getOriginalFilename();
                    String extension1 = "";
                    int i = fileName.lastIndexOf('.');
                    if (i > 0) {
                        extension1 = fileName.substring(i+1);
                    }

                    Archivo archivo = formulario2.getArchivo();
                    archivo.setNombre(fileName);
                    archivo.setTipo(1);
                    archivo.setArchivo(file.getBytes());
                    archivo.setContentType(file.getContentType());
                    Archivo archivo1 = archivoRepository.save(archivo);
                    String nombreArchivo = "archivo-"+archivo1.getIdArchivos()+"."+extension1;
                    archivo1.setNombre(nombreArchivo);
                    archivoRepository.save(archivo1);
                    uploadObject(archivo1);
                }
                if (formulario2.getIdFormularios() == null) {
                    attr.addFlashAttribute("msg", "El formulario 2 se ha guardado exitosamente");
                } else {
                    attr.addFlashAttribute("msg", "El formulario 2 se ha guardado exitosamente");
                }
                formularioRepository.save(formulario2);
                return "redirect:/tecnico/formulario";

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            if (formulario2.getIdFormularios() == null) {
                return "Tecnico/formulario";
            } else {
                return "Tecnico/formulario2";
            }
        }

    }


    //------FORMULARIO3 ----

    @GetMapping({"/formulario3", "formulario3"})
    public String pagformulario3(Model model, @RequestParam("id") String idStr,
                                @ModelAttribute("formulario3") @Valid Formulario formulario3, BindingResult bindingResult,HttpSession httpSession) {

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
                model.addAttribute("formulario3", formulario3);
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
    public String saveFormulario3(@RequestParam("imagenSubida") MultipartFile file,
                                 @ModelAttribute("formulario3") @Valid Formulario formulario3, BindingResult bindingResult,
                                 Model model, RedirectAttributes attr, HttpSession httpSession) {

        List<Ticket> ticketList = ticketRepository.findAll();
        model.addAttribute("listaTicket", ticketList);

        if (file.getSize() > 0 && !file.getContentType().startsWith("image/") && !file.isEmpty()) {
            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
            if (formulario3.getIdFormularios() == null) {
                return "Tecnico/formulario";
            } else {
                return "Tecnico/formulario3";
            }
        }

        String fileName1 = file.getOriginalFilename();
        if (fileName1.contains("..") && !file.isEmpty()) {
            model.addAttribute("msgImagen", "No se permiten '..' en el archivo ");
            if (formulario3.getIdFormularios() == null) {
                return "Tecnico/formulario";
            } else {
                return "Tecnico/formulario3";
            }
        }

        int maxFileSize = 10485760;
        if (file.getSize() > maxFileSize && !file.isEmpty()) {
            System.out.println(file.getSize());
            model.addAttribute("msgImagen", "El archivo subido excede el tamaño máximo permitido (10MB).");
            if (formulario3.getIdFormularios() == null) {
                return "Tecnico/formulario";
            } else {
                return "redirect:/tecnico/formulario3";
            }
        }

        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
            if (formulario3.getArchivo() == null) {
                formulario3.setArchivo(new Archivo());
            }

            try{
                /*Si file contiene algo --> Guardarlo*/
                if(!file.isEmpty()){
                    // Obtenemos el nombre del archivo
                    String fileName = file.getOriginalFilename();
                    String extension1 = "";
                    int i = fileName.lastIndexOf('.');
                    if (i > 0) {
                        extension1 = fileName.substring(i+1);
                    }

                    Archivo archivo = formulario3.getArchivo();
                    archivo.setNombre(fileName);
                    archivo.setTipo(1);
                    archivo.setArchivo(file.getBytes());
                    archivo.setContentType(file.getContentType());
                    Archivo archivo1 = archivoRepository.save(archivo);
                    String nombreArchivo = "archivo-"+archivo1.getIdArchivos()+"."+extension1;
                    archivo1.setNombre(nombreArchivo);
                    archivoRepository.save(archivo1);
                    uploadObject(archivo1);
                }
                if (formulario3.getIdFormularios() == null) {
                    attr.addFlashAttribute("msg", "El formulario 2 se ha guardado exitosamente");
                } else {
                    attr.addFlashAttribute("msg", "El formulario 2 se ha guardado exitosamente");
                }
                formularioRepository.save(formulario3);
                return "redirect:/tecnico/formulario";

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            if (formulario3.getIdFormularios() == null) {
                return "Tecnico/formulario";
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
    public String formularioCerrado(Model model, @RequestParam("id") String idStr,
                                    @ModelAttribute("formulario") @Valid Formulario formulario, BindingResult bindingResult) {
        List<Ticket> listaT = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);

        try {
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !formularioRepository.existsById(id)) {
                return "redirect:/tecnico/datostickets";
            }
            Optional<Formulario> optionalFormulario = formularioRepository.findById(id);
            Optional<Sitio> sitioOptional = sitioRepository.findById(id);
            if (optionalFormulario.isPresent() && sitioOptional.isPresent()) {
                formulario = optionalFormulario.get();
                Sitio sitio = sitioOptional.get();
                model.addAttribute("formulario", formulario);
                model.addAttribute("sitio", sitio);
                model.addAttribute("idTick", formularioRepository.obtenerid(id));
                return "Tecnico/formularioCerrado";
            } else {
                return "redirect:/tecnico/datostickets";
            }
        } catch (NumberFormatException e) {
            return "redirect:/tecnico/datostickets";
        }
    }

}
