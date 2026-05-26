package com.techpark.backend.controller;

import com.techpark.backend.dto.*;
import com.techpark.backend.model.*;
import com.techpark.backend.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ─────────────────────────────────────────────
    //  DASHBOARD - estadísticas generales del parque
    // ─────────────────────────────────────────────

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDTO> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboard());
    }

    // ─────────────────────────────────────────────
    //  CLIMA
    // ─────────────────────────────────────────────

    @PostMapping("/clima")
    public ResponseEntity<Map<String, String>> cambiarClima(@RequestBody Map<String, String> body) {
        String estadoStr = body.get("estado");
        try {
            EstadoClima estado = EstadoClima.valueOf(estadoStr.toUpperCase());
            adminService.cambiarClima(estado);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Clima actualizado a: " + estado,
                    "estado", estado.name()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Estado de clima inválido: " + estadoStr));
        }
    }

    @GetMapping("/clima")
    public ResponseEntity<Map<String, String>> getClima() {
        return ResponseEntity.ok(Map.of("estado", adminService.getClimaActual().name()));
    }

    // ─────────────────────────────────────────────
    //  ATRACCIONES
    // ─────────────────────────────────────────────

    @GetMapping("/atracciones")
    public ResponseEntity<List<AtraccionDTO>> getAtracciones() {
        return ResponseEntity.ok(adminService.getTodasAtracciones());
    }

    @PostMapping("/atracciones")
    public ResponseEntity<AtraccionDTO> crearAtraccion(@RequestBody CrearAtraccionRequest req) {
        return ResponseEntity.ok(adminService.crearAtraccion(req));
    }

    @PatchMapping("/atracciones/{id}/estado")
    public ResponseEntity<Map<String, String>> cambiarEstadoAtraccion(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        try {
            EstadoAtraccion estado = EstadoAtraccion.valueOf(body.get("estado").toUpperCase());
            String motivo = body.getOrDefault("motivo", "");
            adminService.cambiarEstadoAtraccion(id, estado, motivo);
            return ResponseEntity.ok(Map.of("mensaje", "Estado actualizado correctamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Estado inválido"));
        }
    }

    @DeleteMapping("/atracciones/{id}")
    public ResponseEntity<Map<String, String>> eliminarAtraccion(@PathVariable String id) {
        adminService.eliminarAtraccion(id);
        return ResponseEntity.ok(Map.of("mensaje", "Atracción eliminada"));
    }

    // ─────────────────────────────────────────────
    //  ZONAS
    // ─────────────────────────────────────────────

    @GetMapping("/zonas")
    public ResponseEntity<List<ZonaDTO>> getZonas() {
        return ResponseEntity.ok(adminService.getTodasZonas());
    }

    @PostMapping("/zonas")
    public ResponseEntity<ZonaDTO> crearZona(@RequestBody CrearZonaRequest req) {
        return ResponseEntity.ok(adminService.crearZona(req));
    }

    @PostMapping("/zonas/{zonaId}/asignar-operador/{operadorId}")
    public ResponseEntity<Map<String, String>> asignarOperador(
            @PathVariable String zonaId,
            @PathVariable String operadorId) {
        adminService.asignarOperadorAZona(zonaId, operadorId);
        return ResponseEntity.ok(Map.of("mensaje", "Operador asignado correctamente"));
    }

    // ─────────────────────────────────────────────
    //  EMPLEADOS (Operadores)
    // ─────────────────────────────────────────────

    @GetMapping("/empleados")
    public ResponseEntity<List<Empleado>> getEmpleados() {
        return ResponseEntity.ok(adminService.getTodosEmpleados());
    }

    @PostMapping("/empleados")
    public ResponseEntity<EmpleadoDTO> crearOperador(@RequestBody CrearEmpleadoRequest req) {
        return ResponseEntity.ok(adminService.crearOperador(req));
    }

    @DeleteMapping("/empleados/{id}")
    public ResponseEntity<Map<String, String>> eliminarEmpleado(@PathVariable String id) {
        adminService.eliminarEmpleado(id);
        return ResponseEntity.ok(Map.of("mensaje", "Empleado eliminado"));
    }

    // ─────────────────────────────────────────────
    //  VISITANTES
    // ─────────────────────────────────────────────

    @GetMapping("/visitantes")
    public ResponseEntity<List<VisitanteDTO>> getVisitantes() {
        return ResponseEntity.ok(adminService.getTodosVisitantes());
    }

    // ─────────────────────────────────────────────
    //  REPORTES
    // ─────────────────────────────────────────────

    @GetMapping("/reportes/ingresos")
    public ResponseEntity<ReporteIngresosDTO> getReporteIngresos() {
        return ResponseEntity.ok(adminService.generarReporteIngresos());
    }

    @GetMapping("/reportes/mantenimiento")
    public ResponseEntity<List<AtraccionDTO>> getReporteMantenimiento() {
        return ResponseEntity.ok(adminService.getAtraccionesEnMantenimiento());
    }

    @GetMapping("/reportes/aforo")
    public ResponseEntity<Map<String, Object>> getAforo() {
        return ResponseEntity.ok(adminService.getAforoActual());
    }
}
