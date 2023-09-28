package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.Formulario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FormularioRepository extends JpaRepository<Formulario, Integer> {

    //obtener descripcion de formulario para ticket
    @Query(nativeQuery = true, value = "SELECT idTickets FROM formularios where idTickets=?1")
    int obtenerid(int idTicket);

}
