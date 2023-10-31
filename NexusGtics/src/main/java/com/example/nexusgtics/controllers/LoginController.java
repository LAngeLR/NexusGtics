package com.example.nexusgtics.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
    @GetMapping("/openLoginWindow")
    public String loginWindow(){
        return "Sistema/login";
    }
    @GetMapping("/closeSession")
    public String closeSession(){
        return "Sistema/cerrarsesion";
    }


//    @PostMapping(value = "/processLogin")
//    public String processLogin(HttpServletRequest request, Model model) {
//
//        if (authenticationError) {
//            model.addAttribute("errorMsg", "Usuario y/o contraseña incorrectos");
//            return "login"; // Redirige de nuevo a la página de inicio de sesión
//        } else {
//            // Lógica de inicio de sesión exitosa
//            return "redirect:/dashboard"; // Redirige a la página de inicio
//        }
//    }

}
