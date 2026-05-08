package com.techpark.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.techpark.backend.model.Administrador;
import com.techpark.backend.model.Atraccion;
import com.techpark.backend.model.Empleado;
import com.techpark.backend.model.EstadoAtraccion;
import com.techpark.backend.model.Operador;
import com.techpark.backend.model.TipoAtraccion;
import com.techpark.backend.model.Visitante;
import com.techpark.backend.model.Zona;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);

        Visitante visitante = new Visitante("Juan", "123", 20, 1.75, 100);

        Atraccion atraccion1 = new Atraccion("A1", "Montaña Rusa", TipoAtraccion.MECANICA_ALTURA, 20, 1.50, 12, 0);
        Atraccion atraccion2 = new Atraccion("A2", "Carrusel", TipoAtraccion.INFANTIL, 15, 1.00, 5, 0);

        visitante.agregarAHistorial(atraccion1);
        visitante.agregarAHistorial(atraccion2);

        System.out.println("Cantidad de visitas: " + visitante.getCantidadVisitas());
        System.out.println("Primera visita: " + visitante.obtenerVisita(0).getNombre());

        Zona zona = new Zona("Z1", "Zona Aventura", 100);

        Operador operador = new Operador("Carlos", "999", "OP1");

        zona.asignarOperador(operador);
        zona.agregarAtraccion(atraccion1);
        zona.agregarAtraccion(atraccion2);

        System.out.println("Cantidad de operadores en zona: " + zona.getCantidadOperadores());
        System.out.println("Operador asignado: " + zona.obtenerOperador(0).getNombre());

        System.out.println("Cantidad de atracciones en zona: " + zona.getCantidadAtracciones());
        System.out.println("Primera atracción en zona: " + zona.obtenerAtraccion(0).getNombre());

        System.out.println("Zona del operador: " + operador.getZonaAsignada().getNombre());

        System.out.println("\n--- PRUEBA ADMINISTRADOR Y OPERADORES ---");

        Administrador admin = new Administrador("Laura", "111", "ADM1");
        Operador operador2 = new Operador("Ana", "888", "OP2");

        admin.registrarEmpleado(operador);
        admin.registrarEmpleado(operador2);

        Empleado empleadoBuscado = admin.buscarEmpleado("OP1");

        if (empleadoBuscado != null) {
            System.out.println("Empleado encontrado: " + empleadoBuscado.getNombre());
        } else {
            System.out.println("Empleado no encontrado");
        }

        admin.asignarOperadorAZona(operador2, zona);

        System.out.println("Cantidad de operadores actualizada: " + zona.getCantidadOperadores());
        System.out.println("La zona tiene operadores: " + admin.zonaTieneOperadores(zona));

        operador.cambiarEstadoAtraccion(atraccion1, EstadoAtraccion.CERRADA, "Prueba de cierre por administrador");

        System.out.println("Estado atracción 1: " + atraccion1.getEstado());
        System.out.println("Motivo cierre atracción 1: " + atraccion1.getMotivoCierre());
    }
}