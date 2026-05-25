package com.techpark.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techpark.backend.model.Atraccion;
import com.techpark.backend.model.EstadoAtraccion;
import com.techpark.backend.model.TipoAtraccion;

@RestController
@CrossOrigin(origins = "*")
public class ClimaController {

    private static final com.techpark.backend.structures.ArbolAtracciones arbol = AtraccionController.arbol;

    @GetMapping("/api/clima/soleado")
    public Map<String, Object> climaSoleado() {
        for (Atraccion a : arbol.listarTodas()) {
            if (a.getEstado() == EstadoAtraccion.CERRADA) {
                a.setEstado(EstadoAtraccion.ACTIVA);
            }
        }
        Map<String, Object> r = new HashMap<>();
        r.put("clima", "SOLEADO");
        r.put("mensaje", "Todas las atracciones funcionan normalmente");
        return r;
    }

    @GetMapping("/api/clima/lluvia")
    public Map<String, Object> climaLluvia() {
        for (Atraccion a : arbol.listarTodas()) {
        if (a.getTipo() == TipoAtraccion.MECANICA_ALTURA || a.getTipo() == TipoAtraccion.ACUATICA) {
                a.setEstado(EstadoAtraccion.CERRADA);
            } else if (a.getTipo() == TipoAtraccion.INFANTIL || a.getTipo() == TipoAtraccion.SIMULADOR) {
                a.setEstado(EstadoAtraccion.ACTIVA);
            }
        }
        Map<String, Object> r = new HashMap<>();
        r.put("clima", "LLUVIA");
        r.put("mensaje", "Atracciones mecánicas y acuáticas cerradas. Infantiles y simuladores siguen activos");
        return r;
    }

    @GetMapping("/api/clima/tormenta")
    public Map<String, Object> climaTormenta() {
        for (Atraccion a : arbol.listarTodas()) {
            if (a.getEstado() == EstadoAtraccion.ACTIVA) {
                a.setEstado(EstadoAtraccion.CERRADA);
            }
        }
        Map<String, Object> r = new HashMap<>();
        r.put("clima", "TORMENTA");
        r.put("mensaje", "Protocolo de emergencia activado. Todas las atracciones cerradas");
        return r;
    }
}