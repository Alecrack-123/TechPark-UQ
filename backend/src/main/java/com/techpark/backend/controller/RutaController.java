package com.techpark.backend.controller;

import com.techpark.backend.service.ParqueData;
import com.techpark.backend.structures.GrafoParque;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class RutaController {

    @GetMapping("/api/rutas/{origen}/{destino}")
    public Map<String, Object> calcularRuta(
        @PathVariable String origen,
        @PathVariable String destino
    ) {
        GrafoParque.ResultadoRuta resultado = ParqueData.getGrafo().buscarRutaMasCorta(origen, destino);
        Map<String, Object> respuesta = new HashMap<>();

        if (resultado.getRuta().isEmpty()) {
            respuesta.put("mensaje", "No se encontro ruta");
            respuesta.put("origen", origen);
            respuesta.put("destino", destino);
            return respuesta;
        }

        respuesta.put("origen", origen);
        respuesta.put("destino", destino);
        respuesta.put("ruta", resultado.getRuta());
        respuesta.put("cantidadParadas", resultado.getRuta().size());
        respuesta.put("distanciaTotal", resultado.getDistanciaTotal());
        respuesta.put("algoritmo", "Dijkstra");

        return respuesta;
    }

    @GetMapping("/api/rutas/conexiones")
    public Map<String, Object> verConexiones() {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("conexiones", ParqueData.getGrafo().getConexiones());
        return respuesta;
    }

    @GetMapping("/api/rutas/analisis")
    public Map<String, Object> analizarGrafo() {
        Map<String, Object> respuesta = new HashMap<>();
        Map<String, ?> conexiones = ParqueData.getGrafo().getConexiones();

        respuesta.put("totalNodos", conexiones.size());

        String masConectada = "";
        int mayorConexiones = 0;

        for (String nodo : conexiones.keySet()) {
            Object valor = conexiones.get(nodo);
            int cantidad = valor instanceof java.util.List<?> lista ? lista.size() : 0;

            if (cantidad > mayorConexiones) {
                mayorConexiones = cantidad;
                masConectada = nodo;
            }
        }

        respuesta.put("atraccionMasConectada", masConectada);
        respuesta.put("cantidadConexiones", mayorConexiones);
        respuesta.put("clusterPopular", "Atracciones cercanas a " + masConectada);

        return respuesta;
    }
}
