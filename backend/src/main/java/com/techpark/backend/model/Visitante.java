package com.techpark.backend.model;

// import com.techpark.backend.structures.SetPropio;
// import com.techpark.backend.structures.ListaEnlazadaPropia;

public class Visitante {
    private String nombre;
    private String documento;
    private int edad;
    private double estatura;
    private double saldoVirtual;
    private TipoTicket tipoTicket;

    // Estructuras propias requeridas por el proyecto
    // private SetPropio<String> favoritos;
    // private ListaEnlazadaPropia<String> historialVisitas;

    public Visitante(String nombre, String documento, int edad, double estatura, double saldoInicial, TipoTicket tipoTicket) {
        this.nombre = nombre;
        this.documento = documento;
        this.edad = edad;
        this.estatura = estatura;
        this.saldoVirtual = saldoInicial;
        this.tipoTicket = tipoTicket;

        // Aquí inicializaremos las estructuras cuando existan
        // this.favoritos = new SetPropio<>();
        // this.historialVisitas = new ListaEnlazadaPropia<>();
    }

    // Lógica de validación física y financiera
    public boolean puedeIngresar(Atraccion atraccion) {
        if (this.estatura < atraccion.getAlturaMinima()) return false;
        if (this.edad < atraccion.getEdadMinima()) return false;

        if (this.tipoTicket == TipoTicket.GENERAL && atraccion.getCostoAdicional() > 0) {
            return this.saldoVirtual >= atraccion.getCostoAdicional();
        }
        return true;
    }

    public void pagarEntrada(double costo) {
        if (this.saldoVirtual < costo) {
            throw new IllegalStateException("Saldo insuficiente.");
        }
        this.saldoVirtual -= costo;
    }

    // --- NUEVOS MÉTODOS DE INTERACCIÓN ---

    /* Descomentar cuando la ListaEnlazada esté lista
    public void agregarAHistorial(String nombreAtraccion) {
        this.historialVisitas.agregarAlFinal(nombreAtraccion);
    }
    */

    /* Descomentar cuando el Set esté listo
    public void agregarFavorito(String nombreAtraccion) {
        this.favoritos.agregar(nombreAtraccion);
    }
    */
    public TipoTicket getTipoTicket() {
        return this.tipoTicket;
    }

    public double getSaldoVirtual() {
        return this.saldoVirtual;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public double getEstatura() {
        return estatura;
    }

    public void setEstatura(double estatura) {
        this.estatura = estatura;
    }

    public void setSaldoVirtual(double saldoVirtual) {
        this.saldoVirtual = saldoVirtual;
    }

    public void setTipoTicket(TipoTicket tipoTicket) {
        this.tipoTicket = tipoTicket;
    }
}