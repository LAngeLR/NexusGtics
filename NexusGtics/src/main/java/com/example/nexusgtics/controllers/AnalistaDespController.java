package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.Archivo;
import com.example.nexusgtics.entity.Equipo;
import com.example.nexusgtics.entity.Sitio;
import com.example.nexusgtics.entity.Ticket;
import com.example.nexusgtics.repository.ArchivoRepository;
import com.example.nexusgtics.repository.EquipoRepository;
import com.example.nexusgtics.repository.SitioRepository;
import com.example.nexusgtics.repository.TicketRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/analistaDespliegue")
public class AnalistaDespController {
    final TicketRepository ticketRepository;
    final SitioRepository sitioRepository;

    final EquipoRepository equipoRepository;
    final ArchivoRepository archivoRepository;

    public AnalistaDespController(TicketRepository ticketRepository, SitioRepository sitioRepository, EquipoRepository equipoRepository, ArchivoRepository archivoRepository){
        this.ticketRepository = ticketRepository;
        this.sitioRepository = sitioRepository;
        this.equipoRepository = equipoRepository;

        this.archivoRepository = archivoRepository;
    }


    @GetMapping(value = {"/", ""})
    public String paginaPrincipal(){
        return "AnalistaDespliegue/analistaDespliegue";
    }

    @GetMapping("/listaSitio")
    public String listaSitio(Model model){
        List<Sitio>  listaSitio = sitioRepository.findAll();
        model.addAttribute("listaSitio",listaSitio);
        return "AnalistaDespliegue/despliegueListaSitio";
    }

    @GetMapping("/editarSitio")
    public String editarSitio(Model model, @RequestParam("id") String idStr){
        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !sitioRepository.existsById(id)) {
                return "redirect:/analistaDespliegue/listaSitio";
            }
            Optional<Sitio> optSitio = sitioRepository.findById(id);
            if(optSitio.isPresent()){
                Sitio sitio = optSitio.get();

                // Obtén la lista de equipos por sitio
                List<Equipo> listaEquipos = equipoRepository.listaEquiposPorSitio(id);

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
    public String verSitio(Model model, @RequestParam("id") String idStr){
        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !sitioRepository.existsById(id)) {
                return "redirect:/analistaDespliegue/listaSitio";
            }
            Optional<Sitio> optSitio = sitioRepository.findById(id);
            if(optSitio.isPresent()){
                Sitio sitio = optSitio.get();
                model.addAttribute("sitio", sitio);
                return "AnalistaDespliegue/despliegueVistaSitio";
            }else {
                return "redirect:/analistaDespliegue/listaSitio";
            }
        } catch (NumberFormatException e) {
            return "redirect:/analistaDespliegue/listaSitio";
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
            return "redirect:/analistaDespliegue/listaSitio";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/agregarEquipo")
    public String agregarEquipo(@RequestParam("idEquipos") int idEquipos) {
        Optional<Equipo> optionalEquipo = equipoRepository.findById(idEquipos);

        if (optionalEquipo.isPresent()) {
            equipoRepository.agregarEquipo(idEquipos);

            return "redirect:/analistaDespliegue/listaSitio";
        } else {
            return "redirect:/analistaDespliegue/listaSitio";
        }
    }


    @GetMapping("/listaEquipo")
        public String listaEquipo(Model model, @RequestParam("id") int id){
        List<Equipo> listaEquipo = equipoRepository.listaEquiposNoSitio(id);
        model.addAttribute("listaEquipo",listaEquipo);
        return "AnalistaDespliegue/despliegueListaEquipos";
    }
    @GetMapping("/verEquipo")
    public String verEquipo(Model model, @RequestParam("id") String idStr){
        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !equipoRepository.existsById(id)) {
                return "redirect:/analistaDespliegue/listaSitio";
            }
        Optional<Equipo> optionalEquipo = equipoRepository.findById(id);
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

    @GetMapping("/perfil")
    public String perfilAD(){
        return "AnalistaDespliegue/perfilAnalistaDespliegue";
    }
    @GetMapping("/mapaSitios")
    public String mapaSitios(){
        return "AnalistaDespliegue/despliegueMapaSitios";
    }
    @GetMapping("/mapaTickets")
    public String mapaTickets(){
        return "AnalistaDespliegue/despliegueMapaTickets";
    }

    @GetMapping("/ticket")
    public String listaTicket(Model model){
        //'listar'
        List<Ticket> lista = ticketRepository.findAll();
        model.addAttribute("listaTicket", lista);
        return "AnalistaDespliegue/despliegueListaTickets";
    }


    @GetMapping("/crearTicket")
    public String crearTicket(){
        return "AnalistaDespliegue/despliegueCrearTicket";
    }

    @GetMapping("/editarTicket")
    public String editarTicket(){
        return "AnalistaDespliegue/despliegueEditarTicket";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "AnalistaDespliegue/despliegueDashboard";
    }

    @GetMapping("/verTicket")
    public String verticket(){
        return "AnalistaDespliegue/despliegueVistaTicket";
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


