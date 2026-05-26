package com.techpark.backend.service;

import com.techpark.backend.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ParqueService {

    private final Parque parque = new Parque("Tech-Park UQ", 500);

    public Parque getParque() { return parque; }

    public void crearZona(String id, String nombre, int capacidad) {
        parque.agregarZona(new Zona(id, nombre, capacidad));
    }

    public void crearAtraccion(String zonaId, String id, String nombre, TipoAtraccion tipo,
                               int capacidad, double alturaMin, int edadMin, double costoExtra) {
        Atraccion a = new Atraccion(id, nombre, tipo, capacidad, alturaMin, edadMin, costoExtra);
        parque.getZonas().forEach(z -> {
            if (z.getId().equals(zonaId)) {
                z.agregarAtraccion(a);
                parque.agregarAtraccionAlMapa(id, nombre);
            }
        });
    }

    public void conectar(String idA, String idB, double distancia) {
        parque.conectarAtracciones(idA, idB, distancia);
    }

    public void activarAlertaClima(EstadoClima clima) { parque.simularClima(clima); }

    public List<String> rutaOptima(String origen, String destino) {
        return parque.calcularRutaOptima(origen, destino);
    }

    public boolean registrarVisitante(Visitante v) {
        if (!parque.verificarAforo()) return false;
        parque.registrarVisitante(v);
        return true;
    }

    public List<String> atraccionesCerradas() {
        List<String> r = new ArrayList<>();
        parque.getZonas().forEach(z -> z.getAtracciones().forEach(a -> {
            if (a.getEstado() == EstadoAtraccion.CERRADA) r.add(a.getNombre());
        }));
        return r;
    }

    public List<String> atraccionesEnMantenimiento() {
        List<String> r = new ArrayList<>();
        parque.getZonas().forEach(z -> z.getAtracciones().forEach(a -> {
            if (a.getEstado() == EstadoAtraccion.EN_MANTENIMIENTO) r.add(a.getNombre());
        }));
        return r;
    }
}