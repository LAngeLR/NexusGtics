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
    @GetMapping("/perfiltecnico")
    public String perfilTecnico(){
        return "Tecnico/perfiltecnico";
    }

    @GetMapping("/comentarios")
    public String pagcomentarios(){
        return "Tecnico/comentarios";
    }

    @GetMapping("/dashboard")
    public String pagdashboard(){
        return "Tecnico/dashboard";
    }

    @GetMapping("/datosticket")
    public String pagdatostick(){
        return "Tecnico/datos_ticket";
    }

    @GetMapping("/desplazamiento")
    public String pagdesplazamiento(){
        return "Tecnico/desplazamiento";
    }

    @GetMapping("/editarperfil")
    public String pageditarperfil(){
        return "Tecnico/editar_perfil";
    }

    @GetMapping("/formulario")
    public String pagformulario(){
        return "Tecnico/formulario";
    }

    @GetMapping("/mapa")
    public String pagmapa(){
        return "Tecnico/mapa";
    }

    @GetMapping("/ticketasignado")
    public String pagtickasignado(){
        return "Tecnico/ticket_asignado";
    }

}
