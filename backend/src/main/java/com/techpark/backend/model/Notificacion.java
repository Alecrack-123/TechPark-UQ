package com.techpark.backend.model;

import java.util.Date;

public class Notificacion {
    private String tipo;
    private String mensaje;
    private Date fecha;

    /**
     * Constructor de la notificación.
     * La fecha se asigna automáticamente al momento de crear el objeto.
     * * @param mensaje El texto de la alerta que verá el visitante.
     */
    public Notificacion(String mensaje) {
        this("GENERAL", mensaje);
    }

    public Notificacion(String tipo, String mensaje) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fecha = new Date(); // Captura la fecha y hora exacta del sistema
    }

    // --- GETTERS ---
    
    public String getTipo() {
        return tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Date getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        return "[" + fecha.toString() + "] " + tipo + ": " + mensaje;
    }
}
