package com.techpark.backend.model;

import com.techpark.backend.structures.ColaPrioridad;

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
    private ColaPrioridad filaVirtual;
    private boolean filaPausada;

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
        this.filaVirtual = new ColaPrioridad();
        this.filaPausada = false;
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

    public boolean validarAcceso(Visitante v) {
        if (v.getEstatura() < this.alturaMinima) return false;
        if (v.getEdad() < this.edadMinima) return false;

        // Validación de saldo para tickets generales en atracciones con costo extra
        if (v.getTicket() != null && v.getTicket().getTipo() == TipoTicket.GENERAL && this.costoAdicional > 0) {
            return v.getSaldoVirtual() >= this.costoAdicional;
        }
        return true;
    }

    public void registrarIngreso(Visitante v) {
        this.contadorVisitantes++;
        verificarMantenimientoAutomatico();
    }

    public void verificarMantenimientoAutomatico() {
        // Bloqueo automático al alcanzar los 500 visitantes
        if (this.contadorVisitantes >= 500) {
            this.estado = EstadoAtraccion.EN_MANTENIMIENTO;
            this.motivoCierre = "Límite de visitantes alcanzado. Requiere revisión técnica.";
            notificarCambioEstado();
        }
    }

    public void verificarMantenimiento() {
        this.contadorVisitantes = 0;
        this.estado = EstadoAtraccion.ACTIVA;
        this.motivoCierre = "";
        notificarCambioEstado();
    }

    public void iniciarCiclo() {
        System.out.println("La atracción " + this.nombre + " ha iniciado su ciclo.");
    }

    public void detenerCiclo() {
        System.out.println("La atracción " + this.nombre + " ha finalizado su ciclo.");
    }

    public void notificarCambioEstado() {
        System.out.println("Notificación: La atracción " + this.nombre + " ahora está " + this.estado);
    }

    public void agregarVisitanteAFila(Visitante visitante) {
        filaVirtual.encolar(visitante);
    }

    public Visitante sacarVisitanteDeFila() {
        return filaVirtual.desencolar();
    }

    public ColaPrioridad getFilaVirtual() {
        return filaVirtual;
    }

    public int getCantidadEnFila() {
        return filaVirtual.size();
    }

    public boolean isFilaPausada() {
        return filaPausada;
    }

    public void pausarFila() {
        this.filaPausada = true;
    }

    public void reanudarFila() {
        this.filaPausada = false;
    }
}
