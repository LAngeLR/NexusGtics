package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // ------------------------------ SUPERADMIN --------------------------------------- //




    // ------------------------------ ADMIN --------------------------------------- //
    @Query(value ="select * from nexus.usuarios where idCargos not IN (1, 2)", nativeQuery = true )
    List<Usuario> listaDeUsuariosNoAdmin();


    @Modifying
    @Transactional
    @Query(value ="update usuarios set habilitado = false where idUsuarios = ?1;", nativeQuery = true )
    void desactivarUsuario(int id);


    // ------------------------------ ANALISTA OYM --------------------------------------- //





    // ------------------------------ ANALISTA DE PLANIFICACION --------------------------------------- //


    // ------------------------------ TECNICO --------------------------------------- //
    @Query(value ="SELECT nombre, apellido FROM nexus.usuarios", nativeQuery = true )
    List<Usuario> unirUsuarioPorNombre(String nombre);


}
