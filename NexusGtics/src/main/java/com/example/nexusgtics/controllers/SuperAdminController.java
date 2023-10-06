package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.Archivo;
import com.example.nexusgtics.entity.Usuario;
import com.example.nexusgtics.repository.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/superadmin")
public class SuperAdminController {

    final UsuarioRepository usuarioRepository;

    final CargoRepository cargoRepository;

    final EmpresaRepository empresaRepository;

    private final ArchivoRepository archivoRepository;

    public SuperAdminController(UsuarioRepository usuarioRepository, CargoRepository cargoRepository,
                                ArchivoRepository archivoRepository, EmpresaRepository empresaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cargoRepository = cargoRepository;
        this.archivoRepository = archivoRepository;
        this.empresaRepository = empresaRepository;
    }

    @GetMapping({"/","","superadmin"})
    public String paginaPrincipal(){
        return "Superadmin/superadmin";
    }


    /* -------------------------- PERFIL -------------------------- */
    @GetMapping({"/perfil","perfilAdmin","perfiladmin"})
    public String perfilAdmin(){
        return "Administrador/perfilAdmin";
    }

    @GetMapping({"/perfilEditar"})
    public String perfilEditar(Model model, @RequestParam("id") String idStr,
                               @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult){
        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/admin/listaUsuario";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                usuario = optionalUsuario.get();    //modifiqué Usuario usuario para poder usar @ModelAttribute
                model.addAttribute("usuario", usuario);
                return "Administrador/perfilEditar";
            } else {
                return "redirect:/admin";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/listaUsuario";
        }

    }

    @GetMapping({"/perfilContra"})
    public String perfilContra(){
        return "Administrador/perfilContra";
    }
    /* -------------------------- FIN PERFIL -------------------------- */

    @GetMapping({"/perfilsuperadmin", "/perfil", "/perfilSuperadmin"})
    public String perfilsuperadmin(){
        return "Superadmin/perfilSuperadmin";
    }

    @GetMapping({"/listaUsuario","listausuario", "listausuarios","listaUsuarios"})
    public String listaUsuario(Model model){
        List<Usuario> listaUsuarioNoAdmin = usuarioRepository.listaDeUsuariosNoSuperadmin();
        model.addAttribute("listaUsuario", listaUsuarioNoAdmin);
        return "Superadmin/listaUsuario";
    }

    @GetMapping({"/listaBaneados"})
    public String listaBaneado(Model model){
        List<Usuario> listaUsuarioBaneado = usuarioRepository.listaDeUsuariosBaneados();
        model.addAttribute("listaUsuario", listaUsuarioBaneado);
        return "Superadmin/listaUsuarioBaneado";
    }

    // Desactivar USUARIO
    @GetMapping("/banearUsuario")
    public String desabilitarUsuario(@RequestParam("id") int id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            usuarioRepository.desactivarUsuario(id);
        }
        return "redirect:/superadmin/listaUsuario";
    }

    // ACTIVAR USUARIO
    @GetMapping("/activarUsuario")
    public String activarUsuario(@RequestParam("id") int id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            usuarioRepository.activarUsuario(id);
        }
        return "redirect:/superadmin/listaBaneados";
    }

    @GetMapping({"/crearUsuario","crearusuario"})
    public String crearUsuario(Model model){

        model.addAttribute("listaEmpresa", empresaRepository.findAll());
        model.addAttribute("listaCargo", cargoRepository.findAll());

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

    @GetMapping({"/verUsuarioA","verusuarioa"})
    public String verUsuarioA(Model model, @RequestParam("id") int id){
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);

        if(optUsuario.isPresent()){
            Usuario usuario = optUsuario.get();
            model.addAttribute("usuario", usuario);
            return "Superadmin/vistaUsuarioA";
        } else {
            return "redirect:/superadmin/listaUsuario";
        }
    }

    /*CREAR NUEVO USUARIO*/
    @PostMapping("/saveUsuario")
    public String saveUsuario(@RequestParam("imagenSubida") MultipartFile file,
                              Usuario usuario,
                              Model model,
                              RedirectAttributes attr){

        if (!file.getContentType().startsWith("image/")) {
            // El archivo no es una imagen, maneja el caso de error aquí
            model.addAttribute("msgArchivo", "El archivo seleccionado no es una imagen.");
            return "Superadmin/crearUsuario";
        }

        if(usuario.getNombre().length() > 45){
            model.addAttribute("msgNombreLargo", "El nombre es demasiado largo.");
            return "Superadmin/crearUsuario";

        }
        if(!usuario.getNombre().matches("^[a-zA-Z]+$")){
            model.addAttribute("msgNombreMalo", "El nombre tiene caracteres inválidos.");
            return "Superadmin/crearUsuario";
        }

        if(usuario.getApellido().length() > 45){
            model.addAttribute("msgApellidoLargo", "El apellido es demasiado largo.");
            return "Superadmin/crearUsuario";

        }
        if(!usuario.getApellido().matches("^[a-zA-Z]+$")) {
            model.addAttribute("msgApellidoMalo", "El apellido tiene caracteres inválidos.");
            return "Superadmin/crearUsuario";
        }

        if(usuario.getCorreo().length() > 45){
            model.addAttribute("msgCorreoLargo", "El correo es demasiado largo.");
            return "Superadmin/crearUsuario";
        }

        if(!usuario.getCorreo().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            model.addAttribute("msgCorreoLargo", "El correo tiene caracteres inválidos.");
            return "Superadmin/crearUsuario";
        }



        if (usuario.getArchivo() == null) {
            usuario.setArchivo(new Archivo());
        }

        String fileName = file.getOriginalFilename();
        try{
            Archivo archivo = usuario.getArchivo();
            archivo.setNombre(fileName);
            archivo.setTipo(1);
            archivo.setArchivo(file.getBytes());
            archivo.setContentType(file.getContentType());
            archivoRepository.save(archivo);
            int idImagen = archivo.getIdArchivos();
            usuario.getArchivo().setIdArchivos(idImagen);
            usuarioRepository.save(usuario);
            return "redirect:/superadmin/listaUsuario";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*EDITAR USUARIO*/
    @GetMapping({"/editarUsuario","editarusuario"})
    public String editarUsuario(Model model, @RequestParam("id") int id){

        Optional<Usuario> usuario1 = usuarioRepository.findById(id);

        if (usuario1.isPresent()) {
            Usuario usuario = usuario1.get();
            model.addAttribute("usuario", usuario);
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            return "Superadmin/editarUsuario";
        } else {
            return "redirect:/Superadmin";
        }
    }


}
