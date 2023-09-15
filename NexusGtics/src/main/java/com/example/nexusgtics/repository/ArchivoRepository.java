package com.example.nexusgtics.repository;

import com.example.nexusgtics.entity.Archivo;
import com.example.nexusgtics.entity.Cuadrilla;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchivoRepository extends JpaRepository<Archivo, Integer> {
}
