"use client"

import { useState, useEffect, useMemo } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import {
  Map, User, Settings, BarChart3, Clock, Users, AlertTriangle,
  CheckCircle2, XCircle, Zap, Timer, TrendingUp, Ticket,
  Navigation, CloudSun, Menu, X, CloudRain, CloudLightning, Sun,
  MapPin, Layers, LocateFixed,
} from "lucide-react"
import { cn } from "@/lib/utils"
import {
  getAtracciones, getZonas, getEstadisticas, activarClima, buscarAtraccionPorId, calcularRuta, unirseAFila, cargarVisitante, getConexionesGrafo,
  getAforo,
  comprarTicket,
  getReporteTotalVisitantes,
  getReporteAtraccionMasVisitada,
  getReporteFilaMasLarga,
  getReporteCierresClima,
  getReporteIngresosDiarios,
  getReporteAlertasMantenimiento,
  getReporteIncidentesOperativos,
  getAnalisisGrafo,
  getJerarquiaAdmin,
  actualizarEstadoAtraccion,
  registrarRevisionAtraccion,
  procesarFilaAtraccion,
  generarVisitantesDemo,
  actualizarPausaFila,
  cargarEscenarioInicial,
  getNotificaciones,
  type Atraccion, type Zona, type Estadisticas, type AforoParque
} from "@/lib/api"

const navItems = [
  { id: "inicio", label: "Inicio / Mapa", icon: Map },
  { id: "usuario", label: "Panel Usuario", icon: User },
  { id: "admin", label: "Administración", icon: Settings },
  { id: "stats", label: "Estadísticas", icon: BarChart3 },
  { id: "operador", label: "Panel Operador", icon: Settings }
]
const zoneLayouts = [
  {
    id: "Z1",
    nombre: "Zona Aventura",
    capacidad: 100,
    x: 4,
    y: 6,
    width: 44,
    height: 38,
    className: "border-primary/45 bg-primary/10 hover:bg-primary/15",
    accentClass: "bg-primary",
  },
  {
    id: "Z2",
    nombre: "Zona Infantil",
    capacidad: 80,
    x: 54,
    y: 7,
    width: 40,
    height: 32,
    className: "border-chart-5/45 bg-chart-5/10 hover:bg-chart-5/15",
    accentClass: "bg-chart-5",
  },
  {
    id: "Z3",
    nombre: "Zona Acuatica",
    capacidad: 120,
    x: 5,
    y: 51,
    width: 49,
    height: 41,
    className: "border-chart-2/45 bg-chart-2/10 hover:bg-chart-2/15",
    accentClass: "bg-chart-2",
  },
  {
    id: "Z4",
    nombre: "Zona de Comidas",
    capacidad: 60,
    x: 61,
    y: 48,
    width: 34,
    height: 42,
    className: "border-chart-3/45 bg-chart-3/10 hover:bg-chart-3/15",
    accentClass: "bg-chart-3",
  },
] as const

const attractionMapPositions: Record<string, { zoneId: string; x: number; y: number }> = {
  A1: { zoneId: "Z1", x: 21, y: 25 },
  A3: { zoneId: "Z1", x: 39, y: 20 },
  A4: { zoneId: "Z2", x: 73, y: 24 },
  A2: { zoneId: "Z3", x: 28, y: 72 },
  A5: { zoneId: "Z4", x: 73, y: 61 },
  A6: { zoneId: "Z4", x: 84, y: 77 },
}

const zoneByAttractionType: Record<string, string> = {
  MECANICA_ALTURA: "Z1",
  INFANTIL: "Z2",
  ACUATICA: "Z3",
  SIMULADOR: "Z4",
}

