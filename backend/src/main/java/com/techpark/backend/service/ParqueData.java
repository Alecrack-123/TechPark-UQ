package com.techpark.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.techpark.backend.model.Atraccion;
import com.techpark.backend.model.Empleado;
import com.techpark.backend.model.EstadoAtraccion;
import com.techpark.backend.model.Notificacion;
import com.techpark.backend.model.Operador;
import com.techpark.backend.model.TipoAtraccion;
import com.techpark.backend.model.TipoTicket;
import com.techpark.backend.model.Visitante;
import com.techpark.backend.model.Zona;
import com.techpark.backend.structures.ArbolAtracciones;
import com.techpark.backend.structures.ArbolEmpleados;
import com.techpark.backend.structures.GrafoParque;

public final class ParqueData {

    private static final ArbolAtracciones arbolAtracciones = new ArbolAtracciones();
    private static final ArbolEmpleados arbolEmpleados = new ArbolEmpleados();
    private static final List<Zona> zonas = new ArrayList<>();
    private static final List<Empleado> empleados = new ArrayList<>();
    private static final List<Notificacion> notificaciones = new ArrayList<>();
    private static final GrafoParque grafo = new GrafoParque();
    private static final Map<String, String> zonaPorAtraccion = new HashMap<>();
    private static final Map<String, Integer> ocupacionPorZona = new HashMap<>();
    private static final Map<String, Visitante> visitantes = new HashMap<>();
    private static final int capacidadParque = 300;
    private static int ingresosDiarios = 0;
    private static int incidentesOperativos = 0;
    private static int ocupacionParque = 0;
    private static int ticketsVendidos = 0;
    private static double ingresosTickets = 0;

    static {
        cargarEscenarioInicial();
    }

    private ParqueData() {
    }

    public static synchronized void cargarEscenarioInicial() {
        arbolAtracciones.limpiar();
        arbolEmpleados.limpiar();
        zonas.clear();
        empleados.clear();
        notificaciones.clear();
        zonaPorAtraccion.clear();
        ocupacionPorZona.clear();
        visitantes.clear();
        grafo.limpiar();
        ingresosDiarios = 0;
        incidentesOperativos = 0;
        ocupacionParque = 0;
        ticketsVendidos = 0;
        ingresosTickets = 0;

        Zona zona1 = new Zona("Z1", "Zona Aventura", 100);
        Zona zona2 = new Zona("Z2", "Zona Infantil", 80);
        Zona zona3 = new Zona("Z3", "Zona Acuatica", 120);
        Zona zona4 = new Zona("Z4", "Zona de Comidas", 60);

        zonas.add(zona1);
        zonas.add(zona2);
        zonas.add(zona3);
        zonas.add(zona4);

        inicializarAforoZona(zona1);
        inicializarAforoZona(zona2);
        inicializarAforoZona(zona3);
        inicializarAforoZona(zona4);

        Atraccion a1 = new Atraccion("A1", "Montaña Rusa Tech", TipoAtraccion.MECANICA_ALTURA, 24, 1.50, 12, 0);
        a1.setTiempoEstimadoEspera(25);

        Atraccion a2 = new Atraccion("A2", "Río Virtual", TipoAtraccion.ACUATICA, 40, 1.00, 5, 0);
        a2.setTiempoEstimadoEspera(15);

        Atraccion a3 = new Atraccion("A3", "Torre Digital", TipoAtraccion.MECANICA_ALTURA, 16, 1.40, 12, 0);
        a3.setEstado(EstadoAtraccion.EN_MANTENIMIENTO);
        a3.setMotivoCierre("Revisión técnica programada");

        Atraccion a4 = new Atraccion("A4", "Carrusel Cyber", TipoAtraccion.INFANTIL, 32, 1.00, 5, 0);
        a4.setTiempoEstimadoEspera(5);

        Atraccion a5 = new Atraccion("A5", "Simulador 4D", TipoAtraccion.SIMULADOR, 20, 1.20, 10, 0);
        a5.setTiempoEstimadoEspera(35);

        Atraccion a6 = new Atraccion("A6", "Casa del Terror VR", TipoAtraccion.SIMULADOR, 12, 1.30, 12, 0);
        a6.setEstado(EstadoAtraccion.CERRADA);
        a6.setMotivoCierre("Adecuación operativa");

        agregarAtraccionAZona(a1, zona1);
        agregarAtraccionAZona(a3, zona1);
        agregarAtraccionAZona(a4, zona2);
        agregarAtraccionAZona(a2, zona3);
        agregarAtraccionAZona(a5, zona4);
        agregarAtraccionAZona(a6, zona4);

        conectarGrafoInicial();
        cargarEmpleadosIniciales(zona1, zona2, zona3, zona4);
        agregarNotificacion("SISTEMA", "Escenario de prueba inicial cargado");
    }

