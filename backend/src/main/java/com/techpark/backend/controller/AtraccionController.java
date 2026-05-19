package com.techpark.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class AtraccionController {

    @GetMapping("/api/parque/atracciones")
    public String obtenerAtracciones() {
        return "["
                + "{\"id\":1,\"nombre\":\"Montaña Rusa Tech\",\"tipo\":\"Extrema\",\"capacidadMaxima\":24,\"tiempoEsperaMinutos\":25,\"estado\":\"ACTIVA\",\"personasEnFila\":120},"
                + "{\"id\":2,\"nombre\":\"Río Virtual\",\"tipo\":\"Familiar\",\"capacidadMaxima\":40,\"tiempoEsperaMinutos\":15,\"estado\":\"ACTIVA\",\"personasEnFila\":80},"
                + "{\"id\":3,\"nombre\":\"Torre Digital\",\"tipo\":\"Extrema\",\"capacidadMaxima\":16,\"tiempoEsperaMinutos\":0,\"estado\":\"MANTENIMIENTO\",\"personasEnFila\":0},"
                + "{\"id\":4,\"nombre\":\"Carrusel Cyber\",\"tipo\":\"Infantil\",\"capacidadMaxima\":32,\"tiempoEsperaMinutos\":5,\"estado\":\"ACTIVA\",\"personasEnFila\":25},"
                + "{\"id\":5,\"nombre\":\"Simulador 4D\",\"tipo\":\"Tecnológica\",\"capacidadMaxima\":20,\"tiempoEsperaMinutos\":35,\"estado\":\"ACTIVA\",\"personasEnFila\":95},"
                + "{\"id\":6,\"nombre\":\"Casa del Terror VR\",\"tipo\":\"Temática\",\"capacidadMaxima\":12,\"tiempoEsperaMinutos\":0,\"estado\":\"CERRADA\",\"personasEnFila\":0}"
                + "]";
    }
}