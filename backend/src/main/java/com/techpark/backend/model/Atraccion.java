package com.techpark.backend.model;

public class Atraccion {
    private String id;
    private String nombre;
    private TipoAtraccion tipo;
    private int capacidadPorCiclo;
    private double alturaMinima;
    private int edadMinima;
    private double costoAdicional;
    private int contadorVisitantes;
    private int tiempoEstimadoEspera;
    private EstadoAtraccion estado;
    private String motivoCierre;

    // Constructor
    public Atraccion(String id, String nombre, TipoAtraccion tipo, int capacidadPorCiclo,
                     double alturaMinima, int edadMinima, double costoAdicional) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidadPorCiclo = capacidadPorCiclo;
        this.alturaMinima = alturaMinima;
        this.edadMinima = edadMinima;
        this.costoAdicional = costoAdicional;
        this.contadorVisitantes = 0;
        this.tiempoEstimadoEspera = 0;
        this.estado = EstadoAtraccion.ACTIVA;
        this.motivoCierre = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoAtraccion getTipo() {
        return tipo;
    }

    public void setTipo(TipoAtraccion tipo) {
        this.tipo = tipo;
    }

    public int getCapacidadPorCiclo() {
        return capacidadPorCiclo;
    }

    public void setCapacidadPorCiclo(int capacidadPorCiclo) {
        this.capacidadPorCiclo = capacidadPorCiclo;
    }

    public double getAlturaMinima() {
        return alturaMinima;
    }

    public void setAlturaMinima(double alturaMinima) {
        this.alturaMinima = alturaMinima;
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(int edadMinima) {
        this.edadMinima = edadMinima;
    }

    public double getCostoAdicional() {
        return costoAdicional;
    }

    public void setCostoAdicional(double costoAdicional) {
        this.costoAdicional = costoAdicional;
    }

    public int getContadorVisitantes() {
        return contadorVisitantes;
    }

    public void setContadorVisitantes(int contadorVisitantes) {
        this.contadorVisitantes = contadorVisitantes;
    }

    public int getTiempoEstimadoEspera() {
        return tiempoEstimadoEspera;
    }

    public void setTiempoEstimadoEspera(int tiempoEstimadoEspera) {
        this.tiempoEstimadoEspera = tiempoEstimadoEspera;
    }

    public EstadoAtraccion getEstado() {
        return estado;
    }

    public void setEstado(EstadoAtraccion estado) {
        this.estado = estado;
    }

    public String getMotivoCierre() {
        return motivoCierre;
    }

    public void setMotivoCierre(String motivoCierre) {
        this.motivoCierre = motivoCierre;
    }

    // Método para cambiar el estado (usado por operadores o por el clima)
    public void cambiarEstado(EstadoAtraccion nuevoEstado, String motivo) {
        this.estado = nuevoEstado;
        this.motivoCierre = motivo;
    }

    // Método para que el operador reactive la atracción tras la revisión
    public void registrarRevisionTecnica() {
        this.contadorVisitantes = 0; // Se reinicia el contador tras el mantenimiento
        cambiarEstado(EstadoAtraccion.ACTIVA, "");
    }


}
