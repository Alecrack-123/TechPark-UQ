package com.techpark.backend.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techpark.backend.service.ParqueData;

@RestController
@CrossOrigin(origins = "*")
public class VisitanteController {

    @GetMapping("/api/visitante/cargar")
    public Map<String, Object> cargarVisitante() {
        File archivo = new File("visitante.txt");

        if (!archivo.exists()) {
            return visitanteDemo();
        }

        Map<String, Object> visitante = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            visitante.put("nombre", br.readLine());
            visitante.put("documento", br.readLine());
            visitante.put("edad", Integer.parseInt(br.readLine()));
            visitante.put("estatura", Double.parseDouble(br.readLine()));
            visitante.put("saldo", Double.parseDouble(br.readLine()));
            visitante.put("ticket", br.readLine());
        } catch (Exception e) {
            visitante = visitanteDemo();
            visitante.put("advertencia", "No se pudo leer visitante.txt; se cargó un visitante de prueba");
        }

        return visitante;
    }

    @GetMapping("/api/datos/cargar-escenario")
    public Map<String, Object> cargarEscenario() {
        ParqueData.cargarEscenarioInicial();

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Escenario inicial cargado correctamente");
        response.put("atracciones", ParqueData.getArbolAtracciones().listarTodas().size());
        response.put("zonas", ParqueData.getZonas().size());
        response.put("empleados", ParqueData.getEmpleados().size());
        return response;
    }

    private Map<String, Object> visitanteDemo() {
        Map<String, Object> visitante = new HashMap<>();
        visitante.put("nombre", "Visitante Demo");
        visitante.put("documento", "1000000");
        visitante.put("edad", 24);
        visitante.put("estatura", 1.72);
        visitante.put("saldo", 80000);
        visitante.put("ticket", "SIN_TICKET");
        return visitante;
    }
}
