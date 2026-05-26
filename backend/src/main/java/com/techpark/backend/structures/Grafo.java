package com.techpark.backend.structures;

import java.util.*;

public class Grafo {

    public static class Arista {
        public final String destino;
        public final double peso;
        public Arista(String destino, double peso) {
            this.destino = destino; this.peso = peso;
        }
    }

    private final Map<String, List<Arista>> adyacencia = new HashMap<>();

    public void agregarNodo(String id) {
        adyacencia.putIfAbsent(id, new ArrayList<>());
    }

    public void agregarArista(String origen, String destino, double peso) {
        adyacencia.computeIfAbsent(origen, k -> new ArrayList<>()).add(new Arista(destino, peso));
        adyacencia.computeIfAbsent(destino, k -> new ArrayList<>()).add(new Arista(origen, peso));
    }

    public List<String> getNodos() { return new ArrayList<>(adyacencia.keySet()); }

    public List<Arista> getVecinos(String nodo) {
        return adyacencia.getOrDefault(nodo, new ArrayList<>());
    }

    public List<String> dijkstra(String origen, String destino) {
        Map<String, Double> dist   = new HashMap<>();
        Map<String, String> previo = new HashMap<>();
        PriorityQueue<String> cola = new PriorityQueue<>(
                (a, b) -> Double.compare(
                        dist.getOrDefault(a, Double.MAX_VALUE),
                        dist.getOrDefault(b, Double.MAX_VALUE)
                )
        );

        for (String nodo : adyacencia.keySet()) dist.put(nodo, Double.MAX_VALUE);
        dist.put(origen, 0.0);
        cola.add(origen);

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            if (actual.equals(destino)) break;
            for (Arista a : getVecinos(actual)) {
                double nueva = dist.get(actual) + a.peso;
                if (nueva < dist.getOrDefault(a.destino, Double.MAX_VALUE)) {
                    dist.put(a.destino, nueva);
                    previo.put(a.destino, actual);
                    cola.add(a.destino);
                }
            }
        }

        List<String> ruta = new ArrayList<>();
        String paso = destino;
        while (paso != null) { ruta.add(0, paso); paso = previo.get(paso); }
        if (ruta.isEmpty() || !ruta.get(0).equals(origen)) return new ArrayList<>();
        return ruta;
    }

    public List<String> bfs(String origen) {
        List<String> visitados = new ArrayList<>();
        Set<String> visto = new HashSet<>();
        Queue<String> cola = new LinkedList<>();
        cola.add(origen); visto.add(origen);
        while (!cola.isEmpty()) {
            String actual = cola.poll();
            visitados.add(actual);
            for (Arista a : getVecinos(actual)) {
                if (!visto.contains(a.destino)) { visto.add(a.destino); cola.add(a.destino); }
            }
        }
        return visitados;
    }

    public boolean esConexo() {
        if (adyacencia.isEmpty()) return true;
        String inicio = adyacencia.keySet().iterator().next();
        return bfs(inicio).size() == adyacencia.size();
    }
}