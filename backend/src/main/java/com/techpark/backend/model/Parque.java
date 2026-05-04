package com.techpark.backend.model;

// import com.techpark.backend.structures.Grafo;
// import com.techpark.backend.structures.ABB;
// import com.techpark.backend.structures.ListaEnlazada;

public class Parque {
    private String nombre;
    private int capacidadMaxima;
    
    // Estructuras principales comentadas por ahora
    // private Grafo mapaParque;
    // private ABB catalogoAtracciones;
    // private ListaEnlazada<Zona> zonas;
    // private ListaEnlazada<Visitante> visitantes;
    // private ListaEnlazada<Empleado> empleados;

    public Parque(String nombre, int capacidadMaxima) {
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
    }

    // --- MÉTODOS GLOBALES SEGÚN EL DIAGRAMA ---

    public void simularClima(EstadoClima alerta) {
        System.out.println("Simulando clima: " + alerta);
    }

    public void verificarAforo() {
        System.out.println("Verificando aforo total del parque...");
    }

    public void calcularRutaOptima(Atraccion origen, Atraccion destino) {
        System.out.println("Calculando ruta con Dijkstra desde " + origen.getNombre() + " hasta " + destino.getNombre());
    }

    // --- REPORTES ---
    public void generarReporteIngresos() {}
    public void generarReporteVisitantes() {}
    public void generarReporteTiemposEspera() {}
    public void generarReporteMantenimiento() {}
    public void generarReporteIncidentes() {}

    // Getters
    public String getNombre() { return nombre; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
}