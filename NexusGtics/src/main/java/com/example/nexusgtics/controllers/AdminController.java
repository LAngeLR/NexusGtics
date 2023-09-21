package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.*;
import com.example.nexusgtics.repository.*;
import com.sun.net.httpserver.HttpsServer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    final EmpresaRepository empresaRepository;
    final SitioRepository sitioRepository;
    final EquipoRepository equipoRepository;
    final TipoEquipoRepository tipoEquipoRepository;
    final UsuarioRepository usuarioRepository;

    final CargoRepository cargoRepository;
    private final ArchivoRepository archivoRepository;

    public AdminController(EmpresaRepository empresaRepository, SitioRepository sitioRepository, EquipoRepository equipoRepository, TipoEquipoRepository tipoEquipoRepository, UsuarioRepository usuarioRepository, CargoRepository cargoRepository,
                           ArchivoRepository archivoRepository) {
        this.empresaRepository = empresaRepository;
        this.sitioRepository = sitioRepository;
        this.equipoRepository = equipoRepository;
        this.tipoEquipoRepository = tipoEquipoRepository;
        this.usuarioRepository = usuarioRepository;
        this.cargoRepository = cargoRepository;
        this.archivoRepository = archivoRepository;
    }

    @GetMapping({"/","","admin","administrador"})
    public String paginaPrincipal(){
        return "Administrador/admin";
    }

    @GetMapping({"/perfil","perfilAdmin","perfiladmin"})
    public String perfilAdmin(){
        return "Administrador/perfilAdmin";
    }

//---------------------------- GESTION DE USUARIOS -------------------------------------------

    // Lista de usuarios no admin
    @GetMapping({"/listaUsuario","listausuario"})
    public String listaUsuario(Model model){
        List<Usuario> listaUsuarioNoAdmin = usuarioRepository.listaDeUsuariosNoAdmin();
        model.addAttribute("listaUsuario", listaUsuarioNoAdmin);
        return "Administrador/listaUsuario";
    }

    @GetMapping({"/listaBaneados"})
    public String listaBaneado(Model model){
        List<Usuario> listaUsuarioBaneado = usuarioRepository.listaDeUsuariosBaneados();
        model.addAttribute("listaUsuario", listaUsuarioBaneado);
        return "Administrador/listaUsuarioBaneado";
    }

    // DESACTIVAR USUARIO
    @GetMapping("/banearUsuario")
    public String desabilitarUsuario(@RequestParam("id") int id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            usuarioRepository.desactivarUsuario(id);
        }
        return "redirect:/admin/listaUsuario";
    }

    // ACTIVAR USUARIO
    @GetMapping("/activarUsuario")
    public String activarUsuario(@RequestParam("id") int id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            usuarioRepository.activarUsuario(id);
        }
        return "redirect:/admin/banearUsuario";
    }

    @GetMapping({"/crearUsuario","crearusuario"})
    public String crearUsuario(Model model){

        model.addAttribute("listaEmpresa", empresaRepository.findAll());
        model.addAttribute("listaCargo", cargoRepository.findAll());

        return "Administrador/crearUsuario";
    }

    @GetMapping({"/verUsuario","verusuario"})
    public String verUsuario(Model model, @RequestParam("id") int id){
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);

        if(optUsuario.isPresent()){
            Usuario usuario = optUsuario.get();
            model.addAttribute("usuario", usuario);
            return "Administrador/vistaUsuario";
        } else {
            return "redirect:/Administrador/listaUsuario";
        }
    }

    /*CREAR NUEVO USUARIO*/
    @PostMapping("/saveUsuario")
    public String saveUsuario(@RequestParam("imagenSubida") MultipartFile file,
                              Usuario usuario,
                              Model model,
                              RedirectAttributes attr){

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
            return "redirect:/admin/listaUsuario";
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
            return "Administrador/editarUsuario";
        } else {
            return "redirect:/admin";
        }
    }





//------------------------------ GESTION DE EMPRESAS -----------------------------------------
    @GetMapping({"/listaEmpresa","/listaempresa"})
    public String listaEmpresa(Model model){
        List<Empresa> listaEmpresa = empresaRepository.findAll();
        model.addAttribute("listaEmpresa",listaEmpresa);
        return "Administrador/listaEmpresa";
    }

    @GetMapping({"/crearEmpresa","/crearempresa"})
    public String crearEmpresa(){
        return "Administrador/crearEmpresa";
    }

    @GetMapping("/verEmpresa")
    public String verEmpresa(Model model,
                             @RequestParam("id") int id) {

        Optional<Empresa> optEmpresa = empresaRepository.findById(id);

        if (optEmpresa.isPresent()) {
            Empresa empresa = optEmpresa.get();
            model.addAttribute("empresa", empresa);
            return "Administrador/vistaEmpresa";
        } else {
            return "redirect:/Administrador/listaEmpresa";
        }
    }

    @PostMapping("/guardarEmpresa")
    public String guardarEmpresa(Empresa empresa, RedirectAttributes attr){
        empresaRepository.save(empresa);
        return "redirect:/admin/listaEmpresa";
    }

    /*PARA VISUALIZAR FOTOS*/
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable("id") int id){
        Optional<Archivo> opt = archivoRepository.findById(id);

        if(opt.isPresent()){
            Archivo u = opt.get();

            byte[] imagenComoBytes = u.getArchivo();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType(u.getContentType()));


            return new ResponseEntity<>(
                    imagenComoBytes,
                    httpHeaders,
                    HttpStatus.OK);
        } else {
            return  null;
        }
    }


