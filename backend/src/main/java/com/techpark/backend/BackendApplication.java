package com.techpark.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.techpark.backend.model.Administrador;
import com.techpark.backend.model.Atraccion;
import com.techpark.backend.model.Empleado;
import com.techpark.backend.model.EstadoAtraccion;
import com.techpark.backend.model.GestionZonas;
import com.techpark.backend.model.Operador;
import com.techpark.backend.model.TipoAtraccion;
import com.techpark.backend.model.TipoTicket;
import com.techpark.backend.model.Visitante;
import com.techpark.backend.model.Zona;
import com.techpark.backend.model.GestionClima;

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

        operador.cambiarEstadoAtraccion(atraccion1, EstadoAtraccion.ACTIVA, "Se reactiva para prueba de fila");

        System.out.println("\n--- PRUEBA GESTION DE ZONAS ---");

        GestionZonas gestionZonas = new GestionZonas();

        Zona zonaInfantil = new Zona("Z2", "Zona Infantil", 80);
        Zona zonaAcuatica = new Zona("Z3", "Zona Acuatica", 120);
        Zona zonaComidas = new Zona("Z4", "Zona de Comidas", 60);

        gestionZonas.agregarZona(zona);
        gestionZonas.agregarZona(zonaInfantil);
        gestionZonas.agregarZona(zonaAcuatica);
        gestionZonas.agregarZona(zonaComidas);

        gestionZonas.mostrarZonas();

        gestionZonas.modificarZona("Z4", "Zona Gastronomica", 90);

        gestionZonas.mostrarZonas();

        System.out.println("\n--- PRUEBA COLA DE PRIORIDAD ---");

        Visitante visitante1 = new Visitante("Pedro", "111", 25, 1.80, 200);
        Visitante visitante2 = new Visitante("Maria", "222", 19, 1.65, 150);
        Visitante visitante3 = new Visitante("Juanito", "333", 30, 1.90, 300);

        visitante1.comprarTicket(TipoTicket.GENERAL, 50);
        visitante2.comprarTicket(TipoTicket.FAST_PASS, 100);
        visitante3.comprarTicket(TipoTicket.GENERAL, 50);

        atraccion1.agregarVisitanteAFila(visitante1);
        atraccion1.agregarVisitanteAFila(visitante2);
        atraccion1.agregarVisitanteAFila(visitante3);

        System.out.println("Personas en fila: " + atraccion1.getCantidadEnFila());

        operador.procesarFila(atraccion1);

        System.out.println("Personas restantes en fila: " + atraccion1.getCantidadEnFila());
        System.out.println("Visitantes acumulados en atraccion: " + atraccion1.getContadorVisitantes());

        System.out.println("\n--- PRUEBA MANTENIMIENTO AUTOMATICO ---");

        Atraccion atraccionMantenimiento = new Atraccion(
                "A3",
                "Torre Digital",
                TipoAtraccion.MECANICA_ALTURA,
                10,
                1.40,
                12,
                0
        );

        zona.agregarAtraccion(atraccionMantenimiento);

        for (int i = 0; i < 500; i++) {
            atraccionMantenimiento.registrarIngreso(visitante);
        }

        System.out.println("Visitantes acumulados: " + atraccionMantenimiento.getContadorVisitantes());
        System.out.println("Estado actual: " + atraccionMantenimiento.getEstado());
        System.out.println("Motivo cierre: " + atraccionMantenimiento.getMotivoCierre());

        operador.registrarRevisionTecnica(atraccionMantenimiento);

        System.out.println("Estado despues de revision: " + atraccionMantenimiento.getEstado());
        System.out.println("Visitantes despues de revision: " + atraccionMantenimiento.getContadorVisitantes());
    
         System.out.println("\n--- PRUEBA CLIMA ---");

        GestionClima gestionClima = new GestionClima();

        gestionClima.cerrarPorLluvia(atraccion1);

        System.out.println("Estado por lluvia: " + atraccion1.getEstado());
        System.out.println("Motivo: " + atraccion1.getMotivoCierre());

        gestionClima.abrirAtraccion(atraccion1);

        System.out.println("Estado despues de abrir: " + atraccion1.getEstado());

        gestionClima.cerrarPorTormenta(atraccion2);

        System.out.println("Estado por tormenta: " + atraccion2.getEstado());
        System.out.println("Motivo: " + atraccion2.getMotivoCierre());
    }
}