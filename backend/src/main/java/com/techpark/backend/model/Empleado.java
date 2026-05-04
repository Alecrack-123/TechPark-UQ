package com.techpark.backend.model;

public abstract class Empleado extends Persona {
    protected String idEmpleado;

    public Empleado(String nombre, String documento, String idEmpleado) {
        super(nombre, documento);
        this.idEmpleado = idEmpleado;
    }

    public String getIdEmpleado() { return idEmpleado; }
}