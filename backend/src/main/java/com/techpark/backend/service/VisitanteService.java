package com.techpark.backend.service;

import com.techpark.backend.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class VisitanteService {

    private final ParqueService parqueService;

    public VisitanteService(ParqueService parqueService) { this.parqueService = parqueService; }

    public String registrar(String nombre, String documento, int edad, double estatura, double saldo) {
        Visitante v = new Visitante(nombre, documento, edad, estatura, saldo);
        return parqueService.registrarVisitante(v)
                ? "Visitante registrado: " + nombre
                : "Aforo máximo alcanzado.";
    }

    public String comprarTicket(String documento, TipoTicket tipo, double precio) {
        Visitante v = buscarPorDocumento(documento);
        if (v == null) return "Visitante no encontrado.";
        try { v.comprarTicket(tipo, precio); return "Ticket " + tipo + " comprado."; }
        catch (IllegalStateException e) { return "Error: " + e.getMessage(); }
    }

    public String recargarSaldo(String documento, double monto) {
        Visitante v = buscarPorDocumento(documento);
        if (v == null) return "Visitante no encontrado.";
        v.setSaldoVirtual(v.getSaldoVirtual() + monto);
        return "Saldo actualizado: $" + v.getSaldoVirtual();
    }

    public String agregarFavorito(String documento, String idAtraccion) {
        Visitante v = buscarPorDocumento(documento);
        if (v == null) return "Visitante no encontrado.";
        v.agregarFavorito(idAtraccion);
        return "Favorito agregado.";
    }

    public List<String> historial(String documento) {
        Visitante v = buscarPorDocumento(documento);
        if (v == null) return List.of("Visitante no encontrado.");
        List<String> lista = new ArrayList<>();
        v.getHistorialVisitas().forEach(lista::add);
        return lista;
    }

    public Visitante buscarPorDocumento(String documento) {
        final Visitante[] encontrado = {null};
        parqueService.getParque().getVisitantes().forEach(v -> {
            if (v.getDocumento().equals(documento)) encontrado[0] = v;
        });
        return encontrado[0];
    }
}