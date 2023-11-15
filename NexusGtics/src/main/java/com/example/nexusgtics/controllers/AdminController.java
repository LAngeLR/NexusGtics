package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.*;
import com.example.nexusgtics.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.math.RoundingMode;

import static com.example.nexusgtics.controllers.GcsController.downloadObject;
import static com.example.nexusgtics.controllers.GcsController.uploadObject;
import static java.lang.Integer.parseInt;
import static org.aspectj.runtime.internal.Conversions.intValue;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final HttpSession session;

    final EmpresaRepository empresaRepository;
    final SitioRepository sitioRepository;
    final EquipoRepository equipoRepository;
    final TipoEquipoRepository tipoEquipoRepository;
    final UsuarioRepository usuarioRepository;
    final SitiosHasEquiposRepository sitiosHasEquiposRepository;
    final CargoRepository cargoRepository;
    private final TicketRepository ticketRepository;
    private final ArchivoRepository archivoRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(EmpresaRepository empresaRepository, SitioRepository sitioRepository,
                           EquipoRepository equipoRepository, TipoEquipoRepository tipoEquipoRepository,
                           UsuarioRepository usuarioRepository, SitiosHasEquiposRepository sitiosHasEquiposRepository,
                           CargoRepository cargoRepository, PasswordEncoder passwordEncoder,
                           TicketRepository ticketRepository, ArchivoRepository archivoRepository, HttpSession session) {
        this.empresaRepository = empresaRepository;
        this.sitioRepository = sitioRepository;
        this.equipoRepository = equipoRepository;
        this.tipoEquipoRepository = tipoEquipoRepository;
        this.usuarioRepository = usuarioRepository;
        this.sitiosHasEquiposRepository = sitiosHasEquiposRepository;
        this.cargoRepository = cargoRepository;
        this.ticketRepository = ticketRepository;
        this.archivoRepository = archivoRepository;
        this.passwordEncoder = passwordEncoder;
        this.session = session;
    }

    @GetMapping({"/", "", "admin", "administrador"})
    public String paginaPrincipal(Model model, HttpSession httpSession) {
        model.addAttribute("currentPage", "Inicio");
        //Usuario user = (Usuario) session.getAttribute("usuario");
        //System.out.println("User: "+ user.getNombre());
        return "Administrador/admin";
    }

    /* --------------------- PERFIL ---------------------------- */

    @GetMapping({"/perfil", "perfilAdmin", "perfiladmin"})
    public String perfilAdmin() {
        return "Administrador/perfilAdmin";
    }

