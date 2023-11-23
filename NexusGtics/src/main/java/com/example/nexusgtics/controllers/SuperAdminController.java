package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.Archivo;
import com.example.nexusgtics.entity.Cargo;
import com.example.nexusgtics.entity.Empresa;
import com.example.nexusgtics.entity.Usuario;
import com.example.nexusgtics.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import java.time.LocalDate;

import static com.example.nexusgtics.controllers.GcsController.downloadObject;
import static com.example.nexusgtics.controllers.GcsController.uploadObject;
import static java.lang.Integer.parseInt;

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

        LocalDate fechaActual = LocalDate.now();
        model.addAttribute("fechaActual", fechaActual);

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
    public String saveUsuario(@ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                              Model model,
                              RedirectAttributes attr){

        //poner contraseña a lo mismo que el correo hasta antes del @
        if (usuario.getId()==null){
            String mail = usuario.getCorreo();
            String[] partes = mail.split("@");
            String password = partes[0];
            usuario.setContrasenia(new BCryptPasswordEncoder().encode(password));
        }

        //poner los demás campos con los valores por defecto (para que ya no se manden como hidden)
        usuario.setHabilitado(Boolean.TRUE);
        ZoneId zonaHoraria = ZoneId.of("GMT-5");
        LocalDate fechaActual = LocalDate.now(zonaHoraria); // Obtener la fecha actual en la zona horaria GMT-5
        usuario.setFechaRegistro(fechaActual);
        usuario.setTecnicoConCuadrilla(Boolean.FALSE);
        Cargo cargo = cargoRepository.getReferenceById(2);
        usuario.setCargo(cargo);
        Empresa empresa = empresaRepository.getReferenceById(1);
        usuario.setEmpresa(empresa);

        //validar que no se repitan los emails (se pone después de cargoSeleccionado para que se aplique lo anterior antes)
        List<String> correos = usuarioRepository.listaCorreos();
        String correoActual = usuario.getCorreo();
        for (String correo : correos) {
            if (correo.equals(usuario.getCorreo())) {
                model.addAttribute("msgEmail", "El correo electrónico ya existe");
                model.addAttribute("listaEmpresa", empresaRepository.findAll());
                model.addAttribute("listaCargo", cargoRepository.findAll());
                return "Superadmin/crearUsuario";
            }
        }


        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
            if (usuario.getArchivo() == null) {
                usuario.setArchivo(new Archivo());
            }
            try{
                Archivo archivo = usuario.getArchivo();
                archivo.setNombre("fotoPerfil");
                archivo.setTipo(1);
                byte[] image = downloadObject("labgcp-401300", "proyecto-gtics", "userDefault.png");
                archivo.setArchivo(image);
                archivo.setContentType("image/png");
                Archivo archivo1 = archivoRepository.save(archivo);
                String nombreArchivo = "archivo-"+archivo1.getIdArchivos()+".png";
                archivo1.setNombre(nombreArchivo);
                archivoRepository.save(archivo1);
                uploadObject(archivo1);
                archivo1.setArchivo(null);
                //mensaje de creación
                attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha creado exitosamente");
                usuarioRepository.save(usuario);
                return "redirect:/superadmin/listaUsuario";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else { //hay al menos 1 error
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            return "Superadmin/crearUsuario";

        }
    }


    /* DIRECCIONA PARA EDITAR USUARIOS*/
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
                Usuario usuario1 = optionalUsuario.get();
                List<Usuario> administradores = usuarioRepository.listaDeAdministradores();
                boolean encontrado = false;
                for (Usuario administrador : administradores) {
                    System.out.println(administrador.getId());
                    //int idAdministrador = administrador.getId();
                    if (administrador.getId() == id) {
                        encontrado = true;
                        break;
                    }
                }
                if (encontrado) {
                    //Usuario usuario2 = optionalUsuario.get();
                    System.out.printf(usuario1.getNombre() + " " + usuario1.getArchivo().getIdArchivos());
                    model.addAttribute("usuario", usuario1);
                    return "Superadmin/editarUsuario";
                } else {
                    return "redirect:/superadmin/listaUsuario";
                }
            } else {
                return "redirect:/superadmin";
            }
        } catch (NumberFormatException e) {
            return "redirect:/superadmin/listaUsuario";
        }

    }


    /* ACTUALIZAR INFORMACIÓN DE LOS "ADMINISTRADORES" */
    @PutMapping("/updateUsuario")
    public String updateUsuario(@ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                                Model model,
                                RedirectAttributes attr){


        try{
            int id = usuario.getId();
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/superadmin/listaUsuario";
            }

            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                Usuario usuarioDb = optionalUsuario.get();
                //validar que no se repitan los emails
                Integer id1 = usuario.getId();

                List<String> correos = usuarioRepository.listaCorreos2(id1);
                for (String correo : correos) {
                    if (correo.equals(usuario.getCorreo())) {
                        model.addAttribute("msgEmail", "El correo electrónico ya existe");
                        model.addAttribute("listaEmpresa", empresaRepository.findAll());
                        model.addAttribute("listaCargo", cargoRepository.findAll());
                        return "Superadmin/crearUsuario";
                    }
                }


                if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
                    if (usuario.getArchivo() == null) {
                        usuario.setArchivo(new Archivo());
                    }
                    try{
                        if (usuario.getId() == null) {
                            attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha creado exitosamente");
                        } else {
                            attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha actualizado exitosamente");
                        }
                        usuarioDb.setNombre(usuario.getNombre());
                        usuarioDb.setApellido(usuario.getApellido());
                        usuarioDb.setDni(usuario.getDni());
                        usuarioRepository.save(usuarioDb);
                        return "redirect:/superadmin/listaUsuario";

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                } else { //hay al menos 1 error
                    model.addAttribute("listaEmpresa", empresaRepository.findAll());
                    model.addAttribute("listaCargo", cargoRepository.findAll());
                    return "Superadmin/editarUsuario";
                }
            } else {
                return "redirect:/superadmin";
            }

        } catch (NumberFormatException e) {
            return "redirect:/superadmin/listaUsuario";
        }

    }



    /* PERFIL DEL SUPERADMINISTRADOR */
    @PutMapping("/savePerfil")
    public String savePerfil(@RequestParam("imagenSubida") MultipartFile file,
                              @ModelAttribute("usuario") @Valid Usuario usuario,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes attr, HttpSession httpSession){

        // ESTO SE AÑADIO DE BARD
        //session.setAttribute("usuario", usuario);

//        if(usuario.getCargo() == null || usuario.getCargo().getIdCargos() == null || usuario.getCargo().getIdCargos() == -1){
//            model.addAttribute("msgCargo", "Escoger un cargo");
//            model.addAttribute("listaEmpresa", empresaRepository.findAll());
//            model.addAttribute("listaCargo", cargoRepository.findAll());
//
//            if (usuario.getId() == null) {
//                return "Superadmin/superadmin";
//            } else {
//                return "Superadmin/perfilEditar";
//            }
//        }
//        if(usuario.getEmpresa() == null || usuario.getEmpresa().getIdEmpresas() == null || usuario.getEmpresa().getIdEmpresas() == -1){
//            model.addAttribute("msgEmpresa", "Escoger una empresa");
//            model.addAttribute("listaEmpresa", empresaRepository.findAll());
//            model.addAttribute("listaCargo", cargoRepository.findAll());
//            if (usuario.getId() == null) {
//                return "Superadmin/superadmin";
//            } else {
//                return "Superadmin/perfilEditar";
//            }
//        }


        if (file.getSize() > 0 && !file.getContentType().startsWith("image/") && !file.isEmpty()) {
            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
            return "Superadmin/perfilEditar";
        }

//        String fileName1 = file.getOriginalFilename();
//
//        if (fileName1.contains("..") && !file.isEmpty()) {
//            model.addAttribute("msgImagen", "No se permiten '..' en el archivo ");
//            if (usuario.getId() == null) {
//                return "Superadmin/superadmin";
//            } else {
//                return "Superadmin/perfilEditar";
//            }
//        }
//
//        int maxFileSize = 10485760;
//
//        if (file.getSize() > maxFileSize && !file.isEmpty()) {
//            System.out.println(file.getSize());
//            model.addAttribute("msgImagen1", "El archivo subido excede el tamaño máximo permitido (10MB).");
//            if (usuario.getId() == null) {
//                return "Superadmin/superadmin";
//            } else {
//                return "redirect:/superadmin/perfilEditar";
//            }
//        }

        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
            if (usuario.getArchivo() == null) {
                usuario.setArchivo(new Archivo());
            }

            try{
                if(file.getSize()>1){
                    // Obtenemos el nombre del archivo
                    String fileName = file.getOriginalFilename();
                    String extension = "";
                    int i = fileName.lastIndexOf('.');
                    if (i > 0) {
                        extension = fileName.substring(i+1);
                    }
                    Archivo archivo = usuario.getArchivo();
                    archivo.setNombre(fileName);
                    archivo.setTipo(1);
                    archivo.setArchivo(file.getBytes());
                    archivo.setContentType(file.getContentType());
                    Archivo archivo1 = archivoRepository.save(archivo);
                    String nombreArchivo = "archivo-"+archivo1.getIdArchivos()+"."+extension;
                    archivo1.setNombre(nombreArchivo);
                    archivoRepository.save(archivo1);
                    uploadObject(archivo1);
                    archivo1.setArchivo(null);
                } else {
                    Archivo archivo = usuario.getArchivo();
                    archivo.getNombre();
                    archivo.setTipo(1);
                    byte[] image = downloadObject("labgcp-401300", "proyecto-gtics", "deviceDefault.png");
                    archivo.setArchivo(image);
                    archivo.setContentType("image/png");
                    Archivo archivo1 = archivoRepository.save(archivo);
                    String nombreArchivo = "archivo-"+archivo1.getIdArchivos()+".png";
                    archivo1.setNombre(nombreArchivo);
                    archivoRepository.save(archivo1);
                    uploadObject(archivo1);
                    archivo1.setArchivo(null);

                }

                if (usuario.getId() == null) {
                    attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha creado exitosamente");
                } else {
                    attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha actualizado exitosamente");
                }
                usuario.getArchivo().setTipo(1);
                System.out.println("El tipo de archivo es: " + usuario.getArchivo().getTipo());
                usuarioRepository.save(usuario);

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