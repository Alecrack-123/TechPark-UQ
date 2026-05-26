package com.techpark.backend.structures;


public class ColaPrioridad<T> {

    private static class Elemento<T> {
        T dato;
        int prioridad;
        long orden;
        Elemento(T dato, int prioridad, long orden) {
            this.dato = dato; this.prioridad = prioridad; this.orden = orden;
        }
    }

    @SuppressWarnings("unchecked")
    private Elemento<T>[] heap = new Elemento[16];
    private int tamanio = 0;
    private long contadorOrden = 0;

    public void encolar(T dato, int prioridad) {
        if (tamanio == heap.length) crecer();
        heap[tamanio] = new Elemento<>(dato, prioridad, contadorOrden++);
        subirHeap(tamanio);
        tamanio++;
    }

    public T desencolar() {
        if (estaVacia()) throw new RuntimeException("La cola de prioridad está vacía.");
        T resultado = heap[0].dato;
        tamanio--;
        heap[0] = heap[tamanio];
        heap[tamanio] = null;
        if (!estaVacia()) bajarHeap(0);
        return resultado;
    }

    public T verFrente() {
        if (estaVacia()) throw new RuntimeException("La cola de prioridad está vacía.");
        return heap[0].dato;
    }

    public boolean estaVacia() { return tamanio == 0; }
    public int getTamanio()    { return tamanio; }

    private boolean menorQue(int a, int b) {
        if (heap[a].prioridad != heap[b].prioridad)
            return heap[a].prioridad < heap[b].prioridad;
        return heap[a].orden < heap[b].orden;
    }

    private void subirHeap(int i) {
        while (i > 0) {
            int padre = (i - 1) / 2;
            if (menorQue(i, padre)) { swap(i, padre); i = padre; }
            else break;
        }
    }

    private void bajarHeap(int i) {
        while (true) {
            int menor = i, izq = 2 * i + 1, der = 2 * i + 2;
            if (izq < tamanio && menorQue(izq, menor)) menor = izq;
            if (der < tamanio && menorQue(der, menor)) menor = der;
            if (menor == i) break;
            swap(i, menor); i = menor;
        }
    }

    private void swap(int a, int b) {
        Elemento<T> tmp = heap[a]; heap[a] = heap[b]; heap[b] = tmp;
    }

    @SuppressWarnings("unchecked")
    private void crecer() {
        Elemento<T>[] nuevo = new Elemento[heap.length * 2];
        System.arraycopy(heap, 0, nuevo, 0, heap.length);
        heap = nuevo;
    }
}