//---------------------------- GESTION DE USUARIOS -------------------------------------------

    // Lista de usuarios no admin
    @GetMapping({"/listaUsuario", "listausuario"})
    public String listaUsuario(Model model) {
        List<Usuario> listaUsuarioNoAdmin = usuarioRepository.listaDeUsuariosNoAdmin();
        model.addAttribute("currentPage", "Lista de Usuarios");

        model.addAttribute("listaUsuario", listaUsuarioNoAdmin);
        return "Administrador/listaUsuario";
    }

    @GetMapping({"/listaBaneados"})
    public String listaBaneado(Model model) {
        List<Usuario> listaUsuarioBaneado = usuarioRepository.listaDeUsuariosBaneados();
        model.addAttribute("currentPage", "Lista de Baneados");

        model.addAttribute("listaUsuario", listaUsuarioBaneado);
        return "Administrador/listaUsuarioBaneado";
    }

    // DESACTIVAR USUARIO
    @GetMapping("/banearUsuario")
    public String desabilitarUsuario(@RequestParam("id") int id, RedirectAttributes attr) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            usuarioRepository.desactivarUsuario(id);
            attr.addFlashAttribute("msg1", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se sido desactivado exitosamente");

        }
        return "redirect:/admin/listaUsuario";
    }

    // ACTIVAR USUARIO
    @GetMapping("/activarUsuario")
    public String activarUsuario(@RequestParam("id") int id, RedirectAttributes attr) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            usuarioRepository.activarUsuario(id);
            attr.addFlashAttribute("msg1", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se sido activado exitosamente");
        }
        return "redirect:/admin/listaBaneados";
    }

    @GetMapping({"/verUsuario", "verusuario"})
    public String verUsuario(Model model, @RequestParam("id") String idStr) {

        try {
            int id = parseInt(idStr);
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/admin/listaUsuario";
            }
            Optional<Usuario> optUsuario = usuarioRepository.findById(id);
            if (optUsuario.isPresent()) {
                Usuario usuario1 = optUsuario.get();
                model.addAttribute("usuario", usuario1);
                return "Administrador/vistaUsuario";
            } else {
                return "redirect:/admin/listaUsuario";
            }
        } catch (NumberFormatException e) { //errores
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            return "Administrador/crearUsuario";
        }
    }
    @GetMapping({"/crearUsuario", "crearusuario"})
    public String crearUsuario(Model model,
                               @ModelAttribute("usuario") Usuario usuario) {

        model.addAttribute("listaEmpresa", empresaRepository.findAll());
        model.addAttribute("listaCargo", cargoRepository.findAll());
        //para poder mandar "cargoSeleccionado" por defecto como -1
        Cargo cargoSeleccionado = usuario.getCargo();
        if (cargoSeleccionado == null) {
            cargoSeleccionado = new Cargo();
            cargoSeleccionado.setIdCargos(-1);
        }
        model.addAttribute("cargoSeleccionado", cargoSeleccionado);
        //para poder mandar "empresaSeleccionada" por defecto como -1
        Empresa empresaSeleccionada = usuario.getEmpresa();
        if (empresaSeleccionada == null) {
            empresaSeleccionada = new Empresa();
            empresaSeleccionada.setIdEmpresas(-1);
        }
        model.addAttribute("empresaSeleccionada", empresaSeleccionada);
        return "Administrador/crearUsuario";
    }
    /*CREAR NUEVO USUARIO*/
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


        //para "guardar" lo seleccionado y poder mostrarlo cuando haya un error y no tener que ponerlo de nuevo
        Cargo cargoSeleccionado = usuario.getCargo();
        Empresa empresaSeleccionada = usuario.getEmpresa();
        model.addAttribute("cargoSeleccionado", cargoSeleccionado);
        model.addAttribute("empresaSeleccionada", empresaSeleccionada);
        //TODAS LAS VALIDACIONES DESPUÉS DE AQUÍ

        if (usuario.getCargo() == null || usuario.getCargo().getIdCargos() == null || usuario.getCargo().getIdCargos() == -1) {
            model.addAttribute("msgCargo", "Escoger un cargo");
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());

            return "Administrador/crearUsuario";
        }
        if (usuario.getEmpresa() == null || usuario.getEmpresa().getIdEmpresas() == null || usuario.getEmpresa().getIdEmpresas() == -1) {
            model.addAttribute("msgEmpresa", "Escoger una empresa");
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            return "Administrador/crearUsuario";
        }

        //validar que no se repitan los emails (se pone después de cargoSeleccionado para que se aplique lo anterior antes)
        List<String> correos = usuarioRepository.listaCorreos();
        String correoActual = usuario.getCorreo();
        for (String correo : correos) {
            if (correo.equals(usuario.getCorreo())) {
                model.addAttribute("msgEmail", "El correo electrónico ya existe");
                model.addAttribute("listaEmpresa", empresaRepository.findAll());
                model.addAttribute("listaCargo", cargoRepository.findAll());
                return "Administrador/crearUsuario";
            }
        }

        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
            if (usuario.getArchivo() == null) {
                usuario.setArchivo(new Archivo());
            }

            try {
                //guardar foto por defecto
                Archivo archivo = usuario.getArchivo();
                archivo.setNombre("fotoPerfil");
                archivo.setTipo(1);
                byte[] image = downloadObject("labgcp-401300", "proyecto-gtics", "userDefault.png");
                //archivo.setArchivo(image);
                archivo.setContentType("image/png");
                Archivo archivo1 = archivoRepository.save(archivo);
                String nombreArchivo = "archivo-"+archivo1.getIdArchivos()+".png";
                archivo1.setNombre(nombreArchivo);
                archivoRepository.save(archivo1);
                uploadObject(archivo1);

//              mensaje de creación
                attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha creado exitosamente");
                usuarioRepository.save(usuario);
                return "redirect:/admin/listaUsuario";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else { //hay al menos 1 error
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            return "Administrador/crearUsuario";
        }
    }

    @PutMapping("/updateUsuario")
    public String updateUsuario(/*@RequestParam("imagenSubida") MultipartFile file,*/
                                @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                                Model model,
                                RedirectAttributes attr) {
//        Usuario u = (Usuario) httpSession.getAttribute("usuario");
//        Integer idSupervisor = u.getId();
//
//        model.addAttribute("listaTickets",ticketRepository.listaTicketsModificado( 1, idSupervisor));

        try {
            int id = usuario.getId();
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/admin/listaUsuario";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                Usuario usuarioDb = optionalUsuario.get();    //se crea un usuarioDb al que se le pasarán los campos del form que fueron a usuario

                //validar que no se repitan los emails
                Integer id1 = usuario.getId();

                List<String> correos = usuarioRepository.listaCorreos2(id1);
                for (String correo : correos) {
                    if (correo.equals(usuario.getCorreo())) {
                        model.addAttribute("msgEmail", "El correo electrónico ya existe");
                        model.addAttribute("listaEmpresa", empresaRepository.findAll());
                        model.addAttribute("listaCargo", cargoRepository.findAll());
                        return "Administrador/editarUsuario";
                    }
                }

                if (usuario.getCargo() == null || usuario.getCargo().getIdCargos() == null || usuario.getCargo().getIdCargos() == -1) {
                    model.addAttribute("msgCargo", "Escoger un cargo");
                    model.addAttribute("listaEmpresa", empresaRepository.findAll());
                    model.addAttribute("listaCargo", cargoRepository.findAll());

                    return "Administrador/editarUsuario";
                }
                if (usuario.getEmpresa() == null || usuario.getEmpresa().getIdEmpresas() == null || usuario.getEmpresa().getIdEmpresas() == -1) {
                    model.addAttribute("msgEmpresa", "Escoger una empresa");
                    model.addAttribute("listaEmpresa", empresaRepository.findAll());
                    model.addAttribute("listaCargo", cargoRepository.findAll());

                    return "Administrador/editarUsuario";
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
                        //pasar del intermdio al db
                        usuarioDb.setNombre(usuario.getNombre());
                        usuarioDb.setApellido(usuario.getApellido());
                        usuarioDb.setCorreo(usuario.getCorreo());
                        usuarioDb.setDni(usuario.getDni());
                        usuarioDb.setDescripcion(usuario.getDescripcion());
                        usuarioDb.setCargo(usuario.getCargo());
                        usuarioRepository.save(usuarioDb);

                        return "redirect:/admin/listaUsuario";

                    } catch (Exception e) {
                        System.out.println("Error al guardar el equipo");
                        throw new RuntimeException(e);
                    }
                } else { //hay al menos 1 error
                    model.addAttribute("listaEmpresa", empresaRepository.findAll());
                    model.addAttribute("listaCargo", cargoRepository.findAll());

                    return "Administrador/editarUsuario";
                }

            } else {
                return "redirect:/admin";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/listaUsuario";
        }


        //todo lo comentado es de foto que ya no se pide
        // Verificar si se cargó un nuevo archivo
