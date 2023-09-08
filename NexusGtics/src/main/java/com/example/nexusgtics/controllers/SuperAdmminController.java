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

    @GetMapping("/perfilsuperadmin")
    public String perfilsuperadmin(){
        return "Superadmin/perfilsuperadmin";
    }

    @GetMapping("/verusuario")
    public String verusuario(){
        return "Superadmin/verusuario";
    }

    @GetMapping("/vermas")
    public String vermas(){
        return "Superadmin/vermas";
    }

    @GetMapping("/suspenderus")
    public String suspenderusuario(){
        return "Superadmin/suspenderus";
    }

    @GetMapping("/eliminadous")
    public String usuarioeliminado(){
        return "Superadmin/eliminadous";
    }

}
