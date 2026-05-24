package com.techpark.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.techpark.backend.model.Atraccion;
import com.techpark.backend.model.EstadoAtraccion;
import com.techpark.backend.model.TipoAtraccion;
import com.techpark.backend.structures.ArbolAtracciones;

@RestController
@CrossOrigin(origins = "*")
public class AtraccionController {

    private static final ArbolAtracciones arbol = crearArbolInicial();

    private static ArbolAtracciones crearArbolInicial() {
        ArbolAtracciones a = new ArbolAtracciones();

        Atraccion a1 = new Atraccion("A1", "Montaña Rusa Tech", TipoAtraccion.MECANICA_ALTURA, 24, 1.50, 12, 0);
        a1.setTiempoEstimadoEspera(25);

        Atraccion a2 = new Atraccion("A2", "Río Virtual", TipoAtraccion.ACUATICA, 40, 1.00, 5, 0);
        a2.setTiempoEstimadoEspera(15);

        Atraccion a3 = new Atraccion("A3", "Torre Digital", TipoAtraccion.MECANICA_ALTURA, 16, 1.40, 12, 0);
        a3.setEstado(EstadoAtraccion.EN_MANTENIMIENTO);

        Atraccion a4 = new Atraccion("A4", "Carrusel Cyber", TipoAtraccion.INFANTIL, 32, 1.00, 5, 0);
        a4.setTiempoEstimadoEspera(5);

        Atraccion a5 = new Atraccion("A5", "Simulador 4D", TipoAtraccion.SIMULADOR, 20, 1.20, 10, 0);
        a5.setTiempoEstimadoEspera(35);

        Atraccion a6 = new Atraccion("A6", "Casa del Terror VR", TipoAtraccion.SIMULADOR, 12, 1.30, 12, 0);
        a6.setEstado(EstadoAtraccion.CERRADA);

        a.insertar(a1); a.insertar(a2); a.insertar(a3);
        a.insertar(a4); a.insertar(a5); a.insertar(a6);
        return a;
    }

    private Map<String, Object> toDTO(Atraccion a) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id",                  a.getId());
        dto.put("nombre",              a.getNombre());
        dto.put("tipo",                a.getTipo().toString());
        dto.put("capacidadMaxima",     a.getCapacidadPorCiclo());
        dto.put("tiempoEsperaMinutos", a.getTiempoEstimadoEspera());
        dto.put("estado",              a.getEstado().toString());
        dto.put("personasEnFila",      a.getCantidadEnFila());
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
}