package com.techpark.backend.structures;

public class SetPropio<T> {

    private static final int CAPACIDAD_INICIAL = 16;
    private static final double FACTOR_CARGA = 0.75;

    private Object[] tabla;
    private boolean[] ocupado;
    private int tamanio;

    public SetPropio() {
        tabla   = new Object[CAPACIDAD_INICIAL];
        ocupado = new boolean[CAPACIDAD_INICIAL];
        tamanio = 0;
    }

    public boolean agregar(T dato) {
        if ((double) tamanio / tabla.length >= FACTOR_CARGA) rehash();
        int idx = indice(dato, tabla.length);
        while (ocupado[idx]) {
            if (tabla[idx].equals(dato)) return false;
            idx = (idx + 1) % tabla.length;
        }
        tabla[idx] = dato;
        ocupado[idx] = true;
        tamanio++;
        return true;
    }

    public boolean contiene(T dato) {
        int idx = indice(dato, tabla.length);
        int inicio = idx;
        while (ocupado[idx]) {
            if (tabla[idx].equals(dato)) return true;
            idx = (idx + 1) % tabla.length;
            if (idx == inicio) break;
        }
        return false;
    }

    public boolean eliminar(T dato) {
        int idx = indice(dato, tabla.length);
        int inicio = idx;
        while (ocupado[idx]) {
            if (tabla[idx].equals(dato)) {
                tabla[idx] = null;
                ocupado[idx] = false;
                tamanio--;
                return true;
            }
            idx = (idx + 1) % tabla.length;
            if (idx == inicio) break;
        }
        return false;
    }

    public void forEach(java.util.function.Consumer<T> accion) {
        for (int i = 0; i < tabla.length; i++) {
            if (ocupado[i]) {
                @SuppressWarnings("unchecked") T dato = (T) tabla[i];
                accion.accept(dato);
            }
        }
    }

    public int getTamanio() { return tamanio; }
    public boolean estaVacio() { return tamanio == 0; }

    private int indice(T dato, int capacidad) {
        return Math.abs(dato.hashCode()) % capacidad;
    }

    private void rehash() {
        Object[] viejaTabla   = tabla;
        boolean[] viejoOcupado = ocupado;
        tabla   = new Object[viejaTabla.length * 2];
        ocupado = new boolean[tabla.length];
        tamanio = 0;
        for (int i = 0; i < viejaTabla.length; i++) {
            if (viejoOcupado[i]) {
                @SuppressWarnings("unchecked") T dato = (T) viejaTabla[i];
                agregar(dato);
            }
        }
    }
}