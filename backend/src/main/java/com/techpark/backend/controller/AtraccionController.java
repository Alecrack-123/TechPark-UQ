package com.techpark.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.techpark.backend.model.Atraccion;
import com.techpark.backend.model.EstadoAtraccion;
import com.techpark.backend.model.Ticket;
import com.techpark.backend.model.TipoAtraccion;
import com.techpark.backend.model.TipoTicket;
import com.techpark.backend.model.Visitante;
import com.techpark.backend.service.ParqueData;
import com.techpark.backend.structures.ArbolAtracciones;

@RestController
@CrossOrigin(origins = "*")
public class AtraccionController {

    static final ArbolAtracciones arbol = ParqueData.getArbolAtracciones();

    private Map<String, Object> toDTO(Atraccion a) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", a.getId());
        dto.put("nombre", a.getNombre());
        dto.put("tipo", a.getTipo().toString());
        dto.put("capacidadMaxima", a.getCapacidadPorCiclo());
        dto.put("tiempoEsperaMinutos", a.getTiempoEstimadoEspera());
        dto.put("estado", a.getEstado().toString());
        dto.put("personasEnFila", a.getCantidadEnFila());
        dto.put("contadorVisitantes", a.getContadorVisitantes());
        dto.put("motivoCierre", a.getMotivoCierre());
        dto.put("alturaMinima", a.getAlturaMinima());
        dto.put("edadMinima", a.getEdadMinima());
        dto.put("costoAdicional", a.getCostoAdicional());
        dto.put("zonaId", ParqueData.getZonaIdPorAtraccion(a.getId()));
        dto.put("filaPausada", a.isFilaPausada());
        return dto;
    }

    @GetMapping("/api/parque/atracciones")
    public List<Map<String, Object>> obtenerAtracciones() {
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Atraccion a : arbol.listarTodas()) {
            lista.add(toDTO(a));
        }

        return lista;
    }

    @GetMapping("/api/parque/atracciones/buscar/{id}")
    public Map<String, Object> buscarAtraccion(@PathVariable String id) {
        Atraccion encontrada = arbol.buscar(id);

        if (encontrada == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("mensaje", "Atracción no encontrada");
            return error;
        }

        return toDTO(encontrada);
    }

    @PostMapping("/api/parque/atracciones")
    public Map<String, Object> crearAtraccion(@RequestBody Map<String, Object> body) {
        String id = String.valueOf(body.getOrDefault("id", "A" + System.currentTimeMillis()));

        if (arbol.buscar(id) != null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Ya existe una atracción con ese ID");
            return error;
        }

        String nombre = String.valueOf(body.getOrDefault("nombre", "Nueva atracción"));
        TipoAtraccion tipo = TipoAtraccion.valueOf(String.valueOf(body.getOrDefault("tipo", "SIMULADOR")));
        int capacidad = Integer.parseInt(String.valueOf(body.getOrDefault("capacidadMaxima", 20)));
        double altura = Double.parseDouble(String.valueOf(body.getOrDefault("alturaMinima", 1.0)));
        int edad = Integer.parseInt(String.valueOf(body.getOrDefault("edadMinima", 5)));
        double costo = Double.parseDouble(String.valueOf(body.getOrDefault("costoAdicional", 0)));
        int espera = Integer.parseInt(String.valueOf(body.getOrDefault("tiempoEsperaMinutos", 0)));
        String zonaId = String.valueOf(body.getOrDefault("zonaId", "Z1"));

        Atraccion atraccion = new Atraccion(id, nombre, tipo, capacidad, altura, edad, costo);
        atraccion.setTiempoEstimadoEspera(espera);
        ParqueData.asociarAtraccionAZona(atraccion, zonaId);
        ParqueData.agregarNotificacion("ADMIN", "Atracción creada: " + nombre);

        return toDTO(atraccion);
    }

    @PutMapping("/api/parque/atracciones/{id}/estado")
    public Map<String, Object> actualizarEstado(@PathVariable String id, @RequestBody Map<String, String> body) {
        Atraccion encontrada = arbol.buscar(id);

        if (encontrada == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Atracción no encontrada");
            return error;
        }

        EstadoAtraccion estado = EstadoAtraccion.valueOf(body.getOrDefault("estado", "ACTIVA"));
        String motivo = body.getOrDefault("motivo", estado == EstadoAtraccion.ACTIVA ? "" : "Cambio manual de estado");
        encontrada.cambiarEstado(estado, motivo);
        ParqueData.agregarNotificacion("OPERADOR", encontrada.getNombre() + " cambió a " + estado);

        return toDTO(encontrada);
    }

    @DeleteMapping("/api/parque/atracciones/{id}")
    public Map<String, Object> eliminarAtraccion(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        boolean eliminada = ParqueData.eliminarAtraccion(id);

        response.put("eliminada", eliminada);
        response.put("mensaje", eliminada ? "Atracción eliminada" : "Atracción no encontrada");
        return response;
    }

    @PostMapping("/api/parque/atracciones/{id}/revision")
    public Map<String, Object> registrarRevision(@PathVariable String id) {
        Atraccion encontrada = arbol.buscar(id);

        if (encontrada == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Atracción no encontrada");
            return error;
        }

        encontrada.registrarRevisionTecnica();
        ParqueData.agregarNotificacion("MANTENIMIENTO", "Revisión técnica registrada para " + encontrada.getNombre());
        return toDTO(encontrada);
    }

    @PostMapping("/api/parque/atracciones/{id}/procesar-fila")
    public Map<String, Object> procesarFila(@PathVariable String id) {
        Atraccion encontrada = arbol.buscar(id);
        Map<String, Object> response = new HashMap<>();

        if (encontrada == null) {
            response.put("error", "Atracción no encontrada");
            return response;
        }

        if (encontrada.getEstado() != EstadoAtraccion.ACTIVA) {
            response.put("error", "La atracción no está activa");
            ParqueData.registrarIncidenteOperativo();
            return response;
        }

        if (encontrada.isFilaPausada()) {
            response.put("error", "La fila esta pausada por el operador");
            return response;
        }

        int procesados = 0;
        int denegados = 0;

        while (!encontrada.getFilaVirtual().estaVacia() && procesados < encontrada.getCapacidadPorCiclo()) {
            Visitante visitante = encontrada.sacarVisitanteDeFila();

            if (encontrada.validarAcceso(visitante)) {
                encontrada.registrarIngreso(visitante);
                visitante.agregarAHistorial(encontrada);
                ParqueData.registrarIngreso();
                procesados++;
            } else {
                denegados++;
                ParqueData.registrarIncidenteOperativo();
            }
        }

        response.put("mensaje", "Fila procesada");
        response.put("procesados", procesados);
        response.put("denegados", denegados);
        response.put("atraccion", toDTO(encontrada));
        return response;
    }

    @PostMapping("/api/parque/atracciones/{id}/fila/demo")
    public Map<String, Object> agregarVisitantesDemo(@PathVariable String id, @RequestBody Map<String, String> body) {
        Atraccion encontrada = arbol.buscar(id);
        Map<String, Object> response = new HashMap<>();

        if (encontrada == null) {
            response.put("error", "Atraccion no encontrada");
            return response;
        }

        if (encontrada.getEstado() != EstadoAtraccion.ACTIVA) {
            response.put("error", "La atraccion no esta activa");
            return response;
        }

        int cantidad = Integer.parseInt(body.getOrDefault("cantidad", "10"));
        int agregados = 0;

        for (int i = 1; i <= cantidad; i++) {
            TipoTicket tipoTicket = i % 3 == 0 ? TipoTicket.FAST_PASS : i % 5 == 0 ? TipoTicket.FAMILIAR : TipoTicket.GENERAL;
            Visitante visitante = new Visitante(
                "Visitante Demo " + (encontrada.getCantidadEnFila() + i),
                "DEMO-" + id + "-" + System.currentTimeMillis() + "-" + i,
                Math.max(encontrada.getEdadMinima(), 18),
                encontrada.getAlturaMinima() + 0.10,
                100000
            );
            visitante.setTicket(new Ticket(tipoTicket, 0.0));
            encontrada.agregarVisitanteAFila(visitante);
            agregados++;
        }

        ParqueData.agregarNotificacion("FILA", "Se agregaron " + agregados + " visitantes demo a " + encontrada.getNombre());
        response.put("mensaje", "Visitantes demo agregados a la fila");
        response.put("agregados", agregados);
        response.put("atraccion", toDTO(encontrada));
        return response;
    }

    @PutMapping("/api/parque/atracciones/{id}/fila/pausa")
    public Map<String, Object> actualizarPausaFila(@PathVariable String id, @RequestBody Map<String, String> body) {
        Atraccion encontrada = arbol.buscar(id);
        Map<String, Object> response = new HashMap<>();

        if (encontrada == null) {
            response.put("error", "Atraccion no encontrada");
            return response;
        }

        boolean pausada = Boolean.parseBoolean(body.getOrDefault("pausada", "true"));

        if (pausada) {
            encontrada.pausarFila();
            ParqueData.agregarNotificacion("OPERADOR", "Fila pausada en " + encontrada.getNombre());
            response.put("mensaje", "Fila pausada");
        } else {
            encontrada.reanudarFila();
            ParqueData.agregarNotificacion("OPERADOR", "Fila reanudada en " + encontrada.getNombre());
            response.put("mensaje", "Fila reanudada");
        }

        response.put("atraccion", toDTO(encontrada));
        return response;
    }

    @PostMapping("/api/parque/atracciones/{id}/fila")
    public Map<String, Object> unirseAFila(@PathVariable String id, @RequestBody Map<String, String> body) {
        Atraccion encontrada = arbol.buscar(id);
        Map<String, Object> response = new HashMap<>();

        if (encontrada == null) {
            response.put("error", "Atracción no encontrada");
            return response;
        }

        if (encontrada.getEstado() != EstadoAtraccion.ACTIVA) {
            response.put("error", "La atracción no está activa");
            return response;
        }

        if (encontrada.isFilaPausada()) {
            response.put("error", "La fila esta pausada por el operador");
            return response;
        }

        String nombreVisitante = body.getOrDefault("nombre", "Visitante");
        String tipoTicket = body.getOrDefault("tipoTicket", "GENERAL");
        int edad = Integer.parseInt(body.getOrDefault("edad", "25"));
        double estatura = Double.parseDouble(body.getOrDefault("estatura", "1.70"));
        double saldo = Double.parseDouble(body.getOrDefault("saldo", "50000"));

        Visitante visitante = new Visitante(
            nombreVisitante,
            "V" + System.currentTimeMillis(),
            edad,
            estatura,
            saldo
        );

        Ticket ticket = new Ticket(
            tipoTicket.equals("FAST_PASS") ? TipoTicket.FAST_PASS :
                tipoTicket.equals("FAMILIAR") ? TipoTicket.FAMILIAR : TipoTicket.GENERAL,
            0.0
        );
        visitante.setTicket(ticket);

        encontrada.agregarVisitanteAFila(visitante);

        if (encontrada.getCantidadEnFila() >= 500) {
            encontrada.setEstado(EstadoAtraccion.EN_MANTENIMIENTO);
            encontrada.setMotivoCierre("Límite de 500 visitantes alcanzado");
            ParqueData.agregarNotificacion("MANTENIMIENTO", encontrada.getNombre() + " entró en mantenimiento preventivo");
        }

        int posicion = encontrada.getCantidadEnFila();
        int tiempoEstimado = posicion * Math.max(encontrada.getTiempoEstimadoEspera() / Math.max(encontrada.getCapacidadPorCiclo(), 1), 1);

        response.put("mensaje", "Te uniste a la fila de " + encontrada.getNombre());
        response.put("posicion", posicion);
        response.put("tiempoEstimado", tiempoEstimado);
        response.put("tipoTicket", tipoTicket);
        return response;
    }
}
