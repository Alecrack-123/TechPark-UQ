package com.techpark.backend.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class VisitanteController {

    @GetMapping("/api/visitante/cargar")
    public Map<String, Object> cargarVisitante() {

        Map<String, Object> visitante = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader("visitante.txt"));

            String nombre = br.readLine();
            String documento = br.readLine();
            int edad = Integer.parseInt(br.readLine());
            double estatura = Double.parseDouble(br.readLine());
            double saldo = Double.parseDouble(br.readLine());
            String ticket = br.readLine();

            br.close();

            visitante.put("nombre", nombre);
            visitante.put("documento", documento);
            visitante.put("edad", edad);
            visitante.put("estatura", estatura);
            visitante.put("saldo", saldo);
            visitante.put("ticket", ticket);

        } catch (Exception e) {
            visitante.put("error", "No se pudo cargar el archivo visitante.txt");
        }

        return visitante;
    }
}