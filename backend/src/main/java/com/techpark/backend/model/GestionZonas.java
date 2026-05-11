package com.techpark.backend.model;

import com.techpark.backend.structures.ListaEnlazada;

public class GestionZonas {

    private ListaEnlazada<Zona> zonas;

    public GestionZonas() {
        zonas = new ListaEnlazada<>();
    }

    public void agregarZona(Zona zona) {
        if (zona == null) {
            System.out.println("La zona no puede ser nula");
            return;
        }

        if (buscarZona(zona.getId()) != null) {
            System.out.println("Ya existe una zona con ese id");
            return;
        }

        zonas.agregar(zona);
        System.out.println("Zona agregada correctamente: " + zona.getNombre());
    }

    public Zona buscarZona(String id) {
        for (int i = 0; i < zonas.size(); i++) {
            Zona zona = zonas.obtener(i);

            if (zona.getId().equals(id)) {
                return zona;
            }
        }

        return null;
    }

    public void modificarZona(String id, String nombreNuevo, int capacidadNueva) {
        Zona zona = buscarZona(id);

        if (zona == null) {
            System.out.println("No se encontro la zona");
            return;
        }

        zona.setNombre(nombreNuevo);
        zona.setCapacidadMaxima(capacidadNueva);

        System.out.println("Zona modificada correctamente");
    }

    public void mostrarZonas() {
        System.out.println("\n--- ZONAS DEL PARQUE ---");

        if (zonas.estaVacia()) {
            System.out.println("No hay zonas registradas");
            return;
        }

        for (int i = 0; i < zonas.size(); i++) {
            Zona zona = zonas.obtener(i);

            System.out.println(
                    "ID: " + zona.getId()
                    + " | Nombre: " + zona.getNombre()
                    + " | Capacidad: " + zona.getCapacidadMaxima()
                    + " | Atracciones: " + zona.getCantidadAtracciones()
                    + " | Operadores: " + zona.getCantidadOperadores()
            );
        }
    }

    public int getCantidadZonas() {
        return zonas.size();
    }
}