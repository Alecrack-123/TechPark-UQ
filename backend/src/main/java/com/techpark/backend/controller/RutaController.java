package com.techpark.backend.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.techpark.backend.structures.GrafoParque;

@RestController
@CrossOrigin(origins = "*")
public class RutaController {

    private GrafoParque crearGrafo() {
        GrafoParque grafo = new GrafoParque();

        grafo.conectar("A1", "A2");
        grafo.conectar("A2", "A4");
        grafo.conectar("A4", "A5");
        grafo.conectar("A1", "A3");
        grafo.conectar("A3", "A5");
        grafo.conectar("A5", "A6");

        return grafo;
    }

    @GetMapping("/api/rutas/{origen}/{destino}")
    public Map<String, Object> calcularRuta(@PathVariable String origen, @PathVariable String destino) {
        GrafoParque grafo = crearGrafo();

        List<String> ruta = grafo.buscarRuta(origen, destino);

        Map<String, Object> respuesta = new HashMap<>();

        if (ruta.isEmpty()) {
            respuesta.put("mensaje", "No se encontro ruta");
            respuesta.put("origen", origen);
            respuesta.put("destino", destino);
            return respuesta;
        }

        respuesta.put("origen", origen);
        respuesta.put("destino", destino);
        respuesta.put("ruta", ruta);
        respuesta.put("cantidadParadas", ruta.size());

        return respuesta;
    }

    @GetMapping("/api/rutas/conexiones")
    public Map<String, Object> verConexiones() {
        GrafoParque grafo = crearGrafo();

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("conexiones", grafo.getConexiones());

        return respuesta;
    }
} 
    

