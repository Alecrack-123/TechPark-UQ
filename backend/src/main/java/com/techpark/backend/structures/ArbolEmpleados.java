package com.techpark.backend.structures;

import com.techpark.backend.model.Empleado;

public class ArbolEmpleados {

    private NodoArbol raiz;

    public void insertar(Empleado empleado) {
        raiz = insertar(raiz, empleado);
    }

    private NodoArbol insertar(NodoArbol nodo, Empleado empleado) {
        if (nodo == null) {
            return new NodoArbol(empleado);
        }

        if (empleado.getIdEmpleado().compareTo(nodo.empleado.getIdEmpleado()) < 0) {
            nodo.izquierdo = insertar(nodo.izquierdo, empleado);
        } else if (empleado.getIdEmpleado().compareTo(nodo.empleado.getIdEmpleado()) > 0) {
            nodo.derecho = insertar(nodo.derecho, empleado);
        }

        return nodo;
    }

    public Empleado buscar(String idEmpleado) {
        return buscar(raiz, idEmpleado);
    }

    private Empleado buscar(NodoArbol nodo, String idEmpleado) {
        if (nodo == null) {
            return null;
        }

        if (idEmpleado.equals(nodo.empleado.getIdEmpleado())) {
            return nodo.empleado;
        }

        if (idEmpleado.compareTo(nodo.empleado.getIdEmpleado()) < 0) {
            return buscar(nodo.izquierdo, idEmpleado);
        } else {
            return buscar(nodo.derecho, idEmpleado);
        }
    }

    private class NodoArbol {
        Empleado empleado;
        NodoArbol izquierdo;
        NodoArbol derecho;

        public NodoArbol(Empleado empleado) {
            this.empleado = empleado;
        }
    }
}