//------------------------------ GESTION DE SITIOS -----------------------------------------

    @GetMapping({"/listaSitio","/listasitio"})
    public String listaSitio(Model model){
        List<Sitio> listaSitio = sitioRepository.findAll();
        model.addAttribute("listaSitio", listaSitio);
        return "Administrador/listaSitio";
    }

    @GetMapping({"/crearSitio","/crearsitio"})
    public String crearSitio(Model model){
        return "Administrador/crearSitio";
    }

    @GetMapping({"/verSitio","/versitio"})
    public String verSitio(Model model, @RequestParam("id") int id){
        Optional<Sitio> optionalSitio = sitioRepository.findById(id);
        if (optionalSitio.isPresent()){
            Sitio sitio = optionalSitio.get();
            model.addAttribute("sitio", sitio);
            return "Administrador/vistaSitio";
        }else {
            return "Administrador/listaSitio";
        }
    }

    /*EDITAR USUARIO*/
    @GetMapping({"/editarSitio"})
    public String editarSitio(Model model, @RequestParam("id") int id){

        Optional<Sitio> sitio1 = sitioRepository.findById(id);

        if (sitio1.isPresent()) {
            Sitio sitio = sitio1.get();
            model.addAttribute("sitio", sitio);
            return "Administrador/editarUsuario";
        } else {
            return "redirect:/admin";
        }
    }

    @GetMapping({"/ubicarSitio","/ubicarsitio"})
    public String ubicarSitio(){
        return "Administrador/ubicacionSitio";
    }

    @GetMapping({"/inventarioSitio","/inventariositio"})
    public String inventarioSitio(){
        return "Administrador/mapaInventarioSitio";
    }

    /*CREAR NUEVO SITIO*/
    @PostMapping("/saveSitio")
    public String saveSitio(@RequestParam("imagenSubida") MultipartFile file,
                              Sitio sitio,
                              Model model,
                              RedirectAttributes attr){

        if (sitio.getArchivo() == null) {
            sitio.setArchivo(new Archivo());
        }

        String fileName = file.getOriginalFilename();
        try{
            Archivo archivo = sitio.getArchivo();
            archivo.setNombre(fileName);
            archivo.setTipo(1);
            archivo.setArchivo(file.getBytes());
            archivo.setContentType(file.getContentType());
            archivoRepository.save(archivo);
            int idImagen = archivo.getIdArchivos();
            sitio.getArchivo().setIdArchivos(idImagen);
            sitioRepository.save(sitio);
            return "redirect:/admin/listaSitio";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/guardarSitio")
    public String guardarSitio(@RequestParam("departamento") String departamento,
                               @RequestParam("provincia") String provincia,
                               @RequestParam("distrito") String distrito,
                               @RequestParam("ubigeo") Integer ubigeo,
                               @RequestParam("latitud") BigDecimal latitud,
                               @RequestParam("longitud") BigDecimal longitud,
                               @RequestParam("tipo") String tipo,
                               @RequestParam("tipoZona") String tipoZona, RedirectAttributes attr) {

        sitioRepository.guardarSitio(departamento, provincia, distrito, ubigeo, latitud, longitud, tipo, tipoZona);

        return "redirect:/admin/listaSitio";
    }

//-----------------------------------------------------------------------

    // GESTION DE EQUIPOS
    @GetMapping({"/listaEquipo","/listaequipo"})
    public String listaEquipo(Model model){
        List<Equipo> listaEquipo = equipoRepository.findAll();
        model.addAttribute("listaEquipo", listaEquipo);
        return "Administrador/listaEquipo";
    }

    @GetMapping({"/crearEquipo","/crearequipo"})
    public String crearEquipo(Model model){
        model.addAttribute("listaSitios",sitioRepository.findAll());
        model.addAttribute("listaTipoEquipos",tipoEquipoRepository.findAll());
        return "Administrador/crearEquipo";
    }
    @PostMapping("/guardarEquipo")
    public String guardarEquipo(@RequestParam("marca") String marca,
                               @RequestParam("modelo") String modelo,
                               @RequestParam("descripcion") String descripcion,
                               @RequestParam("paginaModelo") String paginaModelo,
                               @RequestParam("idSitios") Integer idSitios,
                               @RequestParam("idTipoEquipo") Integer idTipoEquipo, RedirectAttributes attr) {

        equipoRepository.guardarEquipo(marca, modelo, descripcion, paginaModelo, idSitios, idTipoEquipo);

        return "redirect:/admin/listaEquipo";
    }

    @GetMapping({"/verEquipo","/verequipo"})
    public String verEquipo(Model model, @RequestParam("id") int id){
        Optional<Equipo> optionalEquipo = equipoRepository.findById(id);

        if(optionalEquipo.isPresent()){
            Equipo equipo = optionalEquipo.get();
            model.addAttribute("equipo", equipo);
            return "Administrador/vistaEquipo";
        }else {
            return "redirect:/Administrador/listaEquipo";
        }

    }

    @GetMapping({"/editarEquipo","/editarequipo"})
    public String editarEquipo(){
        return "Administrador/editarEquipo";
    }

}
