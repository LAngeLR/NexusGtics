package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // ------------------------------ SUPERADMIN --------------------------------------- //

    @Query(value ="select * from nexus.usuarios where idCargos not IN (1) and habilitado = 1", nativeQuery = true )
    List<Usuario> listaDeUsuariosTotal();


    // ------------------------------ ADMIN --------------------------------------- //
    @Query(value ="select * from nexus.usuarios where idCargos not IN (1,2) and habilitado = 1", nativeQuery = true )
    List<Usuario> listaDeUsuariosNoAdmin();

    @Query(value ="select * from nexus.usuarios where idCargos not IN (1) and habilitado = 1", nativeQuery = true )
    List<Usuario> listaDeUsuariosNoSuperadmin();

    @Query(value ="select * from nexus.usuarios where habilitado = 0", nativeQuery = true )
    List<Usuario> listaDeUsuariosBaneados();


    @Modifying
    @Transactional
    @Query(value ="update nexus.usuarios set habilitado = false where idUsuarios = ?1", nativeQuery = true )
    void desactivarUsuario(int id);

    @Modifying
    @Transactional
    @Query(value ="update nexus.usuarios set habilitado = true where idUsuarios = ?1", nativeQuery = true )
    void activarUsuario(int id);

    public Usuario findByCorreo(String correo);

    @Query(value ="select contrasenia from nexus.usuarios where idUsuarios=?1", nativeQuery = true )
    String obtenerContraseña(int id);

    @Query(value = "SELECT correo FROM nexus.usuarios", nativeQuery = true)
    List<String> listaCorreos();

    @Modifying
    @Transactional
    @Query(value ="update nexus.usuarios set contrasenia = ?1 where idUsuarios = ?2", nativeQuery = true )
    void actualizarContraA(String cont,int id);

    @Query(value ="select * from nexus.usuarios where idCargos IN (2) and habilitado = 1", nativeQuery = true )
    List<Usuario> listaDeAdministradores();


    // ------------------------------ ANALISTA OYM --------------------------------------- //





    // ------------------------------ ANALISTA DE PLANIFICACION --------------------------------------- //


    // ------------------------------ TECNICO --------------------------------------- //
    @Query(value ="SELECT nombre, apellido FROM nexus.usuarios", nativeQuery = true )
    List<Usuario> unirUsuarioPorNombre(String nombre);

    // ------------------------------ SUPERVISOR --------------------------------------- //
    @Query(nativeQuery = true, value = "SELECT * FROM usuarios where IdCuadrilla=?1")
    List<Usuario> listaDeTecnicosPorCuadrilla(int idCuadrilla);

    @Query(nativeQuery = true, value = "SELECT * FROM usuarios where idCargos=?1 and idCuadrilla IS NULL and idUsuarios!=?2")
    List<Usuario> listaDeSupervisores(int valor, int idSupNo);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value ="UPDATE nexus.usuarios SET tecnicoConCuadrilla = true, IdCuadrilla = ?2 WHERE idUsuarios = ?1")
    void cambiarTecnico(int idUsuario, int idCuadrilla);
}
