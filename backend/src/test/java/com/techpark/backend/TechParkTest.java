package com.techpark.backend;

import com.techpark.backend.model.*;
import com.techpark.backend.structures.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TechParkTest {

    @Test
    public void pruebaListaEnlazadaAgregarYObtener() {
        ListaEnlazada<String> lista = new ListaEnlazada<>();

        lista.agregar("A");
        lista.agregar("B");

        assertEquals(2, lista.size());
        assertEquals("A", lista.obtener(0));
        assertEquals("B", lista.obtener(1));
    }

    @Test
    public void pruebaColaPrioridadFastPassPrimero() {
        ColaPrioridad cola = new ColaPrioridad();

        Visitante visitanteGeneral = new Visitante("Juan", "1", 20, 1.75, 200);
        Visitante visitanteFast = new Visitante("Ana", "2", 22, 1.70, 300);

        visitanteGeneral.comprarTicket(TipoTicket.GENERAL, 50);
        visitanteFast.comprarTicket(TipoTicket.FAST_PASS, 100);

        cola.encolar(visitanteGeneral);
        cola.encolar(visitanteFast);

        assertEquals("Ana", cola.desencolar().getNombre());
    }

    @Test
    public void pruebaArbolAtraccionesBuscar() {
        ArbolAtracciones arbol = new ArbolAtracciones();

        Atraccion atraccion1 = new Atraccion("A1", "Montaña Rusa", TipoAtraccion.MECANICA_ALTURA, 20, 1.50, 12, 0);
        Atraccion atraccion2 = new Atraccion("A2", "Carrusel", TipoAtraccion.INFANTIL, 15, 1.00, 5, 0);

        arbol.insertar(atraccion1);
        arbol.insertar(atraccion2);

        Atraccion encontrada = arbol.buscar("A2");

        assertNotNull(encontrada);
        assertEquals("Carrusel", encontrada.getNombre());
    }

    @Test
    public void pruebaSetFavoritosNoRepite() {
        Visitante visitante = new Visitante("Carlos", "3", 25, 1.80, 200);

        Atraccion atraccion1 = new Atraccion("A1", "Montaña Rusa", TipoAtraccion.MECANICA_ALTURA, 20, 1.50, 12, 0);

        visitante.agregarFavorito(atraccion1);
        visitante.agregarFavorito(atraccion1);

        assertEquals(1, visitante.getCantidadFavoritos());
    }

    @Test
    public void pruebaGestionClimaCierraAtraccion() {
        GestionClima clima = new GestionClima();

        Atraccion atraccion = new Atraccion("A1", "Montaña Rusa", TipoAtraccion.MECANICA_ALTURA, 20, 1.50, 12, 0);

        clima.cerrarPorLluvia(atraccion);

        assertEquals(EstadoAtraccion.CERRADA, atraccion.getEstado());
        assertEquals("Cerrada por lluvia", atraccion.getMotivoCierre());
    }

    @Test
    public void pruebaMantenimientoAutomatico() {
        Visitante visitante = new Visitante("Pedro", "4", 20, 1.75, 100);

        Atraccion atraccion = new Atraccion("A3", "Torre Digital", TipoAtraccion.MECANICA_ALTURA, 10, 1.40, 12, 0);

        for (int i = 0; i < 500; i++) {
            atraccion.registrarIngreso(visitante);
        }

        assertEquals(500, atraccion.getContadorVisitantes());
        assertEquals(EstadoAtraccion.EN_MANTENIMIENTO, atraccion.getEstado());
    }

    @Test
    public void pruebaVisitanteNoPuedeIngresarPorEstatura() {
        Visitante visitante = new Visitante("Luis", "5", 18, 1.20, 100);
        visitante.comprarTicket(TipoTicket.GENERAL, 50);

        Atraccion atraccion = new Atraccion("A1", "Montaña Rusa", TipoAtraccion.MECANICA_ALTURA, 20, 1.50, 12, 0);

        assertFalse(visitante.puedeIngresar(atraccion));
    }
}