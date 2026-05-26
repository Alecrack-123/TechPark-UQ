package com.techpark.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techpark.backend.model.Atraccion;
import com.techpark.backend.model.EstadoAtraccion;
import com.techpark.backend.service.ParqueData;

@RestController
@CrossOrigin(origins = "*")
public class ReportesController {

    @GetMapping("/api/reportes/total-visitantes")
    public Map<String, Object> totalVisitantes() {
        Map<String, Object> response = new HashMap<>();
        response.put("total", ParqueData.getTicketsVendidos());
        response.put("visitantesProcesados", ParqueData.getIngresosDiarios());
        response.put("enFilas", totalPersonasEnFila());
        return response;
    }

    @GetMapping("/api/reportes/ingresos-diarios")
    public Map<String, Object> ingresosDiarios() {
        Map<String, Object> response = new HashMap<>();
        response.put("visitantesProcesados", ParqueData.getIngresosDiarios());
        response.put("ticketsVendidos", ParqueData.getTicketsVendidos());
        response.put("ingresosEstimados", ParqueData.getIngresosTickets());
        return response;
    }

    @GetMapping("/api/reportes/atraccion-mas-visitada")
    public Map<String, Object> atraccionMasVisitada() {
        Atraccion masVisitada = null;
        int mayor = -1;

        for (Atraccion a : ParqueData.getArbolAtracciones().listarTodas()) {
            if (a.getContadorVisitantes() > mayor) {
                mayor = a.getContadorVisitantes();
                masVisitada = a;
            }
        }

        Map<String, Object> response = new HashMap<>();

        if (masVisitada != null) {
            response.put("atraccion", masVisitada.getNombre());
            response.put("visitantes", mayor);
        }

        return response;
    }

    @GetMapping("/api/reportes/fila-mas-larga")
    public Map<String, Object> filaMasLarga() {
        Atraccion peorFila = null;
        int mayor = -1;

        for (Atraccion a : ParqueData.getArbolAtracciones().listarTodas()) {
            if (a.getTiempoEstimadoEspera() > mayor) {
                mayor = a.getTiempoEstimadoEspera();
                peorFila = a;
            }
        }

        Map<String, Object> response = new HashMap<>();

        if (peorFila != null) {
            response.put("atraccion", peorFila.getNombre());
            response.put("tiempo", peorFila.getTiempoEstimadoEspera());
        }

        return response;
    }

    @GetMapping("/api/reportes/cierres-clima")
    public Map<String, Object> cierresClima() {
        int cerradasPorClima = 0;

        for (Atraccion a : ParqueData.getArbolAtracciones().listarTodas()) {
            if (a.getMotivoCierre() != null && a.getMotivoCierre().toLowerCase().contains("clima")) {
                cerradasPorClima++;
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("atraccionesCerradas", cerradasPorClima);
        return response;
    }

    @GetMapping("/api/reportes/alertas-mantenimiento")
    public Map<String, Object> alertasMantenimiento() {
        int alertas = 0;

        for (Atraccion a : ParqueData.getArbolAtracciones().listarTodas()) {
            if (a.getEstado() == EstadoAtraccion.EN_MANTENIMIENTO) {
                alertas++;
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("alertas", alertas);
        return response;
    }

    @GetMapping("/api/reportes/incidentes-operativos")
    public Map<String, Object> incidentesOperativos() {
        Map<String, Object> response = new HashMap<>();
        response.put("incidentes", ParqueData.getIncidentesOperativos());
        return response;
    }

    private int totalPersonasEnFila() {
        int total = 0;

        for (Atraccion a : ParqueData.getArbolAtracciones().listarTodas()) {
            total += a.getCantidadEnFila();
        }

        return total;
    }
}
