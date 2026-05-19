package com.techpark.backend.structures;

import com.techpark.backend.model.TipoTicket;
import com.techpark.backend.model.Visitante;

public class ColaPrioridad {

    private ListaEnlazada<Visitante> colaFastPass;
    private ListaEnlazada<Visitante> colaGeneral;

    public ColaPrioridad() {
        colaFastPass = new ListaEnlazada<>();
        colaGeneral = new ListaEnlazada<>();
    }

    public void encolar(Visitante visitante) {
        if (visitante == null || visitante.getTicket() == null) {
            System.out.println("El visitante no tiene ticket");
            return;
        }

        if (visitante.getTicket().getTipo() == TipoTicket.FAST_PASS) {
            colaFastPass.agregar(visitante);
        } else {
            colaGeneral.agregar(visitante);
        }
    }

    public Visitante desencolar() {
        if (!colaFastPass.estaVacia()) {
            Visitante visitante = colaFastPass.obtener(0);
            colaFastPass.eliminar(visitante);
            return visitante;
        }

        if (!colaGeneral.estaVacia()) {
            Visitante visitante = colaGeneral.obtener(0);
            colaGeneral.eliminar(visitante);
            return visitante;
        }

        return null;
    }

    public boolean estaVacia() {
        return colaFastPass.estaVacia() && colaGeneral.estaVacia();
    }

    public int size() {
        return colaFastPass.size() + colaGeneral.size();
    }
}