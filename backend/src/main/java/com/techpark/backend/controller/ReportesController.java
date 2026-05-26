package com.techpark.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techpark.backend.model.Atraccion;
import com.techpark.backend.model.EstadoAtraccion;

@RestController
@CrossOrigin(origins = "*")
public class ReportesController {

    private static final com.techpark.backend.structures.ArbolAtracciones arbol =
            AtraccionController.arbol;

    @GetMapping("/api/reportes/total-visitantes")
    public Map<String, Object> totalVisitantes() {

        int total = 0;

        for (Atraccion a : arbol.listarTodas()) {
            total += a.getCantidadEnFila();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("total", total);

        return response;
    }

    @GetMapping("/api/reportes/atraccion-mas-visitada")
    public Map<String, Object> atraccionMasVisitada() {

        Atraccion masVisitada = null;
        int mayor = -1;

        for (Atraccion a : arbol.listarTodas()) {

            if (a.getCantidadEnFila() > mayor) {
                mayor = a.getCantidadEnFila();
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

        for (Atraccion a : arbol.listarTodas()) {

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

        int cerradas = 0;

        for (Atraccion a : arbol.listarTodas()) {

            if (a.getEstado() == EstadoAtraccion.CERRADA ||
                a.getEstado() == EstadoAtraccion.EN_MANTENIMIENTO) {

                cerradas++;
            }
        }

        Map<String, Object> response = new HashMap<>();

        response.put("atraccionesCerradas", cerradas);

        return response;
    }
}