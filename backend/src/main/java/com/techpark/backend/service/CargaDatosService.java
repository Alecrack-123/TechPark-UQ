package com.techpark.backend.service;

import com.techpark.backend.model.*;
import org.springframework.stereotype.Service;

@Service
public class CargaDatosService {

    private final ParqueService parqueService;

    public CargaDatosService(ParqueService parqueService) { this.parqueService = parqueService; }

    public void cargarEscenarioPrueba() {
        parqueService.crearZona("Z1", "Zona Aventura", 200);
        parqueService.crearZona("Z2", "Zona Acuática", 150);
        parqueService.crearZona("Z3", "Zona Infantil",  100);

        parqueService.crearAtraccion("Z1","A1","Montaña Rusa Extrema", TipoAtraccion.MECANICA_ALTURA,24,1.40,12,5000);
        parqueService.crearAtraccion("Z1","A2","Torre del Terror",      TipoAtraccion.MECANICA_ALTURA,16,1.35,14,3000);
        parqueService.crearAtraccion("Z2","A3","Río Salvaje",           TipoAtraccion.ACUATICA,        8,1.20,10,2000);
        parqueService.crearAtraccion("Z2","A4","Piscina de Olas",       TipoAtraccion.ACUATICA,       50,0.0,  0,   0);
        parqueService.crearAtraccion("Z3","A5","Carrusel Mágico",       TipoAtraccion.INFANTIL,       12,0.0,  0,1000);
        parqueService.crearAtraccion("Z3","A6","Simulador Espacial",    TipoAtraccion.SIMULADOR,       6,1.10, 8,4000);

        parqueService.conectar("A1","A2", 80);
        parqueService.conectar("A2","A3",150);
        parqueService.conectar("A3","A4", 60);
        parqueService.conectar("A4","A5",200);
        parqueService.conectar("A5","A6", 50);
        parqueService.conectar("A1","A5",300);
        parqueService.conectar("A2","A4",180);

        Visitante v1 = new Visitante("Carlos Pérez", "123456789", 25, 1.75, 100000);
        Visitante v2 = new Visitante("Ana Gómez",    "987654321", 16, 1.55,  50000);
        Visitante v3 = new Visitante("Luis Niño",    "111111111",  8, 1.20,  30000);
        Visitante v4 = new Visitante("María Torres", "222222222", 30, 1.65, 200000);

        v1.comprarTicket(TipoTicket.FAST_PASS, 80000);
        v2.comprarTicket(TipoTicket.GENERAL,   40000);
        v3.comprarTicket(TipoTicket.FAMILIAR,  20000);
        v4.comprarTicket(TipoTicket.GENERAL,   40000);

        parqueService.registrarVisitante(v1);
        parqueService.registrarVisitante(v2);
        parqueService.registrarVisitante(v3);
        parqueService.registrarVisitante(v4);

        parqueService.getParque().registrarEmpleado(new Operador("Juan Rodríguez","OP001","OP001"));
        parqueService.getParque().registrarEmpleado(new Administrador("Sofía Martínez","ADM001","ADM001"));

        System.out.println("Escenario de prueba cargado.");
    }
}