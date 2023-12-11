package com.example.nexusgtics.controllers;

import com.example.nexusgtics.controllers.Email.Authenticate;
import com.example.nexusgtics.controllers.Email.LoginRequest;
import com.example.nexusgtics.entity.*;
import com.example.nexusgtics.repository.*;
import jakarta.activation.DataSource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.math.RoundingMode;

import static com.example.nexusgtics.controllers.GcsController.downloadObject;
import static com.example.nexusgtics.controllers.GcsController.uploadObject;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;
import static java.sql.DriverManager.getConnection;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static List<String> listaCampos = new ArrayList<>();
    private static List<String> listaCamposEquipos = new ArrayList<>();
    @Autowired
    private JdbcTemplate jdbcTemplate;

//    @Autowired
//    private DataSource dataSource;

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

    /* DIRECCIONA A LA VISTA PARA VER DETALLES DEL "USUARIO" */
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

    /* DIRECCIONA AL FORMULARIO DE CREAR USUARIO */
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

        //validar que analistas->nexus otros roles->otras empresas
        if ((usuario.getCargo().getIdCargos()== 3 || usuario.getCargo().getIdCargos()== 4) && usuario.getEmpresa().getIdEmpresas()!=1  ) {
            model.addAttribute("msgNexus1", "Los analistas solo pueden pertenecer a la empresa Nexus");
            model.addAttribute("listaEmpresa", empresaRepository.findAll());
            model.addAttribute("listaCargo", cargoRepository.findAll());
            return "Administrador/crearUsuario";
        }
        if ((usuario.getCargo().getIdCargos()== 5 || usuario.getCargo().getIdCargos()== 6) && usuario.getEmpresa().getIdEmpresas()==1  ) {
            model.addAttribute("msgNexus2", "Los supervisores y técnicos solo pueden pertenecer a empresas externas");
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
        System.out.println("pasó el filtro de mail: "+ usuario.getCorreo());

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
                archivo.setArchivo(image);
                archivo.setContentType("image/png");
                Archivo archivo1 = archivoRepository.save(archivo);
                String nombreArchivo = "archivo-"+archivo1.getIdArchivos()+".png";
                archivo1.setNombre(nombreArchivo);
                archivoRepository.save(archivo1);
                uploadObject(archivo1);
                archivo1.setArchivo(null);
//              mensaje de creación
                attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha creado exitosamente");
                usuarioRepository.save(usuario);
                System.out.println("se guardó");
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
                                @ModelAttribute("usuario") @Valid Usuario usuario,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes attr) {
//        Usuario u = (Usuario) httpSession.getAttribute("usuario");
//        Integer idSupervisor = u.getId();
//
//        model.addAttribute("listaTickets",ticketRepository.listaTicketsModificado( 1, idSupervisor));
        try {
            int id = usuario.getId();
            System.out.println("El ID obtenido fue: "+id);
            if (id <= 0 || !usuarioRepository.existsById(id)) {
                return "redirect:/admin/listaUsuario";
            }

            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);

            if (optionalUsuario.isPresent()) {
                System.out.println(" SI INGRESA AQUIIII");
                Usuario usuarioDb = optionalUsuario.get();    //se crea un usuarioDb al que se le pasarán los campos del form que fueron a usuario
                usuario.setArchivo(optionalUsuario.get().getArchivo());
                //validar que no se repitan los emails
                Integer id1 = usuario.getId();

//                List<String> correos = usuarioRepository.listaCorreos2(id1);
//                for (String correo : correos) {
//                    if (correo.equals(usuario.getCorreo())) {
//                        model.addAttribute("msgEmail", "El correo electrónico ya existe");
//                        model.addAttribute("listaEmpresa", empresaRepository.findAll());
//                        model.addAttribute("listaCargo", cargoRepository.findAll());
//                        return "Administrador/editarUsuario";
//                    }
//                }

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
                //validar que analistas->nexus supervisor y técnicos->otras empresas
                if ((usuario.getCargo().getIdCargos()== 3 || usuario.getCargo().getIdCargos()== 4) && usuario.getEmpresa().getIdEmpresas()!=1  ) {
                    model.addAttribute("msgNexus1", "Los analistas solo pueden pertenecer a la empresa Nexus");
                    model.addAttribute("listaEmpresa", empresaRepository.findAll());
                    model.addAttribute("listaCargo", cargoRepository.findAll());
                    return "Administrador/editarUsuario";
                }
                if ((usuario.getCargo().getIdCargos()== 5 || usuario.getCargo().getIdCargos()== 6) && usuario.getEmpresa().getIdEmpresas()==1  ) {
                    model.addAttribute("msgNexus2", "Los supervisores y técnicos solo pueden pertenecer a empresas externas");
                    model.addAttribute("listaEmpresa", empresaRepository.findAll());
                    model.addAttribute("listaCargo", cargoRepository.findAll());
                    return "Administrador/editarUsuario";
                }
                //------------------------------------------
                if (!bindingResult.hasErrors()) {
                    // Si no hay errores, se realiza el flujo normal
                    if (usuarioDb.getArchivo() == null) {
                        usuarioDb.setArchivo(usuario.getArchivo());
                    }
                    try {
                        //pasar del intermdio al db
                        usuarioDb.setNombre(usuario.getNombre());
                        usuarioDb.setApellido(usuario.getApellido());
                        usuarioDb.setCorreo(usuario.getCorreo());
                        usuarioDb.setDni(usuario.getDni());
                        usuarioDb.setDescripcion(usuario.getDescripcion());
                        usuarioDb.setCargo(usuario.getCargo());
                        usuarioDb.setEmpresa(usuario.getEmpresa());
                        usuarioRepository.save(usuarioDb);
                        attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha actualizado exitosamente");

                        return "redirect:/admin/listaUsuario";
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else { //hay al menos 1 error
                    System.out.println("error binding, else");
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
                Usuario usuario1 = optionalUsuario.get();    //modifiqué Usuario usuario para poder usar @ModelAttribute
                System.out.printf(usuario1.getNombre() + " " + usuario1.getArchivo().getIdArchivos());
                model.addAttribute("usuario", usuario1);
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
        model.addAttribute("camposNuevos", listaCampos);
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

        System.out.println("Tamañao en bits: " +file.getSize());
        System.out.println("Es vacio: " +file.isEmpty());

        if (sitio.getTipo() == null || sitio.getTipo().equals("-1")) {
            model.addAttribute("msgTipo", "Escoger un tipo de Sitio");

            if (sitio.getIdSitios() == null) {
                return "Administrador/crearSitio";
            } else {
                return "Administrador/editarSitio";
            }
        }

        if(file.getSize()>1){
            /*VALIDACIÓN PARA QUE EL NOMBRE DEL ARHCIVO SEA MENOR A 40*/
            if (file.getOriginalFilename().length() > 40) {
                model.addAttribute("msgCadena", "El nombre del archivo no debe sobrepasar los 45 carácteres");

                if (sitio.getIdSitios() == null) {
                    return "Administrador/crearSitio";
                } else {
                    return "Administrador/editarSitio";
                }
            }

            /*VALIDACION DE EXTENSIÓN*/
            String extensionValida = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
            if (!extensionValida.equals("png") && !extensionValida.equals("jpg") && !extensionValida.equals("jpeg")) {
                model.addAttribute("msgExtension", "Solo se permiten archivos con extensión png, jpg y jpeg");
                if (sitio.getIdSitios() == null) {
                    return "Administrador/crearSitio";
                } else {
                    return "Administrador/editarSitio";
                }
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


        /*VALIDACION DE PESO - NO FUNCIONA CORRECTAMENTE*/
        long maximo = 100971520L;
        if (file.getSize() > maximo ) {
            model.addAttribute("msgPeso", "El archivo subido supera los 100MB");
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
            try {
                if(file.getSize()>1){
                    // Obtenemos el nombre del archivo
                    String fileName = file.getOriginalFilename();
                    String extension = "";
                    int i = fileName.lastIndexOf('.');
                    if (i > 0) {
                        extension = fileName.substring(i+1);
                    }
                    Archivo archivo = sitio.getArchivo();
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
                }else {
                    Archivo archivo = sitio.getArchivo();
                    archivo.setNombre("fotoSitio");
                    archivo.setTipo(1);
                    byte[] image = downloadObject("labgcp-401300", "proyecto-gtics", "siteDefault.png");
                    archivo.setArchivo(image);
                    archivo.setContentType("image/png");
                    Archivo archivo1 = archivoRepository.save(archivo);
                    String nombreArchivo = "archivo-"+archivo1.getIdArchivos()+".png";
                    archivo1.setNombre(nombreArchivo);
                    archivoRepository.save(archivo1);
                    uploadObject(archivo1);
                    archivo1.setArchivo(null);

                }

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

    @PutMapping("/updateSitio")
    public String updateSitio(@RequestParam("imagenSubida") MultipartFile file,
                              @ModelAttribute("sitio") @Valid Sitio sitio,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes attr) {

        try{

            Integer idSitio = sitio.getIdSitios();
            Optional<Sitio> optionalSitio = sitioRepository.findById(idSitio);

            if (optionalSitio.isPresent()) {
                Sitio sitioBD = optionalSitio.get();
                sitio.setArchivo(optionalSitio.get().getArchivo());

                if (sitio.getTipo() == null || sitio.getTipo().equals("-1")) {
                    model.addAttribute("msgTipo", "Escoger un tipo de Sitio");
                    return "Administrador/editarSitio";
                }

                if (sitio.getTipoZona() == null || sitio.getTipoZona().equals("-1")) {
                    model.addAttribute("msgZona", "Escoger un tipo de zona");
                    return "Administrador/editarSitio";
                }

                if(file.getSize()>1){
                    /*VALIDACIÓN PARA QUE EL NOMBRE DEL ARHCIVO SEA MENOR A 40*/
                    if (file.getOriginalFilename().length() > 40) {
                        model.addAttribute("msgCadena", "El nombre del archivo no debe sobrepasar los 45 carácteres");
                        return "Administrador/editarSitio";
                    }

                    /*VALIDACION DE EXTENSIÓN*/
                    String extensionValida = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
                    if (!extensionValida.equals("png") && !extensionValida.equals("jpg") && !extensionValida.equals("jpeg")) {
                        model.addAttribute("msgExtension", "Solo se permiten archivos con extensión png, jpg y jpeg");
                        return "Administrador/editarSitio";

                    }

                }

                if (file.getSize() > 0 && !file.getContentType().startsWith("image/")) {
                    model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
                    return "Administrador/editarSitio";
                }

                String fileName1 = file.getOriginalFilename();
                if (fileName1.contains("..") && !file.isEmpty()) {
                    model.addAttribute("msgImagen", "No se permiten '..' en el archivo ");
                    return "Administrador/editarSitio";
                }

                int maxFileSize = 10485760;
                if (file.getSize() > maxFileSize && !file.isEmpty()) {
                    System.out.println(file.getSize());
                    model.addAttribute("msgImagen1", "El archivo subido excede el tamaño máximo permitido (10MB).");
                    return "redirect:/administrador/editarSitio";
                }

                if (!bindingResult.hasErrors()) {
                    // Si no hay errores, se realiza el flujo normal
                    if (sitio.getArchivo() == null) {
                        sitio.setArchivo(new Archivo());
                    }
                    try {
                        if(file.getSize()>1){
                            // Obtenemos el nombre del archivo
                            String fileName = file.getOriginalFilename();
                            String extension = "";
                            int i = fileName.lastIndexOf('.');
                            if (i > 0) {
                                extension = fileName.substring(i+1);
                            }
                            Archivo archivo = sitio.getArchivo();
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
                            sitioBD.setArchivo(archivo1);
                        }

                        if (sitio.getIdSitios() == null) {
                            attr.addFlashAttribute("msg1", "El sitio '" + sitio.getNombre() + "' ha sido creado exitosamente");
                        } else {
                            attr.addFlashAttribute("msg1", "El sitio '" + sitio.getNombre() + "' ha sido actualizado exitosamente");
                        }

                        sitioBD.setNombre(sitio.getNombre());
                        sitioBD.setDepartamento(sitio.getDepartamento());
                        sitioBD.setProvincia(sitio.getProvincia());
                        sitioBD.setDistrito(sitio.getDistrito());
                        sitioBD.setUbigeo(sitio.getUbigeo());
                        sitioBD.setLatitud(sitio.getLatitud());
                        sitioBD.setLongitud(sitio.getLongitud());
                        sitioBD.setTipoZona(sitio.getTipoZona());
                        sitioBD.setTipo(sitio.getTipo());

//                        BigDecimal longitud1 = sitio.getLongitud();
//                        BigDecimal latitud1 = sitio.getLatitud();
//                        sitio.setLongitud(longitud1.setScale(7, RoundingMode.DOWN));
//                        sitio.setLatitud(latitud1.setScale(7, RoundingMode.DOWN));
                        sitioRepository.save(sitioBD);
                        return "redirect:/admin/listaSitio";
                    } catch (Exception e) {
                        System.out.println("Error al guardar el equipo");
                        throw new RuntimeException(e);
                    }
                } else { //hay al menos 1 error
                    System.out.println("se mando en sitio, Binding");
                    return "Administrador/editarSitio";
                }
            } else {
                return "redirect:/admin";
            }

        } catch (NumberFormatException e) {
            return "redirect:/admin/listaUsuario";
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
        model.addAttribute("camposNuevos", listaCamposEquipos);

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

        if(file.getSize()>1){
            /*VALIDACIÓN PARA QUE EL NOMBRE DEL ARHCIVO SEA MENOR A 40*/
            if (file.getOriginalFilename().length() > 40) {
                model.addAttribute("msgCadena", "El nombre del archivo no debe sobrepasar los 45 carácteres");

                if (equipo.getIdEquipos() == null) {
                    return "Administrador/crearEquipo";
                } else {
                    return "Administrador/editarEquipo";
                }
            }

            /*VALIDACION DE EXTENSIÓN*/
            String extensionValida = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
            if (!extensionValida.equals("png") && !extensionValida.equals("jpg") && !extensionValida.equals("jpeg")) {
                model.addAttribute("msgExtension", "Solo se permiten archivos con extensión png, jpg y jpeg");
                if (equipo.getIdEquipos() == null) {
                    return "Administrador/crearEquipo";
                } else {
                    return "Administrador/editarEquipo";
                }
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

            try {

                if(file.getSize()>1){
                    // Obtenemos el nombre del archivo
                    String fileName = file.getOriginalFilename();
                    String extension = "";
                    int i = fileName.lastIndexOf('.');
                    if (i > 0) {
                        extension = fileName.substring(i+1);
                    }
                    Archivo archivo = equipo.getArchivo();
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
                }else {
                    Archivo archivo = equipo.getArchivo();
                    archivo.setNombre("fotoEquipo");
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

    @PutMapping("/updateEquipo")
    public String updateEquipo(@RequestParam("imagenSubida") MultipartFile file,
                               @ModelAttribute("equipo") @Valid Equipo equipo, BindingResult bindingResult,
                               Model model,
                               RedirectAttributes attr) {
        try{

            Integer idEquipos = equipo.getIdEquipos();
            Optional<Equipo> optionalEquipo = equipoRepository.findById(idEquipos);

            if (optionalEquipo.isPresent()) {
                Equipo equipoBD = optionalEquipo.get();
                equipo.setArchivo(optionalEquipo.get().getArchivo());
                if (equipo.getTipoequipo() == null || equipo.getTipoequipo().getIdTipoEquipo() == null) {
                    model.addAttribute("msgTipoEquipo", "Escoger un tipo de Equipo");
                    model.addAttribute("listaTipoEquipos", tipoEquipoRepository.findAll());
                    return "Administrador/editarEquipo";
                }

                if(file.getSize()>1){

                    /*VALIDACIÓN PARA QUE EL NOMBRE DEL ARHCIVO SEA MENOR A 40*/
                    if (file.getOriginalFilename().length() > 40) {
                        model.addAttribute("msgCadena", "El nombre del archivo no debe sobrepasar los 45 carácteres");
                        return "Administrador/editarEquipo";
                    }

                    /*VALIDACION DE EXTENSIÓN*/
                    String extensionValida = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
                    if (!extensionValida.equals("png") && !extensionValida.equals("jpg") && !extensionValida.equals("jpeg")) {
                        model.addAttribute("msgExtension", "Solo se permiten archivos con extensión png, jpg y jpeg");
                        return "Administrador/editarEquipo";
                    }
                }

                if (file.getSize() > 0 && !file.getContentType().startsWith("image/")) {
                    model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
                    return "Administrador/editarEquipo";
                }

                String fileName1 = file.getOriginalFilename();
                if (fileName1.contains("..") && !file.isEmpty()) {
                    model.addAttribute("msgImagen", "No se permiten '..' en el archivo ");
                    return "Administrador/editarEquipo";
                }

                int maxFileSize = 10485760;
                if (file.getSize() > maxFileSize && !file.isEmpty()) {
                    System.out.println(file.getSize());
                    model.addAttribute("msgImagen1", "El archivo subido excede el tamaño máximo permitido (10MB).");
                    return "redirect:/administrador/editarEquipo";
                }

                if (!bindingResult.hasErrors()) {
                    // Si no hay errores, se realiza el flujo normal
                    if (equipo.getArchivo() == null) {
                        equipo.setArchivo(new Archivo());
                    }

                    try {

                        if(file.getSize()>1){
                            // Obtenemos el nombre del archivo
                            String fileName = file.getOriginalFilename();
                            String extension = "";
                            int i = fileName.lastIndexOf('.');
                            if (i > 0) {
                                extension = fileName.substring(i+1);
                            }
                            Archivo archivo = equipo.getArchivo();
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
                            equipoBD.setArchivo(archivo1);
                        }

                        if (equipo.getIdEquipos() == null) {
                            attr.addFlashAttribute("msg", "El equipo '" + equipo.getModelo() + "' ha sido creado exitosamente");
                        } else {
                            attr.addFlashAttribute("msg", "El equipo '" + equipo.getModelo() + "' ha sido actualizado exitosamente");
                        }

                        equipoBD.setTipoequipo(equipo.getTipoequipo());
                        equipoBD.setMarca(equipo.getMarca());
                        equipoBD.setModelo(equipo.getModelo());
                        equipoBD.setDescripcion(equipo.getDescripcion());
                        equipoBD.setPaginaModelo(equipo.getPaginaModelo());

                        equipoRepository.save(equipoBD);
                        return "redirect:/admin/listaEquipo";
                    } catch (Exception e) {
                        System.out.println("Error al guardar el equipo");
                        throw new RuntimeException(e);
                    }
                } else {
                    // Hay al menos 1 error
                    model.addAttribute("listaTipoEquipos", tipoEquipoRepository.findAll());
                    return "Administrador/editarEquipo";
                }

            } else {
                return "redirect:/admin";
            }

        } catch (NumberFormatException e) {
            return "redirect:/admin/editarEquipo";
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
    @PutMapping("/savePerfil")
    public String savePerfil(@RequestParam("imagenSubida") MultipartFile file,
                             @ModelAttribute("usuario") @Valid Usuario usuario,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes attr, HttpSession httpSession) {

        try {
            Integer idUsuario = usuario.getId();
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(idUsuario);

            if (optionalUsuario.isPresent()) {
                Usuario usuarioDB = optionalUsuario.get();

                if(file.getSize()>1){
                    if (file.getOriginalFilename().length() > 40) {
                        model.addAttribute("msgCadena", "El nombre del archivo no debe sobrepasar los 45 carácteres");
                        return "Administrador/perfilEditar";
                    }
                    String extensionValida = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
                    if (!extensionValida.equals("png") && !extensionValida.equals("jpg") && !extensionValida.equals("jpeg")) {
                        model.addAttribute("msgExtension", "Solo se permiten archivos con extensión png, jpg y jpeg");
                        return "Administrador/perfilEditar";
                    }
                }


                if (file.getSize() > 0 && !file.getContentType().startsWith("image/") && !file.isEmpty()) {
                    model.addAttribute("msgImagen", "El archivo subido no es una imagen válida");
                    return "Administrador/perfilEditar";
                }
                String fileName1 = file.getOriginalFilename();
                if (fileName1.contains("..") && !file.isEmpty()) {
                    model.addAttribute("msgImagen1", "No se permiten '..' en el archivo ");
                    return "Administrador/perfilEditar";
                }

                int maxFileSize = 10485760;
                if (file.getSize() > maxFileSize && !file.isEmpty()) {
                    System.out.println(file.getSize());
                    model.addAttribute("msgPeso", "El archivo subido excede el tamaño máximo permitido (10MB).");
                    return "redirect:/admin/perfilEditar";
                }

                if (!bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
                    if (usuario.getArchivo() == null) {
                        usuario.setArchivo(new Archivo());
                    }

                    try {
                        if (file.getSize() > 1) {
                            // Obtenemos el nombre del archivo
                            String fileName = file.getOriginalFilename();
                            String extension = "";
                            int i = fileName.lastIndexOf('.');
                            if (i > 0) {
                                extension = fileName.substring(i + 1);
                            }
                            Archivo archivo = usuario.getArchivo();
                            archivo.setNombre(fileName);
                            archivo.setTipo(1);
                            archivo.setArchivo(file.getBytes());
                            archivo.setContentType(file.getContentType());
                            Archivo archivo1 = archivoRepository.save(archivo);
                            String nombreArchivo = "archivo-" + archivo1.getIdArchivos() + "." + extension;
                            archivo1.setNombre(nombreArchivo);
                            archivoRepository.save(archivo1);
                            uploadObject(archivo1);
                            archivo1.setArchivo(null);
                            usuarioDB.setArchivo(archivo1);
                        }
                        if (usuario.getId() == null) {
                            attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha creado exitosamente");
                        } else {
                            attr.addFlashAttribute("msg", "El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' se ha actualizado exitosamente");
                        }

                        usuarioDB.setNombre(usuario.getNombre());
                        usuarioDB.setApellido(usuario.getApellido());
                        usuarioDB.setCorreo(usuario.getCorreo());
                        usuarioDB.setDescripcion(usuario.getDescripcion());
                        usuarioRepository.save(usuarioDB);
                        session.setAttribute("usuario", usuarioDB);
                        return "redirect:/admin/perfil";
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } else { //hay al menos 1 error
                    return "Administrador/perfilEditar";
                }
            }else {
                return "redirect:/admin/";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/listaUsuario";
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

    @RestController
    @RequestMapping("/auth")
    public static class AuthController {

        Authenticate authenticate;

        public AuthController(Authenticate authenticate) {
            this.authenticate = authenticate;
        }

        @PostMapping("/authenticate")
        public ResponseEntity<Object> authenticate(@RequestBody LoginRequest loginRequest){

            authenticate.sendMessageUser(loginRequest.getEmailUser(), loginRequest.getMessage());

            return  ResponseEntity.ok()
                    .body("HOLA...");
        }
    }

    private static boolean existeElementoSinDistincion(List<String> lista, String nuevoElemento) {
        for (String elemento : lista) {
            if (elemento.equalsIgnoreCase(nuevoElemento)) {
                return true;
            }
        }
        return false;
    }



    /* CAMPOS DINAMICOS */
    @PostMapping("/campoDinamico")
    public String campoDinamico(@RequestParam("campo") String campo,
                                    @RequestParam("tipoDato") int tipoDato,RedirectAttributes redirectAttributes) {

        if (campo.isEmpty() || campo.isBlank() || (tipoDato != 1 && tipoDato != 2 && tipoDato != 3)){
            redirectAttributes.addFlashAttribute("msg4", "Los campos no deben estar vacíos");
            return "redirect:/admin/listaSitio";
        }

        if (!existeElementoSinDistincion(listaCampos, campo)) {
            String sql = "ALTER TABLE nexus.sitios ADD COLUMN " + campo + " " +
                    (tipoDato == 1 ? "DOUBLE" : tipoDato == 2 ? "INT" : "VARCHAR(255)");
            jdbcTemplate.execute(sql);
            System.out.println(listaCampos);
            listaCampos.add(campo);
            redirectAttributes.addFlashAttribute("msg3", "Se ha creado exitosamente el campo '" + campo +"' ");
            return "redirect:/admin/listaSitio";

        } else {

            redirectAttributes.addFlashAttribute("msg4", "El campo '" + campo +"' ya existe en la tabla ");
            return "redirect:/admin/listaSitio";
        }

    }


    @PostMapping("/eliminarCampoDinamico")
    public String eliminarCampoDinamico(@RequestParam("campo") String campo,
                                    RedirectAttributes redirectAttributes) {

        String sql = "ALTER TABLE nexus.sitios DROP COLUMN " + campo;
        System.out.println(sql);
        jdbcTemplate.execute(sql);
        listaCampos.remove(campo);
        redirectAttributes.addFlashAttribute("msg3", "Se ha eliminado exitosamente el campo '" + campo +"' ");
        System.out.println(listaCampos);
        return "redirect:/admin/listaSitio";
    }


    /*CAMPOS DINAMICOS DE EQUIPOS*/
    /* CAMPOS DINAMICOS */
    @PostMapping("/campoDinamicoEquipo")
    public String campoDinamicoEquipo(@RequestParam("campo") String campo,
                                @RequestParam("tipoDato") int tipoDato,RedirectAttributes redirectAttributes) {

        if (campo.isEmpty() || campo.isBlank() || (tipoDato != 1 && tipoDato != 2 && tipoDato != 3)){
            redirectAttributes.addFlashAttribute("msg4", "Los campos no deben estar vacíos");
            return "redirect:/admin/listaEquipo";
        }

        if (!existeElementoSinDistincion(listaCamposEquipos, campo)) {
            String sql = "ALTER TABLE nexus.equipos ADD COLUMN " + campo + " " +
                    (tipoDato == 1 ? "DOUBLE" : tipoDato == 2 ? "INT" : "VARCHAR(255)");
            jdbcTemplate.execute(sql);
            System.out.println(listaCamposEquipos);
            listaCamposEquipos.add(campo);
            redirectAttributes.addFlashAttribute("msg3", "Se ha creado exitosamente el campo '" + campo +"' ");
            return "redirect:/admin/listaEquipo";

        } else {

            redirectAttributes.addFlashAttribute("msg4", "El campo '" + campo +"' ya existe en la tabla ");
            return "redirect:/admin/listaEquipo";
        }

    }



    @PostMapping("/eliminarCampoDinamicoEquipo")
    public String eliminarCampoDinamicoEquipo(@RequestParam("campo") String campo,
                                        RedirectAttributes redirectAttributes) {

        String sql = "ALTER TABLE nexus.equipos DROP COLUMN " + campo;
        System.out.println(sql);
        jdbcTemplate.execute(sql);
        listaCamposEquipos.remove(campo);
        redirectAttributes.addFlashAttribute("msg3", "Se ha eliminado exitosamente el campo '" + campo +"' ");
        System.out.println(listaCamposEquipos);
        return "redirect:/admin/listaEquipo";
    }

}