    private static void agregarAtraccionAZona(Atraccion atraccion, Zona zona) {
        arbolAtracciones.insertar(atraccion);
        zona.agregarAtraccion(atraccion);
        zonaPorAtraccion.put(atraccion.getId(), zona.getId());
    }

    private static void inicializarAforoZona(Zona zona) {
        ocupacionPorZona.put(zona.getId(), 0);
    }

    private static void conectarGrafoInicial() {
        grafo.conectar("A1", "A2", 8);
        grafo.conectar("A2", "A4", 4);
        grafo.conectar("A4", "A5", 6);
        grafo.conectar("A1", "A3", 3);
        grafo.conectar("A3", "A5", 5);
        grafo.conectar("A5", "A6", 2);
    }

    private static void cargarEmpleadosIniciales(Zona zona1, Zona zona2, Zona zona3, Zona zona4) {
        Operador operador1 = new Operador("Carlos", "1001", "E1");
        Operador operador2 = new Operador("Ana", "1002", "E2");
        Operador operador3 = new Operador("Luis", "1003", "E3");
        Operador operador4 = new Operador("Marta", "1004", "E4");

        zona1.asignarOperador(operador1);
        zona2.asignarOperador(operador2);
        zona3.asignarOperador(operador3);
        zona4.asignarOperador(operador4);

        registrarEmpleado(operador1);
        registrarEmpleado(operador2);
        registrarEmpleado(operador3);
        registrarEmpleado(operador4);
    }

    public static ArbolAtracciones getArbolAtracciones() {
        return arbolAtracciones;
    }

    public static ArbolEmpleados getArbolEmpleados() {
        return arbolEmpleados;
    }

    public static List<Zona> getZonas() {
        return zonas;
    }

    public static List<Empleado> getEmpleados() {
        return empleados;
    }

    public static List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public static GrafoParque getGrafo() {
        return grafo;
    }

    public static String getZonaIdPorAtraccion(String atraccionId) {
        return zonaPorAtraccion.get(atraccionId);
    }

    public static Zona buscarZona(String id) {
        for (Zona zona : zonas) {
            if (zona.getId().equals(id)) {
                return zona;
            }
        }

        return null;
    }

    public static void asociarAtraccionAZona(Atraccion atraccion, String zonaId) {
        Zona zona = buscarZona(zonaId);

        if (zona == null) {
            zona = zonas.isEmpty() ? null : zonas.get(0);
        }

        arbolAtracciones.insertar(atraccion);

        if (zona != null) {
            zona.agregarAtraccion(atraccion);
            zonaPorAtraccion.put(atraccion.getId(), zona.getId());
        }
    }

    public static boolean eliminarAtraccion(String id) {
        Atraccion atraccion = arbolAtracciones.buscar(id);

        if (atraccion == null) {
            return false;
        }

        String zonaId = zonaPorAtraccion.remove(id);
        Zona zona = buscarZona(zonaId);

        if (zona != null) {
            zona.getAtracciones().eliminar(atraccion);
        }

        return arbolAtracciones.eliminar(id);
    }

    public static void registrarEmpleado(Empleado empleado) {
        empleados.add(empleado);
        arbolEmpleados.insertar(empleado);
    }

    public static void registrarZona(Zona zona) {
        zonas.add(zona);
        inicializarAforoZona(zona);
    }

    public static void agregarNotificacion(String tipo, String mensaje) {
        notificaciones.add(new Notificacion(tipo, mensaje));
    }

    public static void registrarIngreso() {
        ingresosDiarios++;
    }

    public static int getIngresosDiarios() {
        return ingresosDiarios;
    }

