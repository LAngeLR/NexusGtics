package com.example.nexusgtics.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/")
    public String paginaPrincipal(){
        return "Administrador/administrador";
    }

    // GESTION DE USUARIOS
    @GetMapping("/listaUsuario")
    public String listaUsuario(){
        return "Administrador/listaUsuario";
    }

    @GetMapping("/crearUsuario")
    public String crearUsuario(){
        return "Administrador/crearUsuario";
    }

    @GetMapping("/verUsuario")
    public String verUsuario(){
        return "Administrador/vistaUsuario";
    }

    @GetMapping("/editarUsuario")
    public String editarUsuario(){
        return "Administrador/editarUsuario";
    }


    // GESTION DE SITIOS
    @GetMapping("/listaSitio")
    public String listaSitio(){
        return "Administrador/listaSitio";
    }

    @GetMapping("/crearSitio")
    public String crearSitio(){
        return "Administrador/crearSitio";
    }

    @GetMapping("/verSitio")
    public String verSitio(){
        return "Administrador/vistaSitio";
    }

    @GetMapping("/editarSitio")
    public String editarSitio(){
        return "Administrador/editarSitio";
    }

    @GetMapping("/ubicarSitio")
    public String ubicarSitio(){
        return "Administrador/ubicacionSitio";
    }

    @GetMapping("/inventarioSitio")
    public String inventarioSitio(){
        return "Administrador/mapaInventarioSitio";
    }

}
