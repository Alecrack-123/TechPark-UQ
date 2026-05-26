package com.techpark.backend.structures;

import java.util.ArrayList;
import java.util.List;

import com.techpark.backend.model.Atraccion;

public class ArbolAtracciones {

    private NodoArbol raiz;

    public ArbolAtracciones() {
        this.raiz = null;
    }

    public void limpiar() {
        this.raiz = null;
    }

    public void insertar(Atraccion atraccion) {
        raiz = insertarRecursivo(raiz, atraccion);
    }

    private NodoArbol insertarRecursivo(NodoArbol actual, Atraccion atraccion) {
        if (actual == null) {
            return new NodoArbol(atraccion);
        }
        if (atraccion.getId().compareTo(actual.atraccion.getId()) < 0) {
            actual.izquierda = insertarRecursivo(actual.izquierda, atraccion);
        } else if (atraccion.getId().compareTo(actual.atraccion.getId()) > 0) {
            actual.derecha = insertarRecursivo(actual.derecha, atraccion);
        }
        return actual;
    }

    public Atraccion buscar(String id) {
        return buscarPorId(id);
    }

    public Atraccion buscarPorId(String id) {
        return buscarPorIdRecursivo(raiz, id);
    }

    private Atraccion buscarPorIdRecursivo(NodoArbol actual, String id) {
        if (actual == null) return null;
        if (actual.atraccion.getId().equals(id)) return actual.atraccion;
        if (id.compareTo(actual.atraccion.getId()) < 0) {
            return buscarPorIdRecursivo(actual.izquierda, id);
        } else {
            return buscarPorIdRecursivo(actual.derecha, id);
        }
    }

    public List<Atraccion> listarTodas() {
        List<Atraccion> lista = new ArrayList<>();
        listarEnOrdenRecursivo(raiz, lista);
        return lista;
    }

    public boolean eliminar(String id) {
        if (buscarPorId(id) == null) {
            return false;
        }

        raiz = eliminarRecursivo(raiz, id);
        return true;
    }

    private NodoArbol eliminarRecursivo(NodoArbol actual, String id) {
        if (actual == null) {
            return null;
        }

        if (id.compareTo(actual.atraccion.getId()) < 0) {
            actual.izquierda = eliminarRecursivo(actual.izquierda, id);
            return actual;
        }

        if (id.compareTo(actual.atraccion.getId()) > 0) {
            actual.derecha = eliminarRecursivo(actual.derecha, id);
            return actual;
        }

        if (actual.izquierda == null) {
            return actual.derecha;
        }

        if (actual.derecha == null) {
            return actual.izquierda;
        }

        NodoArbol sucesor = encontrarMinimo(actual.derecha);
        actual.atraccion = sucesor.atraccion;
        actual.derecha = eliminarRecursivo(actual.derecha, sucesor.atraccion.getId());
        return actual;
    }

    private NodoArbol encontrarMinimo(NodoArbol nodo) {
        while (nodo.izquierda != null) {
            nodo = nodo.izquierda;
        }

        return nodo;
    }

    private void listarEnOrdenRecursivo(NodoArbol nodo, List<Atraccion> lista) {
        if (nodo != null) {
            listarEnOrdenRecursivo(nodo.izquierda, lista);
            lista.add(nodo.atraccion);
            listarEnOrdenRecursivo(nodo.derecha, lista);
        }
    }

    public void mostrarEnOrden() {
        mostrarEnOrdenRecursivo(raiz);
    }

    private void mostrarEnOrdenRecursivo(NodoArbol actual) {
        if (actual != null) {
            mostrarEnOrdenRecursivo(actual.izquierda);
            System.out.println(actual.atraccion.getId() + " - " + actual.atraccion.getNombre());
            mostrarEnOrdenRecursivo(actual.derecha);
        }
    }

    public void mostrar() {
        mostrarRecursivo(raiz);
    }

    private void mostrarRecursivo(NodoArbol nodo) {
        if (nodo != null) {
            mostrarRecursivo(nodo.izquierda);
            System.out.println(nodo.atraccion.getId() + " - " + nodo.atraccion.getNombre());
            mostrarRecursivo(nodo.derecha);
        }
    }

    private class NodoArbol {
        Atraccion atraccion;
        NodoArbol izquierda;
        NodoArbol derecha;

        public NodoArbol(Atraccion atraccion) {
            this.atraccion = atraccion;
            this.izquierda = null;
            this.derecha = null;
        }
    }
}
