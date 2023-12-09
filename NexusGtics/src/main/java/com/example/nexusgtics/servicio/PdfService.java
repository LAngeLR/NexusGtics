package com.example.nexusgtics.servicio;

import com.example.nexusgtics.entity.Sitio;
import com.example.nexusgtics.repository.SitioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PdfService {

    private final SitioRepository sitioRepository;

    public PdfService(SitioRepository sitioRepository) {
        this.sitioRepository = sitioRepository;
    }
    public List<Sitio> lisall(){
        return sitioRepository.findAll();

    }

}
