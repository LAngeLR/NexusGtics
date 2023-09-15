package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.Empresa;
import com.example.nexusgtics.entity.Usuario;
import com.example.nexusgtics.repository.EmpresaRepository;
import com.example.nexusgtics.repository.EquipoRepository;
import com.example.nexusgtics.repository.SitioRepository;
import com.example.nexusgtics.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    final EmpresaRepository empresaRepository;
    final SitioRepository sitioRepository;
    final EquipoRepository equipoRepository;

    final UsuarioRepository usuarioRepository;

    public AdminController(EmpresaRepository empresaRepository, SitioRepository sitioRepository, EquipoRepository equipoRepository, UsuarioRepository usuarioRepository) {
        this.empresaRepository = empresaRepository;
        this.sitioRepository = sitioRepository;
        this.equipoRepository = equipoRepository;
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

    // GESTION DE USUARIOS
    @GetMapping({"/listaUsuario","listausuario"})
    public String listaUsuario(Model model){
        List<Usuario> listaUsuario = usuarioRepository.findAll();
        model.addAttribute("listaUsuario", listaUsuario);
        return "Administrador/listaUsuario";
    }

    @GetMapping({"/crearUsuario","crearusuario"})
    public String crearUsuario(){
        return "Administrador/crearUsuario";
    }

    @GetMapping({"/verUsuario","verusuario"})
    public String verUsuario(){
        return "Administrador/vistaUsuario";
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



    // GESTION DE SITIOS
    @GetMapping({"/listaSitio","/listasitio"})
    public String listaSitio(){
        return "Administrador/listaSitio";
    }

    @GetMapping({"/crearSitio","/crearsitio"})
    public String crearSitio(){
        return "Administrador/crearSitio";
    }

    @GetMapping({"/verSitio","/versitio"})
    public String verSitio(){
        return "Administrador/vistaSitio";
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


    // GESTION DE EQUIPOS
    @GetMapping({"/listaEquipo","/listaequipo"})
    public String listaEquipo(){
        return "Administrador/listaEquipo";
    }

    @GetMapping({"/crearEquipo","/crearequipo"})
    public String crearEquipo(){
        return "Administrador/crearEquipo";
    }

    @GetMapping({"/verEquipo","/verequipo"})
    public String verEquipo(){
        return "Administrador/vistaEquipo";
    }

    @GetMapping({"/editarEquipo","/editarequipo"})
    public String editarEquipo(){
        return "Administrador/editarEquipo";
    }

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

    @GetMapping({"/verEmpresa","/verempresa"})
    public String verEmpresa(@RequestParam("id") int id, Model model ){

        Optional<Empresa> optEmpresa = empresaRepository.findById(id);

        if (optEmpresa.isPresent()) {
            Empresa empresa = optEmpresa.get();
            model.addAttribute("empresa", empresa);
            return "Administrador/vistaEmpresa";
        } else {
            return "redirect:Administrador/listaEmpresa";
        }

    }


}