    public static void registrarIncidenteOperativo() {
        incidentesOperativos++;
    }

    public static int getIncidentesOperativos() {
        return incidentesOperativos;
    }

    public static synchronized Map<String, Object> comprarTicket(
            String nombre,
            String documento,
            int edad,
            double estatura,
            double saldo,
            TipoTicket tipoTicket,
            String zonaIngresoId
    ) {
        Map<String, Object> response = new HashMap<>();
        Zona zona = buscarZona(zonaIngresoId);

        if (zona == null) {
            response.put("error", "Zona de ingreso no encontrada");
            return response;
        }

        if (ocupacionParque >= capacidadParque) {
            response.put("error", "Aforo maximo del parque alcanzado");
            return response;
        }

        if (getOcupacionZona(zona.getId()) >= zona.getCapacidadMaxima()) {
            response.put("error", "Aforo maximo alcanzado en " + zona.getNombre());
            return response;
        }

        if (visitantes.containsKey(documento)) {
            response.put("error", "El visitante ya tiene ingreso registrado para hoy");
            return response;
        }

        double precio = getPrecioTicket(tipoTicket);
        Visitante visitante = new Visitante(nombre, documento, edad, estatura, saldo);

        try {
            visitante.comprarTicket(tipoTicket, precio);
        } catch (IllegalStateException error) {
            response.put("error", error.getMessage());
            return response;
        }

        visitantes.put(documento, visitante);
        ocupacionParque++;
        ocupacionPorZona.put(zona.getId(), getOcupacionZona(zona.getId()) + 1);
        ticketsVendidos++;
        ingresosTickets += precio;
        agregarNotificacion("TICKET", "Ticket " + tipoTicket + " vendido a " + nombre + " para " + zona.getNombre());

        response.put("mensaje", "Ticket comprado correctamente");
        response.put("precio", precio);
        response.put("zonaIngreso", zona.getNombre());
        response.put("visitante", visitanteToDTO(visitante));
        response.put("aforo", getAforo());
        return response;
    }

    public static double getPrecioTicket(TipoTicket tipoTicket) {
        if (tipoTicket == TipoTicket.FAST_PASS) {
            return 80000;
        }

        if (tipoTicket == TipoTicket.FAMILIAR) {
            return 40000;
        }

        return 50000;
    }

    public static synchronized Map<String, Object> getAforo() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> zonasAforo = new ArrayList<>();

        for (Zona zona : zonas) {
            Map<String, Object> zonaDTO = new HashMap<>();
            int ocupacion = getOcupacionZona(zona.getId());
            zonaDTO.put("id", zona.getId());
            zonaDTO.put("nombre", zona.getNombre());
            zonaDTO.put("capacidad", zona.getCapacidadMaxima());
            zonaDTO.put("ocupacion", ocupacion);
            zonaDTO.put("cupos", Math.max(zona.getCapacidadMaxima() - ocupacion, 0));
            zonasAforo.add(zonaDTO);
        }

        response.put("capacidadParque", capacidadParque);
        response.put("ocupacionParque", ocupacionParque);
        response.put("cuposParque", Math.max(capacidadParque - ocupacionParque, 0));
        response.put("ticketsVendidos", ticketsVendidos);
        response.put("ingresosTickets", ingresosTickets);
        response.put("zonas", zonasAforo);
        return response;
    }

    public static int getOcupacionZona(String zonaId) {
        return ocupacionPorZona.getOrDefault(zonaId, 0);
    }

    public static int getCapacidadParque() {
        return capacidadParque;
    }

    public static int getOcupacionParque() {
        return ocupacionParque;
    }

    public static int getTicketsVendidos() {
        return ticketsVendidos;
    }

    public static double getIngresosTickets() {
        return ingresosTickets;
    }

    private static Map<String, Object> visitanteToDTO(Visitante visitante) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("nombre", visitante.getNombre());
        dto.put("documento", visitante.getDocumento());
        dto.put("edad", visitante.getEdad());
        dto.put("estatura", visitante.getEstatura());
        dto.put("saldo", visitante.getSaldoVirtual());
        dto.put("ticket", visitante.getTicket() == null ? "SIN_TICKET" : visitante.getTicket().getTipo().name());
        return dto;
    }
}
