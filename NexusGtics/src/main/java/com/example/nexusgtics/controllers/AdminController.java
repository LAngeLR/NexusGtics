package com.example.nexusgtics.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping({"/","","admin","administrador"})
    public String paginaPrincipal(){
        return "Administrador/listaUsuario";
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
    public String listaEmpresa(){
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
