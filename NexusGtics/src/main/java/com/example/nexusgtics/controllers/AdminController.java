package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.Empresa;
import com.example.nexusgtics.entity.Equipo;
import com.example.nexusgtics.entity.Sitio;
import com.example.nexusgtics.entity.Usuario;
import com.example.nexusgtics.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    final EmpresaRepository empresaRepository;
    final SitioRepository sitioRepository;
    final EquipoRepository equipoRepository;
    final TipoEquipoRepository tipoEquipoRepository;
    final UsuarioRepository usuarioRepository;

    public AdminController(EmpresaRepository empresaRepository, SitioRepository sitioRepository, EquipoRepository equipoRepository, TipoEquipoRepository tipoEquipoRepository, UsuarioRepository usuarioRepository) {
        this.empresaRepository = empresaRepository;
        this.sitioRepository = sitioRepository;
        this.equipoRepository = equipoRepository;
        this.tipoEquipoRepository = tipoEquipoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping({"/","","admin","administrador"})
    public String paginaPrincipal(){
        return "Administrador/admin";
    }

    @GetMapping({"/perfil","perfilAdmin","perfiladmin"})
    public String perfilAdmin(){
        return "Administrador/perfilAdmin";
    }

//-----------------------------------------------------------------------

    // GESTION DE USUARIOS
    @GetMapping({"/listaUsuario","listausuario"})
    public String listaUsuario(Model model){
        List<Usuario> listaUsuarioNoAdmin = usuarioRepository.listaDeUsuariosNoAdmin();
        //List<Usuario> listaUsuarioTotal = usuarioRepository.findAll();
        model.addAttribute("listaUsuario", listaUsuarioNoAdmin);
        //model.addAttribute("listaUsuarioTotal", listaUsuarioTotal);
        return "Administrador/listaUsuario";
    }

    @GetMapping("/desabilitarUsuario")
    public String desabilitarUsuario(@RequestParam("id") int id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            usuarioRepository.desactivarUsuario(id);
        }
        return "redirect:Administrador/listaUsuario";
    }

    @GetMapping({"/crearUsuario","crearusuario"})
    public String crearUsuario(){
        return "Administrador/crearUsuario";
    }

    @GetMapping({"/verUsuario","verusuario"})
    public String verUsuario(Model model, @RequestParam("id") int id){
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);

        if(optUsuario.isPresent()){
            Usuario usuario = optUsuario.get();
            model.addAttribute("usuario", usuario);
            return "Administrador/vistaUsuario";
        } else {
            return "redirect:/Administrador/listaUsuario";
        }
    }


    @GetMapping({"/editarUsuario","editarusuario"})
    public String editarUsuario(){
        return "Administrador/editarUsuario";
    }


    // CRUD DE USUARIOS

    @PostMapping("/guardarUsuario")
    public String guardarNuevoUsuario(Usuario usuario){

        usuarioRepository.save(usuario);

        return "redirect:Administrador/listaSitio";
    }


//-----------------------------------------------------------------------

    // GESTION DE SITIOS
    @GetMapping({"/listaSitio","/listasitio"})
    public String listaSitio(Model model){
        List<Sitio> listaSitio = sitioRepository.findAll();
        model.addAttribute("listaSitio", listaSitio);
        return "Administrador/listaSitio";
    }

    @GetMapping({"/crearSitio","/crearsitio"})
    public String crearSitio(){
        return "Administrador/crearSitio";
    }

    @GetMapping({"/verSitio","/versitio"})
    public String verSitio(Model model, @RequestParam("id") int id){
        Optional<Sitio> optionalSitio = sitioRepository.findById(id);
        if (optionalSitio.isPresent()){
            Sitio sitio = optionalSitio.get();
            model.addAttribute("sitio", sitio);
            return "Administrador/vistaSitio";
        }else {
            return "Administrador/listaSitio";
        }
    }

    @GetMapping({"/editarSitio","/editarsitio"})
    public String editarSitio(){
        return "Administrador/editarSitio";
    }

    @GetMapping({"/ubicarSitio","/ubicarsitio"})
    public String ubicarSitio(){
        return "Administrador/ubicacionSitio";
    }

    @GetMapping({"/inventarioSitio","/inventariositio"})
    public String inventarioSitio(){
        return "Administrador/mapaInventarioSitio";
    }

//-----------------------------------------------------------------------

    // GESTION DE EQUIPOS
    @GetMapping({"/listaEquipo","/listaequipo"})
    public String listaEquipo(Model model){
        List<Equipo> listaEquipo = equipoRepository.findAll();
        model.addAttribute("listaEquipo", listaEquipo);
        return "Administrador/listaEquipo";
    }

    @GetMapping({"/crearEquipo","/crearequipo"})
    public String crearEquipo(){
        return "Administrador/crearEquipo";
    }

    @GetMapping({"/verEquipo","/verequipo"})
    public String verEquipo(Model model, @RequestParam("id") int id){
        Optional<Equipo> optionalEquipo = equipoRepository.findById(id);

        if(optionalEquipo.isPresent()){
            Equipo equipo = optionalEquipo.get();
            model.addAttribute("equipo", equipo);
            return "Administrador/vistaEquipo";
        }else {
            return "redirect:/Administrador/listaEquipo";
        }

    }

    @GetMapping({"/editarEquipo","/editarequipo"})
    public String editarEquipo(){
        return "Administrador/editarEquipo";
    }

//-----------------------------------------------------------------------

    //GESTION DE EMPRESAS
    @GetMapping({"/listaEmpresa","/listaempresa"})
    public String listaEmpresa(Model model){
        List<Empresa> listaEmpresa = empresaRepository.findAll();
        model.addAttribute("listaEmpresa",listaEmpresa);
        return "Administrador/listaEmpresa";
    }

    @GetMapping({"/crearEmpresa","/crearempresa"})
    public String crearEmpresa(){
        return "Administrador/crearEmpresa";
    }


    @GetMapping("/verEmpresa")
    public String verEmpresa(Model model,
                                      @RequestParam("id") int id) {

        Optional<Empresa> optEmpresa = empresaRepository.findById(id);

        if (optEmpresa.isPresent()) {
            Empresa empresa = optEmpresa.get();
            model.addAttribute("empresa", empresa);
            return "Administrador/vistaEmpresa";
        } else {
            return "redirect:/Administrador/listaEmpresa";
        }
    }

    @PostMapping("/guardarEmpresa")
    public String guardarEmpresa(Empresa empresa, RedirectAttributes attr){
        empresaRepository.save(empresa);
        return "redirect:/admin/listaEmpresa";
    }


}
