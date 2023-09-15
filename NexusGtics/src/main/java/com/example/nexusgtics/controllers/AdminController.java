package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.Empresa;
import com.example.nexusgtics.repository.EmpresaRepository;
import com.example.nexusgtics.repository.EquipoRepository;
import com.example.nexusgtics.repository.SitioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    final EmpresaRepository empresaRepository;
    final SitioRepository sitioRepository;
    final EquipoRepository equipoRepository;

    public AdminController(EmpresaRepository empresaRepository, SitioRepository sitioRepository, EquipoRepository equipoRepository) {
        this.empresaRepository = empresaRepository;
        this.sitioRepository = sitioRepository;
        this.equipoRepository = equipoRepository;
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
    public String listaUsuario(){
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
    public String verEmpresa(){
        return "Administrador/vistaEmpresa";
    }


}
