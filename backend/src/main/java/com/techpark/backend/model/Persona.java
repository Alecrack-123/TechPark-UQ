package com.techpark.backend.model;

public abstract class Persona {
    protected String nombre;
    protected String documento;

    public Persona(String nombre, String documento) {
        this.nombre = nombre;
        this.documento = documento;
    }

    public String getNombre() { return nombre; }
    public String getDocumento() { return documento; }
}