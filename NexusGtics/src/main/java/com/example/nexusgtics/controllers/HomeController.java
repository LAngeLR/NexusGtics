package com.example.nexusgtics.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = {"/login","/",""})
    public String paginaPrincipal(){
        return "Sistema/login";
    }

    @GetMapping("/registro")
    public String registro(){
        return "Sistema/registro";
    }

    @GetMapping("/olvidarcontra")
    public String olvidarContra(){
        return "Sistema/olvidarcontra";
    }

    @GetMapping("/cerrarsesion")
    public String cerrarsesion(){
        return "Sistema/cerrarsesion";
    }
}
