package com.example.nexusgtics.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/superadmin")
public class SuperAdmminController {
    @GetMapping("/")
    public String paginaPrincipal(){
        return "Superadmin/superadmin";
    }
}
