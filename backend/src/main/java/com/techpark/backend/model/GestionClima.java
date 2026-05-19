package com.techpark.backend.model;

public class GestionClima {

    public void cerrarPorLluvia(Atraccion atraccion) {

        atraccion.setEstado(EstadoAtraccion.CERRADA);

        atraccion.setMotivoCierre("Cerrada por lluvia");

        System.out.println("Atraccion cerrada por lluvia");
    }

    public void cerrarPorTormenta(Atraccion atraccion) {

        atraccion.setEstado(EstadoAtraccion.CERRADA);

        atraccion.setMotivoCierre("Cerrada por tormenta");

        System.out.println("Atraccion cerrada por tormenta");
    }

    public void abrirAtraccion(Atraccion atraccion) {

        atraccion.setEstado(EstadoAtraccion.ACTIVA);

        atraccion.setMotivoCierre("");

        System.out.println("Atraccion abierta nuevamente");
    }
}
