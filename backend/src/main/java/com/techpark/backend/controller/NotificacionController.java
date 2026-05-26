package com.techpark.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techpark.backend.model.Notificacion;
import com.techpark.backend.service.ParqueData;

@RestController
@CrossOrigin(origins = "*")
public class NotificacionController {

    @GetMapping("/api/notificaciones")
    public List<Map<String, Object>> obtenerNotificaciones() {
        List<Map<String, Object>> response = new ArrayList<>();

        for (Notificacion notificacion : ParqueData.getNotificaciones()) {
            Map<String, Object> dto = new HashMap<>();
            dto.put("tipo", notificacion.getTipo());
            dto.put("mensaje", notificacion.getMensaje());
            dto.put("fecha", notificacion.getFecha());
            response.add(dto);
        }

        return response;
    }
}
