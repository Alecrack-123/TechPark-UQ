package com.techpark.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class NotificacionController {

    @GetMapping("/api/notificaciones")
    public String obtenerNotificaciones() {

        return "["
                + "{\"tipo\":\"FILA\",\"mensaje\":\"Tu turno en Montaña Rusa se acerca\"},"
                + "{\"tipo\":\"CLIMA\",\"mensaje\":\"Alerta de lluvia en el parque\"},"
                + "{\"tipo\":\"MANTENIMIENTO\",\"mensaje\":\"Torre Digital en mantenimiento\"}"
                + "]";
    }
}
