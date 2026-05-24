package com.techpark.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techpark.backend.model.Zona;

@RestController
@CrossOrigin(origins = "*") 
public class ZonaController {

    @GetMapping("/api/zonas")
    public String obtenerZonas() {

        Zona zona1 = new Zona("Z1", "Zona Aventura", 100);
        Zona zona2 = new Zona("Z2", "Zona Infantil", 80);
        Zona zona3 = new Zona("Z3", "Zona Acuatica", 120);
        Zona zona4 = new Zona("Z4", "Zona de Comidas", 60);

        return "["
                + "{\"id\":\"" + zona1.getId() + "\",\"nombre\":\"" + zona1.getNombre() + "\",\"capacidad\":" + zona1.getCapacidadMaxima() + "},"
                + "{\"id\":\"" + zona2.getId() + "\",\"nombre\":\"" + zona2.getNombre() + "\",\"capacidad\":" + zona2.getCapacidadMaxima() + "},"
                + "{\"id\":\"" + zona3.getId() + "\",\"nombre\":\"" + zona3.getNombre() + "\",\"capacidad\":" + zona3.getCapacidadMaxima() + "},"
                + "{\"id\":\"" + zona4.getId() + "\",\"nombre\":\"" + zona4.getNombre() + "\",\"capacidad\":" + zona4.getCapacidadMaxima() + "}"
                + "]";
    }
}