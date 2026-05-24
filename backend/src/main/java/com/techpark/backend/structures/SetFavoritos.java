package com.techpark.backend.structures;

import com.techpark.backend.model.Atraccion;

public class SetFavoritos {

    private ListaEnlazada<Atraccion> favoritos;

    public SetFavoritos() {
        favoritos = new ListaEnlazada<>();
    }

    public void agregar(Atraccion atraccion) {

        if (atraccion == null) {
            return;
        }

        if (!contiene(atraccion)) {
            favoritos.agregar(atraccion);
        }
    }

    public boolean contiene(Atraccion atraccion) {

        for (int i = 0; i < favoritos.size(); i++) {

            Atraccion actual = favoritos.obtener(i);

            if (actual.getId().equals(atraccion.getId())) {
                return true;
            }
        }

        return false;
    }

    public int size() {
        return favoritos.size();
    }

    public Atraccion obtener(int indice) {
        return favoritos.obtener(indice);
    }

    public void mostrarFavoritos() {

        for (int i = 0; i < favoritos.size(); i++) {
            System.out.println(favoritos.obtener(i).getNombre());
        }
    }
}