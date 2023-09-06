package com.example.nexusgtics.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tecnico")
public class TecnicoController {
    @GetMapping("/")
    public String paginaPrincipal(){
        return "Tecnico/tecnico";
    }
}
