package com.techpark.backend.controller;

import com.techpark.backend.*;
import com.techpark.backend.service.OperadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/operadores")
@CrossOrigin(origins = "*")
public class OperadorController {

    private final OperadorService operadorService;

    public OperadorController(OperadorService operadorService) {
        this.operadorService = operadorService;
    }

    // Listar todos los operadores
    @GetMapping
    public ResponseEntity<List<EmpleadoDTO>> listar() {
        return ResponseEntity.ok(operadorService.getTodos());
    }

    // Buscar operador por id
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> buscar(@PathVariable String id) {
        return ResponseEntity.ok(operadorService.buscarPorId(id));
    }

    // Procesar siguiente visitante en fila de una atracción
    @PostMapping("/{id}/procesar-fila/{atraccionId}")
    public ResponseEntity<Map<String, String>> procesarFila(
            @PathVariable String id,
            @PathVariable String atraccionId) {
        return ResponseEntity.ok(operadorService.procesarFila(id, atraccionId));
    }

    // Registrar revisión técnica de una atracción
    @PostMapping("/{id}/revision/{atraccionId}")
    public ResponseEntity<Map<String, String>> registrarRevision(
            @PathVariable String id,
            @PathVariable String atraccionId,
            @RequestBody Map<String, String> body) {
        String observacion = body.getOrDefault("observacion", "Sin observaciones");
        return ResponseEntity.ok(operadorService.registrarRevision(id, atraccionId, observacion));
    }

    // Cambiar estado de atracción desde el operador
    @PatchMapping("/{id}/atraccion/{atraccionId}/estado")
    public ResponseEntity<Map<String, String>> cambiarEstado(
            @PathVariable String id,
            @PathVariable String atraccionId,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
                operadorService.cambiarEstadoAtraccion(id, atraccionId, body.get("estado"), body.getOrDefault("motivo", ""))
        );
    }
}