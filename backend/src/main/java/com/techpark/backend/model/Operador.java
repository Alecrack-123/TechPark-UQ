package com.techpark.backend.model;

// import com.techpark.backend.structures.ColaPrioridad;

public class Operador extends Empleado {

    private Zona zonaAsignada;

    public Operador(String nombre, String documento, String idEmpleado) {
        super(nombre, documento, idEmpleado);
    }

    // --- MÉTODOS DE GESTIÓN DE ATRACCIÓN ---

    public void cambiarEstadoAtraccion(Atraccion atr, EstadoAtraccion nuevoEstado, String motivo) {

        if (zonaAsignada == null) {
            System.out.println("El operador no tiene una zona asignada.");
            return;
        }

        if (!zonaAsignada.contieneAtraccion(atr)) {
            System.out.println("El operador no puede gestionar una atracción fuera de su zona.");
            return;
        }

        atr.setEstado(nuevoEstado);
        atr.setMotivoCierre(motivo);
        atr.notificarCambioEstado();

        System.out.println("Operador " + this.nombre + " cambió el estado de " + atr.getNombre() + " a " + nuevoEstado);
    }

    public void registrarRevisionTecnica(Atraccion atr) {

        if (zonaAsignada == null) {
            System.out.println("El operador no tiene una zona asignada.");
            return;
        }

        if (!zonaAsignada.contieneAtraccion(atr)) {
            System.out.println("El operador no puede revisar una atracción fuera de su zona.");
            return;
        }

        System.out.println("Operador " + this.nombre + " iniciando revisión técnica en " + atr.getNombre() + "...");
        atr.verificarMantenimiento();
        System.out.println("Revisión finalizada. Atracción operativa.");
    }

    // --- LÓGICA PRINCIPAL: PROCESAR FILA ---

    public void procesarFila(Atraccion atr) {
        System.out.println("Operador " + this.nombre + " procesando fila para " + atr.getNombre());

        /*
        if (atr.getEstado() != EstadoAtraccion.ACTIVA) {
            System.out.println("No se puede procesar la fila. La atracción no está ACTIVA.");
            return;
        }

        int capacidad = atr.getCapacidadPorCiclo();
        int ingresados = 0;
        ColaPrioridad<Visitante> fila = atr.getFilaVirtual();

        while (!fila.estaVacia() && ingresados < capacidad) {
            Visitante v = fila.desencolar();

            if (atr.validarAcceso(v)) {

                if (v.getTicket() != null && v.getTicket().getTipo() == TipoTicket.GENERAL && atr.getCostoAdicional() > 0) {
                    v.pagar(atr.getCostoAdicional());
                }

                atr.registrarIngreso(v);
                ingresados++;

                System.out.println("Ingreso aprobado: " + v.getNombre());
            } else {
                System.out.println("Ingreso denegado: " + v.getNombre() + " (No cumple requisitos o saldo insuficiente).");
            }
        }

        System.out.println("Ciclo iniciado con " + ingresados + " visitantes.");
        atr.iniciarCiclo();
        */
    }

    // --- GETTERS Y SETTERS ---

    public Zona getZonaAsignada() {
        return zonaAsignada;
    }

    public void setZonaAsignada(Zona zonaAsignada) {
        this.zonaAsignada = zonaAsignada;
    }
}