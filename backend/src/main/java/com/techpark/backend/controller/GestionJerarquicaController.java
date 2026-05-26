package com.techpark.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class GestionJerarquicaController {

    @GetMapping("/api/admin/jerarquia")
    public List<Map<String, Object>> obtenerJerarquia() {

        List<Map<String, Object>> jerarquia = new ArrayList<>();

        Map<String, Object> zona1 = new HashMap<>();
        zona1.put("zona", "Zona Aventura");
        zona1.put("operador", "Carlos");
        zona1.put("rol", "Operador");
        zona1.put("atraccionesAsignadas", 2);

        Map<String, Object> zona2 = new HashMap<>();
        zona2.put("zona", "Zona Infantil");
        zona2.put("operador", "Ana");
        zona2.put("rol", "Operadora");
        zona2.put("atraccionesAsignadas", 1);

        Map<String, Object> zona3 = new HashMap<>();
        zona3.put("zona", "Zona Acuatica");
        zona3.put("operador", "Luis");
        zona3.put("rol", "Operador");
        zona3.put("atraccionesAsignadas", 1);

        jerarquia.add(zona1);
        jerarquia.add(zona2);
        jerarquia.add(zona3);

        return jerarquia;
    }
}