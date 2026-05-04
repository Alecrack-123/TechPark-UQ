package com.techpark.backend.model;

// import com.techpark.backend.structures.ListaEnlazada;

public class Zona {
    private String id;
    private String nombre;
    private int capacidadMaxima;
    private Operador operadorAsignado;
    
    // private ListaEnlazada<Atraccion> atracciones;

    public Zona(String id, String nombre, int capacidadMaxima) {
        this.id = id;
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
        // this.atracciones = new ListaEnlazada<>();
    }

    public void agregarAtraccion(Atraccion a) {
        // this.atracciones.agregarAlFinal(a);
    }

    public void asignarOperador(Operador o) {
        this.operadorAsignado = o;
        // Vinculación bidireccional
        o.setZonaAsignada(this); 
    }

    public int calcularOcupacion() {
        // Aquí recorreremos la lista de atracciones para sumar sus contadores
        return 0; 
    }

    // --- GETTERS ---
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public Operador getOperadorAsignado() { return operadorAsignado; }
}