//        if (!file.isEmpty()) {
//            try {
//                // Procesar el archivo
//                Archivo archivo = new Archivo();
//                archivo.setNombre(file.getOriginalFilename());
//                archivo.setTipo(1);
//                archivo.setArchivo(file.getBytes());
//                archivo.setContentType(file.getContentType());
//                archivoRepository.save(archivo);
//
//                // Asignar el nuevo archivo al equipo
//                usuario.setArchivo(archivo);
//            } catch (IOException e) {
//                System.out.println("Error al procesar el archivo");
//                throw new RuntimeException(e);
//            }
//        }
//        if (file.getSize() > 0 && !file.getContentType().startsWith("image/")) {
//            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
//            if (usuario.getId() == null) {
//                return "Administrador/crearUsuario";
//            } else {
//                return "Administrador/editarUsuario";
//            }
//        }

    }

    /*EDITAR USUARIO*/
    @GetMapping({"/editarUsuario", "editarusuario"})
    public String editarUsuario(Model model, @RequestParam("id") String idStr,
                                @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult) {

        try {
            int id = parseInt(idStr);
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/admin/listaUsuario";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                usuario = optionalUsuario.get();    //modifiqué Usuario usuario para poder usar @ModelAttribute
                model.addAttribute("usuario", usuario);
                model.addAttribute("listaEmpresa", empresaRepository.findAll());
                model.addAttribute("listaCargo", cargoRepository.findAll());
                return "Administrador/editarUsuario";
            } else {
                return "redirect:/admin";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/listaUsuario";
        }
    }


    //------------------------------ GESTION DE EMPRESAS -----------------------------------------
    @GetMapping({"/listaEmpresa", "/listaempresa"})
    public String listaEmpresa(Model model) {
        List<Empresa> listaEmpresa = empresaRepository.findAll();
        model.addAttribute("listaEmpresa", listaEmpresa);
        return "Administrador/listaEmpresa";
    }

    @GetMapping({"/crearEmpresa", "/crearempresa"})
    public String crearEmpresa(@ModelAttribute("empresa") Empresa empresa) {
        return "Administrador/crearEmpresa";
    }

    @GetMapping("/verEmpresa")
    public String verEmpresa(Model model,
                             @RequestParam("id") String idStr) {

        try {
            int id = parseInt(idStr);
            if (id <= 0 || !empresaRepository.existsById(id)) {
                return "redirect:/admin/listaEmpresa";
            }
            Optional<Empresa> optEmpresa = empresaRepository.findById(id);
            if (optEmpresa.isPresent()) {
                Empresa empresa = optEmpresa.get();
                model.addAttribute("empresa", empresa);
                return "Administrador/vistaEmpresa";
            } else {
                return "redirect:/admin/listaEmpresa";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/listaEmpresa";
        }
    }

    @PostMapping("/guardarEmpresa")
    public String guardarEmpresa(@ModelAttribute("empresa") @Valid Empresa empresa,
                                 BindingResult bindingResult, RedirectAttributes attr) {

        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
            // Obtener la zona horaria GMT-5
            ZoneId zonaHoraria = ZoneId.of("GMT-5");
            // Obtener la fecha actual en la zona horaria GMT-5
            LocalDate fechaAfiliacion = LocalDate.now(zonaHoraria);
            empresa.setFechaAfiliacion(fechaAfiliacion);
            empresaRepository.save(empresa);
            attr.addFlashAttribute("msg1", "La empresa '" + empresa.getNombre() + "' ha sido creado exitosamente");

            return "redirect:/admin/listaEmpresa";
        } else { //hay al menos 1 error
            return "Administrador/crearEmpresa";
        }
    }

    /*PARA VISUALIZAR FOTOS*/
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable("id") int id) {
        Optional<Archivo> opt = archivoRepository.findById(id);

        if (opt.isPresent()) {
            Archivo u = opt.get();

            byte[] imagenComoBytes = u.getArchivo();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType(u.getContentType()));


            return new ResponseEntity<>(
                    imagenComoBytes,
                    httpHeaders,
                    HttpStatus.OK);
        } else {
            return null;
        }
    }


