package com.techpark.backend.service;

import com.techpark.backend.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ReporteService {

    private final ParqueService parqueService;

    public ReporteService(ParqueService parqueService) { this.parqueService = parqueService; }

    public int totalVisitantesConTicket() {
        final int[] total = {0};
        parqueService.getParque().getVisitantes().forEach(v -> { if (v.getTicket() != null) total[0]++; });
        return total[0];
    }

    public List<Map<String, Object>> atraccionesMasVisitadas() {
        List<Map<String, Object>> lista = new ArrayList<>();
        parqueService.getParque().getZonas().forEach(z -> z.getAtracciones().forEach(a -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("nombre",     a.getNombre());
            item.put("visitantes", a.getContadorVisitantes());
            item.put("estado",     a.getEstado().name());
            lista.add(item);
        }));
        lista.sort((x, y) -> Integer.compare((int) y.get("visitantes"), (int) x.get("visitantes")));
        return lista;
    }

    public double tiempoPromedioEspera() {
        final int[] suma = {0}, count = {0};
        parqueService.getParque().getZonas().forEach(z -> z.getAtracciones().forEach(a -> {
            if (a.getEstado() == EstadoAtraccion.ACTIVA) { suma[0] += a.getTiempoEstimadoEspera(); count[0]++; }
        }));
        return count[0] == 0 ? 0 : (double) suma[0] / count[0];
    }

    public List<String> cierresPorClima() {
        List<String> lista = new ArrayList<>();
        parqueService.getParque().getZonas().forEach(z -> z.getAtracciones().forEach(a -> {
            if (a.getMotivoCierre() != null && a.getMotivoCierre().contains("clima"))
                lista.add(a.getNombre() + " — " + a.getMotivoCierre());
        }));
        return lista;
    }

    public List<String> alertasMantenimiento() {
        List<String> lista = new ArrayList<>();
        parqueService.getParque().getZonas().forEach(z -> z.getAtracciones().forEach(a -> {
            if (a.getEstado() == EstadoAtraccion.EN_MANTENIMIENTO)
                lista.add(a.getNombre() + " — " + a.getMotivoCierre());
        }));
        return lista;
    }

    public Map<String, Object> resumenGeneral() {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("parque",               parqueService.getParque().getNombre());
        r.put("capacidadMaxima",      parqueService.getParque().getCapacidadMaxima());
        r.put("visitantesHoy",        totalVisitantesConTicket());
        r.put("tiempoPromedioEspera", tiempoPromedioEspera());
        r.put("alertasMantenimiento", alertasMantenimiento().size());
        r.put("cierresPorClima",      cierresPorClima().size());
        return r;
    }
}