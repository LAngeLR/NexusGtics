package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.Formulario;
import com.example.nexusgtics.entity.TecnologiainstaladaFormulario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FormularioRepository extends JpaRepository<Formulario, Integer> {

    //obtener descripcion de formulario para ticket
    @Query(nativeQuery = true, value = "SELECT idTickets FROM formularios where idTickets=?1")
    int obtenerid(int idTicket);

    @Query(nativeQuery = true, value = "SELECT * FROM formularios where idTickets=?1 limit 1")
    List<Formulario> formulariosSD(int idTicket);


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value =
            "INSERT INTO formularios (`fechaLlenado`, `descripcion`, `confirmacion`, `idTecnico`, `idImagenesForm`, `idTickets`, `idTipoTicket`, `hrelevantes`, `conexion`, `movilidad`, `nomredantario`, `dni`, `area`, `observaciones`, `construccion`, `instalacion`, `despliegue`, `trabarealizados`, `equipoencendido`, `equipoconectado`, `situacion`, `acciones`, `bateriasestado`, `averia`) VALUES ('2023-12-12 20:50:00', '-', '1', '1', '1', '1', '1', '-', '1', '1', '-', '1', '1', '-', '1', '1', '1', '-', '1', '1', '-', '-', '1', '-')")

    void actualizarFormulario();


}
