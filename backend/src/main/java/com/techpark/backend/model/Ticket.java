package com.techpark.backend.model;

import java.util.Date;

public class Ticket {
    private TipoTicket tipo;
    private double precio;
    private Date fechaCompra;

    /**
     * Constructor para crear un ticket.
     * @param tipo El enum (GENERAL, FAMILIAR o FAST_PASS)
     * @param precio El valor pagado por el visitante
     */
    public Ticket(TipoTicket tipo, double precio) {
        this.tipo = tipo;
        this.precio = precio;
        this.fechaCompra = new Date(); // Asigna automáticamente la fecha y hora actual
    }

    // --- GETTERS ---
    // No solemos poner setters aquí porque un ticket no debería modificarse tras la compra
    
    public TipoTicket getTipo() {
        return tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    @Override
    public String toString() {
        return "Ticket [" + tipo + " | Precio: $" + precio + " | Fecha: " + fechaCompra + "]";
    }
}