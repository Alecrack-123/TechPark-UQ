# Diagrama de estructuras propias - TechPark UQ

```mermaid
flowchart LR
  subgraph "Mapa del parque"
    GP["GrafoParque"]
    N1["Nodo: Atraccion A1"]
    N2["Nodo: Atraccion A2"]
    N3["Nodo: Atraccion A3"]
    E1["Arista con peso"]
    GP --> N1
    GP --> N2
    GP --> N3
    N1 --- E1 --- N2
  end

  subgraph "Busqueda eficiente"
    AA["ArbolAtracciones (ABB por ID)"]
    A1["A1"]
    A2["A2"]
    A3["A3"]
    AA --> A2
    A2 --> A1
    A2 --> A3
  end

  subgraph "Filas virtuales"
    CP["ColaPrioridad"]
    FP["ListaEnlazada Fast-Pass"]
    GN["ListaEnlazada General"]
    CP --> FP
    CP --> GN
    FP -->|"sale primero"| ING["Ingreso a atraccion"]
    GN --> ING
  end

  subgraph "Visitante"
    SF["SetFavoritos"]
    HF["ListaEnlazada Historial"]
    V["Visitante"]
    V --> SF
    V --> HF
  end

  subgraph "Administracion"
    AE["ArbolEmpleados (ABB por ID)"]
    ZO["Zona"]
    LO["ListaEnlazada Operadores"]
    AE --> OP["Operador"]
    ZO --> LO
  end
```

## Uso en el sistema

- `GrafoParque`: representa atracciones como nodos y senderos como aristas ponderadas. Calcula ruta con Dijkstra.
- `ColaPrioridad`: separa visitantes `FAST_PASS` y `GENERAL`; procesa primero los Fast-Pass.
- `ListaEnlazada`: se usa para operadores por zona, historial de visitas y soporte interno de colas.
- `ArbolAtracciones`: permite buscar atracciones por ID de forma eficiente.
- `ArbolEmpleados`: organiza empleados para gestion jerarquica.
- `SetFavoritos`: evita favoritos repetidos para cada visitante.
