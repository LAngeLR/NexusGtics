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




    // ------------------------------ ADMIN --------------------------------------- //
    @Query(value ="select * from nexus.usuarios where idCargos not IN (1, 2)", nativeQuery = true )
    List<Usuario> listaDeUsuariosNoAdmin();


    @Modifying
    @Transactional
    @Query(value ="update nexus.usuarios set habilitado = false where idUsuarios = ?1", nativeQuery = true )
    void desactivarUsuario(int id);


    // ------------------------------ ANALISTA OYM --------------------------------------- //





    // ------------------------------ ANALISTA DE PLANIFICACION --------------------------------------- //


    // ------------------------------ TECNICO --------------------------------------- //
    @Query(value ="SELECT nombre, apellido FROM nexus.usuarios", nativeQuery = true )
    List<Usuario> unirUsuarioPorNombre(String nombre);

    // ------------------------------ SUPERVISOR --------------------------------------- //
    @Query(nativeQuery = true, value = "SELECT * FROM usuarios where IdCuadrilla=?1")
    List<Usuario> listaDeTecnicosPorCuadrilla(int idCuadrilla);

    @Query(nativeQuery = true, value = "SELECT * FROM usuarios where idCargos=?1 and idCuadrilla IS NULL")
    List<Usuario> listaDeSupervisores(int valor);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value ="UPDATE nexus.usuarios SET tecnicoConCuadrilla = true, IdCuadrilla = ?2 WHERE idUsuarios = ?1")
    void cambiarTecnico(int idUsuario, int idCuadrilla);
}
