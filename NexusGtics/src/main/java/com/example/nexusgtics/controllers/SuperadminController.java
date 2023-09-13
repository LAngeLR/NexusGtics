package com.example.nexusgtics.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/superadmin")
public class SuperadminController {

    @GetMapping("/")
    public String paginaPrincipal(){
        return "Superadmin/superadmin";
    }

    // GESTION DE USUARIOS
    @GetMapping("/listaUsuario")
    public String listaUsuario(){
        return "Superadmin/listaUsuario";
    }

    @GetMapping("/crearUsuario")
    public String crearUsuario(){
        return "Superadmin/crearAdministrador";
    }

    @GetMapping("/verUsuario")
    public String verUsuario(){
        return "Superadmin/vistaUsuario";
    }

    @GetMapping("/verAdministrador")
    public String verUsuario(){
        return "Superadmin/vistaAdministrador";
    }

    @GetMapping("/editarUsuario")
    public String editarUsuario(){
        return "Superadmin/editarAdministrador";
    }



}
