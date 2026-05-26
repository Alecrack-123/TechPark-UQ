package com.techpark.backend.controller;

import com.techpark.backend.model.*;
import com.techpark.backend.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/parque")
@CrossOrigin(origins = "*")
public class ParqueController {

    private final ParqueService      parqueService;
    private final AtraccionService   atraccionService;
    private final ReporteService     reporteService;
    private final CargaDatosService  cargaDatosService;

    public ParqueController(ParqueService p, AtraccionService a, ReporteService r, CargaDatosService c) {
        this.parqueService    = p;
        this.atraccionService = a;
        this.reporteService   = r;
        this.cargaDatosService = c;
    }

    @GetMapping("/estado")
    public ResponseEntity<Map<String, Object>> estado() {
        return ResponseEntity.ok(reporteService.resumenGeneral());
    }

    @GetMapping("/mapa")
    public ResponseEntity<Map<String, String>> mapa() {
        return ResponseEntity.ok(atraccionService.estadoTodas());
    }

    @GetMapping("/ruta")
    public ResponseEntity<List<String>> ruta(@RequestParam String origen,
                                             @RequestParam String destino) {
        return ResponseEntity.ok(parqueService.rutaOptima(origen, destino));
    }

    @PostMapping("/clima")
    public ResponseEntity<String> activarClima(@RequestParam String estado) {
        try {
            parqueService.activarAlertaClima(EstadoClima.valueOf(estado.toUpperCase()));
            return ResponseEntity.ok("Alerta climática activada: " + estado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Use: SOLEADO, LLUVIA o TORMENTA");
        }
    }

    @GetMapping("/reportes/visitadas")
    public ResponseEntity<List<Map<String, Object>>> masVisitadas() {
        return ResponseEntity.ok(reporteService.atraccionesMasVisitadas());
    }

    @GetMapping("/reportes/mantenimiento")
    public ResponseEntity<List<String>> mantenimiento() {
        return ResponseEntity.ok(reporteService.alertasMantenimiento());
    }

    @GetMapping("/reportes/clima")
    public ResponseEntity<List<String>> cierresClima() {
        return ResponseEntity.ok(reporteService.cierresPorClima());
    }

    @PostMapping("/cargar-datos")
    public ResponseEntity<String> cargarDatos() {
        cargaDatosService.cargarEscenarioPrueba();
        return ResponseEntity.ok("Escenario cargado.");
    }

    @PutMapping("/atracciones/{id}/estado")
    public ResponseEntity<String> cambiarEstado(@PathVariable String id,
                                                @RequestParam String estado,
                                                @RequestParam(defaultValue = "") String motivo) {
        try {
            Atraccion a = atraccionService.buscarPorId(id);
            if (a == null) return ResponseEntity.notFound().build();
            a.cambiarEstado(EstadoAtraccion.valueOf(estado.toUpperCase()), motivo);
            return ResponseEntity.ok("Estado actualizado.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Use: ACTIVA, EN_MANTENIMIENTO o CERRADA");
        }
    }
}