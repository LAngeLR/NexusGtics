package com.example.nexusgtics.repository;
import com.example.nexusgtics.entity.Comentario;
import com.example.nexusgtics.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value ="INSERT INTO comentarios (comentario, fechaPublicacion, idTickets, idComentarista) VALUES (?3,?4,?2,?1)")
    void ingresarComentario(int id, int idTicket, String comentario, Date fechaCreacion);

    @Query(nativeQuery = true, value = "SELECT * FROM comentarios WHERE idTickets=?1 ORDER BY fechaPublicacion DESC")
    List<Comentario> listarComentarios(int id);
}
