package com.techpark.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techpark.backend.model.Atraccion;
import com.techpark.backend.model.EstadoAtraccion;
import com.techpark.backend.model.TipoAtraccion;
import com.techpark.backend.service.ParqueData;
import com.techpark.backend.structures.ArbolAtracciones;

@RestController
@CrossOrigin(origins = "*")
public class ClimaController {

    private static final ArbolAtracciones arbol = ParqueData.getArbolAtracciones();

    @GetMapping("/api/clima/soleado")
    public Map<String, Object> climaSoleado() {
        int reactivadas = 0;

        for (Atraccion a : arbol.listarTodas()) {
            if (a.getEstado() == EstadoAtraccion.CERRADA && a.getMotivoCierre().toLowerCase().contains("clima")) {
                a.cambiarEstado(EstadoAtraccion.ACTIVA, "");
                reactivadas++;
            }
        }

        ParqueData.agregarNotificacion("CLIMA", "Clima soleado. Atracciones cerradas por clima reactivadas: " + reactivadas);

        Map<String, Object> r = new HashMap<>();
        r.put("clima", "SOLEADO");
        r.put("mensaje", "Clima normal. Atracciones cerradas por clima reactivadas: " + reactivadas);
        r.put("afectadas", reactivadas);
        return r;
    }

    @GetMapping("/api/clima/lluvia")
    public Map<String, Object> climaLluvia() {
        int afectadas = cerrarPorClima("lluvia fuerte");

        Map<String, Object> r = new HashMap<>();
        r.put("clima", "LLUVIA");
        r.put("mensaje", "Atracciones mecánicas de altura y acuáticas cerradas por lluvia fuerte");
        r.put("afectadas", afectadas);
        return r;
    }

    @GetMapping("/api/clima/tormenta")
    public Map<String, Object> climaTormenta() {
        int afectadas = cerrarPorClima("tormenta eléctrica");

        Map<String, Object> r = new HashMap<>();
        r.put("clima", "TORMENTA");
        r.put("mensaje", "Protocolo de emergencia activado por tormenta eléctrica");
        r.put("afectadas", afectadas);
        return r;
    }

    private int cerrarPorClima(String motivo) {
        int afectadas = 0;

        for (Atraccion a : arbol.listarTodas()) {
            if (a.getTipo() == TipoAtraccion.MECANICA_ALTURA || a.getTipo() == TipoAtraccion.ACUATICA) {
                a.cambiarEstado(EstadoAtraccion.CERRADA, "Cierre por clima: " + motivo);
                afectadas++;
            }
        }

        ParqueData.agregarNotificacion("CLIMA", "Alerta de " + motivo + ". Atracciones afectadas: " + afectadas);
        return afectadas;
    }
}
