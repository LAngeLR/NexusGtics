package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.*;
import com.example.nexusgtics.repository.*;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.Instant;

import java.util.*;

@Controller
@RequestMapping("/supervisor")
public class SupervisorController {

    private final CuadrillaRepository cuadrillaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TicketRepository ticketRepository;
    private final SitioRepository sitioRepository;
    private final FormularioRepository formularioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ArchivoRepository archivoRepository;
    final CargoRepository cargoRepository;
    final EmpresaRepository empresaRepository;


    public SupervisorController(CuadrillaRepository cuadrillaRepository,
                                UsuarioRepository usuarioRepository,
                                TicketRepository ticketRepository,
                                SitioRepository sitioRepository,
                                FormularioRepository formularioRepository,
                                PasswordEncoder passwordEncoder,
                                EmpresaRepository empresaRepository,
                                CargoRepository cargoRepository,
                                ArchivoRepository archivoRepository) {
        this.cuadrillaRepository = cuadrillaRepository;
        this.usuarioRepository = usuarioRepository;
        this.ticketRepository = ticketRepository;
        this.sitioRepository = sitioRepository;
        this.formularioRepository = formularioRepository;
        this.passwordEncoder = passwordEncoder;
        this.cargoRepository = cargoRepository;
        this.empresaRepository = empresaRepository;
        this.archivoRepository = archivoRepository;
    }

    @GetMapping( {"/",""})
    public String paginaPrincipal(){
        return "Supervisor/menuSupervisor";
    }

    @GetMapping("/listaTickets")
    public String Tickets(Model model){

        List<Ticket> listaTickets = ticketRepository.listaTicketsModificado( 1);

        model.addAttribute("listaTickets",listaTickets);

        return "Supervisor/listaTickets";
    }

    @GetMapping("/listaCuadrillas")
    public String Cuadrillas(Model model){

        List<Cuadrilla> listaCuadrilla = cuadrillaRepository.findAll();

        Map<Integer, Integer> trabajosFinalizadosPorCuadrilla = new HashMap<>();
        Map<Integer, Integer> numeroTecnicosPorCuadrilla = new HashMap<>();


        for (Cuadrilla cuadrilla : listaCuadrilla) {
            int trabajosFinalizados;
            Integer trabajosFinalizadosResult = cuadrillaRepository.contarTrabajosFinalizados(cuadrilla.getIdCuadrillas());

            trabajosFinalizados = (trabajosFinalizadosResult != null) ? trabajosFinalizadosResult : 0;

            trabajosFinalizadosPorCuadrilla.put(cuadrilla.getIdCuadrillas(), trabajosFinalizados);
        }

        for (Cuadrilla cuadrilla1 : listaCuadrilla) {

            int tecnicosCuadrilla = cuadrillaRepository.numeroTecnicosPorCuadrilla(cuadrilla1.getIdCuadrillas());

            numeroTecnicosPorCuadrilla.put(cuadrilla1.getIdCuadrillas(), tecnicosCuadrilla);
        }
        model.addAttribute("listaCuadrilla", listaCuadrilla);
        model.addAttribute("trabajosFinalizadosPorCuadrilla", trabajosFinalizadosPorCuadrilla);
        model.addAttribute("numeroTecnicosPorCuadrilla", numeroTecnicosPorCuadrilla);
        return "Supervisor/listaCuadrillas";
    }


