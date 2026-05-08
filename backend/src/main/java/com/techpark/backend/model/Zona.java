package com.techpark.backend.model;

import com.techpark.backend.structures.ListaEnlazada;

public class Zona {

    private String id;
    private String nombre;
    private int capacidadMaxima;
    private Operador operadorAsignado;

    private ListaEnlazada<Atraccion> atracciones;
    private ListaEnlazada<Operador> operadoresAsignados;

    public Zona(String id, String nombre, int capacidadMaxima) {
        this.id = id;
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;

        this.atracciones = new ListaEnlazada<>();
        this.operadoresAsignados = new ListaEnlazada<>();
    }

    public void agregarAtraccion(Atraccion a) {

        if (a != null) {
            this.atracciones.agregar(a);
        }
    }

    public void asignarOperador(Operador o) {

        if (o != null && !operadoresAsignados.contiene(o)) {

            this.operadorAsignado = o;

            // Vinculación bidireccional
            o.setZonaAsignada(this);

            this.operadoresAsignados.agregar(o);
        }
    }

    public boolean retirarOperador(Operador operador) {

        if (operadoresAsignados.size() <= 1) {

            throw new IllegalStateException(
                    "La zona no puede quedar sin operador."
            );
        }

        if (operadorAsignado == operador) {
            operadorAsignado = null;
        }

        return operadoresAsignados.eliminar(operador);
    }

    public boolean tieneOperadores() {
        return !operadoresAsignados.estaVacia();
    }

    public boolean contieneAtraccion(Atraccion atraccion) {
        return atracciones.contiene(atraccion);
    }

    public int calcularOcupacion() {
        return 0;
    }

    public int getCantidadAtracciones() {
        return atracciones.size();
    }

    public Atraccion obtenerAtraccion(int indice) {
        return atracciones.obtener(indice);
    }

    public ListaEnlazada<Atraccion> getAtracciones() {
        return atracciones;
    }

    public int getCantidadOperadores() {
        return operadoresAsignados.size();
    }

    public Operador obtenerOperador(int indice) {
        return operadoresAsignados.obtener(indice);
    }

    public ListaEnlazada<Operador> getOperadoresAsignados() {
        return operadoresAsignados;
    }

    // --- GETTERS ---
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public Operador getOperadorAsignado() {
        return operadorAsignado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public void setId(String id) {
        this.id = id;
    }
}