//------------------------------ GESTION DE SITIOS -----------------------------------------

    @GetMapping({"/listaSitio", "/listasitio"})
    public String listaSitio(Model model) {
        List<Sitio> listaSitio = sitioRepository.listaDeSitios();
        model.addAttribute("listaSitio", listaSitio);
        return "Administrador/listaSitio";
    }

    @GetMapping({"/crearSitio", "/crearsitio"})
    public String crearSitio(Model model,
                             @ModelAttribute("sitio") Sitio sitio) {
        return "Administrador/crearSitio";
    }

    @GetMapping({"/verSitio", "/versitio"})
    public String verSitio(Model model, @RequestParam("id") String idStr) {

        try {
            int id = parseInt(idStr);
            if (id <= 0 || !sitioRepository.existsById(id)) {
                return "redirect:/admin/listaSitio";
            }
            Optional<Sitio> optionalSitio = sitioRepository.findById(id);
            if (optionalSitio.isPresent()) {
                Sitio sitio = optionalSitio.get();
                // Obtén la lista de equipos por sitio
                List<SitiosHasEquipo> listaEquipos = sitiosHasEquiposRepository.listaEquiposPorSitio(id);
                model.addAttribute("sitio", sitio);
                model.addAttribute("listaEquipos", listaEquipos);
                return "Administrador/vistaSitio";
            } else {
                return "redirect:/admin/listaSitio";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/listaSitio";
        }
    }

    /*EDITAR Sitio*/
    @GetMapping({"/editarSitio"})
    public String editarSitio(Model model, @RequestParam("id") String idStr,
                              @ModelAttribute("sitio") @Valid Sitio sitio, BindingResult bindingResult) {

        try {
            int id = parseInt(idStr);
            if (id <= 0 || !sitioRepository.existsById(id)) {
                return "redirect:/admin/listaSitio";
            }
            Optional<Sitio> optionalSitio = sitioRepository.findById(id);
            if (optionalSitio.isPresent()) {
                sitio = optionalSitio.get();
                model.addAttribute("sitio", sitio);
                return "Administrador/editarSitio";
            } else {
                return "redirect:/admin/listaSitio";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/listaSitio";
        }
    }

    @GetMapping({"/ubicarSitio", "/ubicarsitio"})
    public String ubicarSitio() {
        return "Administrador/ubicacionSitio";
    }

    @GetMapping({"/inventarioSitio", "/inventariositio"})
    public String inventarioSitio(Model model) {
        List<Ticket> listaT= ticketRepository.findAll();
        model.addAttribute("listaTicket", listaT);
        List<Sitio> sitioList = sitioRepository.listaDeSitios();
        model.addAttribute("sitioList", sitioList);
        return "Administrador/mapaInventarioSitio";
    }

    /*CREAR NUEVO SITIO*/
    @PostMapping("/saveSitio")
    public String saveSitio(@RequestParam("imagenSubida") MultipartFile file,
                            @ModelAttribute("sitio") @Valid Sitio sitio,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes attr) {

        String tipoSeleccionado = sitio.getTipo();
        String tipoZonaSeleccionado = sitio.getTipoZona();
        model.addAttribute("tipoSeleccionado", tipoSeleccionado);
        model.addAttribute("tipoZonaSeleccionado", tipoZonaSeleccionado);

        if (sitio.getTipo() == null || sitio.getTipo().equals("-1")) {
            model.addAttribute("msgTipo", "Escoger un tipo de Sitio");

            if (sitio.getIdSitios() == null) {
                return "Administrador/crearSitio";
            } else {
                return "Administrador/editarSitio";
            }
        }
        if (sitio.getTipoZona() == null || sitio.getTipoZona().equals("-1")) {
            model.addAttribute("msgZona", "Escoger un tipo de zona");
            if (sitio.getIdSitios() == null) {
                return "Administrador/crearSitio";
            } else {
                return "Administrador/editarSitio";
            }
        }
        if (file.getSize() > 0 && !file.getContentType().startsWith("image/")) {
            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
            if (sitio.getIdSitios() == null) {
                return "Administrador/crearSitio";
            } else {
                return "Administrador/editarSitio";
            }
        }

        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
            if (sitio.getArchivo() == null) {
                sitio.setArchivo(new Archivo());
            }
            String fileName = file.getOriginalFilename();
            try {
                Archivo archivo = sitio.getArchivo();
                archivo.setNombre(fileName);
                archivo.setTipo(1);
                archivo.setArchivo(file.getBytes());
                archivo.setContentType(file.getContentType());
                archivoRepository.save(archivo);
                int idImagen = archivo.getIdArchivos();
                sitio.getArchivo().setIdArchivos(idImagen);

                if (sitio.getIdSitios() == null) {
                    attr.addFlashAttribute("msg1", "El sitio '" + sitio.getNombre() + "' ha sido creado exitosamente");
                } else {
                    attr.addFlashAttribute("msg1", "El sitio '" + sitio.getNombre() + "' ha sido actualizado exitosamente");
                }
                //truncar latitud y long
                BigDecimal longitud1 = sitio.getLongitud();
                BigDecimal latitud1 = sitio.getLatitud();
                sitio.setLongitud(longitud1.setScale(7, RoundingMode.DOWN));
                sitio.setLatitud(latitud1.setScale(7, RoundingMode.DOWN));

                sitioRepository.save(sitio);
                return "redirect:/admin/listaSitio";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else { //hay al menos 1 error
            if (sitio.getIdSitios() == null) {
                return "Administrador/crearSitio";
            } else {
                return "Administrador/editarSitio";
            }
        }
    }

    @PostMapping("/updateSitio")
    public String updateSitio(@RequestParam("imagenSubida") MultipartFile file,
                              @ModelAttribute("sitio") @Valid Sitio sitio,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes attr) {

        if (sitio.getTipo() == null || sitio.getTipo().equals("-1")) {
            model.addAttribute("msgTipo", "Escoger un tipo de Sitio");

            if (sitio.getIdSitios() == null) {
                return "Administrador/crearSitio";
            } else {
                return "Administrador/editarSitio";
            }
        }
        if (sitio.getTipoZona() == null || sitio.getTipoZona().equals("-1")) {
            model.addAttribute("msgZona", "Escoger un tipo de zona");
            if (sitio.getIdSitios() == null) {
                return "Administrador/crearSitio";
            } else {
                return "Administrador/editarSitio";
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

                BigDecimal longitud1 = sitio.getLongitud();
                BigDecimal latitud1 = sitio.getLatitud();
                sitio.setLongitud(longitud1.setScale(7, RoundingMode.DOWN));
                sitio.setLatitud(latitud1.setScale(7, RoundingMode.DOWN));
                archivoRepository.save(archivo);

                // Asignar el nuevo archivo al equipo
                sitio.setArchivo(archivo);
            } catch (IOException e) {
                System.out.println("Error al procesar el archivo");
                throw new RuntimeException(e);
            }
        }
        if (file.getSize() > 0 && !file.getContentType().startsWith("image/")) {
            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
            if (sitio.getIdSitios() == null) {
                return "Administrador/crearSitio";
            } else {
                return "Administrador/editarSitio";
            }
        }

        if (!bindingResult.hasErrors()) {
            // Si no hay errores, se realiza el flujo normal
            if (sitio.getArchivo() == null) {
                sitio.setArchivo(new Archivo());
            }

            try {
                if (sitio.getIdSitios() == null) {
                    attr.addFlashAttribute("msg1", "El sitio '" + sitio.getNombre() + "' ha sido creado exitosamente");
                } else {
                    attr.addFlashAttribute("msg1", "El sitio '" + sitio.getNombre() + "' ha sido actualizado exitosamente");
                }
                sitioRepository.save(sitio);
                return "redirect:/admin/listaSitio";
            } catch (Exception e) {
                System.out.println("Error al guardar el equipo");
                throw new RuntimeException(e);
            }
        } else { //hay al menos 1 error
            System.out.println("se mando en sitio, Binding");
            if (sitio.getIdSitios() == null) {
                return "Administrador/crearSitio";
            } else {
                return "Administrador/editarSitio";
            }
        }
    }


    /* Eliminar sitio */
    @GetMapping("/eliminarSitio")
    public String eliminarSitio(@RequestParam("id") int id, RedirectAttributes attr) {
        Optional<Sitio> optionalSitio = sitioRepository.findById(id);
        if (optionalSitio.isPresent()) {
            Sitio sitio = optionalSitio.get();
            sitioRepository.eliminarEmpresa(id);

            attr.addFlashAttribute("msg2", "El sitio '" + sitio.getDistrito() + "' ha sido eliminado exitosamente");

        }
        return "redirect:/admin/listaSitio";
    }


//-----------------------------------------------------------------------

    // GESTION DE EQUIPOS
    @GetMapping({"/listaEquipo", "/listaequipo"})
    public String listaEquipo(Model model) {
        List<Equipo> listaEquipo = equipoRepository.listaEquiposHabilitados();
        model.addAttribute("listaEquipo", listaEquipo);
        return "Administrador/listaEquipo";
    }

    @GetMapping({"/crearEquipo","/crearequipo"})
    public String crearEquipo(Model model, @ModelAttribute("equipo") Equipo equipo){
        model.addAttribute("listaTipoEquipos",tipoEquipoRepository.findAll());
        //para poder mandar "tipoEquipoSeleccionado" por defecto como -1
        Tipoequipo tipoEquipoSeleccionado = equipo.getTipoequipo();
        if (tipoEquipoSeleccionado == null) {
            tipoEquipoSeleccionado = new Tipoequipo();
            tipoEquipoSeleccionado.setIdTipoEquipo(-1);
        }
        model.addAttribute("tipoEquipoSeleccionado",tipoEquipoSeleccionado);

        return "Administrador/crearEquipo";
    }


    @PostMapping("/saveEquipo")
    public String saveEquipo(@RequestParam("imagenSubida") MultipartFile file,
                             @ModelAttribute("equipo") @Valid Equipo equipo, BindingResult bindingResult,
                             Model model,
                             RedirectAttributes attr) {


        //para "guardar" lo seleccionado y poder mostrarlo cuando haya un error y no tener q ponerlo de nuevo
        Tipoequipo tipoEquipoSeleccionado = equipo.getTipoequipo();
        model.addAttribute("tipoEquipoSeleccionado", tipoEquipoSeleccionado);
        if(equipo.getTipoequipo() == null || equipo.getTipoequipo().getIdTipoEquipo() == null){

            model.addAttribute("msgTipoEquipo", "Escoger un tipo de Equipo");
            model.addAttribute("msgImagen", "Escoger un tipo de imagen valido");
            model.addAttribute("listaTipoEquipos", tipoEquipoRepository.findAll());

            if (equipo.getIdEquipos() == null) {
                return "Administrador/crearEquipo";
            } else {
                return "Administrador/editarEquipo";
            }
        }
        if (file.getSize() > 0 && !file.getContentType().startsWith("image/")) {
            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
            if (equipo.getIdEquipos() == null) {
                return "Administrador/crearEquipo";
            } else {
                return "Administrador/editarEquipo";
            }
        }

        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
            if (equipo.getArchivo() == null) {
                equipo.setArchivo(new Archivo());
            }

            String fileName = file.getOriginalFilename();
            try {
                Archivo archivo = equipo.getArchivo();
                archivo.setNombre(fileName);
                archivo.setTipo(1);
                archivo.setArchivo(file.getBytes());
                archivo.setContentType(file.getContentType());
                archivoRepository.save(archivo);
                int idImagen = archivo.getIdArchivos();
                equipo.getArchivo().setIdArchivos(idImagen);
                if (equipo.getIdEquipos() == null) {
                    attr.addFlashAttribute("msg", "El equipo '" + equipo.getModelo() + "' ha sido creado exitosamente");
                } else {
                    attr.addFlashAttribute("msg", "El equipo '" + equipo.getModelo() + "' ha sido actualizado exitosamente");
                }
                equipoRepository.save(equipo);
                return "redirect:/admin/listaEquipo";
            } catch (IOException e) {
                System.out.println("error save");
                throw new RuntimeException(e);
            }

        } else { //hay al menos 1 error
            model.addAttribute("listaTipoEquipos", tipoEquipoRepository.findAll());
            if (equipo.getIdEquipos() == null) {
                return "Administrador/crearEquipo";
            } else {
                return "Administrador/editarEquipo";
            }
        }
    }

    @PostMapping("/updateEquipo")
    public String updateEquipo(@RequestParam("imagenSubida") MultipartFile file,
                               @ModelAttribute("equipo") @Valid Equipo equipo, BindingResult bindingResult,
                               Model model,
                               RedirectAttributes attr) {

        if (equipo.getTipoequipo() == null || equipo.getTipoequipo().getIdTipoEquipo() == null) {
            model.addAttribute("msgTipoEquipo", "Escoger un tipo de Equipo");
            model.addAttribute("listaTipoEquipos", tipoEquipoRepository.findAll());

            if (equipo.getIdEquipos() == null) {
                return "Administrador/crearEquipo";
            } else {
                return "Administrador/editarEquipo";
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
                equipo.setArchivo(archivo);
            } catch (IOException e) {
                System.out.println("Error al procesar el archivo");
                throw new RuntimeException(e);
            }
        }
        if (file.getSize() > 0 && !file.getContentType().startsWith("image/")) {
            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
            if (equipo.getIdEquipos() == null) {
                return "Administrador/crearEquipo";
            } else {
                return "Administrador/editarEquipo";
            }
        }

        if (!bindingResult.hasErrors()) {
            // Si no hay errores, se realiza el flujo normal
            if (equipo.getArchivo() == null) {
                equipo.setArchivo(new Archivo());
            }

            try {
                if (equipo.getIdEquipos() == null) {
                    attr.addFlashAttribute("msg", "El equipo '" + equipo.getModelo() + "' ha sido creado exitosamente");
                } else {
                    attr.addFlashAttribute("msg", "El equipo '" + equipo.getModelo() + "' ha sido actualizado exitosamente");
                }

                equipoRepository.save(equipo);
                return "redirect:/admin/listaEquipo";
            } catch (Exception e) {
                System.out.println("Error al guardar el equipo");
                throw new RuntimeException(e);
            }
        } else {
            // Hay al menos 1 error
            model.addAttribute("listaTipoEquipos", tipoEquipoRepository.findAll());
            if (equipo.getIdEquipos() == null) {
                return "Administrador/crearEquipo";
            } else {
                return "Administrador/editarEquipo";
            }
        }
    }

    @GetMapping({"/verEquipo", "/verequipo"})
    public String verEquipo(Model model, @RequestParam("id") String idStr) {
        try {
            int id = parseInt(idStr);
            if (id <= 0 || !equipoRepository.existsById(id)) {
                return "redirect:/admin/listaEquipo";
            }
            Optional<Equipo> optionalEquipo = equipoRepository.findById(id);
            if (optionalEquipo.isPresent()) {
                Equipo equipo = optionalEquipo.get();
                model.addAttribute("equipo", equipo);
                return "Administrador/vistaEquipo";
            } else {
                return "redirect:/admin/listaEquipo";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/listaEquipo";
        }
    }


    @GetMapping({"/editarEquipo", "/editarequipo"})
    public String editarEquipo(Model model, @RequestParam("id") String idStr, @ModelAttribute("equipo") Equipo equipo) {
        try {
            int id = parseInt(idStr);
            if (id <= 0 || !equipoRepository.existsById(id)) {
                return "redirect:/admin/listaEquipo";
            }
            Optional<Equipo> optionalEquipo = equipoRepository.findById(id);
            if (optionalEquipo.isPresent()) {
                equipo = optionalEquipo.get();
                model.addAttribute("equipo", equipo);
                model.addAttribute("listaTipoEquipos", tipoEquipoRepository.findAll());
                return "Administrador/editarEquipo";
            } else {
                return "redirect:/admin/listaEquipo";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/listaEquipo";
        }
    }

    @GetMapping("/deshabilitarEquipo")
    public String deshabilitarEquipo(@RequestParam("id") int id, RedirectAttributes attr) {
        Optional<Equipo> optionalEquipo = equipoRepository.findById(id);
        if (optionalEquipo.isPresent()) {
            Equipo equipo = optionalEquipo.get();
            equipoRepository.deshabilitarEquipo(id);
            attr.addFlashAttribute("msg1", "El equipo '" + equipo.getTipoequipo().getNombreTipo() + "' ha sido eliminado exitosamente");

        }
        return "redirect:/admin/listaEquipo";
    }

    // ------------------------------------- Acabo equipos -----------------------------

    /* PERFIL DEL ADMINISTRADOR */
    @PostMapping("/savePerfil")
    public String savePerfil(@RequestParam("imagenSubida") MultipartFile file,
                             @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                             Model model,
                             RedirectAttributes attr, HttpSession httpSession) {


        if (usuario.getCargo() == null || usuario.getCargo().getIdCargos() == null || usuario.getCargo().getIdCargos() == -1) {
            model.addAttribute("msgCargo", "Escoger un cargo");
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());

            if (usuario.getId() == null) {
                return "Administrador/perfil";
            } else {
                return "Administrador/perfilEditar";
            }
        }
        if (usuario.getEmpresa() == null || usuario.getEmpresa().getIdEmpresas() == null || usuario.getEmpresa().getIdEmpresas() == -1) {
            model.addAttribute("msgEmpresa", "Escoger una empresa");
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            if (usuario.getId() == null) {
                return "Administrador/perfil";
            } else {
                return "Administrador/perfilEditar";
            }
        }
        /*Validación de imagen*/
        if (file.getSize() > 0 && !file.getContentType().startsWith("image/") && !file.isEmpty()) {
            model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
            if (usuario.getId() == null) {
                return "Administrador/perfil";
            } else {
                return "Administrador/perfilEditar";
            }
        }

        String fileName1 = file.getOriginalFilename();
        /*Validación para evitar 2 puntos*/
        if (fileName1.contains("..") && !file.isEmpty()) {
            model.addAttribute("msgImagen", "No se permiten '..' en el archivo ");
            if (usuario.getId() == null) {
                return "Administrador/perfil";
            } else {
                return "Administrador/perfilEditar";
            }
        }

        /*Validación para archivos grande (NO FUNCIONA :C)*/
        int maxFileSize = 10485760;
        if (file.getSize() > maxFileSize && !file.isEmpty()) {
            System.out.println(file.getSize());
            model.addAttribute("msgImagen1", "El archivo subido excede el tamaño máximo permitido (10MB).");
            if (usuario.getId() == null) {
                return "Administrador/perfil";
            } else {
                return "redirect:/admin/perfilEditar";
            }
        }

        if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
            if (usuario.getArchivo() == null) {
                usuario.setArchivo(new Archivo());
            }

            try {
                /*Si file contiene algo --> Guardarlo*/
                if(!file.isEmpty()){
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
                }

                if (usuario.getId() == null) {
                    attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha creado exitosamente");
                } else {
                    attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha actualizado exitosamente");
                }
                usuarioRepository.save(usuario);

                session.setAttribute("usuario", usuario);
                return "redirect:/admin/perfil";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else { //hay al menos 1 error
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            if (usuario.getId() == null) {
                return "Administrador/perfil";
            } else {
                return "Administrador/perfilEditar";
            }
        }
    }

    @GetMapping({"/perfilEditar"})
    public String perfilEditar(Model model,
                               @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession) {

        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        int id = u.getId();
        try {
            //int id = Integer.parseInt(idStr);
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/admin/listaUsuario";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                usuario = optionalUsuario.get();    //modifiqué Usuario usuario para poder usar @ModelAttribute
                model.addAttribute("usuario", usuario);
                model.addAttribute("listaEmpresa", empresaRepository.findAll());
                model.addAttribute("listaCargo", cargoRepository.findAll());
                return "Administrador/perfilEditar";
            } else {
                return "redirect:/admin/perfil";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/listaUsuario";
        }

    }

    @GetMapping({"/perfilContra"})
    public String perfilContra(Model model,
                               @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession) {
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        int id = u.getId();
        try {
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/admin/listaUsuario";
            }
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                model.addAttribute("idUsuario", id);
                return "Administrador/perfilContra";
            } else {
                return "redirect:/admin/perfil";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/listaUsuario";
        }
    }

    @PostMapping({"/actualizarContra"})
    public String actualizarContra(Model model, @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, HttpSession httpSession,
                                   @RequestParam("password") String contrasenia,
                                   @RequestParam("newpassword") String contraseniaNueva, @RequestParam("renewpassword") String contraseniaConfirm,
                                   RedirectAttributes redirectAttributes) {
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        int id = u.getId();

        String contraseniaAlmacenada = usuarioRepository.obtenerContraseña(id);

        if (passwordEncoder.matches(contrasenia, contraseniaAlmacenada)) {
            String contraseniaNuevaEncriptada = passwordEncoder.encode(contraseniaNueva);
            usuarioRepository.actualizarContraA(contraseniaNuevaEncriptada, id);
            redirectAttributes.addFlashAttribute("msg1", "La contraseña se ha actualizado exitosamente");

            return "redirect:/admin/perfil";
        } else {
            redirectAttributes.addFlashAttribute("error", "La contraseña actual no es correcta.");
            return "redirect:/admin/perfilContra";
        }
    }

    // ------------------------------------- Acabo Perfil del administrador -----------------------------


    // Direcciona al form CAMPOS DINAMICOS
    @GetMapping({"/crearCampo", "/crearcampo"})
    public String crearCampo(Model model,
                             @ModelAttribute("equipo") Equipo equipo) {
        model.addAttribute("listaSitios", sitioRepository.findAll());
        model.addAttribute("listaTipoEquipos", tipoEquipoRepository.findAll());
        return "Administrador/campoDinamico";
    }

    // CREAR NUEVO CAMPO DINAMICO (POST)
    @GetMapping({"/saveCampo", "/savecampo"})
    public String saveCampo(@ModelAttribute("sitio") Sitio sitio,
                            @RequestParam("campo") String campo,
                            @RequestParam("valor") String valor) {
        CampoDinamico nuevoCampo = new CampoDinamico();
        nuevoCampo.setNombre(campo);
        nuevoCampo.setValor(valor);
        nuevoCampo.setSitio(sitio);

        // sitio.getCamposDinamicos().add(nuevoCampo);
        return "redirect:/admin/listaSitio";
    }

}