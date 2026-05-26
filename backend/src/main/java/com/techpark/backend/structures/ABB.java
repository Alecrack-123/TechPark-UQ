package com.techpark.backend.structures;



public class ABB<T extends Comparable<T>> {

    private static class Nodo<T> {
        T dato;
        Nodo<T> izq, der;
        Nodo(T dato) { this.dato = dato; }
    }

    private Nodo<T> raiz;

    public void insertar(T dato) { raiz = insertarRec(raiz, dato); }

    private Nodo<T> insertarRec(Nodo<T> nodo, T dato) {
        if (nodo == null) return new Nodo<>(dato);
        int cmp = dato.compareTo(nodo.dato);
        if (cmp < 0)      nodo.izq = insertarRec(nodo.izq, dato);
        else if (cmp > 0) nodo.der = insertarRec(nodo.der, dato);
        return nodo;
    }

    public boolean contiene(T dato) { return buscarRec(raiz, dato) != null; }

    public T buscar(T dato) {
        Nodo<T> n = buscarRec(raiz, dato);
        return (n != null) ? n.dato : null;
    }

    private Nodo<T> buscarRec(Nodo<T> nodo, T dato) {
        if (nodo == null) return null;
        int cmp = dato.compareTo(nodo.dato);
        if (cmp == 0) return nodo;
        return cmp < 0 ? buscarRec(nodo.izq, dato) : buscarRec(nodo.der, dato);
    }

    public void eliminar(T dato) { raiz = eliminarRec(raiz, dato); }

    private Nodo<T> eliminarRec(Nodo<T> nodo, T dato) {
        if (nodo == null) return null;
        int cmp = dato.compareTo(nodo.dato);
        if (cmp < 0)      nodo.izq = eliminarRec(nodo.izq, dato);
        else if (cmp > 0) nodo.der = eliminarRec(nodo.der, dato);
        else {
            if (nodo.izq == null) return nodo.der;
            if (nodo.der == null) return nodo.izq;
            Nodo<T> sucesor = minimoNodo(nodo.der);
            nodo.dato = sucesor.dato;
            nodo.der = eliminarRec(nodo.der, sucesor.dato);
        }
        return nodo;
    }

    private Nodo<T> minimoNodo(Nodo<T> nodo) {
        while (nodo.izq != null) nodo = nodo.izq;
        return nodo;
    }

    public void inorden(java.util.function.Consumer<T> accion) { inordenRec(raiz, accion); }

    private void inordenRec(Nodo<T> nodo, java.util.function.Consumer<T> accion) {
        if (nodo == null) return;
        inordenRec(nodo.izq, accion);
        accion.accept(nodo.dato);
        inordenRec(nodo.der, accion);
    }

    public boolean estaVacio() { return raiz == null; }
}