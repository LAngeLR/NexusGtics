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

}
