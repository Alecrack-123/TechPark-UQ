package com.techpark.backend.controller;

import com.techpark.backend.*;
import com.techpark.backend.service.VisitanteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/visitantes")
@CrossOrigin(origins = "*")
public class VisitanteController {

    private final VisitanteService visitanteService;

    public VisitanteController(VisitanteService visitanteService) {
        this.visitanteService = visitanteService;
    }

    // Registrar nuevo visitante
    @PostMapping
    public ResponseEntity<VisitanteDTO> registrar(@RequestBody RegistrarVisitanteRequest req) {
        return ResponseEntity.ok(visitanteService.registrarVisitante(req));
    }

    // Listar todos los visitantes
    @GetMapping
    public ResponseEntity<List<VisitanteDTO>> listar() {
        return ResponseEntity.ok(visitanteService.getTodos());
    }

    // Consultar visitante por documento
    @GetMapping("/{documento}")
    public ResponseEntity<VisitanteDTO> buscar(@PathVariable String documento) {
        return ResponseEntity.ok(visitanteService.buscarPorDocumento(documento));
    }

    // Consultar saldo virtual
    @GetMapping("/{documento}/saldo")
    public ResponseEntity<Map<String, Object>> getSaldo(@PathVariable String documento) {
        return ResponseEntity.ok(visitanteService.getSaldo(documento));
    }

    // Recargar saldo virtual
    @PostMapping("/{documento}/recargar")
    public ResponseEntity<Map<String, Object>> recargarSaldo(
            @PathVariable String documento,
            @RequestBody Map<String, Double> body) {
        double monto = body.get("monto");
        return ResponseEntity.ok(visitanteService.recargarSaldo(documento, monto));
    }

    // Verificar si puede ingresar a una atracción
    @GetMapping("/{documento}/puede-ingresar/{atraccionId}")
    public ResponseEntity<Map<String, Object>> puedeIngresar(
            @PathVariable String documento,
            @PathVariable String atraccionId) {
        return ResponseEntity.ok(visitanteService.puedeIngresar(documento, atraccionId));
    }
}