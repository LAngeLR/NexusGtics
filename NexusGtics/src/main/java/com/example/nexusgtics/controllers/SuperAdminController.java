package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.Usuario;
import com.example.nexusgtics.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/superadmin")
public class SuperAdminController {

    final UsuarioRepository usuarioRepository;

    public SuperAdminController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping({"/","","superadmin"})
    public String paginaPrincipal(){
        return "Superadmin/superadmin";
    }

    @GetMapping("/perfilsuperadmin")
    public String perfilsuperadmin(){
        return "perfilSuperadmin";
    }

    @GetMapping({"/listaUsuario","listausuario"})
    public String listaUsuario(Model model){
        List<Usuario> listaUsuarioNoAdmin = usuarioRepository.listaDeUsuariosNoAdmin();
        //List<Usuario> listaUsuarioTotal = usuarioRepository.findAll();
        model.addAttribute("listaUsuario", listaUsuarioNoAdmin);
        //model.addAttribute("listaUsuarioTotal", listaUsuarioTotal);
        return "Superadmin/listaUsuario";
    }

    @GetMapping("/desabilitarUsuario")
    public String desabilitarUsuario(@RequestParam("id") int id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            usuarioRepository.desactivarUsuario(id);
        }
        return "redirect:Superadmin/listaUsuario";
    }

    @GetMapping({"/crearUsuario","crearusuario"})
    public String crearUsuario(){
        return "Superadmin/crearUsuario";
    }

    @GetMapping({"/verUsuario","verusuario"})
    public String verUsuario(Model model, @RequestParam("id") int id){
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);

        if(optUsuario.isPresent()){
            Usuario usuario = optUsuario.get();
            model.addAttribute("usuario", usuario);
            return "Superadmin/vistaUsuario";
        } else {
            return "redirect:/Superadmin/listaUsuario";
        }
    }


    @GetMapping({"/editarUsuario","editarusuario"})
    public String editarUsuario(){
        return "Superadmin/editarUsuario";
    }


    // CRUD DE USUARIOS

    @PostMapping("/guardarUsuario")
    public String guardarNuevoUsuario(Usuario usuario){

        usuarioRepository.save(usuario);

        return "redirect:Superadmin/listaUsuario";
    }



}
