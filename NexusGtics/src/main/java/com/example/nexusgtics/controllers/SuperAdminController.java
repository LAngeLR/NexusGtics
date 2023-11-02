package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.Archivo;
import com.example.nexusgtics.entity.Usuario;
import com.example.nexusgtics.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.websocket.SessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
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

    @Autowired
    private HttpSession session;

    final UsuarioRepository usuarioRepository;

    final CargoRepository cargoRepository;

    final EmpresaRepository empresaRepository;

    private final ArchivoRepository archivoRepository;

    private final PasswordEncoder passwordEncoder;

    public SuperAdminController(EmpresaRepository empresaRepository, SitioRepository sitioRepository, EquipoRepository equipoRepository, TipoEquipoRepository tipoEquipoRepository, UsuarioRepository usuarioRepository, CargoRepository cargoRepository,
                                ArchivoRepository archivoRepository, PasswordEncoder passwordEncoder) {
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.cargoRepository = cargoRepository;
        this.archivoRepository = archivoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


    @GetMapping({"/","","superadmin","SuperAdmin"})
    public String paginaPrincipal(Model model){
        model.addAttribute("currentPage", "Inicio");
        return "Superadmin/superadmin";
    }

    /* --------------------- PERFIL ---------------------------- */

    @GetMapping({"/perfil","perfilSuperadmin","perfilsuperadmin"})
    public String perfilSuperadmin(Model model,
                                   @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession){
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        model.addAttribute("usuario", u);
        return "Superadmin/perfilSuperadmin";
    }

//---------------------------- GESTION DE USUARIOS -------------------------------------------

    // Lista de usuarios no admin
    @GetMapping({"/listaUsuario","listausuario","listausuarios","listaUsuarios"})
    public String listaUsuario(Model model){
        List<Usuario> listaDeUsuariosTotal = usuarioRepository.listaDeUsuariosTotal();
        model.addAttribute("currentPage", "Lista de Usuarios");

        model.addAttribute("listaUsuario", listaDeUsuariosTotal);
        return "Superadmin/listaUsuario";
    }

    @GetMapping({"/listaBaneados"})
    public String listaBaneado(Model model){
        List<Usuario> listaUsuarioBaneado = usuarioRepository.listaDeUsuariosBaneados();
        model.addAttribute("currentPage", "Lista de Baneados");

        model.addAttribute("listaUsuario", listaUsuarioBaneado);
        return "Superadmin/listaUsuarioBaneado";
    }

    // DESACTIVAR USUARIO
    @GetMapping("/banearUsuario")
    public String desabilitarUsuario(@RequestParam("id") int id, RedirectAttributes attr) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            usuarioRepository.desactivarUsuario(id);
            attr.addFlashAttribute("msg1", "El usuario '" + usuario.getNombre() + " " +usuario.getApellido() + "' se sido desactivado exitosamente");

        }
        return "redirect:/superadmin/listaUsuario";
    }

    // ACTIVAR USUARIO
    @GetMapping("/activarUsuario")
    public String activarUsuario(@RequestParam("id") int id, RedirectAttributes attr) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            usuarioRepository.activarUsuario(id);
            attr.addFlashAttribute("msg1", "El usuario '" + usuario.getNombre() + " " +usuario.getApellido() + "' se sido activado exitosamente");
        }
        return "redirect:/superadmin/listaBaneados";
    }

    @GetMapping({"/crearUsuario","crearusuario"})
    public String crearUsuario(Model model,
                               @ModelAttribute("usuario") Usuario usuario){

        model.addAttribute("listaEmpresa", empresaRepository.findAll());
        model.addAttribute("listaCargo", cargoRepository.findAll());

        return "Superadmin/crearUsuario";
    }

    @GetMapping({"/verUsuario","verusuario"})
    public String verUsuario(Model model, @RequestParam("id") String idStr){

        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/superadmin/listaUsuario";
            }
            Optional<Usuario> optUsuario = usuarioRepository.findById(id);
            if(optUsuario.isPresent()){
                Usuario usuario1 = optUsuario.get();
                model.addAttribute("usuario", usuario1);
                return "Superadmin/vistaUsuario";
            } else {
                return "redirect:/superadmin/listaUsuario";
            }
        } catch (NumberFormatException e) { //errores
            return "Superadmin/crearUsuario";
        }
    }

    @GetMapping({"/verUsuarioA","verusuarioa"})
    public String verUsuarioA(Model model, @RequestParam("id") String idStr){

        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/superadmin/listaUsuario";
            }
            Optional<Usuario> optUsuario = usuarioRepository.findById(id);
            if(optUsuario.isPresent()){
                Usuario usuario1 = optUsuario.get();
                model.addAttribute("usuario", usuario1);
                return "Superadmin/vistaUsuarioA";
            } else {
                return "redirect:/superadmin/listaUsuario";
            }
        } catch (NumberFormatException e) { //errores
            return "Superadmin/crearUsuario";
        }
    }


    /*CREAR NUEVO USUARIO --> Nuevo Administrador*/
    @PostMapping("/saveUsuario")
    public String saveUsuario(@RequestParam("imagenSubida") MultipartFile file,
                              @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                              Model model,
                              RedirectAttributes attr){
        if (usuario.getId()==null){
            usuario.setContrasenia(new BCryptPasswordEncoder().encode("123"));
        }
        List<String> correos = usuarioRepository.listaCorreos();
        for (String correo : correos) {
            if (correo.equals(usuario.getCorreo())) {
                model.addAttribute("msgEmail", "El correo electrónico ya existe");
                model.addAttribute("listaEmpresa", empresaRepository.findAll());
                model.addAttribute("listaCargo", cargoRepository.findAll());
                return "Superadmin/crearUsuario";
            }
        }

        if(usuario.getCargo() == null || usuario.getCargo().getIdCargos() == null || usuario.getCargo().getIdCargos() == -1){
            model.addAttribute("msgCargo", "Escoger un cargo");
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());

            if (usuario.getId() == null) {
                return "Superadmin/crearUsuario";
            } else {
                return "Superadmin/editarUsuario";
            }
        }
        if(usuario.getEmpresa() == null || usuario.getEmpresa().getIdEmpresas() == null || usuario.getEmpresa().getIdEmpresas() == -1){
            model.addAttribute("msgEmpresa", "Escoger una empresa");
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            if (usuario.getId() == null) {
                return "Superadmin/crearUsuario";
            } else {
                return "Superadmin/editarUsuario";
            }
        }
        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
            if (usuario.getArchivo() == null) {
                usuario.setArchivo(new Archivo());
            }
            String fileName = file.getOriginalFilename();
            try{
                //validación de nombre, apellido y correo
                Archivo archivo = usuario.getArchivo();
                archivo.setNombre(fileName);
                archivo.setTipo(1);
                archivo.setArchivo(file.getBytes());
                archivo.setContentType(file.getContentType());
                archivoRepository.save(archivo);
                int idImagen = archivo.getIdArchivos();
                usuario.getArchivo().setIdArchivos(idImagen);
                if (usuario.getId() == null) {
                    attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha creado exitosamente");
                } else {
                    attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha actualizado exitosamente");
                }
                usuarioRepository.save(usuario);
                return "redirect:/superadmin/listaUsuario";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else { //hay al menos 1 error
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            if (usuario.getId() == null) {
                return "Superadmin/crearUsuario";
            } else {
                return "Superadmin/editarUsuario";
            }
        }
    }


    /*EDITAR USUARIOS*/
    @GetMapping({"/editarUsuario","editarusuario"})
    public String editarUsuario(Model model, @RequestParam("id") String idStr,
                                @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult){

        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/superadmin/listaUsuario";
            }

            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {

                usuario = optionalUsuario.get();    //modifiqué Usuario usuario para poder usar @ModelAttribute

                List<Usuario> administradores = usuarioRepository.listaDeAdministradores();
                boolean encontrado = false;
                for (Usuario administrador : administradores) {
                    System.out.println(administrador.getId());
                    //int idAdministrador = administrador.getId();
                    if (administrador.getId() == id) {
                        encontrado = true;
                        model.addAttribute("usuario", usuario);
                        break;
                    }
                }

                if (encontrado) {
                    return "Superadmin/editarUsuario";
                } else {
                    return "redirect:/superadmin/listaUsuario";
                }

                //return "Superadmin/editarUsuario";
            } else {
                return "redirect:/superadmin";
            }
        } catch (NumberFormatException e) {
            return "redirect:/superadmin/listaUsuario";
        }

    }


    @PostMapping("/updateUsuario")
    public String updateUsuario(@RequestParam("imagenSubida") MultipartFile file,
                                @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                                Model model,
                                RedirectAttributes attr){
        if(usuario.getCargo() == null || usuario.getCargo().getIdCargos() == null || usuario.getCargo().getIdCargos() == -1){
            model.addAttribute("msgCargo", "Escoger un cargo");
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());

            if (usuario.getId() == null) {
                return "Superadmin/crearUsuario";
            } else {
                return "Superadmin/editarUsuario";
            }
        }
        if(usuario.getEmpresa() == null || usuario.getEmpresa().getIdEmpresas() == null || usuario.getEmpresa().getIdEmpresas() == -1){
            model.addAttribute("msgEmpresa", "Escoger una empresa");
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            if (usuario.getId() == null) {
                return "Superadmin/crearUsuario";
            } else {
                return "Superadmin/editarUsuario";
            }
        }
        // Verificar si se cargó un nuevo archivo
        if (!file.isEmpty()) {
            try {
                // Procesar el archivo
                Archivo archivo = new Archivo();
                archivo.setNombre(file.getOriginalFilename());
                archivo.setTipo(1);
                archivo.setArchivo(file.getBytes());
                archivo.setContentType(file.getContentType());
                archivoRepository.save(archivo);

                // Asignar el nuevo archivo al equipo
                usuario.setArchivo(archivo);
            } catch (IOException e) {
                System.out.println("Error al procesar el archivo");
                throw new RuntimeException(e);
            }
        }

        if (!bindingResult.hasErrors()) {
            // Si no hay errores, se realiza el flujo normal
            if (usuario.getArchivo() == null) {
                usuario.setArchivo(new Archivo());
            }

            try {
                if (usuario.getId() == null) {
                    attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha creado exitosamente");
                } else {
                    attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha actualizado exitosamente");
                }
                usuarioRepository.save(usuario);
                return "redirect:/superadmin/listaUsuario";
            } catch (Exception e) {
                System.out.println("Error al guardar el equipo");
                throw new RuntimeException(e);
            }
        } else { //hay al menos 1 error
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            if (usuario.getId() == null) {
                return "Superadmin/crearUsuario";
            } else {
                return "Superadmin/editarUsuario";
            }
        }
    }



    /* PERFIL DEL SUPERADMINISTRADOR */
    @PostMapping("/savePerfil")
    public String savePerfil(@RequestParam("imagenSubida") MultipartFile file,
                              @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                              Model model,
                              RedirectAttributes attr, HttpSession httpSession){

        // ESTO SE AÑADIO DE BARD
        //session.setAttribute("usuario", usuario);

        if(usuario.getCargo() == null || usuario.getCargo().getIdCargos() == null || usuario.getCargo().getIdCargos() == -1){
            model.addAttribute("msgCargo", "Escoger un cargo");
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());

            if (usuario.getId() == null) {
                return "Superadmin/superadmin";
            } else {
                return "Superadmin/perfilEditar";
            }
        }
        if(usuario.getEmpresa() == null || usuario.getEmpresa().getIdEmpresas() == null || usuario.getEmpresa().getIdEmpresas() == -1){
            model.addAttribute("msgEmpresa", "Escoger una empresa");
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            if (usuario.getId() == null) {
                return "Superadmin/superadmin";
            } else {
                return "Superadmin/perfilEditar";
            }
        }

        if (file.getSize() > 0 && !file.getContentType().startsWith("image/")) {
            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
            if (usuario.getId() == null) {
                return "Superadmin/superadmin";
            } else {
                return "Superadmin/perfilEditar";
            }
        }

        int maxFileSize = 10485760;

        if (file.getSize() > maxFileSize) {
            System.out.println(file.getSize());
            model.addAttribute("msgImagen1", "El archivo subido excede el tamaño máximo permitido (10MB).");
            if (usuario.getId() == null) {
                return "Superadmin/superadmin";
            } else {
                return "redirect:/superadmin/perfilEditar";
            }
        }

        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
            if (usuario.getArchivo() == null) {
                usuario.setArchivo(new Archivo());
            }
            String fileName = file.getOriginalFilename();
            try{
                //validación de nombre, apellido y correo
                Archivo archivo = usuario.getArchivo();
                archivo.setNombre(fileName);
                archivo.setTipo(1);
                archivo.setArchivo(file.getBytes());
                archivo.setContentType(file.getContentType());
                archivoRepository.save(archivo);
                int idImagen = archivo.getIdArchivos();
                usuario.getArchivo().setIdArchivos(idImagen);
                if (usuario.getId() == null) {
                    attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha creado exitosamente");
                } else {
                    attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha actualizado exitosamente");
                }
                usuarioRepository.save(usuario);
                //Usuario u = (Usuario) httpSession.getAttribute("usuario");
                //HttpSession session = request.getSession(true);
                //session.setAttribute("nombreUsuario", "nuevoNombre");
                session.setAttribute("usuario", usuario);
                return "redirect:/superadmin/perfil";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else { //hay al menos 1 error
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            if (usuario.getId() == null) {
                return "Superadmin/perfil";
            } else {
                return "Superadmin/perfilEditar";
            }
        }
    }

    @GetMapping({"/perfilEditar"})
    public String perfilEditar(Model model,
                               @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession){

        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        int id = u.getId();
        try{
            //int id = Integer.parseInt(idStr);
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/superadmin/listaUsuario";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                usuario = optionalUsuario.get();    //modifiqué Usuario usuario para poder usar @ModelAttribute
                model.addAttribute("usuario", usuario);
                model.addAttribute("listaEmpresa", empresaRepository.findAll());
                model.addAttribute("listaCargo", cargoRepository.findAll());
                return "Superadmin/perfilEditar";
            } else {
                return "redirect:/superadmin/perfil";
            }
        } catch (NumberFormatException e) {
            return "redirect:/superadmin/listaUsuario";
        }

    }

    @GetMapping({"/perfilContra"})
    public String perfilContra(Model model,
                               @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession){
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        int id = u.getId();
        try{
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/superadmin/listaUsuario";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                model.addAttribute("idUsuario",id);
                return "Superadmin/perfilContra";
            } else {
                return "redirect:/superadmin/perfil";
            }
        } catch (NumberFormatException e) {
            return "redirect:/superadmin/listaUsuario";
        }
    }

    @PostMapping({"/actualizarContra"})
    public String actualizarContra(Model model, @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession,
                                   @RequestParam("password") String contrasenia,
                                   @RequestParam("newpassword") String contraseniaNueva, @RequestParam("renewpassword") String contraseniaConfirm,
                                   RedirectAttributes redirectAttributes){
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        int id = u.getId();

        String contraseniaAlmacenada = usuarioRepository.obtenerContraseña(id);

        if (passwordEncoder.matches(contrasenia, contraseniaAlmacenada)) {
            String contraseniaNuevaEncriptada = passwordEncoder.encode(contraseniaNueva);
            usuarioRepository.actualizarContraA(contraseniaNuevaEncriptada, id);
            redirectAttributes.addFlashAttribute("msg1", "La contraseña se ha actualizado exitosamente");

            return "redirect:/superadmin/perfilSuperadmin";
        } else {
            redirectAttributes.addFlashAttribute("error","La contraseña actual no es correcta.");
            return "redirect:/superadmin/perfilContra";
        }
    }

}