package com.techpark.backend.structures;

import java.util.*;

public class GrafoParque {

    private Map<String, List<String>> conexiones;

    public GrafoParque() {
        conexiones = new HashMap<>();
    }

    public void agregarAtraccion(String id) {
        conexiones.putIfAbsent(id, new ArrayList<>());
    }

    public void conectar(String origen, String destino) {
        agregarAtraccion(origen);
        agregarAtraccion(destino);

        conexiones.get(origen).add(destino);
        conexiones.get(destino).add(origen);
    }

    public List<String> buscarRuta(String origen, String destino) {
        Queue<String> cola = new LinkedList<>();
        Map<String, String> anterior = new HashMap<>();
        Set<String> visitados = new HashSet<>();

        cola.add(origen);
        visitados.add(origen);

        while (!cola.isEmpty()) {
            String actual = cola.poll();

            if (actual.equals(destino)) {
                break;
            }

            for (String vecino : conexiones.getOrDefault(actual, new ArrayList<>())) {
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    anterior.put(vecino, actual);
                    cola.add(vecino);
                }
            }
        }

        List<String> ruta = new ArrayList<>();

        if (!origen.equals(destino) && !anterior.containsKey(destino)) {
            return ruta;
        }

        String actual = destino;

        while (actual != null) {
            ruta.add(0, actual);
            actual = anterior.get(actual);
        }

        return ruta;
    }

    public Map<String, List<String>> getConexiones() {
        return conexiones;
    }
}