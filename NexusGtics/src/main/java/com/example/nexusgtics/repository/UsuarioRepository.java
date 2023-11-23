package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    @Query(value = "SELECT correo FROM nexus.usuarios WHERE idUsuarios <> :id", nativeQuery = true)
    List<String> listaCorreos2(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value ="update nexus.usuarios set contrasenia = ?1 where idUsuarios = ?2", nativeQuery = true )
    void actualizarContraA(String cont,int id);

    @Query(value ="select * from nexus.usuarios where idCargos IN (2) and habilitado = 1", nativeQuery = true )
    List<Usuario> listaDeAdministradores();


    // ------------------------------ ANALISTA OYM --------------------------------------- //





    // ------------------------------ ANALISTA DE PLANIFICACION --------------------------------------- //


    // ------------------------------ TECNICO --------------------------------------- //

    @Query(nativeQuery = true, value = "SELECT * FROM usuarios where idCargos=?1 and idUsuarios!=?2 and tecnicoConCuadrilla=false and idEmpresas=?3")
    List<Usuario> listaDeTecnicos(int valor, int idSupNo, int idEmpresa);

    // ------------------------------ SUPERVISOR --------------------------------------- //
    @Query(nativeQuery = true, value = "SELECT u.* FROM tecnicoscuadrillas tc inner join usuarios u on tc.idTecnico = u.idUsuarios where idCuadrilla = ?1")
    List<Usuario> listaDeTecnicosPorCuadrilla(int idCuadrilla);

    @Query(nativeQuery = true, value = "SELECT * FROM usuarios where idCargos=?1 and idUsuarios!=?2 and tecnicoConCuadrilla=false and idEmpresas=?3")
    List<Usuario> listaDeSupervisores(int valor, int idSupNo, int idEmpresa);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value ="UPDATE nexus.usuarios SET tecnicoConCuadrilla = true WHERE idUsuarios = ?1")
    void cambiarTecnico(int idUsuario);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS NumeroUsuariosCreadosEsteMes\n" +
            "FROM usuarios\n" +
            "WHERE idCargos = ?1\n" +
            "  AND habilitado = 1\n" +
            "  AND idEmpresas = ?2\n" +
            "  AND MONTH(fechaRegistro) = MONTH(CURRENT_DATE())\n" +
            "  AND YEAR(fechaRegistro) = YEAR(CURRENT_DATE());\n")
    int tecnicosEsteMes(int cargo, int idEmpresa);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS umespasado\n" +
            "FROM usuarios\n" +
            "WHERE idCargos = ?1\n" +
            "  AND habilitado = 1\n" +
            "  AND idEmpresas = ?2\n" +
            "  AND MONTH(fechaRegistro) = MONTH(CURRENT_DATE() - INTERVAL 1 MONTH)\n" +
            "  AND YEAR(fechaRegistro) = YEAR(CURRENT_DATE() - INTERVAL 1 MONTH);\n" +
            "\n")
    int tecnicosMesPasado(int cargo, int idEmpresa);

}
