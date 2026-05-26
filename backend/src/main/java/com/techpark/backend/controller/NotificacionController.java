package com.techpark.backend.controller;

package com.techpark.backend.controller;

import com.techpark.backend.service.NotificacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    // Listar todas las notificaciones activas
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listar() {
        return ResponseEntity.ok(notificacionService.getTodas());
    }

    // Enviar notificación a todos los visitantes
    @PostMapping("/enviar")
    public ResponseEntity<Map<String, String>> enviar(@RequestBody Map<String, String> body) {
        String mensaje = body.get("mensaje");
        return ResponseEntity.ok(notificacionService.enviarATodos(mensaje));
    }

    // Enviar alerta de clima
    @PostMapping("/alerta-clima")
    public ResponseEntity<Map<String, String>> alertaClima(@RequestBody Map<String, String> body) {
        String clima = body.get("clima");
        return ResponseEntity.ok(notificacionService.enviarAlertaClima(clima));
    }
}
