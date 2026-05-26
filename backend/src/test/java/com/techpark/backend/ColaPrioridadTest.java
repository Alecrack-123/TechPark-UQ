package com.techpark.backend.structures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ColaPrioridadTest {

    @Test
    void fastPassSaleAntesQueGeneral() {
        ColaPrioridad<String> cola = new ColaPrioridad<>();
        cola.encolar("General-1",  2);
        cola.encolar("FastPass-1", 1);
        cola.encolar("General-2",  2);
        cola.encolar("FastPass-2", 1);
        assertEquals("FastPass-1", cola.desencolar());
        assertEquals("FastPass-2", cola.desencolar());
        assertEquals("General-1",  cola.desencolar());
        assertEquals("General-2",  cola.desencolar());
    }

    @Test
    void colaVaciaLanzaExcepcion() {
        assertThrows(RuntimeException.class, new ColaPrioridad<String>()::desencolar);
    }

    @Test
    void tamanioCorrectoTrasOperaciones() {
        ColaPrioridad<Integer> cola = new ColaPrioridad<>();
        cola.encolar(1, 2); cola.encolar(2, 1); cola.encolar(3, 2);
        assertEquals(3, cola.getTamanio());
        cola.desencolar();
        assertEquals(2, cola.getTamanio());
    }
}