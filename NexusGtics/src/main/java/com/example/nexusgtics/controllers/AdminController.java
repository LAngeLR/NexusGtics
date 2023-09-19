package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.*;
import com.example.nexusgtics.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

//-----------------------------------------------------------------------

    // GESTION DE USUARIOS

    // Lista de usuarios no admin
    @GetMapping({"/listaUsuario","listausuario"})
    public String listaUsuario(Model model){
        List<Usuario> listaUsuarioNoAdmin = usuarioRepository.listaDeUsuariosNoAdmin();
        model.addAttribute("listaUsuario", listaUsuarioNoAdmin);
        return "Administrador/listaUsuario";
    }

    // Desabilitar Usuarios
    @GetMapping("/desabilitarUsuario")
    public String desabilitarUsuario(@RequestParam("id") int id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            usuarioRepository.desactivarUsuario(id);
        }
        return "redirect:Administrador/listaUsuario";
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

    @PostMapping("/saveUsuario")
    public String saveUsuario(@RequestParam("imagenSubida") MultipartFile file,
                              Usuario usuario,
                              Model model,
                              RedirectAttributes attr){

        if (usuario.getArchivo() == null) {
            usuario.setArchivo(new Archivo());
        }

        if(file.isEmpty()){
            model.addAttribute("msg", "Debe subir un archivo");
            return "redirect:/admin/crearUsuario";
        }

        String fileName = file.getOriginalFilename();

        try{
            Archivo archivo = usuario.getArchivo();
            archivo.setTipo(1);
            archivo.setArchivo(file.getBytes());
            archivoRepository.save(archivo);


            int idImagen = archivo.getIdArchivos();
            usuario.getArchivo().setIdArchivos(idImagen);
            usuarioRepository.save(usuario);
            return "redirect:/admin/listaUsuario";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping({"/editarUsuario","editarusuario"})
    public String editarUsuario(){
        return "Administrador/editarUsuario";
    }


    // CRUD DE USUARIOS


    /*  Aun no saleee  */
    @PostMapping("/guardarUsuario")
    public String guardarNuevoUsuario(Usuario usuario, RedirectAttributes attr){
        usuarioRepository.save(usuario);
        return "redirect:Administrador/listaSitio";
    }


//-----------------------------------------------------------------------

    // GESTION DE SITIOS
    @GetMapping({"/listaSitio","/listasitio"})
    public String listaSitio(Model model){
        List<Sitio> listaSitio = sitioRepository.findAll();
        model.addAttribute("listaSitio", listaSitio);
        return "Administrador/listaSitio";
    }

    @GetMapping({"/crearSitio","/crearsitio"})
    public String crearSitio(){
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

    @GetMapping({"/editarSitio","/editarsitio"})
    public String editarSitio(){
        return "Administrador/editarSitio";
    }

    @GetMapping({"/ubicarSitio","/ubicarsitio"})
    public String ubicarSitio(){
        return "Administrador/ubicacionSitio";
    }

    @GetMapping({"/inventarioSitio","/inventariositio"})
    public String inventarioSitio(){
        return "Administrador/mapaInventarioSitio";
    }


    @PostMapping("/guardarSitio")
    public String guardarSitio(@RequestParam("departamento") String departamento,
                               @RequestParam("provincia") String provincia,
                               @RequestParam("distrito") String distrito,
                               @RequestParam("ubigeo") Integer ubigeo,
                               @RequestParam("latitud") BigDecimal latitud,
                               @RequestParam("longitud") BigDecimal longitud,
                               @RequestParam("tipo") Boolean tipo,
                               @RequestParam("tipoZona") Boolean tipoZona, RedirectAttributes attr) {

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

//-----------------------------------------------------------------------

    //GESTION DE EMPRESAS
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


}
