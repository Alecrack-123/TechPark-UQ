package com.techpark.backend.model;

public class Administrador extends Empleado {

    public Administrador(String nombre, String documento, String idEmpleado) {
        super(nombre, documento, idEmpleado);
    }

    // Métodos de gestión según tu diagrama UML
    public void gestionarEmpleados() {
        System.out.println("Administrador gestionando empleados...");
    }

    public void configurarZonas() {
        System.out.println("Administrador configurando zonas...");
    }

    public void asignarOperadores() {
        System.out.println("Administrador asignando operadores a las zonas...");
    }

    public void consultarEstadisticas() {
        System.out.println("Generando estadísticas globales del parque...");
    }

    // Método vital para simular eventos climáticos que afecten las atracciones
    public void activarAlertaClima(EstadoClima estado) {
        System.out.println("¡ALERTA! El administrador ha cambiado el clima del parque a: " + estado);
        // Aquí luego recorreremos el parque para cerrar atracciones si hay TORMENTA
    }
}