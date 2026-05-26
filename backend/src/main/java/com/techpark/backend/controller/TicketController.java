package com.techpark.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.techpark.backend.model.TipoTicket;
import com.techpark.backend.service.ParqueData;

@RestController
@CrossOrigin(origins = "*")
public class TicketController {

    @GetMapping("/api/aforo")
    public Map<String, Object> obtenerAforo() {
        return ParqueData.getAforo();
    }

    @GetMapping("/api/tickets/precios")
    public Map<String, Object> obtenerPrecios() {
        Map<String, Object> response = new HashMap<>();
        response.put("GENERAL", ParqueData.getPrecioTicket(TipoTicket.GENERAL));
        response.put("FAMILIAR", ParqueData.getPrecioTicket(TipoTicket.FAMILIAR));
        response.put("FAST_PASS", ParqueData.getPrecioTicket(TipoTicket.FAST_PASS));
        return response;
    }

    @PostMapping("/api/tickets/comprar")
    public Map<String, Object> comprarTicket(@RequestBody Map<String, Object> body) {
        String nombre = String.valueOf(body.getOrDefault("nombre", "Visitante"));
        String documento = String.valueOf(body.getOrDefault("documento", "V-" + System.currentTimeMillis()));
        int edad = Integer.parseInt(String.valueOf(body.getOrDefault("edad", 18)));
        double estatura = Double.parseDouble(String.valueOf(body.getOrDefault("estatura", 1.60)));
        double saldo = Double.parseDouble(String.valueOf(body.getOrDefault("saldo", 0)));
        String zonaIngresoId = String.valueOf(body.getOrDefault("zonaIngresoId", "Z1"));
        TipoTicket tipoTicket;

        try {
            tipoTicket = TipoTicket.valueOf(String.valueOf(body.getOrDefault("tipoTicket", "GENERAL")));
        } catch (IllegalArgumentException error) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Tipo de ticket no valido");
            return response;
        }

        return ParqueData.comprarTicket(nombre, documento, edad, estatura, saldo, tipoTicket, zonaIngresoId);
    }
}
