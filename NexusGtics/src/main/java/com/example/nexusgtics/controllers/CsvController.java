package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.Sitio;
import com.example.nexusgtics.repository.SitioRepository;
import com.example.nexusgtics.servicio.CsvGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CsvController {
    private final SitioRepository sitioRepository;
    private final CsvGeneratorUtil csvGeneratorUtil;

    public CsvController(SitioRepository sitioRepository, CsvGeneratorUtil csvGeneratorUtil) {
        this.sitioRepository = sitioRepository;
        this.csvGeneratorUtil = csvGeneratorUtil;
    }
    @GetMapping("/SitiosCsv")
    public List< Sitio > obtenerLosSitos () {
        return sitioRepository.findAll();
    }

    @GetMapping("/sitio/csv")
    public ResponseEntity<byte[]> generateCsvFile() {
        List<Sitio> sitios = sitioRepository.findAll();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "sitios.csv");

        byte[] csvBytes = csvGeneratorUtil.generateCsv(sitios).getBytes();

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }
}
