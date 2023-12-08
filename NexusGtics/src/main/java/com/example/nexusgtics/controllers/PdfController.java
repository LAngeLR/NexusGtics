package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.Sitio;
import com.example.nexusgtics.servicio.PdfService;
import com.example.nexusgtics.servicio.SitioPdfExporter;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class PdfController {

    private final PdfService service;

    public PdfController(PdfService service) {
        this.service = service;
    }

    @GetMapping("/sitios/pdf")
    public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=sitios_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);


        List<Sitio> listSitio = service.lisall();

        SitioPdfExporter exporter = new SitioPdfExporter(listSitio);
        exporter.export(response);


    }
}