export default function TechParkDashboard() {
  const [activePanel, setActivePanel] = useState("inicio")
  const [atracciones, setAtracciones] = useState<Atraccion[]>([])
  const [zonas, setZonas] = useState<Zona[]>([])
  const [estadisticas, setEstadisticas] = useState<Estadisticas | null>(null)
  const [loading, setLoading] = useState(true)
  const [sidebarOpen, setSidebarOpen] = useState(false)
  const [busqueda, setBusqueda] = useState("")
  const [resultadoBusqueda, setResultadoBusqueda] = useState<any>(null)
  const [climaActual, setClimaActual] = useState("SOLEADO")
  const [mensajeClima, setMensajeClima] = useState("Todas las atracciones funcionan normalmente")
  const [loadingClima, setLoadingClima] = useState(false)
  const [origenRuta, setOrigenRuta] = useState("A1")
  const [destinoRuta, setDestinoRuta] = useState("A6")
  const [resultadoRuta, setResultadoRuta] = useState<any>(null)
  const [visitante, setVisitante] = useState<any>(null)
  const [aforo, setAforo] = useState<AforoParque | null>(null)
  const [tipoTicketCompra, setTipoTicketCompra] = useState<"GENERAL" | "FAMILIAR" | "FAST_PASS">("GENERAL")
  const [zonaIngresoTicket, setZonaIngresoTicket] = useState("Z1")
  const [mensajeTicket, setMensajeTicket] = useState("")
  const [filaActual, setFilaActual] = useState<any>(null)
  const [favoritos, setFavoritos] = useState<string[]>([])
  const [historial, setHistorial] = useState<string[]>([])
  const [atraccionOperador, setAtraccionOperador] = useState("")
  const [cantidadDemoFila, setCantidadDemoFila] = useState(10)
  const [mensajeOperador, setMensajeOperador] = useState("")
  const [conexionesGrafo, setConexionesGrafo] = useState<any>(null)
  const [reporteTotalVisitantes, setReporteTotalVisitantes] = useState<any>(null)
  const [reporteMasVisitada, setReporteMasVisitada] = useState<any>(null)
  const [reporteFilaLarga, setReporteFilaLarga] = useState<any>(null)
  const [reporteCierresClima, setReporteCierresClima] = useState<any>(null)
  const [reporteIngresos, setReporteIngresos] = useState<any>(null)
  const [reporteMantenimiento, setReporteMantenimiento] = useState<any>(null)
  const [reporteIncidentes, setReporteIncidentes] = useState<any>(null)
  const [notificaciones, setNotificaciones] = useState<any[]>([])
  const [analisisGrafo, setAnalisisGrafo] = useState<any>(null)
  const [jerarquiaAdmin, setJerarquiaAdmin] = useState<any[]>([])
  const [selectedZonaId, setSelectedZonaId] = useState("Z1")
  const [selectedAtraccionId, setSelectedAtraccionId] = useState<string | null>(null)
  const [mapFilter, setMapFilter] = useState<"todas" | "activas">("todas")
  

  useEffect(() => {
    const cargarDatos = async () => {
      setLoading(true)
      try {
        const [dataAtracciones, dataZonas, dataStats, dataConexiones, dataAforo] = await Promise.all([
          getAtracciones(),
          getZonas(),
          getEstadisticas(),
          getConexionesGrafo(),
          getAforo(),
        ])
        setAtracciones(dataAtracciones)
        setZonas(dataZonas)
        setEstadisticas(dataStats)
        setConexionesGrafo(dataConexiones)
        setAforo(dataAforo)
      } catch (error) {
        console.error("Error cargando datos:", error)
        setAtracciones([])
      } finally {
        setLoading(false)
      }
    }
    cargarDatos()
  }, [])

  const zonasMapa = useMemo(() => {
    return zoneLayouts.map((layout) => {
      const zonaApi = zonas.find((zona) => zona.id === layout.id)

      return {
        ...layout,
        nombre: zonaApi?.nombre ?? layout.nombre,
        capacidad: zonaApi?.capacidad ?? layout.capacidad,
      }
    })
  }, [zonas])

  const atraccionesConMapa = useMemo(() => {
    return atracciones.map((atraccion, index) => {
      const position = attractionMapPositions[String(atraccion.id)]

      if (position) {
        return { atraccion, ...position }
      }

      const zoneId = zoneByAttractionType[atraccion.tipo] ?? "Z1"
      const zoneLayout = zoneLayouts.find((zone) => zone.id === zoneId) ?? zoneLayouts[0]
      const column = index % 3
      const row = Math.floor(index / 3) % 2

      return {
        atraccion,
        zoneId,
        x: zoneLayout.x + 12 + column * 8,
        y: zoneLayout.y + 13 + row * 12,
      }
    })
  }, [atracciones])

  const atraccionesVisiblesMapa = useMemo(() => {
    if (mapFilter === "activas") {
      return atraccionesConMapa.filter(({ atraccion }) => atraccion.estado === "ACTIVA")
    }

    return atraccionesConMapa
  }, [atraccionesConMapa, mapFilter])

  const selectedZona = zonasMapa.find((zona) => zona.id === selectedZonaId) ?? zonasMapa[0]!
  const selectedZonaAforo = aforo?.zonas.find((zona) => zona.id === selectedZonaId)
  const ticketPrecios = {
    GENERAL: 50000,
    FAMILIAR: 40000,
    FAST_PASS: 80000,
  }

  const selectedZonaAtracciones = useMemo(() => {
    return atraccionesConMapa
      .filter(({ zoneId }) => zoneId === selectedZonaId)
      .map(({ atraccion }) => atraccion)
  }, [atraccionesConMapa, selectedZonaId])

  const selectedAtraccion = useMemo(() => {
    const atraccionDeZona = selectedZonaAtracciones.find(
      (atraccion) => String(atraccion.id) === selectedAtraccionId
    )

    return atraccionDeZona ?? selectedZonaAtracciones[0] ?? null
  }, [selectedAtraccionId, selectedZonaAtracciones])

  const conexionesMapa = useMemo(() => {
    const conexiones = conexionesGrafo?.conexiones

    if (!conexiones) {
      return []
    }

    const vistas: Array<{ origen: string; destino: string; peso: number }> = []
    const visitadas = new Set<string>()

    Object.entries(conexiones).forEach(([origen, destinos]: any) => {
      if (!Array.isArray(destinos)) return

      destinos.forEach((destinoInfo: any) => {
        const destino = typeof destinoInfo === "string" ? destinoInfo : destinoInfo.destino
        const peso = typeof destinoInfo === "string" ? 1 : Number(destinoInfo.peso ?? 1)

        if (!destino) return

        const key = [origen, destino].sort().join("-")
        if (visitadas.has(key)) return

        visitadas.add(key)
        vistas.push({ origen, destino, peso })
      })
    })

    return vistas
  }, [conexionesGrafo])

  const rutaSeleccionada = Array.isArray(resultadoRuta?.ruta) ? resultadoRuta.ruta : []

  const esTramoDeRuta = (origen: string, destino: string) => {
    for (let i = 0; i < rutaSeleccionada.length - 1; i++) {
      const a = rutaSeleccionada[i]
      const b = rutaSeleccionada[i + 1]

      if ((a === origen && b === destino) || (a === destino && b === origen)) {
        return true
      }
    }

    return false
  }

  const seleccionarZona = (zonaId: string) => {
    setSelectedZonaId(zonaId)
    const primeraAtraccion = atraccionesConMapa.find(({ zoneId }) => zoneId === zonaId)
    setSelectedAtraccionId(primeraAtraccion ? String(primeraAtraccion.atraccion.id) : null)
  }

  const seleccionarAtraccionMapa = (atraccion: Atraccion, zoneId: string) => {
    setSelectedZonaId(zoneId)
    setSelectedAtraccionId(String(atraccion.id))
  }

    const buscarAtraccion = async () => {
    if (busqueda.trim() === "") return

    try {
      const data = await buscarAtraccionPorId(busqueda)
      setResultadoBusqueda(data)
    } catch (error) {
      console.error("Error buscando atraccion:", error)
    }
    }

    const cargarDatosAdmin = async () => {
    
      try {
    
        const conexiones = await getConexionesGrafo()
    
        const total = await getReporteTotalVisitantes()
    
        const masVisitada = await getReporteAtraccionMasVisitada()
    
        const filaLarga = await getReporteFilaMasLarga()
    
        const cierres = await getReporteCierresClima()
        const ingresos = await getReporteIngresosDiarios()
        const mantenimiento = await getReporteAlertasMantenimiento()
        const incidentes = await getReporteIncidentesOperativos()
    
        const analisis = await getAnalisisGrafo()
    
        const jerarquia = await getJerarquiaAdmin()
        const avisos = await getNotificaciones()
    
        setConexionesGrafo(conexiones)
    
        setReporteTotalVisitantes(total)
    
        setReporteMasVisitada(masVisitada)
    
        setReporteFilaLarga(filaLarga)
    
        setReporteCierresClima(cierres)
        setReporteIngresos(ingresos)
        setReporteMantenimiento(mantenimiento)
        setReporteIncidentes(incidentes)
    
        setAnalisisGrafo(analisis)
    
        setJerarquiaAdmin(jerarquia)
        setNotificaciones(avisos)
    
      } catch (error) {
    
        console.error("Error cargando datos admin:", error)
      }
    }
    const getAtraccionSeleccionada = () => {
      return atracciones.find(a => String(a.id) === atraccionOperador)
    }
    
    const validarAccesoOperador = () => {
      const atraccion = getAtraccionSeleccionada()
    
      if (!atraccion) {
        setMensajeOperador("Seleccione una atracción primero")
        return
      }
    
      setMensajeOperador(
        "Acceso validado para " +
        atraccion.nombre +
        ". Se revisaron edad, altura y capacidad por ciclo."
      )
    }
    
    const cambiarEstadoOperador = async (estado: "ACTIVA" | "CERRADA" | "EN_MANTENIMIENTO") => {
      const atraccion = getAtraccionSeleccionada()
    
      if (!atraccion) {
        setMensajeOperador("Seleccione una atracción primero")
        return
      }
    
      try {
        await actualizarEstadoAtraccion(atraccionOperador, estado)
        const nuevasAtracciones = await getAtracciones()
        setAtracciones(nuevasAtracciones)
        setMensajeOperador("Estado actualizado para " + atraccion.nombre + ": " + estado)
      } catch (error) {
        console.error(error)
        setMensajeOperador("No se pudo actualizar el estado en el backend")
      }
    }
    
    const registrarRevisionOperador = async () => {
      const atraccion = getAtraccionSeleccionada()

      if (!atraccion) {
        setMensajeOperador("Seleccione una atracciÃ³n primero")
        return
      }

      try {
        await registrarRevisionAtraccion(atraccionOperador)
        const nuevasAtracciones = await getAtracciones()
        setAtracciones(nuevasAtracciones)
        setMensajeOperador("RevisiÃ³n tÃ©cnica registrada correctamente para " + atraccion.nombre)
      } catch (error) {
        console.error(error)
        setMensajeOperador("No se pudo registrar la revisiÃ³n tÃ©cnica")
      }
    }

    const procesarFilaOperador = async () => {
      const atraccion = getAtraccionSeleccionada()
    
      if (!atraccion) {
        setMensajeOperador("Seleccione una atracción primero")
        return
      }
    
      try {
        const data = await procesarFilaAtraccion(atraccionOperador)
        const nuevasAtracciones = await getAtracciones()
        const nuevasStats = await getEstadisticas()
        setAtracciones(nuevasAtracciones)
        setEstadisticas(nuevasStats)
        setMensajeOperador(
          data.error ||
          "Fila procesada en " +
          atraccion.nombre +
          ". Personas procesadas: " +
          data.procesados +
          ". Denegadas: " +
          data.denegados
        )
      } catch (error) {
        console.error(error)
        setMensajeOperador("No se pudo procesar la fila")
      }
    }

    const generarVisitantesDemoOperador = async () => {
      const atraccion = getAtraccionSeleccionada()

      if (!atraccion) {
        setMensajeOperador("Seleccione una atracción primero")
        return
      }

      try {
        const data = await generarVisitantesDemo(atraccionOperador, cantidadDemoFila)
        const nuevasAtracciones = await getAtracciones()
        setAtracciones(nuevasAtracciones)
        setMensajeOperador(
          data.error ||
          `${data.agregados} visitantes demo agregados a la fila de ${atraccion.nombre}`
        )
      } catch (error) {
        console.error(error)
        setMensajeOperador("No se pudieron generar visitantes demo")
      }
    }

    const actualizarPausaFilaOperador = async (pausada: boolean) => {
      const atraccion = getAtraccionSeleccionada()

      if (!atraccion) {
        setMensajeOperador("Seleccione una atracción primero")
        return
      }

      try {
        const data = await actualizarPausaFila(atraccionOperador, pausada)
        const nuevasAtracciones = await getAtracciones()
        setAtracciones(nuevasAtracciones)
        setMensajeOperador(data.error || `${data.mensaje} en ${atraccion.nombre}`)
      } catch (error) {
        console.error(error)
        setMensajeOperador("No se pudo cambiar el estado de la fila")
      }
    }

    const manejarFila = async (id: string) => {
      if (!visitante || !visitante.ticket || visitante.ticket === "SIN_TICKET") {
        alert("Debes comprar un ticket antes de unirte a una fila")
        return
      }

      try {
        const data = await unirseAFila(
          id,
          visitante?.nombre || "Visitante",
          visitante?.ticket || "GENERAL",
          {
            edad: visitante?.edad,
            estatura: visitante?.estatura,
            saldo: visitante?.saldo,
          }
        )
    
        const atraccion = atracciones.find(a => String(a.id) === id)
    
        setFilaActual(data)
    
        if (atraccion) {
          if (!favoritos.includes(atraccion.nombre)) {
            setFavoritos([...favoritos, atraccion.nombre])
          }
    
          setHistorial([...historial, atraccion.nombre])
        }
    
        const nuevasAtracciones = await getAtracciones()
        setAtracciones(nuevasAtracciones)
    
        alert(data.mensaje || data.error)
    
      } catch (error) {
        console.error(error)
        alert("Error al unirse a la fila")
      }
    }

  const manejarCargarVisitante = async () => {
    try {
      const data = await cargarVisitante()
      setVisitante(data)
      setMensajeTicket("")
  
      if (data.error) {
        alert(data.error)
      } else {
        alert("Visitante cargado correctamente: " + data.nombre)
      }
  
    } catch (error) {
      console.error("Error cargando visitante:", error)
      alert("No se pudo cargar el visitante")
    }
  }

  const manejarComprarTicket = async () => {
    if (!visitante) {
      setMensajeTicket("Carga un visitante antes de comprar el ticket")
      return
    }

    try {
      const data = await comprarTicket({
        nombre: visitante.nombre,
        documento: visitante.documento,
        edad: Number(visitante.edad),
        estatura: Number(visitante.estatura),
        saldo: Number(visitante.saldo),
        tipoTicket: tipoTicketCompra,
        zonaIngresoId: zonaIngresoTicket,
      })

      if (data.error) {
        setMensajeTicket(data.error)
        return
      }

      setVisitante(data.visitante)
      setAforo(data.aforo)
      setMensajeTicket(`${data.mensaje}. Precio: $${data.precio}. Zona: ${data.zonaIngreso}`)

      const [nuevasStats, nuevoReporteIngresos, nuevoReporteTotal, nuevasZonas] = await Promise.all([
        getEstadisticas(),
        getReporteIngresosDiarios(),
        getReporteTotalVisitantes(),
        getZonas(),
      ])

      setEstadisticas(nuevasStats)
      setReporteIngresos(nuevoReporteIngresos)
      setReporteTotalVisitantes(nuevoReporteTotal)
      setZonas(nuevasZonas)
    } catch (error) {
      console.error("Error comprando ticket:", error)
      setMensajeTicket("No se pudo comprar el ticket")
    }
  }

  const manejarCargarEscenario = async () => {
    try {
      const data = await cargarEscenarioInicial()
      const [nuevasAtracciones, nuevasZonas, nuevasStats, nuevasConexiones, nuevoAforo] = await Promise.all([
        getAtracciones(),
        getZonas(),
        getEstadisticas(),
        getConexionesGrafo(),
        getAforo(),
      ])

      setAtracciones(nuevasAtracciones)
      setZonas(nuevasZonas)
      setEstadisticas(nuevasStats)
      setConexionesGrafo(nuevasConexiones)
      setAforo(nuevoAforo)
      setResultadoRuta(null)
      setMensajeTicket("")
      alert(data.mensaje || "Escenario cargado")
    } catch (error) {
      console.error("Error cargando escenario:", error)
      alert("No se pudo cargar el escenario inicial")
    }
  }

  const manejarRuta = async () => {
    try {
      const data = await calcularRuta(origenRuta, destinoRuta)
      setResultadoRuta(data)
    } catch (error) {
      console.error("Error calculando ruta:", error)
      alert("Error al calcular ruta")
    }
  }
  
  const agregarFavorito = (nombre: string) => {
  
    if (!favoritos.includes(nombre)) {
  
      setFavoritos([...favoritos, nombre])
  
      alert(nombre + " agregado a favoritos")
    }
  }

  const cambiarClima = async (tipo: 'soleado' | 'lluvia' | 'tormenta') => {
    setLoadingClima(true)
    try {
      const data = await activarClima(tipo)
      setClimaActual(data.clima)
      setMensajeClima(data.mensaje)
      // recargar atracciones para reflejar cambios
      const nuevasAtracciones = await getAtracciones()
      setAtracciones(nuevasAtracciones)
    } catch (error) {
      console.error("Error cambiando clima:", error)
    } finally {
      setLoadingClima(false)
    }
  }

  const getEstadoBadge = (estado: Atraccion["estado"]) => {
    switch (estado) {
      case "ACTIVA":
        return <Badge className="bg-primary/20 text-primary border-primary/30"><CheckCircle2 className="w-3 h-3 mr-1" />Activa</Badge>
      case "CERRADA":
        return <Badge variant="destructive" className="bg-destructive/20 text-destructive border-destructive/30"><XCircle className="w-3 h-3 mr-1" />Cerrada</Badge>
      case "EN_MANTENIMIENTO":
        return <Badge className="bg-chart-3/20 text-chart-3 border-chart-3/30"><AlertTriangle className="w-3 h-3 mr-1" />Mantenimiento</Badge>
      default:
        return <Badge variant="secondary">Desconocido</Badge>
    }
  }

  const getEstadoMarkerClass = (estado: Atraccion["estado"]) => {
    switch (estado) {
      case "ACTIVA":
        return "border-primary/70 bg-primary text-primary-foreground shadow-primary/25"
      case "CERRADA":
        return "border-destructive/70 bg-destructive text-white shadow-destructive/25"
      case "EN_MANTENIMIENTO":
        return "border-chart-3/70 bg-chart-3 text-background shadow-chart-3/25"
      default:
        return "border-border bg-secondary text-secondary-foreground"
    }
  }

  const getTipoIcon = (tipo: string) => {
    switch (tipo) {
      case "MECANICA_ALTURA": return <Zap className="w-4 h-4 text-chart-4" />
      case "ACUATICA": return <Users className="w-4 h-4 text-chart-2" />
      case "INFANTIL": return <Ticket className="w-4 h-4 text-chart-5" />
      case "SIMULADOR": return <TrendingUp className="w-4 h-4 text-primary" />
      default: return <Ticket className="w-4 h-4 text-muted-foreground" />
    }
  }

  const getClimaIcon = () => {
    switch (climaActual) {
      case "LLUVIA": return <CloudRain className="w-5 h-5 text-blue-400" />
      case "TORMENTA": return <CloudLightning className="w-5 h-5 text-yellow-400" />
      default: return <Sun className="w-5 h-5 text-chart-3" />
    }
  }

  return (
    <div className="flex min-h-screen">
      {/* Sidebar */}
      <aside className={cn(
        "fixed inset-y-0 left-0 z-50 w-64 bg-sidebar border-r border-sidebar-border transform transition-transform duration-200 ease-in-out lg:relative lg:translate-x-0",
        sidebarOpen ? "translate-x-0" : "-translate-x-full"
      )}>
        <div className="flex flex-col h-full">
          <div className="flex items-center justify-between p-6 border-b border-sidebar-border">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-lg bg-primary/20 flex items-center justify-center">
                <Zap className="w-6 h-6 text-primary" />
              </div>
              <div>
                <h1 className="font-bold text-lg text-sidebar-foreground">TechPark</h1>
                <p className="text-xs text-muted-foreground">Quindío</p>
              </div>
            </div>
            <Button variant="ghost" size="icon" className="lg:hidden" onClick={() => setSidebarOpen(false)}>
              <X className="w-5 h-5" />
            </Button>
          </div>

          <nav className="flex-1 p-4 space-y-1">
            {navItems.map((item) => {
              const Icon = item.icon
              return (
                <button
                  key={item.id}
                  onClick={() => {
                    setActivePanel(item.id)
                    setSidebarOpen(false)
                  
                    if (item.id === "admin" || item.id === "stats") {
                      cargarDatosAdmin()
                    }
                  }}className={cn(
                    "w-full flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-colors",
                    activePanel === item.id
                      ? "bg-sidebar-accent text-primary"
                      : "text-muted-foreground hover:bg-sidebar-accent/50 hover:text-sidebar-foreground"
                  )}
                >
                  <Icon className="w-5 h-5" />
                  {item.label}
                </button>
              )
            })}
          </nav>

          <div className="p-4 border-t border-sidebar-border">
            <div className="flex items-center gap-3 p-3 rounded-lg bg-sidebar-accent/50">
              {getClimaIcon()}
              <div>
                <p className="text-sm font-medium text-sidebar-foreground">{climaActual}</p>
                <p className="text-xs text-muted-foreground">Estado del parque</p>
              </div>
            </div>
          </div>
        </div>
      </aside>

      {sidebarOpen && (
        <div className="fixed inset-0 bg-background/80 backdrop-blur-sm z-40 lg:hidden" onClick={() => setSidebarOpen(false)} />
      )}

      <main className="flex-1 lg:ml-0">
        <header className="sticky top-0 z-30 flex items-center gap-4 px-4 py-3 bg-background/95 backdrop-blur border-b border-border lg:hidden">
          <Button variant="ghost" size="icon" onClick={() => setSidebarOpen(true)}>
            <Menu className="w-5 h-5" />
          </Button>
          <h1 className="font-semibold">TechPark UQ</h1>
        </header>

        <div className="p-6 lg:p-8">
          

          {/* ── PANEL INICIO ── */}
          {activePanel === "inicio" && (
            <div className="space-y-6">
              <div>
                <h2 className="text-2xl font-bold text-foreground">Visualización General del Parque</h2>
                <p className="text-muted-foreground mt-1">Monitoreo en tiempo real de zonas y disponibilidad de atracciones</p>
                <div className="flex gap-2 mt-4">
                  <input
                    type="text"
                    placeholder="Buscar atraccion por ID. Ej: A1"
                    value={busqueda}
                    onChange={(e) => setBusqueda(e.target.value)}
                    className="px-3 py-2 rounded-md border border-border bg-background text-foreground"
                  />
                  <Button onClick={buscarAtraccion}>Buscar</Button>
                </div>
                {resultadoBusqueda && (
                  <Card className="bg-card border-border mt-4">
                    <CardContent className="p-4">
                      <p className="font-semibold text-foreground">Resultado de busqueda:</p>
                      {resultadoBusqueda.mensaje ? (
                        <p className="text-sm text-destructive">{resultadoBusqueda.mensaje}</p>
                      ) : (
                        <>
                          <p className="text-sm text-muted-foreground">ID: {resultadoBusqueda.id}</p>
                          <p className="text-sm text-muted-foreground">Nombre: {resultadoBusqueda.nombre}</p>
                          <p className="text-sm text-muted-foreground">Tipo: {resultadoBusqueda.tipo}</p>
                          <p className="text-sm text-muted-foreground">Capacidad: {resultadoBusqueda.capacidadMaxima}</p>
                          <p className="text-sm text-muted-foreground">Estado: {resultadoBusqueda.estado}</p>
                          <p className="text-sm text-muted-foreground">Tiempo espera: {resultadoBusqueda.tiempoEsperaMinutos} min</p>
                        </>
                      )}
                    </CardContent>
                  </Card>
                )}
              </div>

              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                <Card className="bg-card border-border">
                  <CardContent className="p-4">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-lg bg-primary/20 flex items-center justify-center">
                        <CheckCircle2 className="w-5 h-5 text-primary" />
                      </div>
                      <div>
                        <p className="text-2xl font-bold text-foreground">{atracciones.filter(a => a.estado === "ACTIVA").length}</p>
                        <p className="text-xs text-muted-foreground">Activas</p>
                      </div>
                    </div>
                  </CardContent>
                </Card>
                <Card className="bg-card border-border">
                  <CardContent className="p-4">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-lg bg-chart-3/20 flex items-center justify-center">
                        <AlertTriangle className="w-5 h-5 text-chart-3" />
                      </div>
                      <div>
                        <p className="text-2xl font-bold text-foreground">{atracciones.filter(a => a.estado === "EN_MANTENIMIENTO").length}</p>
                        <p className="text-xs text-muted-foreground">Mantenimiento</p>
                      </div>
                    </div>
                  </CardContent>
                </Card>
                <Card className="bg-card border-border">
                  <CardContent className="p-4">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-lg bg-chart-2/20 flex items-center justify-center">
                        <Users className="w-5 h-5 text-chart-2" />
                      </div>
                      <div>
                        <p className="text-2xl font-bold text-foreground">{atracciones.reduce((acc, a) => acc + a.personasEnFila, 0)}</p>
                        <p className="text-xs text-muted-foreground">En filas</p>
                      </div>
                    </div>
                  </CardContent>
                </Card>
                <Card className="bg-card border-border">
                  <CardContent className="p-4">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-lg bg-chart-4/20 flex items-center justify-center">
                        <Timer className="w-5 h-5 text-chart-4" />
                      </div>
                      <div>
                        <p className="text-2xl font-bold text-foreground">
                          {Math.round(
                            atracciones.filter(a => a.estado === "ACTIVA").reduce((acc, a) => acc + a.tiempoEsperaMinutos, 0) /
                            (atracciones.filter(a => a.estado === "ACTIVA").length || 1)
                          )}
                        </p>
                        <p className="text-xs text-muted-foreground">Min promedio</p>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              </div>

              <Card className="bg-card border-border">
                <CardHeader className="gap-3">
                  <div className="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">
                    <div>
                      <CardTitle className="flex items-center gap-2 text-foreground">
                        <MapPin className="w-5 h-5 text-primary" />
                        Mapa interactivo del parque
                      </CardTitle>
                      <p className="text-sm text-muted-foreground mt-1">
                        Explora zonas, selecciona marcadores y revisa disponibilidad en tiempo real.
                      </p>
                    </div>
                    <div className="flex items-center gap-2 rounded-md border border-border bg-secondary/60 p-1">
                      <Button
                        size="sm"
                        variant={mapFilter === "todas" ? "default" : "ghost"}
                        onClick={() => setMapFilter("todas")}
                      >
                        <Layers className="w-4 h-4" />
                        Todas
                      </Button>
                      <Button
                        size="sm"
                        variant={mapFilter === "activas" ? "default" : "ghost"}
                        onClick={() => setMapFilter("activas")}
                      >
                        <CheckCircle2 className="w-4 h-4" />
                        Activas
                      </Button>
                    </div>
                  </div>
                </CardHeader>
                <CardContent>
                  <div className="grid grid-cols-1 gap-4 xl:grid-cols-[minmax(0,1fr)_340px]">
                    <div className="relative min-h-[430px] overflow-hidden rounded-md border border-border bg-[radial-gradient(circle_at_25%_20%,rgba(46,229,157,0.16),transparent_30%),radial-gradient(circle_at_70%_70%,rgba(80,140,255,0.13),transparent_28%),linear-gradient(135deg,rgba(255,255,255,0.04),rgba(255,255,255,0.01))] sm:min-h-[520px]">
                      <div className="absolute inset-4 rounded-md border border-border/60 bg-background/20" />
                      <div className="absolute left-[6%] right-[6%] top-1/2 h-px bg-border/60" />
                      <div className="absolute bottom-[8%] top-[8%] left-1/2 w-px bg-border/60" />
                      <div className="absolute left-[44%] top-[40%] h-[22%] w-[14%] rounded-md border border-border/60 bg-background/35" />

                      <svg className="pointer-events-none absolute inset-0 z-10 h-full w-full" viewBox="0 0 100 100" preserveAspectRatio="none">
                        {conexionesMapa.map(({ origen, destino, peso }) => {
                          const inicio = attractionMapPositions[origen]
                          const fin = attractionMapPositions[destino]
                          const enRuta = esTramoDeRuta(origen, destino)

                          if (!inicio || !fin) {
                            return null
                          }

                          return (
                            <g key={`${origen}-${destino}`}>
                              <line
                                x1={inicio.x}
                                y1={inicio.y}
                                x2={fin.x}
                                y2={fin.y}
                                stroke={enRuta ? "oklch(0.72 0.19 160)" : "oklch(0.65 0 0 / 0.45)"}
                                strokeWidth={enRuta ? 0.9 : 0.35}
                                strokeLinecap="round"
                                strokeDasharray={enRuta ? "0" : "1.2 1.2"}
                              />
                              <text
                                x={(inicio.x + fin.x) / 2}
                                y={(inicio.y + fin.y) / 2}
                                textAnchor="middle"
                                className="fill-muted-foreground text-[2.3px]"
                              >
                                {peso}
                              </text>
                            </g>
                          )
                        })}
                      </svg>

                      {zonasMapa.map((zona) => {
                        const atraccionesZona = atraccionesConMapa.filter(({ zoneId }) => zoneId === zona.id)
                        const atraccionesActivasZona = atraccionesZona.filter(({ atraccion }) => atraccion.estado === "ACTIVA").length
                        const zonaAforo = aforo?.zonas.find((zonaActual) => zonaActual.id === zona.id)

                        return (
                          <button
                            key={zona.id}
                            type="button"
                            onClick={() => seleccionarZona(zona.id)}
                            className={cn(
                              "absolute rounded-md border p-3 text-left transition-all focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring",
                              zona.className,
                              selectedZonaId === zona.id
                                ? "border-primary/80 ring-2 ring-primary/50"
                                : "opacity-90 hover:opacity-100"
                            )}
                            style={{
                              left: `${zona.x}%`,
                              top: `${zona.y}%`,
                              width: `${zona.width}%`,
                              height: `${zona.height}%`,
                            }}
                            aria-label={`Seleccionar ${zona.nombre}`}
                          >
                            <span className="flex items-center justify-between gap-2">
                              <span className="flex min-w-0 items-center gap-2">
                                <span className={cn("h-2.5 w-2.5 shrink-0 rounded-full", zona.accentClass)} />
                                <span className="truncate text-xs font-semibold uppercase text-muted-foreground">{zona.id}</span>
                              </span>
                              <span className="text-xs font-medium text-muted-foreground">
                                {atraccionesActivasZona}/{atraccionesZona.length}
                              </span>
                            </span>
                            <span className="mt-2 block text-sm font-semibold leading-tight text-foreground">
                              {zona.nombre}
                            </span>
                            <span className="mt-1 block text-xs text-muted-foreground">
                              Aforo {zonaAforo?.ocupacion ?? 0}/{zona.capacidad}
                            </span>
                          </button>
                        )
                      })}

                      {atraccionesVisiblesMapa.map(({ atraccion, zoneId, x, y }) => (
                        <button
                          key={atraccion.id}
                          type="button"
                          onClick={(event) => {
                            event.stopPropagation()
                            seleccionarAtraccionMapa(atraccion, zoneId)
                          }}
                          className={cn(
                            "absolute z-20 flex h-9 min-w-9 -translate-x-1/2 -translate-y-1/2 items-center justify-center gap-1 rounded-full border px-2 text-xs font-bold shadow-lg transition-all hover:scale-105 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring",
                            getEstadoMarkerClass(atraccion.estado),
                            selectedAtraccion?.id === atraccion.id && "ring-2 ring-white/80"
                          )}
                          style={{ left: `${x}%`, top: `${y}%` }}
                          aria-label={`Ver ${atraccion.nombre}`}
                        >
                          <MapPin className="w-3.5 h-3.5" />
                          <span>{atraccion.id}</span>
                        </button>
                      ))}

                      <div className="absolute bottom-3 left-3 right-3 z-30 flex flex-wrap items-center gap-2 rounded-md border border-border bg-background/90 p-2 backdrop-blur">
                        <span className="text-xs font-medium text-muted-foreground">Estado</span>
                        <span className="flex items-center gap-1.5 text-xs text-muted-foreground">
                          <span className="h-2.5 w-2.5 rounded-full bg-primary" /> Activa
                        </span>
                        <span className="flex items-center gap-1.5 text-xs text-muted-foreground">
                          <span className="h-2.5 w-2.5 rounded-full bg-chart-3" /> Mantenimiento
                        </span>
                        <span className="flex items-center gap-1.5 text-xs text-muted-foreground">
                          <span className="h-2.5 w-2.5 rounded-full bg-destructive" /> Cerrada
                        </span>
                      </div>
                    </div>

                    <div className="space-y-4">
                      <div className="rounded-md border border-border bg-secondary/35 p-4">
                        <div className="flex items-start justify-between gap-3">
                          <div>
                            <p className="text-xs font-medium uppercase text-muted-foreground">Zona seleccionada</p>
                            <h3 className="mt-1 text-lg font-semibold text-foreground">{selectedZona.nombre}</h3>
                          </div>
                          <Badge className="bg-primary/20 text-primary border-primary/30">{selectedZona.id}</Badge>
                        </div>
                        <div className="mt-4 grid grid-cols-3 gap-2">
                          <div className="rounded-md bg-background/55 p-3 text-center">
                            <p className="text-xl font-bold text-foreground">{selectedZonaAtracciones.length}</p>
                            <p className="text-xs text-muted-foreground">Atracciones</p>
                          </div>
                          <div className="rounded-md bg-background/55 p-3 text-center">
                            <p className="text-xl font-bold text-primary">
                              {selectedZonaAtracciones.filter((atraccion) => atraccion.estado === "ACTIVA").length}
                            </p>
                            <p className="text-xs text-muted-foreground">Activas</p>
                          </div>
                          <div className="rounded-md bg-background/55 p-3 text-center">
                            <p className="text-xl font-bold text-foreground">{selectedZona.capacidad}</p>
                            <p className="text-xs text-muted-foreground">Capacidad</p>
                          </div>
                        </div>
                        <div className="mt-3 rounded-md bg-background/55 p-3">
                          <div className="mb-2 flex items-center justify-between text-xs text-muted-foreground">
                            <span>Aforo zona</span>
                            <span>{selectedZonaAforo?.cupos ?? selectedZona.capacidad} cupos</span>
                          </div>
                          <div className="h-2 overflow-hidden rounded-full bg-secondary">
                            <div
                              className="h-full rounded-full bg-primary transition-all"
                              style={{
                                width: `${Math.min(((selectedZonaAforo?.ocupacion ?? 0) / (selectedZona.capacidad || 1)) * 100, 100)}%`,
                              }}
                            />
                          </div>
                          <p className="mt-2 text-sm font-semibold text-foreground">
                            {selectedZonaAforo?.ocupacion ?? 0}/{selectedZona.capacidad} visitantes
                          </p>
                        </div>
                      </div>

                      <div className="rounded-md border border-border bg-secondary/35 p-4">
                        <div className="flex items-center gap-2">
                          <LocateFixed className="w-4 h-4 text-primary" />
                          <p className="text-sm font-semibold text-foreground">Detalle de atraccion</p>
                        </div>

                        {selectedAtraccion ? (
                          <div className="mt-4 space-y-4">
                            <div className="flex items-start justify-between gap-3">
                              <div className="min-w-0">
                                <div className="flex items-center gap-2">
                                  {getTipoIcon(selectedAtraccion.tipo)}
                                  <h4 className="truncate font-semibold text-foreground">{selectedAtraccion.nombre}</h4>
                                </div>
                                <p className="mt-1 text-xs text-muted-foreground">{selectedAtraccion.tipo}</p>
                              </div>
                              {getEstadoBadge(selectedAtraccion.estado)}
                            </div>

                            <div className="grid grid-cols-2 gap-2">
                              <div className="rounded-md bg-background/55 p-3">
                                <div className="flex items-center gap-2 text-muted-foreground">
                                  <Clock className="w-4 h-4" />
                                  <span className="text-xs">Espera</span>
                                </div>
                                <p className="mt-1 text-lg font-semibold text-foreground">
                                  {selectedAtraccion.estado === "ACTIVA" ? `${selectedAtraccion.tiempoEsperaMinutos} min` : "No disp."}
                                </p>
                              </div>
                              <div className="rounded-md bg-background/55 p-3">
                                <div className="flex items-center gap-2 text-muted-foreground">
                                  <Users className="w-4 h-4" />
                                  <span className="text-xs">Fila</span>
                                </div>
                                <p className="mt-1 text-lg font-semibold text-foreground">
                                  {selectedAtraccion.personasEnFila}/{selectedAtraccion.capacidadMaxima}
                                </p>
                              </div>
                            </div>
                          </div>
                        ) : (
                          <p className="mt-4 text-sm text-muted-foreground">
                            Selecciona una zona con atracciones para ver su disponibilidad.
                          </p>
                        )}
                      </div>

                      <div className="rounded-md border border-border bg-secondary/35 p-3">
                        <div className="mb-2 flex items-center justify-between gap-2 px-1">
                          <p className="text-sm font-semibold text-foreground">Atracciones de la zona</p>
                          <Badge variant="secondary">{selectedZonaAtracciones.length}</Badge>
                        </div>
                        <div className="max-h-[220px] space-y-2 overflow-y-auto pr-1">
                          {selectedZonaAtracciones.length === 0 ? (
                            <p className="rounded-md bg-background/55 p-3 text-sm text-muted-foreground">
                              No hay atracciones asignadas a esta zona.
                            </p>
                          ) : (
                            selectedZonaAtracciones.map((atraccion) => (
                              <button
                                key={atraccion.id}
                                type="button"
                                onClick={() => setSelectedAtraccionId(String(atraccion.id))}
                                className={cn(
                                  "w-full rounded-md border p-3 text-left transition-colors hover:border-primary/50 hover:bg-primary/10 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring",
                                  selectedAtraccion?.id === atraccion.id
                                    ? "border-primary/60 bg-primary/10"
                                    : "border-border bg-background/55"
                                )}
                              >
                                <span className="flex items-center justify-between gap-2">
                                  <span className="min-w-0 truncate text-sm font-medium text-foreground">{atraccion.nombre}</span>
                                  <span className={cn("h-2.5 w-2.5 shrink-0 rounded-full", getEstadoMarkerClass(atraccion.estado).split(" ")[1])} />
                                </span>
                                <span className="mt-1 flex items-center gap-3 text-xs text-muted-foreground">
                                  <span>{atraccion.id}</span>
                                  <span>{atraccion.estado === "ACTIVA" ? `${atraccion.tiempoEsperaMinutos} min` : "No disponible"}</span>
                                </span>
                              </button>
                            ))
                          )}
                        </div>
                      </div>
                    </div>
                  </div>
                </CardContent>
              </Card>

              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {loading ? (
                  <Card className="bg-card border-border col-span-full">
                    <CardContent className="p-6 text-center text-muted-foreground">Cargando atracciones...</CardContent>
                  </Card>
                ) : atracciones.length === 0 ? (
                  <Card className="bg-card border-border col-span-full">
                    <CardContent className="p-6 text-center text-destructive">
                      No se pudo conectar al backend. Verifica que el servidor esté corriendo en localhost:8080.
                    </CardContent>
                  </Card>
                ) : (
                  atracciones.map((atraccion) => (
                    <Card key={atraccion.id} className={cn("bg-card border-border transition-all hover:border-primary/50", atraccion.estado !== "ACTIVA" && "opacity-60")}>
                      <CardHeader className="pb-3">
                        <div className="flex items-start justify-between">
                          <div className="flex items-center gap-2">
                            {getTipoIcon(atraccion.tipo)}
                            <CardTitle className="text-base font-semibold text-foreground">{atraccion.nombre}</CardTitle>
                          </div>
                          {getEstadoBadge(atraccion.estado)}
                        </div>
                        <p className="text-sm text-muted-foreground">{atraccion.tipo}</p>
                      </CardHeader>
                      <CardContent className="pt-0">
                        <div className="grid grid-cols-2 gap-4">
                          <div className="flex items-center gap-2">
                            <Clock className="w-4 h-4 text-muted-foreground" />
                            <span className="text-sm text-foreground">{atraccion.estado === "ACTIVA" ? `${atraccion.tiempoEsperaMinutos} min` : "-"}</span>
                          </div>
                          <div className="flex items-center gap-2">
                            <Users className="w-4 h-4 text-muted-foreground" />
                            <span className="text-sm text-foreground">{atraccion.personasEnFila} / {atraccion.capacidadMaxima}</span>
                          </div>
                        </div>

                        {atraccion.estado === "ACTIVA" && atraccion.tiempoEsperaMinutos > 0 && (
                          <div className="mt-3">
                            <div className="h-1.5 bg-secondary rounded-full overflow-hidden">
                              <div className="h-full bg-primary rounded-full transition-all" style={{ width: `${Math.min((atraccion.personasEnFila / atraccion.capacidadMaxima) * 100, 100)}%` }} />
                            </div>
                          </div>
                        )}

                        {atraccion.estado === "ACTIVA" && (
                          <Button className="mt-4 w-full" onClick={() => manejarFila(String(atraccion.id))}>
                            Unirse a la fila
                          </Button>
                        )}
                      </CardContent>
                    </Card>
                  ))
                )}
              </div>
            </div>
          )}

          {/* ── PANEL USUARIO ── */}
          {activePanel === "usuario" && (
            <div className="space-y-6">
            {filaActual && (
              <Card className="bg-card border-border">
                <CardHeader>
                  <CardTitle>Fila Virtual Actual</CardTitle>
                </CardHeader>
                <CardContent>
                  <p>{filaActual.mensaje}</p>
                  <p>Posición: {filaActual.posicion}</p>
                  <p>Tiempo estimado: {filaActual.tiempoEstimado} min</p>
                  <p>Ticket: {filaActual.tipoTicket}</p>
                </CardContent>
              </Card>
            )}
              <Card className="bg-card border-border">
                <CardHeader>
                  <CardTitle>Favoritos</CardTitle>
                </CardHeader>
                <CardContent>
                  {favoritos.length === 0 ? (
                    <p>No hay favoritos</p>
                  ) : (
                    favoritos.map((f, i) => <p key={i}>{f}</p>)
                  )}
                </CardContent>
              </Card>
              
              <Card className="bg-card border-border">
                <CardHeader>
                  <CardTitle>Historial</CardTitle>
                </CardHeader>
                <CardContent>
                  {historial.length === 0 ? (
                    <p>No hay visitas registradas</p>
                  ) : (
                    historial.map((h, i) => <p key={i}>{h}</p>)
                  )}
                </CardContent>
              </Card>
              <Card className="bg-card border-border">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2 text-foreground">
                    <User className="w-5 h-5 text-primary" />
                    Perfil del Visitante
                  </CardTitle>
                </CardHeader>
              
                <CardContent className="space-y-4">
                  <Button onClick={manejarCargarVisitante}>
                    Cargar visitante desde archivo
                  </Button>
              
                  {visitante && (
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                      <div className="p-3 rounded-lg bg-secondary">
                        <p className="text-xs text-muted-foreground">Nombre</p>
                        <p className="font-semibold text-foreground">{visitante.nombre}</p>
                      </div>
              
                      <div className="p-3 rounded-lg bg-secondary">
                        <p className="text-xs text-muted-foreground">Documento</p>
                        <p className="font-semibold text-foreground">{visitante.documento}</p>
                      </div>
              
                      <div className="p-3 rounded-lg bg-secondary">
                        <p className="text-xs text-muted-foreground">Edad</p>
                        <p className="font-semibold text-foreground">{visitante.edad}</p>
                      </div>
              
                      <div className="p-3 rounded-lg bg-secondary">
                        <p className="text-xs text-muted-foreground">Estatura</p>
                        <p className="font-semibold text-foreground">{visitante.estatura}</p>
                      </div>
              
                      <div className="p-3 rounded-lg bg-secondary">
                        <p className="text-xs text-muted-foreground">Saldo virtual</p>
                        <p className="font-semibold text-primary">${visitante.saldo}</p>
                      </div>
              
                      <div className="p-3 rounded-lg bg-secondary">
                        <p className="text-xs text-muted-foreground">Ticket</p>
                        <Badge className="bg-primary/20 text-primary">
                          {visitante.ticket}
                        </Badge>
                      </div>
                    </div>
                  )}

                  <div className="rounded-md border border-border bg-secondary/35 p-4">
                    <div className="mb-3 flex items-center justify-between gap-3">
                      <div>
                        <p className="text-sm font-semibold text-foreground">Compra de ticket</p>
                        <p className="text-xs text-muted-foreground">
                          Aforo parque: {aforo?.ocupacionParque ?? 0}/{aforo?.capacidadParque ?? 0}
                        </p>
                      </div>
                      <Badge variant="secondary">{aforo?.cuposParque ?? 0} cupos</Badge>
                    </div>

                    <div className="grid grid-cols-1 gap-3 md:grid-cols-[1fr_1fr_auto]">
                      <select
                        value={tipoTicketCompra}
                        onChange={(e) => setTipoTicketCompra(e.target.value as "GENERAL" | "FAMILIAR" | "FAST_PASS")}
                        className="h-10 rounded-md border border-border bg-background px-3 text-sm text-foreground"
                      >
                        <option value="GENERAL">GENERAL - ${ticketPrecios.GENERAL}</option>
                        <option value="FAMILIAR">FAMILIAR - ${ticketPrecios.FAMILIAR}</option>
                        <option value="FAST_PASS">FAST_PASS - ${ticketPrecios.FAST_PASS}</option>
                      </select>

                      <select
                        value={zonaIngresoTicket}
                        onChange={(e) => setZonaIngresoTicket(e.target.value)}
                        className="h-10 rounded-md border border-border bg-background px-3 text-sm text-foreground"
                      >
                        {zonas.map((zona) => {
                          const zonaAforo = aforo?.zonas.find((zonaActual) => zonaActual.id === zona.id)

                          return (
                            <option key={zona.id} value={zona.id}>
                              {zona.nombre} ({zonaAforo?.cupos ?? zona.cupos ?? zona.capacidad} cupos)
                            </option>
                          )
                        })}
                      </select>

                      <Button onClick={manejarComprarTicket} disabled={!visitante}>
                        <Ticket className="w-4 h-4" />
                        Comprar
                      </Button>
                    </div>

                    {mensajeTicket && (
                      <p className="mt-3 rounded-md bg-background/70 p-3 text-sm text-foreground">
                        {mensajeTicket}
                      </p>
                    )}
                  </div>
                </CardContent>
              </Card>
          
              <Card className="bg-card border-border">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2 text-foreground">
                    <Navigation className="w-5 h-5 text-primary" />
                    Calcular Ruta Óptima
                  </CardTitle>
                </CardHeader>
          
                <CardContent className="space-y-4">
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
          
                    <input
                      type="text"
                      value={origenRuta}
                      onChange={(e) => setOrigenRuta(e.target.value)}
                      placeholder="Origen Ej: A1"
                      className="px-3 py-2 rounded-md border border-border bg-background text-foreground"
                    />
          
                    <input
                      type="text"
                      value={destinoRuta}
                      onChange={(e) => setDestinoRuta(e.target.value)}
                      placeholder="Destino Ej: A6"
                      className="px-3 py-2 rounded-md border border-border bg-background text-foreground"
                    />
          
                    <Button onClick={manejarRuta}>
                      Calcular ruta
                    </Button>
          
                  </div>
          
                  {resultadoRuta && (
                    <div className="p-4 rounded-lg bg-secondary border border-border">
          
                      {resultadoRuta.mensaje ? (
          
                        <p className="text-sm text-destructive">
                          {resultadoRuta.mensaje}
                        </p>
          
                      ) : (
          
                        <>
                          <p className="text-sm text-foreground">
                            Ruta: {resultadoRuta.ruta.join(" → ")}
                          </p>
          
                          <p className="text-sm text-muted-foreground">
                            Paradas: {resultadoRuta.cantidadParadas}
                          </p>
                          <p className="text-sm text-muted-foreground">
                            Distancia total: {resultadoRuta.distanciaTotal} unidades ({resultadoRuta.algoritmo || "BFS"})
                          </p>
                        </>
          
                      )}
          
                    </div>
                  )}
          
                </CardContent>
              </Card>
          
              <div>
                <h2 className="text-2xl font-bold text-foreground">
                  Panel del Visitante
                </h2>
          
                <p className="text-muted-foreground mt-1">
                  Estado actual de las atracciones y tiempos de espera
                </p>
              </div>
          
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          
                {atracciones.map((atraccion) => (
          
                  <Card
                    key={atraccion.id}
                    className={cn(
                      "bg-card border-border",
                      atraccion.estado !== "ACTIVA" && "opacity-50"
                    )}
                    
                  >
          
                    <CardHeader className="pb-2">
          
                      <div className="flex items-center justify-between">
          
                        <CardTitle className="text-sm font-semibold text-foreground">
                          {atraccion.nombre}
                        </CardTitle>
          
                        {getEstadoBadge(atraccion.estado)}
          
                      </div>
          
                    </CardHeader>
          
                    <CardContent className="pt-0 space-y-2">
          
                      <div className="flex items-center gap-2 text-sm text-muted-foreground">
                        <Clock className="w-4 h-4" />
                        <span>
                          Espera: {atraccion.estado === "ACTIVA"
                            ? `${atraccion.tiempoEsperaMinutos} min`
                            : "No disponible"}
                        </span>
                      </div>
          
                      <div className="flex items-center gap-2 text-sm text-muted-foreground">
                        <Users className="w-4 h-4" />
                        <span>
                          En fila: {atraccion.personasEnFila} / {atraccion.capacidadMaxima}
                        </span>
                      </div>
          
                      {atraccion.estado === "ACTIVA" && (
                        <>
                          <Button
                            size="sm"
                            className="w-full mt-2"
                            variant="outline"
                            onClick={() => manejarFila(String(atraccion.id))}
                          >
                            Unirse a la fila
                          </Button>
                      
                          <Button
                            size="sm"
                            variant="secondary"
                            className="w-full mt-2"
                            onClick={() => agregarFavorito(atraccion.nombre)}
                          >
                            Agregar a favoritos
                          </Button>
                        </>
                      )}
                    </CardContent>
          
                  </Card>
                                  ))}
          
              </div>
          
            </div>
                  )}

          {/* ── PANEL ADMIN ── */}
          {activePanel === "admin" && (
            <div className="space-y-6">
          
              <div>
                <h2 className="text-2xl font-bold text-foreground">
                  Panel de Administración
                </h2>
          
                <p className="text-muted-foreground mt-1">
                  Gestión global del parque y análisis del sistema
                </p>
              </div>
          
              <Button className="mt-4" onClick={manejarCargarEscenario}>
                Cargar escenario de prueba
              </Button>

              {/* Zonas */}
              <div>
                <h3 className="text-lg font-semibold text-foreground mb-3">
                  Zonas del Parque
                </h3>
          
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                  {zonas.map((zona) => (
                    <Card key={zona.id} className="bg-card border-border">
                      <CardContent className="p-4">
                        <div className="w-10 h-10 rounded-lg bg-primary/20 flex items-center justify-center mb-3">
                          <Map className="w-5 h-5 text-primary" />
                        </div>
          
                        <h4 className="font-semibold text-foreground">
                          {zona.nombre}
                        </h4>
          
                        <p className="text-sm text-muted-foreground mt-1">
                          Capacidad: {zona.capacidad}
                        </p>
          
                        <Badge className="mt-2 bg-primary/20 text-primary">
                          {zona.id}
                        </Badge>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              </div>
          
              {/* Análisis del Grafo */}
              <div>
                <h3 className="text-lg font-semibold text-foreground mb-3">
                  Análisis del Grafo
                </h3>
          
                <Card className="bg-card border-border">
                  <CardContent className="p-6 space-y-4">
                    <div className="p-4 rounded-lg bg-secondary">
                      <p className="font-semibold text-foreground">
                        Conectividad del Parque
                      </p>
          
                      <div className="mt-2 space-y-1 text-sm text-muted-foreground">
                        {conexionesGrafo?.conexiones ? (
                          Object.entries(conexionesGrafo.conexiones).map(([origen, destinos]: any) => (
                            <div key={String(origen)}>
                              <span className="font-medium text-foreground">
                                {String(origen)}
                              </span>
                              {" ↔ "}
                              {Array.isArray(destinos)
                                ? destinos.map((destino: any) =>
                                  typeof destino === "string" ? destino : `${destino.destino} (${destino.peso})`
                                ).join(", ")
                                : "Sin conexiones"}
                            </div>
                          ))
                        ) : (
                          <p>Sin datos de conexiones</p>
                        )}
                      </div>
                    </div>
          
                    <div className="p-4 rounded-lg bg-primary/10 border border-primary/20">
                      <p className="font-semibold text-foreground">
                        Atracción más conectada
                      </p>
          
                      <div className="text-sm text-muted-foreground mt-1">
                        <p>
                          {analisisGrafo?.atraccionMasConectada || "Sin datos"}
                        </p>
          
                        <p className="mt-2">
                          Cluster popular: {analisisGrafo?.clusterPopular || "Sin datos"}
                        </p>
          
                        <p className="mt-2">
                          Total nodos: {analisisGrafo?.totalNodos || 0}
                        </p>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              </div>
          
              {/* Gestión Jerárquica */}
              <div>
                <h3 className="text-lg font-semibold text-foreground mb-3">
                  Gestión Jerárquica
                </h3>
          
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <Card className="bg-card border-border">
                    <CardHeader>
                      <CardTitle className="text-foreground">
                        Supervisión de Operadores
                      </CardTitle>
                    </CardHeader>
          
                    <CardContent className="space-y-2 text-sm text-muted-foreground">
                      {jerarquiaAdmin.length > 0 ? (
                        jerarquiaAdmin.map((item, index) => (
                          <p key={index}>
                            {item.operador} → {item.zona}
                          </p>
                        ))
                      ) : (
                        <p>Sin datos de operadores</p>
                      )}
                    </CardContent>
                  </Card>
          
                  <Card className="bg-card border-border">
                    <CardHeader>
                      <CardTitle className="text-foreground">
                        Supervisión de Zonas
                      </CardTitle>
                    </CardHeader>
          
                    <CardContent className="space-y-2 text-sm text-muted-foreground">
                      {jerarquiaAdmin.length > 0 ? (
                        jerarquiaAdmin.map((item, index) => (
                          <p key={index}>
                            {item.zona} → {item.atraccionesAsignadas} atracciones
                          </p>
                        ))
                      ) : (
                        <p>Sin datos de zonas</p>
                      )}
                    </CardContent>
                  </Card>
                </div>
              </div>
          
          
              {/* Clima */}
              <div>
                <h3 className="text-lg font-semibold text-foreground mb-3">
                  Control Climático
                </h3>
          
                <Card className="bg-card border-border">
                  <CardContent className="p-6">
                    <div className="flex items-center gap-3 mb-4 p-3 rounded-lg bg-secondary">
                      {getClimaIcon()}
          
                      <div>
                        <p className="font-medium text-foreground">
                          Clima actual: {climaActual}
                        </p>
          
                        <p className="text-sm text-muted-foreground">
                          {mensajeClima}
                        </p>
                      </div>
                    </div>
          
                    <div className="flex gap-3 flex-wrap">
                      <Button
                        onClick={() => cambiarClima("soleado")}
                        disabled={loadingClima}
                        className="bg-yellow-500/20 text-yellow-400 hover:bg-yellow-500/30 border border-yellow-500/30"
                        variant="outline"
                      >
                        <Sun className="w-4 h-4 mr-2" />
                        Soleado
                      </Button>
          
                      <Button
                        onClick={() => cambiarClima("lluvia")}
                        disabled={loadingClima}
                        className="bg-blue-500/20 text-blue-400 hover:bg-blue-500/30 border border-blue-500/30"
                        variant="outline"
                      >
                        <CloudRain className="w-4 h-4 mr-2" />
                        Lluvia
                      </Button>
          
                      <Button
                        onClick={() => cambiarClima("tormenta")}
                        disabled={loadingClima}
                        className="bg-destructive/20 text-destructive hover:bg-destructive/30 border border-destructive/30"
                        variant="outline"
                      >
                        <CloudLightning className="w-4 h-4 mr-2" />
                        Tormenta
                      </Button>
                    </div>
                  </CardContent>
                </Card>
              </div>

              <div>
                <h3 className="text-lg font-semibold text-foreground mb-3">
                  Notificaciones del Sistema
                </h3>
                <Card className="bg-card border-border">
                  <CardContent className="p-4 space-y-2">
                    {notificaciones.length === 0 ? (
                      <p className="text-sm text-muted-foreground">Sin notificaciones registradas</p>
                    ) : (
                      notificaciones.slice(-5).reverse().map((notificacion, index) => (
                        <div key={index} className="rounded-md bg-secondary p-3">
                          <p className="text-xs font-medium text-primary">{notificacion.tipo}</p>
                          <p className="text-sm text-foreground">{notificacion.mensaje}</p>
                        </div>
                      ))
                    )}
                  </CardContent>
                </Card>
              </div>
          
            </div>
          )}
             
      
          {/* ── PANEL OPERADOR ── */}
          {activePanel === "operador" && (
            <div className="space-y-6">
          
              <div>
                <h2 className="text-2xl font-bold text-foreground">
                  Panel Operador
                </h2>
          
                <p className="text-muted-foreground mt-1">
                  Gestión de atracciones y control de acceso
                </p>
              </div>
          
              <Card className="bg-card border-border">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2 text-foreground">
                    <Settings className="w-5 h-5 text-primary" />
                    Gestión de Atracción
                  </CardTitle>
                </CardHeader>
          
                <CardContent className="space-y-4">
          
                  <select
                    value={atraccionOperador}
                    onChange={(e) => setAtraccionOperador(e.target.value)}
                    className="w-full px-3 py-2 rounded-md border border-border bg-background text-foreground"
                  >
                    <option value="">Seleccione una atracción</option>
          
                    {atracciones.map((atraccion) => (
                      <option key={atraccion.id} value={atraccion.id}>
                        {atraccion.nombre}
                      </option>
                    ))}
                  </select>
          
                  {getAtraccionSeleccionada() && (
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          
                      <div className="p-3 rounded-lg bg-secondary">
                        <p className="text-xs text-muted-foreground">
                          Estado actual
                        </p>
          
                        <p className="font-semibold text-foreground">
                          {getAtraccionSeleccionada()?.estado}
                        </p>
                      </div>
          
                      <div className="p-3 rounded-lg bg-secondary">
                        <p className="text-xs text-muted-foreground">
                          Personas en fila
                        </p>
          
                        <p className="font-semibold text-foreground">
                          {getAtraccionSeleccionada()?.personasEnFila}
                        </p>
                      </div>
          
                      <div className="p-3 rounded-lg bg-secondary">
                        <p className="text-xs text-muted-foreground">
                          Capacidad por ciclo
                        </p>
          
                        <p className="font-semibold text-foreground">
                          {getAtraccionSeleccionada()?.capacidadMaxima}
                        </p>
                      </div>
          
                      <div className="p-3 rounded-lg bg-secondary">
                        <p className="text-xs text-muted-foreground">
                          Tiempo espera
                        </p>
          
                        <p className="font-semibold text-foreground">
                          {getAtraccionSeleccionada()?.tiempoEsperaMinutos} min
                        </p>
                      </div>

                      <div className="p-3 rounded-lg bg-secondary md:col-span-2">
                        <p className="text-xs text-muted-foreground">
                          Estado de fila
                        </p>

                        <p className="font-semibold text-foreground">
                          {getAtraccionSeleccionada()?.filaPausada ? "Pausada" : "Avanzando por ciclos"}
                        </p>
                      </div>
          
                    </div>
                  )}

                  <div className="rounded-md border border-border bg-secondary/35 p-4">
                    <div className="mb-3 flex flex-col gap-1">
                      <p className="text-sm font-semibold text-foreground">Simulador de fila</p>
                      <p className="text-xs text-muted-foreground">
                        Genera visitantes de prueba para poblar la cola y procesa ciclos segÃºn la capacidad.
                      </p>
                    </div>

                    <div className="grid grid-cols-1 gap-3 md:grid-cols-[160px_1fr_1fr_1fr]">
                      <input
                        type="number"
                        min={1}
                        max={100}
                        value={cantidadDemoFila}
                        onChange={(e) => setCantidadDemoFila(Number(e.target.value))}
                        className="h-10 rounded-md border border-border bg-background px-3 text-sm text-foreground"
                      />

                      <Button variant="outline" onClick={generarVisitantesDemoOperador}>
                        <Users className="w-4 h-4" />
                        Generar visitantes
                      </Button>

                      <Button
                        variant="outline"
                        onClick={() => actualizarPausaFilaOperador(true)}
                      >
                        <XCircle className="w-4 h-4" />
                        Pausar fila
                      </Button>

                      <Button
                        variant="outline"
                        onClick={() => actualizarPausaFilaOperador(false)}
                      >
                        <CheckCircle2 className="w-4 h-4" />
                        Reanudar fila
                      </Button>
                    </div>
                  </div>
          
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3">
          
                    <Button
                      variant="outline"
                      onClick={validarAccesoOperador}
                    >
                      Validar acceso
                    </Button>
          
                    <Button
                      variant="outline"
                      onClick={() => cambiarEstadoOperador("CERRADA")}
                    >
                      Cerrar atracción
                    </Button>
          
                    <Button
                      variant="outline"
                      onClick={() => cambiarEstadoOperador("EN_MANTENIMIENTO")}
                    >
                      Enviar a mantenimiento
                    </Button>
          
                    <Button
                      variant="outline"
                      onClick={() => cambiarEstadoOperador("ACTIVA")}
                    >
                      Reactivar atracción
                    </Button>
          
                    <Button onClick={registrarRevisionOperador}>
                      Registrar revisión técnica
                    </Button>
          
                    <Button
                      className="bg-primary text-primary-foreground"
                      onClick={procesarFilaOperador}
                    >
                      Procesar siguiente ciclo
                    </Button>
          
                  </div>
          
                  {mensajeOperador && (
                    <div className="p-4 rounded-lg bg-secondary border border-border">
                      <p className="text-sm text-foreground">
                        {mensajeOperador}
                      </p>
                    </div>
                  )}
          
                </CardContent>
              </Card>
          
            </div>
          )}
          {/* ── PANEL STATS ── */}
          {activePanel === "stats" && (
            <div className="space-y-6">
              <div>
                <h2 className="text-2xl font-bold text-foreground">Estadísticas e Informes</h2>
                <p className="text-muted-foreground mt-1">Datos en tiempo real del parque</p>
              </div>

              {estadisticas && (
                <div className="grid grid-cols-2 md:grid-cols-3 xl:grid-cols-6 gap-4">
                  <Card className="bg-card border-border">
                    <CardContent className="p-4 text-center">
                      <p className="text-3xl font-bold text-foreground">{estadisticas.totalAtracciones}</p>
                      <p className="text-sm text-muted-foreground mt-1">Total Atracciones</p>
                    </CardContent>
                  </Card>
                  <Card className="bg-card border-border">
                    <CardContent className="p-4 text-center">
                      <p className="text-3xl font-bold text-primary">{estadisticas.atraccionesActivas}</p>
                      <p className="text-sm text-muted-foreground mt-1">Activas</p>
                    </CardContent>
                  </Card>
                  <Card className="bg-card border-border">
                    <CardContent className="p-4 text-center">
                      <p className="text-3xl font-bold text-foreground">{estadisticas.totalPersonasEnFila}</p>
                      <p className="text-sm text-muted-foreground mt-1">En Filas</p>
                    </CardContent>
                  </Card>
                  <Card className="bg-card border-border">
                    <CardContent className="p-4 text-center">
                      <p className="text-3xl font-bold text-chart-3">{estadisticas.tiempoPromedioEspera} min</p>
                      <p className="text-sm text-muted-foreground mt-1">Tiempo Promedio</p>
                    </CardContent>
                  </Card>
                  <Card className="bg-card border-border">
                    <CardContent className="p-4 text-center">
                      <p className="text-3xl font-bold text-foreground">{aforo?.ocupacionParque ?? estadisticas.ocupacionParque ?? 0}</p>
                      <p className="text-sm text-muted-foreground mt-1">Aforo Actual</p>
                    </CardContent>
                  </Card>
                  <Card className="bg-card border-border">
                    <CardContent className="p-4 text-center">
                      <p className="text-3xl font-bold text-primary">{aforo?.ticketsVendidos ?? estadisticas.ticketsVendidos ?? 0}</p>
                      <p className="text-sm text-muted-foreground mt-1">Tickets Vendidos</p>
                    </CardContent>
                  </Card>
                </div>
              )}

              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <Card className="bg-card border-border">
                  <CardContent className="p-4">
                    <p className="text-sm text-muted-foreground">Ingresos diarios estimados</p>
                    <p className="text-2xl font-bold text-foreground">
                      ${reporteIngresos?.ingresosEstimados ?? 0}
                    </p>
                    <p className="text-xs text-muted-foreground">
                      Tickets vendidos: {reporteIngresos?.ticketsVendidos ?? 0}
                    </p>
                  </CardContent>
                </Card>
                <Card className="bg-card border-border">
                  <CardContent className="p-4">
                    <p className="text-sm text-muted-foreground">Alertas de mantenimiento</p>
                    <p className="text-2xl font-bold text-chart-3">
                      {reporteMantenimiento?.alertas ?? 0}
                    </p>
                  </CardContent>
                </Card>
                <Card className="bg-card border-border">
                  <CardContent className="p-4">
                    <p className="text-sm text-muted-foreground">Incidentes operativos</p>
                    <p className="text-2xl font-bold text-destructive">
                      {reporteIncidentes?.incidentes ?? 0}
                    </p>
                  </CardContent>
                </Card>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <Card className="bg-card border-border">
                  <CardHeader>
                    <CardTitle className="text-foreground">Atracciones por Estado</CardTitle>
                  </CardHeader>
                  <CardContent className="space-y-3">
                    {["ACTIVA", "EN_MANTENIMIENTO", "CERRADA"].map((estado) => {
                      const count = atracciones.filter(a => a.estado === estado).length
                      const label = estado === "ACTIVA" ? "Activas" : estado === "EN_MANTENIMIENTO" ? "Mantenimiento" : "Cerradas"
                      const color = estado === "ACTIVA" ? "bg-primary" : estado === "EN_MANTENIMIENTO" ? "bg-chart-3" : "bg-destructive"
                      return (
                        <div key={estado} className="flex items-center gap-3">
                          <span className="text-sm text-muted-foreground w-28">{label}</span>
                          <div className="flex-1 h-2 bg-secondary rounded-full overflow-hidden">
                            <div className={`h-full ${color} rounded-full`} style={{ width: `${(count / (atracciones.length || 1)) * 100}%` }} />
                          </div>
                          <span className="text-sm font-medium text-foreground w-4">{count}</span>
                        </div>
                      )
                    })}
                  </CardContent>
                </Card>

                <Card className="bg-card border-border">
                  <CardHeader>
                    <CardTitle className="text-foreground">Tiempos de Espera</CardTitle>
                  </CardHeader>
                  <CardContent className="space-y-3">
                    {atracciones.filter(a => a.estado === "ACTIVA" && a.tiempoEsperaMinutos > 0)
                      .sort((a, b) => b.tiempoEsperaMinutos - a.tiempoEsperaMinutos)
                      .map((a) => (
                        <div key={a.id} className="flex items-center gap-3">
                          <span className="text-sm text-muted-foreground w-36 truncate">{a.nombre}</span>
                          <div className="flex-1 h-2 bg-secondary rounded-full overflow-hidden">
                            <div className="h-full bg-primary rounded-full" style={{ width: `${(a.tiempoEsperaMinutos / 60) * 100}%` }} />
                          </div>
                          <span className="text-sm font-medium text-foreground w-12 text-right">{a.tiempoEsperaMinutos} min</span>
                        </div>
                      ))}
                  </CardContent>
                </Card>
              </div>
            </div>
          )}

        </div>
      </main>
    </div>
  )
}