    @GetMapping("/ticketNuevo")
    public String nuevoTicket(Model model, @RequestParam("id") String idStr){
        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/supervisor/listaTickets";
            }
            Optional<Ticket> ticketBuscado = ticketRepository.findById(id);
            List<Usuario> listaSupervisor = usuarioRepository.listaDeSupervisores(5);
            List<Cuadrilla> listaCuadrillas = cuadrillaRepository.findAll();
            if (ticketBuscado.isPresent()) {
                Ticket ticket = ticketBuscado.get();
                model.addAttribute("ticket", ticket);
                model.addAttribute("listaSupervisores",listaSupervisor);
                model.addAttribute("listaCuadrillas",listaCuadrillas);

                return "Supervisor/ticketNuevo";
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
                model.addAttribute("tickets", ticket);
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
                model.addAttribute("tickets", ticket);
                return "Supervisor/ticketCerrado";
            } else {
                return "redirect:/supervisor/listaTickets";
            }
        } catch (NumberFormatException e) {
            return "redirect:/supervisor/listaTickets";
        }
    }

    @PostMapping("/actualizarSupervisor")
    public String actualizarSupervisor(Ticket ticket , RedirectAttributes redirectAttributes){
        ticketRepository.actualizarSupervisor(ticket.getIdTickets(),ticket.getIdSupervisorEncargado().getId());
        redirectAttributes.addAttribute("id",ticket.getIdTickets());
        redirectAttributes.addFlashAttribute("mensaje","Supervisor " + ticket.getIdSupervisorEncargado().getNombre()+" asignado");
        return "redirect:/supervisor/ticketNuevo";
    }

    @PostMapping("/actualizarCuadrilla")
    public String actualizarCuadrilla(Ticket ticket, RedirectAttributes redirectAttributes){

        ticketRepository.actualizarCuadrilla(ticket.getIdTickets(),ticket.getIdCuadrilla().getIdCuadrillas());
        ticketRepository.actualizarEstado(ticket.getIdTickets(),3);

        redirectAttributes.addFlashAttribute("abc","Cuadrilla " + ticket.getIdCuadrilla().getIdCuadrillas()+ " asignada");
        return "redirect:/supervisor/listaTickets";
    }

    @PostMapping("/actualizarEstado")
    public String actualizarEstado(@RequestParam("idTickets") int id, @RequestParam("cambioEstado") String cambioEstado, RedirectAttributes redirectAttributes) {

        int estadoUtilizar;
        redirectAttributes.addAttribute("id",id);

        if (cambioEstado.equals("Cerrado")) {
            estadoUtilizar = 7;
            ticketRepository.actualizarEstado(id,estadoUtilizar);
            return "redirect:/supervisor/listaTickets";
        } else{
            return "redirect:/supervisor/ticketCerrado";
        }
    }


    @GetMapping("/comentarios")
    public String comentarioTicket(Model model, @RequestParam("id") int id){

        model.addAttribute("id",id);
        model.addAttribute("estado",ticketRepository.obtenerEstado(id));
        return "Supervisor/comentariosTickets";
    }

    @GetMapping("/formulario")
    public String formulario(Model model, @RequestParam("id") String idStr){
        try {
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !ticketRepository.existsById(id)) {
                return "redirect:/supervisor/ticketCerrado?id="+idStr;
            }
            List<Formulario> formularioOptional = formularioRepository.formulariosSD(id);
            if (!formularioOptional.isEmpty()) {
                Formulario formulario = formularioOptional.get(0);
                model.addAttribute("formulario", formulario);
                return "Supervisor/formulario";
            } else {
                return "redirect:/supervisor/ticketCerrado?id="+idStr;
            }
        } catch (NumberFormatException e) {
            return "redirect:/supervisor/ticketCerrado?id="+idStr;
        }
    }

    @GetMapping("/perfil")
    public String perfilSupervisor(){
        return "Supervisor/perfilSupervisor";
    }

    @GetMapping("/mapaTickets")
    public String mapaTickets(Model model){

        List<Ticket> listaT= ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        List<Sitio> sitioList = sitioRepository.findAll();
        model.addAttribute("sitioList", sitioList);

        return "Supervisor/mapaTickets";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "Supervisor/dashboardSupervisor";
    }

    @GetMapping("/crearCuadrilla")
    public String crearCuadrilla(Model model,  @RequestParam(name = "id", required = false, defaultValue = "-1") int id){

        if(id == -1){
            model.addAttribute("valor", id);
            int numero= cuadrillaRepository.cantidadCuadrillas();
            model.addAttribute("cantidad",numero+1);
        }
        else{
            model.addAttribute("valor", cuadrillaRepository.numeroTecnicosPorCuadrilla(id));
            model.addAttribute("a", id);
        }
        model.addAttribute("listaTecnicos",usuarioRepository.listaDeSupervisores(6));
        return "Supervisor/crearCuadrilla";
    }

    @PostMapping("/guardarCuadrilla")
    public String guardarCuadrilla(Cuadrilla cuadrilla, RedirectAttributes redirectAttributes) {

        Instant fechaCreacion = Instant.now();
        cuadrilla.setFechaCreacion(fechaCreacion);

        cuadrillaRepository.save(cuadrilla);
        redirectAttributes.addFlashAttribute("msg","Cuadrilla " + cuadrilla.getIdCuadrillas() + " creada Correctamente");
        usuarioRepository.cambiarTecnico(cuadrilla.getTecnico().getId(),cuadrilla.getIdCuadrillas());

        return "redirect:/supervisor/crearCuadrilla?id=" + cuadrilla.getIdCuadrillas();
    }

    @PostMapping("/guardarTecnicos")

    public String guardarTecnicos(
            @RequestParam(name = "tecnicosSeleccionados", required = false) List<Integer> tecnicosSeleccionados, @RequestParam("a") int valor){
        if (tecnicosSeleccionados != null && !tecnicosSeleccionados.isEmpty()) {
            for (Integer userId : tecnicosSeleccionados) {
                Usuario usuario = usuarioRepository.findById(userId).orElse(null);

                if (usuario != null) {
                    usuarioRepository.cambiarTecnico(usuario.getId(), valor);
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
            List<Cuadrilla> listaCuadrilla = cuadrillaRepository.findAll();
            List<Ticket> listaTicketsCerradosPorCuadrilla = ticketRepository.listaTicketsCerradosPorCuadrilla(id);

            Map<Integer, Integer> trabajosFinalizadosPorCuadrilla = new HashMap<>();

            for (Cuadrilla cuadrilla : listaCuadrilla) {
                Integer trabajosFinalizados = cuadrillaRepository.contarTrabajosFinalizados(cuadrilla.getIdCuadrillas()); // Reemplaza "getId()" con el método adecuado para obtener el ID de la cuadrilla
                trabajosFinalizados = (trabajosFinalizados != null) ? trabajosFinalizados : 0;
                trabajosFinalizadosPorCuadrilla.put(cuadrilla.getIdCuadrillas(), trabajosFinalizados);
            }

            if (optShipper.isPresent()) {
                Cuadrilla cuadrilla = optShipper.get();
                model.addAttribute("cuadrilla", cuadrilla);
                model.addAttribute("integrantes", integrantesCuadrilla);
                model.addAttribute("trabajosFinalizadosPorCuadrilla", trabajosFinalizadosPorCuadrilla);
                model.addAttribute("listaTicketsCerradosPorCuadrilla", listaTicketsCerradosPorCuadrilla);
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

    @PostMapping("/actualizarContra")
    public String actualizarContra(Model model, @RequestParam("id") int id, @RequestParam("password") String contrasenia, @RequestParam("newpassword") String contraseniaNueva, @RequestParam("renewpassword") String contraseniaConfirm, RedirectAttributes redirectAttributes){

        String idStr=String.valueOf(id);

        String contraseniaAlmacenada = usuarioRepository.obtenerContraseña(id);

        if (passwordEncoder.matches(contrasenia, contraseniaAlmacenada)) {
            String contraseniaNuevaEncriptada = passwordEncoder.encode(contraseniaNueva);
            usuarioRepository.actualizarContraA(contraseniaNuevaEncriptada, id);
            return "redirect:/supervisor/perfil";
        } else {
            redirectAttributes.addFlashAttribute("error","La contraseña actual no es correcta.");
            return "redirect:/supervisor/perfilContra?id="+idStr;
        }
    }

    @GetMapping("/perfilContra")

    public String perfilContra(Model model, @RequestParam("id") String idStr){

        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/supervisor/perfil";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                model.addAttribute("idUsuario",id);
                return "Supervisor/perfilContra";
            } else {
                return "redirect:/perfil";
            }
        } catch (NumberFormatException e) {
            return "redirect:/supervisor/perfil";
        }
    }

    @GetMapping({"/perfilEditar"})
    public String perfilEditar(Model model, @RequestParam("id") String idStr,
                               @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult){
        try{
            int id = Integer.parseInt(idStr);
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
}