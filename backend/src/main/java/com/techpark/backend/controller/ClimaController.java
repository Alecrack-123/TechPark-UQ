package com.techpark.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class ClimaController {

    @GetMapping("/api/clima/soleado")
    public String climaSoleado() {

        return "{"
                + "\"clima\":\"SOLEADO\","
                + "\"mensaje\":\"Todas las atracciones funcionan normalmente\""
                + "}";
    }

    @GetMapping("/api/clima/lluvia")
    public String climaLluvia() {

        return "{"
                + "\"clima\":\"LLUVIA\","
                + "\"mensaje\":\"Las atracciones extremas fueron cerradas por seguridad\""
                + "}";
    }

    @GetMapping("/api/clima/tormenta")
    public String climaTormenta() {

        return "{"
                + "\"clima\":\"TORMENTA\","
                + "\"mensaje\":\"El parque entra en protocolo de emergencia\""
                + "}";
    }
}