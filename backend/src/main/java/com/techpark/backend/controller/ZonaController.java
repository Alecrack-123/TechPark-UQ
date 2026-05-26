package com.techpark.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.techpark.backend.model.Zona;
import com.techpark.backend.service.ParqueData;

@RestController
@CrossOrigin(origins = "*")
public class ZonaController {

    @GetMapping("/api/zonas")
    public List<Map<String, Object>> obtenerZonas() {
        List<Map<String, Object>> response = new ArrayList<>();

        for (Zona zona : ParqueData.getZonas()) {
            response.add(toDTO(zona));
        }

        return response;
    }

    @PostMapping("/api/zonas")
    public Map<String, Object> crearZona(@RequestBody Map<String, Object> body) {
        String id = String.valueOf(body.getOrDefault("id", "Z" + (ParqueData.getZonas().size() + 1)));

        if (ParqueData.buscarZona(id) != null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Ya existe una zona con ese ID");
            return error;
        }

        String nombre = String.valueOf(body.getOrDefault("nombre", "Nueva zona"));
        int capacidad = Integer.parseInt(String.valueOf(body.getOrDefault("capacidad", 50)));
        Zona zona = new Zona(id, nombre, capacidad);

        ParqueData.registrarZona(zona);
        ParqueData.agregarNotificacion("ADMIN", "Zona creada: " + nombre);
        return toDTO(zona);
    }

    @PutMapping("/api/zonas/{id}")
    public Map<String, Object> modificarZona(@PathVariable String id, @RequestBody Map<String, Object> body) {
        Zona zona = ParqueData.buscarZona(id);

        if (zona == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Zona no encontrada");
            return error;
        }

        if (body.containsKey("nombre")) {
            zona.setNombre(String.valueOf(body.get("nombre")));
        }

        if (body.containsKey("capacidad")) {
            zona.setCapacidadMaxima(Integer.parseInt(String.valueOf(body.get("capacidad"))));
        }

        ParqueData.agregarNotificacion("ADMIN", "Zona modificada: " + zona.getNombre());
        return toDTO(zona);
    }

    private Map<String, Object> toDTO(Zona zona) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", zona.getId());
        dto.put("nombre", zona.getNombre());
        dto.put("capacidad", zona.getCapacidadMaxima());
        dto.put("ocupacion", ParqueData.getOcupacionZona(zona.getId()));
        dto.put("cupos", Math.max(zona.getCapacidadMaxima() - ParqueData.getOcupacionZona(zona.getId()), 0));
        dto.put("atracciones", zona.getCantidadAtracciones());
        dto.put("operadores", zona.getCantidadOperadores());
        return dto;
    }
}
