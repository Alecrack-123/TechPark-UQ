package com.techpark.backend.structures;

import java.util.*;

public class GrafoParque {

    private Map<String, List<Arista>> conexiones;

    public GrafoParque() {
        conexiones = new HashMap<>();
    }

    public void limpiar() {
        conexiones.clear();
    }

    public void agregarAtraccion(String id) {
        conexiones.putIfAbsent(id, new ArrayList<>());
    }

    public void conectar(String origen, String destino) {
        conectar(origen, destino, 1);
    }

    public void conectar(String origen, String destino, int peso) {
        agregarAtraccion(origen);
        agregarAtraccion(destino);

        conexiones.get(origen).add(new Arista(destino, peso));
        conexiones.get(destino).add(new Arista(origen, peso));
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

            for (Arista arista : conexiones.getOrDefault(actual, new ArrayList<>())) {
                String vecino = arista.destino;

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

    public ResultadoRuta buscarRutaMasCorta(String origen, String destino) {
        Map<String, Integer> distancias = new HashMap<>();
        Map<String, String> anterior = new HashMap<>();
        PriorityQueue<NodoRuta> pendientes = new PriorityQueue<>(Comparator.comparingInt(n -> n.distancia));

        for (String nodo : conexiones.keySet()) {
            distancias.put(nodo, Integer.MAX_VALUE);
        }

        distancias.put(origen, 0);
        pendientes.add(new NodoRuta(origen, 0));

        while (!pendientes.isEmpty()) {
            NodoRuta actual = pendientes.poll();

            if (actual.distancia > distancias.getOrDefault(actual.id, Integer.MAX_VALUE)) {
                continue;
            }

            if (actual.id.equals(destino)) {
                break;
            }

            for (Arista arista : conexiones.getOrDefault(actual.id, new ArrayList<>())) {
                int nuevaDistancia = actual.distancia + arista.peso;

                if (nuevaDistancia < distancias.getOrDefault(arista.destino, Integer.MAX_VALUE)) {
                    distancias.put(arista.destino, nuevaDistancia);
                    anterior.put(arista.destino, actual.id);
                    pendientes.add(new NodoRuta(arista.destino, nuevaDistancia));
                }
            }
        }

        List<String> ruta = reconstruirRuta(origen, destino, anterior);
        int distancia = distancias.getOrDefault(destino, Integer.MAX_VALUE);

        if (ruta.isEmpty()) {
            distancia = 0;
        }

        return new ResultadoRuta(ruta, distancia);
    }

    private List<String> reconstruirRuta(String origen, String destino, Map<String, String> anterior) {
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

    public Map<String, List<Map<String, Object>>> getConexiones() {
        Map<String, List<Map<String, Object>>> respuesta = new HashMap<>();

        for (Map.Entry<String, List<Arista>> entry : conexiones.entrySet()) {
            List<Map<String, Object>> aristas = new ArrayList<>();

            for (Arista arista : entry.getValue()) {
                Map<String, Object> dato = new HashMap<>();
                dato.put("destino", arista.destino);
                dato.put("peso", arista.peso);
                aristas.add(dato);
            }

            respuesta.put(entry.getKey(), aristas);
        }

        return respuesta;
    }

    private static class Arista {
        private String destino;
        private int peso;

        private Arista(String destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }
    }

    private static class NodoRuta {
        private String id;
        private int distancia;

        private NodoRuta(String id, int distancia) {
            this.id = id;
            this.distancia = distancia;
        }
    }

    public static class ResultadoRuta {
        private final List<String> ruta;
        private final int distanciaTotal;

        public ResultadoRuta(List<String> ruta, int distanciaTotal) {
            this.ruta = ruta;
            this.distanciaTotal = distanciaTotal;
        }

        public List<String> getRuta() {
            return ruta;
        }

        public int getDistanciaTotal() {
            return distanciaTotal;
        }
    }

}
