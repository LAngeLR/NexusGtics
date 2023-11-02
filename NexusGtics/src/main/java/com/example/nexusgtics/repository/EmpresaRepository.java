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
}
