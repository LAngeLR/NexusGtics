package com.example.nexusgtics.servicio;

import java.awt.Color;
import java.io.IOException;
import java.util.List;


import com.example.nexusgtics.entity.Sitio;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class SitioPdfExporter {

    private List<Sitio> listSitio;

    public SitioPdfExporter(List<Sitio> listSitio) {
        this.listSitio = listSitio;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new Color(4, 41, 112));
        cell.setPadding(8);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Nombre", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Provincia", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Distrito", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Ubigeo", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Latitud", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Longitud", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Tipo", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Tipo de Zona", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (Sitio sit : listSitio) {
            table.addCell(String.valueOf(sit.getNombre()));
            table.addCell(sit.getProvincia());
            table.addCell(sit.getDistrito());
            table.addCell(String.valueOf(sit.getUbigeo()));
            table.addCell(String.valueOf(sit.getLatitud()));
            table.addCell(String.valueOf(sit.getLongitud()));
            table.addCell(sit.getTipo());
            table.addCell(sit.getTipoZona());
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        try (Document document = new Document(PageSize.A4)) {
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            // Agrega la imagen y el texto al documento
            addImageAndText(document);

            // Resto del código...
            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setSize(18);
            font.setColor(new Color(4, 41, 112));

            Paragraph p = new Paragraph("Lista de Sitios", font);
            p.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(p);

            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100f);
            table.setWidths(new float[]{1.5f, 1.7f, 1.5f, 1.5f, 2f, 2f, 1.5f, 1.5f});
            table.setSpacingBefore(40);

            writeTableHeader(table);
            writeTableData(table);

            document.add(table);
        }
    }

    private void addImageAndText(Document document) throws IOException, DocumentException {
        // Cargar la imagen desde el recurso
        Resource resource = new ClassPathResource("/static/assets/img/logo.png");
        Image img = Image.getInstance(resource.getURL());
        img.scaleAbsolute(35, 35);

        // Crear el párrafo que contiene la imagen y el texto
        Paragraph paragraph = new Paragraph();

        // Establecer un espacio vertical muy pequeño antes del párrafo para moverlo hacia arriba
        float verticalSpaceBefore = 8f; // Ajusta según sea necesario
        paragraph.setSpacingBefore(verticalSpaceBefore);

        // Agregar la imagen al párrafo
        paragraph.add(new Chunk(img, 0, -5, true));

        // Agregar espacio horizontal del mismo tamaño que la imagen
        paragraph.add(new Chunk(" ", FontFactory.getFont(FontFactory.HELVETICA, img.getWidth())));

        // Agregar el texto al párrafo
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(33);
        font.setColor(new Color(4, 41, 112));
        paragraph.add(new Phrase("Nexus", font));

        // Establecer espacio vertical muy pequeño después del párrafo para moverlo hacia arriba
        float verticalSpaceAfter = 2f; // Ajusta según sea necesario
        paragraph.setSpacingAfter(verticalSpaceAfter);

        // Agregar el párrafo al documento
        document.add(paragraph);
    }



}
