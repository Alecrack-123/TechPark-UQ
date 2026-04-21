package com.techpark.backend.model;

public class Operador {
    private String idEmpleado;
    private String nombre;
    // private Zona zonaAsignada; // Se conectará después

    public Operador(String idEmpleado, String nombre) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
    }

    /**
     * Lógica principal de Validación de Acceso y Gestión de Fila
     */
    public void procesarFilaDeAtraccion(Atraccion atraccion) {
        // Obtenemos la estructura de la fila (que implementarás en el paquete structures)
        // ColaPrioridadPropia fila = atraccion.getFilaVirtual();

        int capacidadRestante = atraccion.getCapacidadPorCiclo();
        int visitantesIngresados = 0;

        // Simulamos el ciclo: Mientras haya gente en la fila y haya cupos en el carrito
        /* while (!fila.estaVacia() && capacidadRestante > 0) {
            // El desencolar() ya te debe entregar primero a los Fast-Pass
            Visitante proximoVisitante = fila.desencolar();

            // 1. VALIDACIÓN DE SEGURIDAD (Se llama al método del visitante)
            if (proximoVisitante.puedeIngresar(atraccion)) {

                // 2. VALIDACIÓN DE COBRO (Si es General y hay costo extra)
                if (proximoVisitante.getTipoTicket() == TipoTicket.GENERAL && atraccion.getCostoAdicional() > 0) {
                    proximoVisitante.pagarEntrada(atraccion.getCostoAdicional());
                }

                // 3. REGISTRO EXITOSO
                visitantesIngresados++;
                capacidadRestante--;

                // Aquí podrías agregar el visitante al Set de historial
                // proximoVisitante.agregarAHistorial(atraccion.getNombre());

            } else {
                // El visitante es rechazado y pierde su turno en la fila
                System.out.println("Acceso denegado para el visitante por no cumplir restricciones físicas o de saldo.");
            }
        }
        */

        // 4. ACTUALIZACIÓN DE LA ATRACCIÓN
       // if (visitantesIngresados > 0) {
            // Esto dispara automáticamente el mantenimiento si llega a 500
          //  atraccion.registrarVisitantes(visitantesIngresados);
      //  }
    }

    // Método para mantenimiento preventivo
    public void registrarRevisionTecnica(Atraccion atraccion) {
        atraccion.registrarRevisionTecnica();
        System.out.println("Revisión completada por el operador " + this.nombre + ". Atracción activa.");
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
