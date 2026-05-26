package com.techpark.backend.structures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ListaEnlazadaTest {

    @Test
    void agregarYObtenerElementos() {
        ListaEnlazada<String> lista = new ListaEnlazada<>();
        lista.agregarAlFinal("A"); lista.agregarAlFinal("B"); lista.agregarAlFinal("C");
        assertEquals(3, lista.getTamanio());
        assertEquals("A", lista.obtener(0));
        assertEquals("C", lista.obtener(2));
    }

    @Test
    void eliminarElementoExistente() {
        ListaEnlazada<String> lista = new ListaEnlazada<>();
        lista.agregarAlFinal("X"); lista.agregarAlFinal("Y");
        assertTrue(lista.eliminar("X"));
        assertFalse(lista.contiene("X"));
        assertEquals(1, lista.getTamanio());
    }

    @Test
    void listaVaciaInicialmente() {
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        assertTrue(lista.estaVacia());
        assertEquals(0, lista.getTamanio());
    }
}