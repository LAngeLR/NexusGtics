package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.*;
import com.example.nexusgtics.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = {"/analistaOYM"})
public class AnalistaOYMController {
    final TicketRepository ticketRepository;
    final SitioRepository sitioRepository;
    final UsuarioRepository usuarioRepository;
    final EquipoRepository equipoRepository;
    final EmpresaRepository empresaRepository;
    final ArchivoRepository archivoRepository;
    final SitiosHasEquiposRepository sitiosHasEquiposRepository;



    public AnalistaOYMController(SitioRepository sitioRepository, TicketRepository ticketRepository, EquipoRepository equipoRepository, EmpresaRepository empresaRepository, EmpresaRepository empresaRepository1, UsuarioRepository usuarioRepository, ArchivoRepository archivoRepository, SitiosHasEquiposRepository sitiosHasEquiposRepository){
        this.sitioRepository = sitioRepository;
        this.ticketRepository = ticketRepository;
        this.equipoRepository = equipoRepository;
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.archivoRepository = archivoRepository;
        this.sitiosHasEquiposRepository = sitiosHasEquiposRepository;
    }

    @GetMapping(value = {"/",""})
    public String paginaPrincipal(){
        return "AnalistaOYM/analistaOYM";
    }

    /* -------------------------- PERFIL -------------------------- */
    @GetMapping({"/perfil","perfilAdmin","perfiladmin"})
    public String perfilAdmin(){
        return "Administrador/perfilAdmin";
    }

    @GetMapping({"/perfilEditar"})
    public String perfilEditar(Model model, @RequestParam("id") String idStr,
                               @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult){
        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/admin/listaUsuario";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                usuario = optionalUsuario.get();    //modifiqué Usuario usuario para poder usar @ModelAttribute
                model.addAttribute("usuario", usuario);
                return "Administrador/perfilEditar";
            } else {
                return "redirect:/admin";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/listaUsuario";
        }

    }

    @GetMapping({"/perfilContra"})
    public String perfilContra(){
        return "Administrador/perfilContra";
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
    @GetMapping("/perfil")
    public String perfilAD(){
        return "AnalistaOYM/perfilAnalistaOYM";
    }
    @GetMapping("/mapaSitios")
    public String mapaSitios(){
        return "AnalistaOYM/oymMapaSitios";
    }
    @GetMapping("/mapaTickets")
    public String mapaTickets(){
        return "AnalistaOYM/oymMapaTickets";
    }

    @GetMapping("/ticket")
    public String listaTicket(Model model){
        //'listar'
        List<Ticket> listaTicket = ticketRepository.findAll();
        model.addAttribute("listaTicket", listaTicket);
        return "AnalistaOYM/oymListaTickets";
    }

    /* DIRECCIONAR A FORMULARIO*/
    @GetMapping("/crearTicket")
    public String crearTicket(Model model) {
        model.addAttribute("listaEmpresa", empresaRepository.findAll());
        model.addAttribute("listaSitios", sitioRepository.findAll());

        return "AnalistaOYM/oymCrearTicket";
    }

    /*CREAR NUEVO TICKET*/
    @PostMapping("/saveTicket")
    public String saveTicket(RedirectAttributes attr,
                              @ModelAttribute("ticket") @Valid Ticket ticket,
                              BindingResult bindingResult,
                             Model model){
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
    public String verticket(Model model, @RequestParam("id") int id){
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if(optionalTicket.isPresent()){
            Ticket ticket = optionalTicket.get();
            model.addAttribute("ticket", ticket);
            return "AnalistaOYM/oymVistaTicket";
        }else{
            return "redirect:/analistaOYM/ticket";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "AnalistaOYM/oymDashboard";
    }


    @GetMapping("/comentarios")
    public String comentarios(){
        return "AnalistaOYM/oymComentarios";
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
