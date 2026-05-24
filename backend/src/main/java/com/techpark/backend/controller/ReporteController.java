package com.techpark.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class ReporteController {

    @GetMapping("/api/reportes/total-visitantes")
    public String totalVisitantes() {

        int totalVisitantes = 320;

        return "{"
                + "\"reporte\":\"Total de visitantes en filas\","
                + "\"totalVisitantes\":" + totalVisitantes
                + "}";
    }

    @GetMapping("/api/reportes/atraccion-mas-visitada")
    public String atraccionMasVisitada() {

        String nombre = "Montaña Rusa Tech";
        int visitantes = 120;

        return "{"
                + "\"reporte\":\"Atraccion mas visitada\","
                + "\"nombre\":\"" + nombre + "\","
                + "\"visitantes\":" + visitantes
                + "}";
    }

    @GetMapping("/api/reportes/fila-mas-larga")
    public String filaMasLarga() {

        String nombre = "Montaña Rusa Tech";
        int personasEnFila = 120;

        return "{"
                + "\"reporte\":\"Fila mas larga\","
                + "\"nombre\":\"" + nombre + "\","
                + "\"personasEnFila\":" + personasEnFila
                + "}";
    }

    @GetMapping("/api/reportes/cierres-clima")
    public String cierresPorClima() {

        int cierres = 2;

        return "{"
                + "\"reporte\":\"Cierres por clima\","
                + "\"cantidad\":" + cierres
                + "}";
    }
}