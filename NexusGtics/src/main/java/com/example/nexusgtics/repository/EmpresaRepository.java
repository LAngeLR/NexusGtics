package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.Empresa;
import com.example.nexusgtics.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {

    //------------------------ Analista m --------------------//
    @Query(value ="select * from empresas where idEmpresas not IN (1);", nativeQuery = true )
    List<Empresa> noNexus();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM empresas WHERE idEmpresas != 1 AND MONTH(fechaAfiliacion) = MONTH(CURRENT_DATE);\n")
    Integer numeroEmpresasAfiliadasMes();

    @Query(nativeQuery = true, value = "SELECT\n" +
            "    COALESCE(\n" +
            "        (\n" +
            "            (\n" +
            "                (SELECT COUNT(*) FROM empresas WHERE idEmpresas != 1 AND MONTH(fechaAfiliacion) = MONTH(NOW())) -\n" +
            "                (SELECT COUNT(*) FROM empresas WHERE idEmpresas != 1 AND MONTH(fechaAfiliacion) = MONTH(DATE_SUB(NOW(), INTERVAL 1 MONTH)))\n" +
            "            ) /\n" +
            "            (SELECT COUNT(*) FROM empresas WHERE idEmpresas != 1 AND MONTH(fechaAfiliacion) = MONTH(DATE_SUB(NOW(), INTERVAL 1 MONTH)))\n" +
            "        ) * 100\n" +
            "    , 0) as diferencia_porcentaje_empresas;")
    Integer EmpresasAfiliadasDiferencia();

}
