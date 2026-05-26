package com.techpark.backend.controller;
package com.techpark.backend.controller;

import com.techpark.backend.dto.ComprarTicketRequest;
import com.techpark.backend.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Consultar tipos y precios disponibles
    @GetMapping("/tipos")
    public ResponseEntity<List<Map<String, Object>>> getTipos() {
        return ResponseEntity.ok(ticketService.getTiposDisponibles());
    }

    // Comprar ticket para un visitante
    @PostMapping("/comprar")
    public ResponseEntity<Map<String, Object>> comprar(@RequestBody ComprarTicketRequest req) {
        return ResponseEntity.ok(ticketService.comprarTicket(req));
    }

    // Consultar ticket de un visitante
    @GetMapping("/visitante/{documento}")
    public ResponseEntity<Map<String, Object>> getTicketVisitante(@PathVariable String documento) {
        return ResponseEntity.ok(ticketService.getTicketPorDocumento(documento));
    }
}