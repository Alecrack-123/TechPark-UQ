package com.techpark.backend.controller;
import com.techpark.backend.model.*;


import com.techpark.backend.service.ZonaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/zonas")
@CrossOrigin(origins = "*")
public class ZonaController {

    private final ZonaService zonaService;

    public ZonaController(ZonaService zonaService) {
        this.zonaService = zonaService;
    }

    // Listar todas las zonas con sus atracciones
    @GetMapping
    public ResponseEntity<List<ZonaDTO>> listar() {
        return ResponseEntity.ok(zonaService.getTodas());
    }

    // Detalle de una zona
    @GetMapping("/{id}")
    public ResponseEntity<ZonaDTO> detalle(@PathVariable String id) {
        return ResponseEntity.ok(zonaService.buscarPorId(id));
    }

    // Ocupación actual de una zona
    @GetMapping("/{id}/ocupacion")
    public ResponseEntity<Map<String, Object>> ocupacion(@PathVariable String id) {
        return ResponseEntity.ok(zonaService.getOcupacion(id));
    }

    // Atracciones disponibles en una zona
    @GetMapping("/{id}/atracciones")
    public ResponseEntity<Map<String, Object>> atracciones(@PathVariable String id) {
        return ResponseEntity.ok(zonaService.getAtracciones(id));
    }
}