package com.techpark.backend.model;

import com.techpark.backend.structures.ArbolEmpleados;

public class Administrador extends Empleado {

    private ArbolEmpleados arbolEmpleados;

    public Administrador(String nombre, String documento, String idEmpleado) {
        super(nombre, documento, idEmpleado);
        this.arbolEmpleados = new ArbolEmpleados();
    }

    // Registrar empleado en el árbol
    public void registrarEmpleado(Empleado empleado) {
        if (empleado != null) {
            arbolEmpleados.insertar(empleado);
        }
    }

    // Buscar empleado por ID
    public Empleado buscarEmpleado(String idEmpleado) {
        return arbolEmpleados.buscar(idEmpleado);
    }

    // Asignar operador a una zona
    public void asignarOperadorAZona(Operador operador, Zona zona) {
        if (operador != null && zona != null) {
            zona.asignarOperador(operador);
        }
    }

    // Retirar operador de una zona
    public void retirarOperadorDeZona(Operador operador, Zona zona) {
        if (operador != null && zona != null) {
            zona.retirarOperador(operador);
        }
    }

    // Validar si una zona tiene operadores
    public boolean zonaTieneOperadores(Zona zona) {
        if (zona == null) {
            return false;
        }

        return zona.tieneOperadores();
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