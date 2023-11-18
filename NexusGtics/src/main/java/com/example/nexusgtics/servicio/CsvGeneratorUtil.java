package com.example.nexusgtics.servicio;

import com.example.nexusgtics.entity.Sitio;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CsvGeneratorUtil {
    private static final String CSV_HEADER = "Nombre,Tipo,Provincia,Distrito,Departamento,Ubigeo,Latitud,Longitud,TipoZona\n";
    public String generateCsv(List<Sitio> sitios) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(CSV_HEADER);

        for (Sitio sitio : sitios) {
            csvContent.append(sitio.getNombre()).append(",")
                    .append(sitio.getTipo()).append(",")
                    .append(sitio.getProvincia()).append(",")
                    .append(sitio.getDistrito()).append(",")
                    .append(sitio.getDepartamento()).append(",")
                    .append(sitio.getUbigeo()).append(",")
                    .append(sitio.getLatitud()).append(",")
                    .append(sitio.getLongitud()).append(",")
                    .append(sitio.getTipoZona()).append("\n");
        }
        return csvContent.toString();
    }
}
