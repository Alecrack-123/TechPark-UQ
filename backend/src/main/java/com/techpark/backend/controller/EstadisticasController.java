package com.techpark.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techpark.backend.model.Atraccion;
import com.techpark.backend.model.EstadoAtraccion;
import com.techpark.backend.service.ParqueData;

@RestController
@CrossOrigin(origins = "*")
public class EstadisticasController {

    @GetMapping("/api/estadisticas")
    public Map<String, Object> obtenerEstadisticas() {
        List<Atraccion> atracciones = ParqueData.getArbolAtracciones().listarTodas();
        int activas = 0;
        int totalPersonasEnFila = 0;
        int sumaEspera = 0;

        for (Atraccion atraccion : atracciones) {
            if (atraccion.getEstado() == EstadoAtraccion.ACTIVA) {
                activas++;
                sumaEspera += atraccion.getTiempoEstimadoEspera();
            }

            totalPersonasEnFila += atraccion.getCantidadEnFila();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalAtracciones", atracciones.size());
        response.put("atraccionesActivas", activas);
        response.put("totalPersonasEnFila", totalPersonasEnFila);
        response.put("tiempoPromedioEspera", activas == 0 ? 0 : Math.round((double) sumaEspera / activas));
        response.put("ingresosDiarios", ParqueData.getIngresosDiarios());
        response.put("incidentesOperativos", ParqueData.getIncidentesOperativos());
        response.put("capacidadParque", ParqueData.getCapacidadParque());
        response.put("ocupacionParque", ParqueData.getOcupacionParque());
        response.put("ticketsVendidos", ParqueData.getTicketsVendidos());
        response.put("ingresosTickets", ParqueData.getIngresosTickets());
        return response;
    }
}
