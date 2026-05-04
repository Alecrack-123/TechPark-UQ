package com.techpark.backend.model;

// import com.techpark.backend.structures.ColaPrioridad;

public class Operador extends Empleado {
    private Zona zonaAsignada;

    public Operador(String nombre, String documento, String idEmpleado) {
        super(nombre, documento, idEmpleado);
    }

    // --- MÉTODOS DE GESTIÓN DE ATRACCIÓN ---

    public void cambiarEstadoAtraccion(Atraccion atr, EstadoAtraccion nuevoEstado, String motivo) {
        atr.setEstado(nuevoEstado);
        atr.setMotivoCierre(motivo);
        atr.notificarCambioEstado();
        System.out.println("Operador " + this.nombre + " cambió el estado de " + atr.getNombre() + " a " + nuevoEstado);
    }

    public void registrarRevisionTecnica(Atraccion atr) {
        System.out.println("Operador " + this.nombre + " iniciando revisión técnica en " + atr.getNombre() + "...");
        atr.verificarMantenimiento(); // Reinicia el contador de 500 y la pone ACTIVA
        System.out.println("Revisión finalizada. Atracción operativa.");
    }

    // --- LÓGICA PRINCIPAL: PROCESAR FILA ---
    
    public void procesarFila(Atraccion atr) {
        System.out.println("Operador " + this.nombre + " procesando fila para " + atr.getNombre());
        
        // 1. Verificamos que la atracción no esté en mantenimiento ni cerrada
        /* ¡Descomentar cuando implementes la Cola de Prioridad y agregues los getters en Atraccion!
        if (atr.getEstado() != EstadoAtraccion.ACTIVA) {
            System.out.println("No se puede procesar la fila. La atracción no está ACTIVA.");
            return;
        }

        int capacidad = atr.getCapacidadPorCiclo();
        int ingresados = 0;
        ColaPrioridad<Visitante> fila = atr.getFilaVirtual(); 

        // 2. Dejamos entrar visitantes hasta llenar la atracción o vaciar la fila
        while (!fila.estaVacia() && ingresados < capacidad) {
            // ¡Aquí tu estructura propia brilla! Sacará primero a los de prioridad 1 (Fast-Pass)
            Visitante v = fila.desencolar(); 

            // 3. Validar seguridad (estatura, edad, saldo)
            if (atr.validarAcceso(v)) {
                
                // Cobrar si es entrada general y la atracción tiene costo adicional
                if (v.getTicket() != null && v.getTicket().getTipo() == TipoTicket.GENERAL && atr.getCostoAdicional() > 0) {
                    v.pagar(atr.getCostoAdicional());
                }
                
                // 4. Registrar ingreso oficial
                atr.registrarIngreso(v);
                ingresados++;
                
                // Opcional: Agregar al historial del visitante
                // v.getHistorialVisitas().agregarAlFinal(atr);
                
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