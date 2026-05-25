"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import {
  Map, User, Settings, BarChart3, Clock, Users, AlertTriangle,
  CheckCircle2, XCircle, Zap, Timer, TrendingUp, Ticket,
  Navigation, CloudSun, Menu, X, CloudRain, CloudLightning, Sun,
} from "lucide-react"
import { cn } from "@/lib/utils"
import {
  getAtracciones, getZonas, getEstadisticas, activarClima, buscarAtraccionPorId, unirseAFila,
  type Atraccion, type Zona, type Estadisticas
} from "@/lib/api"

const navItems = [
  { id: "inicio", label: "Inicio / Mapa", icon: Map },
  { id: "usuario", label: "Panel Usuario", icon: User },
  { id: "admin", label: "Administración", icon: Settings },
  { id: "stats", label: "Estadísticas", icon: BarChart3 },
]
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

  useEffect(() => {
    const cargarDatos = async () => {
      setLoading(true)
      try {
        const [dataAtracciones, dataZonas, dataStats] = await Promise.all([
          getAtracciones(),
          getZonas(),
          getEstadisticas(),
        ])
        setAtracciones(dataAtracciones)
        setZonas(dataZonas)
        setEstadisticas(dataStats)
      } catch (error) {
        console.error("Error cargando datos:", error)
        setAtracciones([])
      } finally {
        setLoading(false)
      }
    }
    cargarDatos()
  }, [])

    const buscarAtraccion = async () => {
    if (busqueda.trim() === "") return

    try {
      const data = await buscarAtraccionPorId(busqueda)
      setResultadoBusqueda(data)
    } catch (error) {
      console.error("Error buscando atraccion:", error)
    }
  }

  const manejarFila = async (id: string) => {
    try {
      const data = await unirseAFila(id, "Juan", "FAST_PASS")

      alert(
        data.mensaje +
        "\nPosicion: " + data.posicion +
        "\nTiempo estimado: " + data.tiempoEstimado + " min"
      )

    } catch (error) {
      console.error(error)
      alert("Error al unirse a la fila")
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
                  onClick={() => { setActivePanel(item.id); setSidebarOpen(false) }}
                  className={cn(
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
              <div>
                <h2 className="text-2xl font-bold text-foreground">Panel del Visitante</h2>
                <p className="text-muted-foreground mt-1">Estado actual de las atracciones y tiempos de espera</p>
              </div>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {atracciones.map((atraccion) => (
                  <Card key={atraccion.id} className={cn("bg-card border-border", atraccion.estado !== "ACTIVA" && "opacity-50")}>
                    <CardHeader className="pb-2">
                      <div className="flex items-center justify-between">
                        <CardTitle className="text-sm font-semibold text-foreground">{atraccion.nombre}</CardTitle>
                        {getEstadoBadge(atraccion.estado)}
                      </div>
                    </CardHeader>
                    <CardContent className="pt-0 space-y-2">
                      <div className="flex items-center gap-2 text-sm text-muted-foreground">
                        <Clock className="w-4 h-4" />
                        <span>Espera: {atraccion.estado === "ACTIVA" ? `${atraccion.tiempoEsperaMinutos} min` : "No disponible"}</span>
                      </div>
                      <div className="flex items-center gap-2 text-sm text-muted-foreground">
                        <Users className="w-4 h-4" />
                        <span>En fila: {atraccion.personasEnFila} / {atraccion.capacidadMaxima}</span>
                      </div>
                      {atraccion.estado === "ACTIVA" && (
                        <Button size="sm" className="w-full mt-2" variant="outline">
                          Unirse a la fila
                        </Button>
                      )}
                    </CardContent>
                  </Card>
                ))}
              </div>

              <Card className="bg-card border-border">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2 text-foreground">
                    <Navigation className="w-5 h-5 text-primary" />
                    Ruta Sugerida
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="space-y-3">
                    {atracciones.filter(a => a.estado === "ACTIVA").slice(0, 4).map((a, index) => (
                      <div key={a.id} className="flex items-center gap-3">
                        <div className={cn(
                          "w-8 h-8 rounded-full flex items-center justify-center text-sm font-medium",
                          index === 0 ? "bg-primary text-primary-foreground" : "bg-secondary text-secondary-foreground"
                        )}>
                          {index + 1}
                        </div>
                        <span className={cn("text-sm", index === 0 ? "font-medium text-foreground" : "text-muted-foreground")}>{a.nombre}</span>
                        {index === 0 && <Badge className="ml-auto bg-primary/20 text-primary">Menor espera</Badge>}
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>
            </div>
          )}

          {/* ── PANEL ADMIN ── */}
          {activePanel === "admin" && (
            <div className="space-y-6">
              <div>
                <h2 className="text-2xl font-bold text-foreground">Panel de Administración</h2>
                <p className="text-muted-foreground mt-1">Gestión de zonas y control de contingencias climáticas</p>
              </div>

              {/* Zonas */}
              <div>
                <h3 className="text-lg font-semibold text-foreground mb-3">Zonas del Parque</h3>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                  {zonas.map((zona) => (
                    <Card key={zona.id} className="bg-card border-border">
                      <CardContent className="p-4">
                        <div className="w-10 h-10 rounded-lg bg-primary/20 flex items-center justify-center mb-3">
                          <Map className="w-5 h-5 text-primary" />
                        </div>
                        <h4 className="font-semibold text-foreground">{zona.nombre}</h4>
                        <p className="text-sm text-muted-foreground mt-1">Capacidad: {zona.capacidad}</p>
                        <Badge className="mt-2 bg-primary/20 text-primary">{zona.id}</Badge>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              </div>

              {/* Clima */}
              <div>
                <h3 className="text-lg font-semibold text-foreground mb-3">Control Climático</h3>
                <Card className="bg-card border-border">
                  <CardContent className="p-6">
                    <div className="flex items-center gap-3 mb-4 p-3 rounded-lg bg-secondary">
                      {getClimaIcon()}
                      <div>
                        <p className="font-medium text-foreground">Clima actual: {climaActual}</p>
                        <p className="text-sm text-muted-foreground">{mensajeClima}</p>
                      </div>
                    </div>
                    <div className="flex gap-3 flex-wrap">
                      <Button
                        onClick={() => cambiarClima('soleado')}
                        disabled={loadingClima}
                        className="bg-yellow-500/20 text-yellow-400 hover:bg-yellow-500/30 border border-yellow-500/30"
                        variant="outline"
                      >
                        <Sun className="w-4 h-4 mr-2" /> Soleado
                      </Button>
                      <Button
                        onClick={() => cambiarClima('lluvia')}
                        disabled={loadingClima}
                        className="bg-blue-500/20 text-blue-400 hover:bg-blue-500/30 border border-blue-500/30"
                        variant="outline"
                      >
                        <CloudRain className="w-4 h-4 mr-2" /> Lluvia
                      </Button>
                      <Button
                        onClick={() => cambiarClima('tormenta')}
                        disabled={loadingClima}
                        className="bg-destructive/20 text-destructive hover:bg-destructive/30 border border-destructive/30"
                        variant="outline"
                      >
                        <CloudLightning className="w-4 h-4 mr-2" /> Tormenta
                      </Button>
                    </div>
                  </CardContent>
                </Card>
              </div>
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
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
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
                </div>
              )}

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