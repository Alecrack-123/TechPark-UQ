package com.techpark.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techpark.backend.model.Operador;
import com.techpark.backend.model.Zona;
import com.techpark.backend.service.ParqueData;

@RestController
@CrossOrigin(origins = "*")
public class GestionJerarquicaController {

    @GetMapping("/api/admin/jerarquia")
    public List<Map<String, Object>> obtenerJerarquia() {
        List<Map<String, Object>> jerarquia = new ArrayList<>();

        for (Zona zona : ParqueData.getZonas()) {
            for (int i = 0; i < zona.getCantidadOperadores(); i++) {
                Operador operador = zona.obtenerOperador(i);
                Map<String, Object> item = new HashMap<>();
                item.put("zona", zona.getNombre());
                item.put("zonaId", zona.getId());
                item.put("operador", operador.getNombre());
                item.put("operadorId", operador.getIdEmpleado());
                item.put("rol", "Operador");
                item.put("atraccionesAsignadas", zona.getCantidadAtracciones());
                jerarquia.add(item);
            }
        }

        return jerarquia;
    }
}
