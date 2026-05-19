package com.techpark.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class EstadisticasController {

    @GetMapping("/api/estadisticas")
    public String obtenerEstadisticas() {

        int totalAtracciones = 6;
        int atraccionesActivas = 4;
        int totalPersonasEnFila = 320;
        int tiempoPromedioEspera = 20;

        return "{"
                + "\"totalAtracciones\":" + totalAtracciones + ","
                + "\"atraccionesActivas\":" + atraccionesActivas + ","
                + "\"totalPersonasEnFila\":" + totalPersonasEnFila + ","
                + "\"tiempoPromedioEspera\":" + tiempoPromedioEspera
                + "}";
    }
}