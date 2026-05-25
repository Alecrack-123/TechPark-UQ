package com.techpark.backend.model;

import com.techpark.backend.structures.ListaEnlazada;
import com.techpark.backend.structures.SetFavoritos;

public class Visitante extends Persona {
    private int edad;
    private double estatura;
    private double saldoVirtual;
    private Ticket ticket; // Ahora usamos la clase Ticket en lugar del enum directo

    // Estructuras propias requeridas por el proyecto
    private SetFavoritos favoritos;
    private ListaEnlazada<Atraccion> historialVisitas;

    public Visitante(String nombre, String documento, int edad, double estatura, double saldoInicial) {
        super(nombre, documento); // Se envían a la clase Persona
        this.edad = edad;
        this.estatura = estatura;
        this.saldoVirtual = saldoInicial;

        // Inicialización de estructuras cuando las tengas listas
        this.favoritos = new SetFavoritos();
        this.historialVisitas = new ListaEnlazada<>();
    }

    // Lógica de validación física y financiera según el nuevo Ticket
    public boolean puedeIngresar(Atraccion atraccion) {
        if (this.estatura < atraccion.getAlturaMinima()) return false;
        if (this.edad < atraccion.getEdadMinima()) return false;

        // Validamos si tiene ticket y si es GENERAL para verificar el saldo extra
        if (this.ticket != null && this.ticket.getTipo() == TipoTicket.GENERAL && atraccion.getCostoAdicional() > 0) {
            return this.saldoVirtual >= atraccion.getCostoAdicional();
        }

        // Si no tiene ticket, técnicamente no puede entrar a la fila
        return this.ticket != null;
    }

    public void comprarTicket(TipoTicket tipo, double precio) {
        if (this.saldoVirtual >= precio) {
            this.pagar(precio);
            this.ticket = new Ticket(tipo, precio);
        } else {
            throw new IllegalStateException("Saldo insuficiente para comprar el ticket.");
        }
    }

    public void pagar(double monto) {
        if (this.saldoVirtual < monto) {
            throw new IllegalStateException("Saldo insuficiente.");
        }
        this.saldoVirtual -= monto;
    }

    public void recibirNotificacion(Notificacion n) {
        System.out.println("Mensaje para " + this.getNombre() + ": " + n.getMensaje());
    }

    // --- MÉTODOS DE INTERACCIÓN CON ESTRUCTURAS ---

    public void agregarAHistorial(Atraccion atraccion) {
        if (atraccion != null) {
            this.historialVisitas.agregar(atraccion);
        }
    }

    public ListaEnlazada<Atraccion> getHistorialVisitas() {
        return historialVisitas;
    }

    public int getCantidadVisitas() {
        return historialVisitas.size();
    }

    public Atraccion obtenerVisita(int indice) {
        return historialVisitas.obtener(indice);
    }

    public void agregarFavorito(Atraccion atraccion) {
        favoritos.agregar(atraccion);
    }

    public boolean esFavorito(Atraccion atraccion) {
        return favoritos.contiene(atraccion);
    }

    public int getCantidadFavoritos() {
        return favoritos.size();
    }

    public Atraccion obtenerFavorito(int indice) {
        return favoritos.obtener(indice);
    }

    public void mostrarFavoritos() {
        favoritos.mostrarFavoritos();
    }

    // --- GETTERS Y SETTERS ---

    public Ticket getTicket() {
        return this.ticket;
    }
   
    public void setTicket(Ticket ticket) {
    this.ticket = ticket;
    }

    public double getSaldoVirtual() {
        return this.saldoVirtual;
    }

    public int getEdad() {
        return edad;
    }

    public double getEstatura() {
        return estatura;
    }

    public void setSaldoVirtual(double saldoVirtual) {
        this.saldoVirtual = saldoVirtual;
    }

    public void setEstatura(double estatura) {
        this.estatura = estatura;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
}