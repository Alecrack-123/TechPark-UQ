package com.techpark.backend.controller;

import com.techpark.backend.model.*;
import com.techpark.backend.service.AtraccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/atracciones")
@CrossOrigin(origins = "*")
public class AtraccionController {

    private final AtraccionService atraccionService;

    public AtraccionController(AtraccionService atraccionService) {
        this.atraccionService = atraccionService;
    }

    // Listar atracciones activas (vista pública)
    @GetMapping
    public ResponseEntity<List<Atraccion>> listarActivas() {
        return ResponseEntity.ok(atraccionService.getActivas());
    }

    // Consultar detalle de una atracción
    @GetMapping("/{id}")
    public ResponseEntity<Atraccion> detalle(@PathVariable String id) {
        return ResponseEntity.ok(atraccionService.buscarPorId(id));
    }

    // Consultar requisitos (altura, edad, costo)
    @GetMapping("/{id}/requisitos")
    public ResponseEntity<Map<String, Object>> requisitos(@PathVariable String id) {
        return ResponseEntity.ok(atraccionService.getRequisitos(id));
    }

    // Consultar tiempo de espera estimado
    @GetMapping("/{id}/tiempo-espera")
    public ResponseEntity<Map<String, Object>> tiempoEspera(@PathVariable String id) {
        return ResponseEntity.ok(atraccionService.getTiempoEspera(id));
    }

    // Registrar ingreso de visitante a la atracción
    @PostMapping("/{id}/ingresar/{documento}")
    public ResponseEntity<Map<String, String>> registrarIngreso(
            @PathVariable String id,
            @PathVariable String documento) {
        return ResponseEntity.ok(atraccionService.registrarIngreso(id, documento));
    }
}