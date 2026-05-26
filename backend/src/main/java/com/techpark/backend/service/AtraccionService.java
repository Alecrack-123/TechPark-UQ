package com.techpark.backend.service;

import com.techpark.backend.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AtraccionService {

    private final ParqueService parqueService;

    public AtraccionService(ParqueService parqueService) { this.parqueService = parqueService; }

    public Atraccion buscarPorId(String id) {
        final Atraccion[] encontrada = {null};
        parqueService.getParque().getZonas().forEach(z ->
                z.getAtracciones().forEach(a -> { if (a.getId().equals(id)) encontrada[0] = a; })
        );
        return encontrada[0];
    }

    public String unirseAFila(String idAtraccion, Visitante visitante) {
        Atraccion a = buscarPorId(idAtraccion);
        if (a == null) return "Atracción no encontrada.";
        if (a.getEstado() != EstadoAtraccion.ACTIVA) return "La atracción no está activa.";
        if (!a.validarAcceso(visitante)) return "No cumple los requisitos de acceso.";
        a.unirseAFila(visitante);
        return "En fila. Espera aprox: " + a.getTiempoEstimadoEspera() + " min.";
    }

    public String cambiarEstado(Operador operador, String idAtraccion,
                                EstadoAtraccion nuevoEstado, String motivo) {
        Atraccion a = buscarPorId(idAtraccion);
        if (a == null) return "Atracción no encontrada.";
        if (operador.getZonaAsignada() == null
                || !operador.getZonaAsignada().getAtracciones().contiene(a))
            return "El operador no tiene permisos sobre esta atracción.";
        operador.cambiarEstadoAtraccion(a, nuevoEstado, motivo);
        return "Estado actualizado a " + nuevoEstado;
    }

    public String registrarRevision(Operador operador, String idAtraccion) {
        Atraccion a = buscarPorId(idAtraccion);
        if (a == null) return "Atracción no encontrada.";
        operador.registrarRevisionTecnica(a);
        return "Revisión registrada. Atracción activa.";
    }

    public Map<String, String> estadoTodas() {
        Map<String, String> mapa = new LinkedHashMap<>();
        parqueService.getParque().getZonas().forEach(z ->
                z.getAtracciones().forEach(a -> mapa.put(a.getId(), a.getEstado().name()))
        );
        return mapa;
    }
}